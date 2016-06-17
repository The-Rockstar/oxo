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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.oxo.haiti.R;
import com.oxo.haiti.adapter.QuestionAdapter;
import com.oxo.haiti.model.AnswerModel;
import com.oxo.haiti.model.AreaModel;
import com.oxo.haiti.model.PersonModel;
import com.oxo.haiti.model.QuestionsModel;
import com.oxo.haiti.model.RtfModel;
import com.oxo.haiti.service.RestAdapter;
import com.oxo.haiti.storage.ContentStorage;
import com.oxo.haiti.storage.SnappyNoSQL;
import com.oxo.haiti.ui.base.BaseActivity;
import com.oxo.haiti.utils.CommonInterface;
import com.oxo.haiti.utils.Connectivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentControler extends BaseActivity implements View.OnClickListener, CommonInterface {

    public static int childCount = 0;
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
    private boolean isRepeater = false;
    private int repeaterOption;
    private AreaModel areaModel = new AreaModel();
    public static int loopOne = 1, loopTwo = 1, loopThree = 1, loopFour = 1;
    List<PersonModel> personModels = new ArrayList<>();
    public static boolean isLoaded = false;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_controler);
        key = getIntent().getStringExtra("key");
        areaModel.set_id(key);

        if (getIntent().getExtras().getString("SURVEY").equals("ONE")) {
            questionsModelList = SnappyNoSQL.getInstance().getSurveyQuestionsOne();
            resumeSurvey("1", key);
            setUpAdapter();
            viewPager.setCurrentItem(ContentStorage.getInstance(this).getPositionSurveyOne(key));
        } else if (getIntent().getExtras().getString("SURVEY").equals("FOUR")) {
            findViewById(R.id.stop_survey).setVisibility(View.INVISIBLE);
            findViewById(R.id.prev).setVisibility(View.GONE);
            questionsModelList = SnappyNoSQL.getInstance().getSurveyQuestionsFour();
            resumeSurvey("3", key);
            setUpAdapter();
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
//        viewPager.setCurrentItem(executeQuestionId(100));
    }

    public List<QuestionsModel> getQuestionsModelList() {
        return questionsModelList;
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


    public AreaModel getAreaModel() {
        executeAnswers();
        return areaModel;
    }

    public AreaModel saveAreax() {
        return SnappyNoSQL.getInstance().saveArea(areaModel, key);
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
        viewPager.offsetLeftAndRight(1);
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
                        getNextPosition(suveryAnswer.getNextId(), questionsModelList.get(position), suveryAnswer.getAnswer(), false, null, false);
                    }
                }
                if (questionsModelList.get(position).getQuestionType().equals("message") || questionsModelList.get(position).getQuestionType().equals("hh_profile") || questionsModelList.get(position).getQuestionType().equals("hh_person") || questionsModelList.get(position).getQuestionType().equals("hh_children")) {
                    getNextPosition(questionsModelList.get(position).getAnswers().get(0).getOptionNext(), questionsModelList.get(position), "READ", false, questionsModelList.get(position).getAnswers().get(0).getOptionStatus(), false);
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
                    List<AnswerModel.SuveryAnswer> answers = answerModel.getSuveryAnswers();
                    answers.add(suveryAnswer);
                    if (!prevSteps.contains(nextPosition)) {
                        prevSteps.push(nextPosition);
                    }
                    viewPager.setCurrentItem(nextPosition);
                } else {
                    List<AnswerModel.SuveryAnswer> answers = answerModel.getSuveryAnswers();
                    answers.add(suveryAnswer);
                    if (!prevSteps.contains(nextPosition)) {
                        prevSteps.push(nextPosition);
                    }
                    viewPager.setCurrentItem(nextPosition);
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
                if (getIntent().getExtras().getString("SURVEY").equals("ONE")) {
                    executeAnswers();
                    SnappyNoSQL.getInstance().saveArea(areaModel, key);
                    Intent intent = new Intent(this, ResumeActivity.class);
                    intent.putExtras(getIntent());
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.settings:
                optionMenu();
                break;
        }
    }


    public List<PersonModel> getPersonModels() {
        executeAnswers();
        return personModels;
    }

    public void executeAnswers() {
        personModels.clear();
        loopOne = 0;
        loopTwo = 0;
        loopThree = 0;
        loopFour = 0;
        List<String> stringList = new ArrayList<>();
        PersonModel personModel = null;
        for (AnswerModel.SuveryAnswer answer : answerModel.getSuveryAnswers()) {

            if (answer.getQuestionId().equals("hid_140") && !TextUtils.isEmpty(answer.getAnswer())) {
                personModel = new PersonModel();
                personModel.setName(answer.getAnswer());
                personModel.setTypo("hh_person");
                personModels.add(personModel);

            }
            if (answer.getQuestionId().equals("hid_144") && !TextUtils.isEmpty(answer.getAnswer())) {
                if (personModel != null)
                    personModel.setSex(answer.getAnswer());
            }
            if (answer.getQuestionId().equals("hid_148") && !TextUtils.isEmpty(answer.getAnswer())) {
                if (personModel != null)
                    personModel.setAge(answer.getAnswer());
            }


            if (answer.getQuestionId().equals("hid_176") && !TextUtils.isEmpty(answer.getAnswer())) {
                personModel = new PersonModel();
                personModel.setSex(answer.getAnswer());
                personModel.setTypo("hh_children");
                personModels.add(personModel);
            }
            if (answer.getQuestionId().equals("hid_180") && !TextUtils.isEmpty(answer.getAnswer())) {
                if (personModel != null)
                    personModel.setAge(answer.getAnswer());
            }

            if (answer.getQuestionId().equals("hid_140") && !TextUtils.isEmpty(answer.getAnswer())) {
                loopOne++;
                stringList.add(answer.getAnswer());
                RtfModel usersModel = new RtfModel();
                usersModel.setName(answer.getAnswer());
                usersModel.setSurveyId(key);
                usersModel.setUserId("" + new Random().nextInt(99));
                areaModel.setMemberRtfModels(usersModel);
            } else if (answer.getQuestionId().equals("hid_2") || answer.getQuestionId().equals("hid_3") || answer.getQuestionId().equals("hid_4") || answer.getQuestionId().equals("hid_5") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.setBlock(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_1") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.setSit(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_7") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.setHH(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_6") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.setGps(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_8") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.setLat(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_10") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.set_long(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_14") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.setName(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_15") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.setDesc(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_176") && !TextUtils.isEmpty(answer.getAnswer())) {
                loopTwo++;
            } else if (answer.getQuestionId().equals("hid_244") && !TextUtils.isEmpty(answer.getAnswer())) {
                loopThree++;
            } else if (answer.getQuestionId().equals("hid_280") && !TextUtils.isEmpty(answer.getAnswer())) {
                loopFour++;
            }
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
                        if (getIntent().getExtras().getString("SURVEY").equals("ONE") || getIntent().getExtras().getString("SURVEY").equals("FOUR")) {
                            executeAnswers();
                            SnappyNoSQL.getInstance().saveArea(areaModel, key);
                            clearSaveState(key);
//                            Intent intent = new Intent(FragmentControler.this, SurveyTwo.class);
                            //                          intent.putExtras(getIntent());
                            //                        startActivity(intent);
                            finish();
                        } else {
                            removePerson(key, getIntent().getStringExtra("Name"));
                            findViewById(R.id.main_layout).setVisibility(View.GONE);
                            findViewById(R.id.finish_this).setVisibility(View.VISIBLE);
                            findViewById(R.id.finish_this).setOnClickListener(FragmentControler.this);
                        }
                    }
                });

            }
        }.execute(null, null, null);
    }


    private void SyncData() {
        if (status != null)
            answerModel.setStatus(status);
        final String data = new Gson().toJson(answerModel);
        if (answerModel.getSuveryAnswers().size() > 0)
            if (Connectivity.InternetAvailable(this)) {
                Call commonModelCall = RestAdapter.getInstance(this).getApiService().syncData(data);
                commonModelCall.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Log.d("", "onResponse: " + response.message());
                        hideBar();
                        // messageToast("Success  " + response.message());

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
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
    public void getNextPosition(int position, QuestionsModel questionsModel, String answer, boolean isNew, Object object, boolean localIsRepeater) {
        fetchViewData(nextPosition, answer, questionsModel);

        if (localIsRepeater) {
            isRepeater = localIsRepeater;
        }

        findViewById(R.id.next).setVisibility(View.VISIBLE);
        int next = executeQuestionId(position);
//        if (isNew) {
        suveryAnswer = new AnswerModel.SuveryAnswer();
        suveryAnswer.setAnswer(answer);
        suveryAnswer.setNextId(position);
        suveryAnswer.setQuestionId(questionsModel.getQuestionId());
        Log.d("Q_ID", questionsModel.getQuestionId());
        suveryAnswer.setQuestionKey(questionsModel.getQuestionKey());
        if (isRepeater || localIsRepeater && (repeaterOption == 0) || localIsRepeater) {
            repeaterOption = position;
            isRepeater = localIsRepeater;
            if (questionsModel.getAnswers() != null && questionsModel.getAnswers().size() >= position)
                isRepeater = questionsModel.getAnswers().get(position).isRepeater();
        }
//     } else {

        if (object != null)
            status = (String) object;
    }


    private void fetchViewData(int order, String answer, QuestionsModel questionsModel) {

        switch (order) {
            default:
                if (!TextUtils.isEmpty(areaModel.getHH())) {
                    SnappyNoSQL.getInstance().saveArea(areaModel, key);
                }
                break;
        }

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

    @Override
    public void questionId(String id, QuestionsModel questionsModel, String answer) {
//        if (questionsModel.getQuestionOrder() == 140) {
//            RtfModel usersModel = new RtfModel();
//            usersModel.setName(answer);
//            usersModel.setSurveyId(key);
//            usersModel.setUserId("" + new Random().nextInt(99));
//            areaModel.setMemberRtfModels(usersModel);
//        }
    }

    String nextId = null;


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


                        Intent intent = new Intent(FragmentControler.this, FragmentControler.class);
                        intent.putExtra("RESUME", false);
                        intent.putExtra("key", "StopStatus" + key);
                        intent.putExtra("SURVEY", "FOUR");
                        startActivity(intent);
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


    public boolean getRepeater() {
        return isRepeater;
    }
}
