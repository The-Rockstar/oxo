package com.oxo.haiti.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.oxo.haiti.R;
import com.oxo.haiti.adapter.QuestionAdapter;
import com.oxo.haiti.model.AnswerModel;
import com.oxo.haiti.model.CommonModel;
import com.oxo.haiti.model.QuestionsModel;
import com.oxo.haiti.service.RestAdapter;
import com.oxo.haiti.storage.ContentStorage;
import com.oxo.haiti.storage.SnappyNoSQL;
import com.oxo.haiti.ui.base.BaseActivity;
import com.oxo.haiti.utils.CommonInterface;
import com.oxo.haiti.utils.Connectivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentControler extends BaseActivity implements View.OnClickListener, CommonInterface {

    private ViewPager viewPager;
    private QuestionAdapter questionAdapter;
    private int nextPosition;
    private Stack<Integer> prevSteps = new Stack<>();
    private List<QuestionsModel> questionsModelList = null;
    private AnswerModel.SuveryAnswer suveryAnswer;
    private AnswerModel answerModel;
    private Toolbar toolbar;
    private String key = null;
    private String status = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_controler);
        key = getIntent().getStringExtra("key");

        if (getIntent().getExtras().getString("SURVEY").equals("ONE")) {
            questionsModelList = SnappyNoSQL.getInstance().getSurveyQuestionsOne();
            resumeSurvey("1", key);
            setUpAdapter();
            viewPager.setCurrentItem(ContentStorage.getInstance(this).getPositionSurveyOne(key));
        } else {
            questionsModelList = SnappyNoSQL.getInstance().getSurveyQuestionsTwo();
            resumeSurvey("2", key);
            setUpAdapter();
            viewPager.setCurrentItem(ContentStorage.getInstance(this).getPositionSurveyOne(key));
        }

        findViewById(R.id.prev).setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.stop_survey).setOnClickListener(this);
        setUpToolbar();
        viewPager.setCurrentItem(executeQuestionId(472));
    }

    private void resumeSurvey(String surveyID, String isOne) {
        if (getIntent().getExtras().getBoolean("RESUME")) {
            answerModel = SnappyNoSQL.getInstance().getSaveState(isOne);
            prevSteps = SnappyNoSQL.getInstance().getStack(isOne);
            if (answerModel == null)
                newObject(surveyID);
            else
                findViewById(R.id.prev).setVisibility(View.VISIBLE);
        } else {
            newObject(surveyID);
        }
    }

    private void newObject(String surveyID) {
        answerModel = new AnswerModel();
        answerModel.setSurveryPid(ContentStorage.getInstance(this).getUserID());
        answerModel.setSurveryId(surveyID);

    }

    private void pauseSurvey() {
        if (getIntent().getExtras().getString("SURVEY").equals("ONE")) {
            ContentStorage.getInstance(this).savePositionSurveyOne(viewPager.getCurrentItem(), key);
            SnappyNoSQL.getInstance().saveState(answerModel, key);
            SnappyNoSQL.getInstance().saveStack(prevSteps, key);
        } else {
            ContentStorage.getInstance(this).savePositionSurveyOne(viewPager.getCurrentItem(), key);
            SnappyNoSQL.getInstance().saveState(answerModel, key);
            SnappyNoSQL.getInstance().saveStack(prevSteps, key);

        }
    }


    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.findViewById(R.id.settings).setOnClickListener(this);
    }


    private void setUpAdapter() {
        viewPager = (ViewPager) findViewById(R.id.main_container);
        questionAdapter = new QuestionAdapter(getSupportFragmentManager(), this, questionsModelList, answerModel);
        viewPager.setAdapter(questionAdapter);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (AnswerModel.SuveryAnswer suveryAnswer : answerModel.getSuveryAnswers()) {
                    if (suveryAnswer != null && questionsModelList.get(position) != null && questionsModelList.get(position).getQuestionId().equals(suveryAnswer.getQuestionId())) {
                        getNextPosition(suveryAnswer.getNextId(), questionsModelList.get(position), suveryAnswer.getAnswer(), false, null);
                    }
                }
                if (questionsModelList.get(position).getQuestionType().equals("message")) {
                    getNextPosition(questionsModelList.get(position).getAnswers().get(0).getOptionNext(), questionsModelList.get(position), "READ", false, questionsModelList.get(position).getAnswers().get(0).getOptionStatus());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        switch (v.getId()) {
            case R.id.stop_survey:
                warning();
                break;
            case R.id.next:
                System.out.println("next_position" + nextPosition);
                if (questionAdapter.getCount() != nextPosition && nextPosition != 0) {
                    findViewById(R.id.next).setVisibility(View.INVISIBLE);
                    findViewById(R.id.prev).setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(nextPosition);
                    List<AnswerModel.SuveryAnswer> answers = answerModel.getSuveryAnswers();
                    if (!answers.contains(suveryAnswer)) {
                        answers.add(suveryAnswer);
                    }
                    if (!prevSteps.contains(nextPosition)) {
                        prevSteps.push(nextPosition);
                    }
                } else {
                    backgroundStart();
                }
                break;
            case R.id.prev:
                if (!prevSteps.empty())
                    viewPager.setCurrentItem(prevSteps.pop());
                else {
                    viewPager.setCurrentItem(0);
                    findViewById(R.id.prev).setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.finish_this:
                finish();
                break;
            case R.id.settings:
                optionMenu();
                break;
        }
    }

    void backgroundStart() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                SyncData();
                clearSaveState(key);
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                hideBar();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.main_layout).setVisibility(View.GONE);
                        findViewById(R.id.finish_this).setVisibility(View.VISIBLE);
                        findViewById(R.id.finish_this).setOnClickListener(FragmentControler.this);

                    }
                });

            }
        }.execute();
    }


    private void SyncData() {
        if (status != null)
            answerModel.setStatus(status);
        final String data = new Gson().toJson(answerModel);
        if (answerModel.getSuveryAnswers().size() > 0)
            if (Connectivity.InternetAvailable(this)) {
                Call<CommonModel> commonModelCall = RestAdapter.getInstance(this).getApiService().syncData(data);
                commonModelCall.enqueue(new Callback<CommonModel>() {
                    @Override
                    public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                        Log.d("", "onResponse: " + response.message());
                        hideBar();
                        // messageToast("Success  " + response.message());

                    }

                    @Override
                    public void onFailure(Call<CommonModel> call, Throwable t) {
                        Log.d("", "onResponse: Error");
                        SnappyNoSQL.getInstance().saveOfflineSurvey(data);
                        hideBar();


                    }
                });
            } else {
                SnappyNoSQL.getInstance().saveOfflineSurvey(data);
            }
    }


    private void optionMenu() {
        PopupMenu popup = new PopupMenu(this, toolbar.findViewById(R.id.settings));
        popup.getMenuInflater().inflate(R.menu.settings, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.one:
//                        pauseSurvey();
//                        finish();
//                        break;
//                    case R.id.two:
//                        warning();
//                        break;
                    case R.id.three:
                        clearSaveState(key);
                        ContentStorage.getInstance(FragmentControler.this).loggedIn(false);
                        Intent intent = new Intent(FragmentControler.this, AuthActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        break;
                }


                return true;
            }
        });
        popup.show();
    }

    private void stopSurvey() {
        clearSaveState(key);
        SyncData();
        finish();
    }


    @Override
    public void getNextPosition(int position, QuestionsModel questionsModel, String answer, boolean isNew, Object object) {
        findViewById(R.id.next).setVisibility(View.VISIBLE);
        int next = executeQuestionId(position);
        if (isNew) {
            suveryAnswer = new AnswerModel.SuveryAnswer();
            suveryAnswer.setAnswer(answer);
            suveryAnswer.setNextId(position);
            suveryAnswer.setQuestionId(questionsModel.getQuestionId());
            suveryAnswer.setQuestionKey(questionsModel.getQuestionKey());
        }
        if (object != null)
            status = (String) object;
    }


    int executeQuestionId(int order) {
        if (order == 0) {
            nextPosition = order;
            return nextPosition;
        }
        for (int i = 0; i < questionsModelList.size(); i++) {
            if (questionsModelList.get(i).getQuestionOrder().equals(order)) {
                nextPosition = i;
                return nextPosition;
            }
        }
        return 0;
    }


    @Override
    public void hideNext() {
        findViewById(R.id.next).setVisibility(View.INVISIBLE);
    }

    @Override
    public void hidePrev() {

    }

    String nextId = null;

    @Override
    public void questionId(String id) {
        nextId = id;

    }

    @Override
    public String getNextId() {
        return nextId;
    }


    private void warning() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        pauseSurvey();
                        finish();

//                        stopSurvey();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }


    @Override
    protected void messageCallback(boolean flag) {
    }
}
