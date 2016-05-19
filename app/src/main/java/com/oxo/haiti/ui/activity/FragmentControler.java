package com.oxo.haiti.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

import java.util.List;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentControler extends BaseActivity implements View.OnClickListener, CommonInterface {

    private ViewPager viewPager;
    private QuestionAdapter questionAdapter;
    private int nextPosition;
    Stack<Integer> prevSteps = new Stack<>();
    List<QuestionsModel> questionsModelList = null;
    AnswerModel.SuveryAnswer suveryAnswer;
    AnswerModel answerModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_controler);
        answerModel = new AnswerModel();
        answerModel.setSurveryPid(ContentStorage.getInstance(this).getUserID());
        if (getIntent().getExtras().getString("SURVEY").equals("ONE")) {
            questionsModelList = SnappyNoSQL.getInstance().getSurveyQuestionsOne();
            answerModel.setSurveryId("1");
        } else {
            questionsModelList = SnappyNoSQL.getInstance().getSurveyQuestionsTwo();
            answerModel.setSurveryId("2");
        }
        setUpAdapter();

        findViewById(R.id.prev).setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);
    }


    private void setUpAdapter() {
        viewPager = (ViewPager) findViewById(R.id.main_container);
        questionAdapter = new QuestionAdapter(getSupportFragmentManager(), this, questionsModelList);
        viewPager.setAdapter(questionAdapter);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
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
            case R.id.next:
                if (questionAdapter.getCount() != nextPosition) {
                    viewPager.setCurrentItem(nextPosition);
                    findViewById(R.id.next).setVisibility(View.GONE);
                    List<AnswerModel.SuveryAnswer> answers = answerModel.getSuveryAnswers();
                    if (!answers.contains(suveryAnswer)) {
                        answers.add(suveryAnswer);
                    }
                    if (!prevSteps.contains(nextPosition)) {
                        prevSteps.push(nextPosition);
                    }
                } else {
                    final String data = new Gson().toJson(answerModel);
                    if (Connectivity.InternetAvailable(this)) {
                        Call<CommonModel> commonModelCall = RestAdapter.getInstance(this).getApiService().syncData(data);
                        commonModelCall.enqueue(new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                Log.d("", "onResponse: " + response.message());
                                messageToast("Success  " + response.message());

                            }

                            @Override
                            public void onFailure(Call<CommonModel> call, Throwable t) {
                                Log.d("", "onResponse: Error");
                                SnappyNoSQL.getInstance().saveOfflineSurvey(data);


                            }
                        });
                    } else {
                        SnappyNoSQL.getInstance().saveOfflineSurvey(data);
                    }
                    findViewById(R.id.main_layout).setVisibility(View.GONE);
                    findViewById(R.id.finish_this).setVisibility(View.VISIBLE);
                    findViewById(R.id.finish_this).setOnClickListener(this);
                }
                break;
            case R.id.prev:
                if (!prevSteps.empty())
                    viewPager.setCurrentItem(prevSteps.pop());
                else
                    viewPager.setCurrentItem(0);
                break;
            case R.id.finish_this:
                finish();
                break;
        }
    }

    @Override
    public void getNextPosition(int position, QuestionsModel questionsModel, String answer) {
        findViewById(R.id.next).setVisibility(View.VISIBLE);
        this.nextPosition = position;
        suveryAnswer = new AnswerModel.SuveryAnswer();
        suveryAnswer.setAnswer(answer);
        suveryAnswer.setQuestionId(questionsModel.getQuestionId());
        suveryAnswer.setQuestionKey(questionsModel.getQuestionKey());
    }

}