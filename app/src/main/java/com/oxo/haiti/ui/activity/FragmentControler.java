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
import com.oxo.haiti.model.Positions;
import com.oxo.haiti.model.QuestionsModel;
import com.oxo.haiti.service.RestAdapter;
import com.oxo.haiti.storage.ContentStorage;
import com.oxo.haiti.storage.SnappyNoSQL;
import com.oxo.haiti.ui.base.BaseActivity;
import com.oxo.haiti.ui.fargments.DynamicFragment;
import com.oxo.haiti.utils.CommonInterface;
import com.oxo.haiti.utils.Connectivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentControler extends BaseActivity implements View.OnClickListener, CommonInterface {

    //    public static int childCount = 0;
    public static boolean MainYear = false;
    private ViewPager viewPager;
    private QuestionAdapter questionAdapter;
    private int nextPosition;
    private Stack<Integer> prevSteps = new Stack<>();
    private Stack<Integer> nextSteps = new Stack<>();
    private List<QuestionsModel> questionsModelList = null;
    private AnswerModel.SuveryAnswer suveryAnswer;
    private AnswerModel answerModel;
    private Toolbar toolbar;
    private String key = null;
    private String status = null;
    private boolean isRepeater = false;
    private int repeaterOption;
    private AreaModel areaModel = new AreaModel();
    public static int loopOne = 1, loopTwo = 1, loopThree = 1, loopFour = 1, loopFive = 1;
   public List<PersonModel> personModels = new ArrayList<>();
    public static boolean isLoaded = false;
    public static boolean resumeFlag = false;
    public List<PersonModel> OneCilds = new ArrayList<>();
    public static final String child = "hh_children";
    public static final String persons = "hh_person";
    public static final String loopThreesPerson = "hh_Loop_three";
    public static final String loopFourPerson = "hh_Loop_four";

//    Map<Integer, Positions> positionsMap = new HashMap<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loopOne = 1;
        loopTwo = 1;
        loopThree = 1;
        loopFour = 1;
        isLoaded = false;
        resumeFlag = false;
        MainYear = false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_controler);
        key = getIntent().getStringExtra("key");
        areaModel.set_id(key);
        try {
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
        } catch (Exception e) {

        }
//        viewPager.setCurrentItem(executeQuestionId(468));
    }


    private void resumeSurvey(String surveyID, String isOne) throws Exception {
        if (getIntent().getExtras().getBoolean("RESUME")) {
            answerModel = SnappyNoSQL.getInstance().getSaveState(isOne);
            prevSteps = SnappyNoSQL.getInstance().getStack(isOne);
            if (answerModel == null)
                newObject(surveyID);
            else
                findViewById(R.id.prev).setVisibility(View.VISIBLE);

            if (getIntent().getExtras().getString("SURVEY").equals("ONE")) {
                areaModel = SnappyNoSQL.getInstance().getArea(key);
                if (areaModel.getMemberRtfModels().size() > 0) {
                    personModels = areaModel.getMemberRtfModels();
                }
                areaModel.set_id(key);
            }

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
        String s = new String(key);
        if (key.contains("ONE") || key.contains("TWO") || key.contains("THREE")) {
            s = s.replace("ONE", "");
            s = s.replace("TWO", "");
            s = s.replace("FOUR", "");

        }
        answerModel.setGenerated_survey(s);


    }

    private void pauseSurvey() {

        if (areaModel != null && personModels != null) {
            areaModel.setMemberRtfModels(personModels);
            answerModel.setAreaModel(areaModel);
        }


        if (getIntent().getExtras().getString("SURVEY").equals("ONE")) {
            ContentStorage.getInstance(this).savePositionSurveyOne(viewPager.getCurrentItem(), key);
            SnappyNoSQL.getInstance().saveState(answerModel, key);
            SnappyNoSQL.getInstance().saveStack(prevSteps, key);
        } else {
            ContentStorage.getInstance(this).savePositionSurveyOne(viewPager.getCurrentItem(), key);
            SnappyNoSQL.getInstance().saveState(answerModel, key);
            SnappyNoSQL.getInstance().saveStack(prevSteps, key);
        }


        try {

            if (status != null) {
                answerModel.setStatus(status);
            }
            answerModel.setGenerated_survey(key);
            final String data = new Gson().toJson(answerModel);
            Log.d("FINAL data", data);
            SnappyNoSQL.getInstance().saveOfflineSurvey(data);
        } catch (Exception e) {
            Log.d("Stop", "Inter mediator");
        }

    }


    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.findViewById(R.id.settings).setOnClickListener(this);
    }


    int currantPosition = 0;

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
                currantPosition = position;
                for (AnswerModel.SuveryAnswer suveryAnswer : answerModel.getSuveryAnswers()) {
                    if (suveryAnswer != null && questionsModelList.get(position) != null && questionsModelList.get(position).getQuestionId().equals(suveryAnswer.getQuestionId())) {
                        getNextPosition(suveryAnswer.getNextId(), questionsModelList.get(position), suveryAnswer.getAnswer(), false, null, false);
                    }
                }
                Log.d("Position", "=" + position);
                if (questionsModelList.get(position).getQuestionType().equals("message") || questionsModelList.get(position).getQuestionType().equals("hh_profile") || questionsModelList.get(position).getQuestionType().equals("hh_person") || questionsModelList.get(position).getQuestionType().equals("hh_children")) {
                    getNextPosition(questionsModelList.get(position).getAnswers().get(0).getOptionNext(), questionsModelList.get(position), "READ", false, questionsModelList.get(position).getAnswers().get(0).getOptionStatus(), false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    int lastPosition1;

    @Override
    public void onClick(View v) {
        try {
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
                    if (getIntent().getExtras().getString("SURVEY").equals("ONE")) {
                        if (order == 188 || order == 192 || order == 160 || order == 164) {
                            resumeFlag = false;
                        }
                    } else
                        resumeFlag = true;

                    System.out.println("next_position" + nextPosition + " loop :" + loopOne);

                    if (questionAdapter.getCount() != nextPosition && nextPosition != 0) {
                        findViewById(R.id.next).setVisibility(View.INVISIBLE);
                        findViewById(R.id.prev).setVisibility(View.VISIBLE);
                        Stack<AnswerModel.SuveryAnswer> answers = answerModel.getSuveryAnswers();
                        if (getIntent().getExtras().getString("SURVEY").equals("ONE")) {
                            if (order < 137 || (order > 204 && order < 241) || order > 328) {
                                boolean updateItem = false;
                                for (AnswerModel.SuveryAnswer temp : answers) {
                                    if (temp.getQuestionId().equals(suveryAnswer.getQuestionId())) {
                                        updateItem = true;
                                        answers.remove(temp);
                                        answers.push(suveryAnswer);
                                        break;
                                    }
                                }
                                if (!updateItem)
                                    answers.add(suveryAnswer);

                            } else if (order >= 140 && order <= 164) {
                                skyForce(currantPosition + "One" + loopOne);
                                suveryAnswer.setExtra(currantPosition + "One" + loopOne);
                                boolean updateItem = false;
                                for (AnswerModel.SuveryAnswer temp : answers) {
                                    if (temp.getQuestionId().equals(suveryAnswer.getQuestionId()) && temp.getLoopCount() == loopOne) {
                                        updateItem = true;
                                        answers.remove(temp);
                                        suveryAnswer.setLoopCount(loopOne);
                                        suveryAnswer.setLoopNumber(1);
                                        answers.push(suveryAnswer);
                                        Log.d("LOOPSTORE", "UPDATE IN LOOP :" + loopOne);
                                        break;
                                    }
                                }
                                if (!updateItem) {
                                    Log.d("LOOPSTORE", "Add IN LOOP :" + loopOne);
                                    suveryAnswer.setLoopCount(loopOne);
                                    suveryAnswer.setLoopNumber(1);
                                    answers.add(suveryAnswer);
                                }
                            } else if (order >= 176 && order <= 192) {
                                skyForce(currantPosition + "Two" + loopTwo);
                                suveryAnswer.setExtra(currantPosition + "Two" + loopTwo);

                                boolean updateItem = false;
                                for (AnswerModel.SuveryAnswer temp : answers) {
                                    if (temp.getQuestionId().equals(suveryAnswer.getQuestionId()) && temp.getLoopCount() == loopTwo) {
                                        updateItem = true;
                                        answers.remove(temp);
                                        suveryAnswer.setLoopCount(loopTwo);
                                        suveryAnswer.setLoopNumber(2);
                                        answers.push(suveryAnswer);
                                        break;
                                    }
                                }
                                if (!updateItem) {
                                    suveryAnswer.setLoopCount(loopTwo);
                                    suveryAnswer.setLoopNumber(2);
                                    answers.add(suveryAnswer);
                                }
                            } else if (order >= 244 && order <= 268) {
                                skyForce(currantPosition + "Three" + loopThree);
                                suveryAnswer.setExtra(currantPosition + "Three" + loopThree);

                                boolean updateItem = false;
                                for (AnswerModel.SuveryAnswer temp : answers) {
                                    if (temp.getQuestionId().equals(suveryAnswer.getQuestionId()) && temp.getLoopCount() == loopThree) {
                                        updateItem = true;
                                        answers.remove(temp);
                                        suveryAnswer.setLoopCount(loopThree);
                                        suveryAnswer.setLoopNumber(3);
                                        answers.push(suveryAnswer);
                                        break;
                                    }
                                }
                                if (!updateItem) {
                                    suveryAnswer.setLoopCount(loopThree);
                                    suveryAnswer.setLoopNumber(3);
                                    answers.add(suveryAnswer);
                                }
                            } else if (order >= 280 && order <= 300) {
                                skyForce(currantPosition + "Four" + loopFour);
                                suveryAnswer.setExtra(currantPosition + "Four" + loopFour);

                                boolean updateItem = false;
                                for (AnswerModel.SuveryAnswer temp : answers) {
                                    if (temp.getQuestionId().equals(suveryAnswer.getQuestionId()) && temp.getLoopCount() == loopFour) {
                                        updateItem = true;
                                        answers.remove(temp);
                                        suveryAnswer.setLoopCount(loopFour);
                                        suveryAnswer.setLoopNumber(4);
                                        answers.push(suveryAnswer);
                                        break;
                                    }
                                }
                                if (!updateItem) {
                                    suveryAnswer.setLoopCount(loopFour);
                                    suveryAnswer.setLoopNumber(4);
                                    answers.add(suveryAnswer);
                                }
                            } else if (order >= 305 && order <= 336) {
                                executeAnswers();
                                skyForce(currantPosition + "FIVE" + loopFive);
                                suveryAnswer.setExtra(currantPosition + "FIVE" + loopFive);

                                boolean updateItem = false;
                                for (AnswerModel.SuveryAnswer temp : answers) {
                                    if (temp.getQuestionId().equals(suveryAnswer.getQuestionId()) && temp.getLoopCount() == loopFive) {
                                        updateItem = true;
                                        answers.remove(temp);
                                        suveryAnswer.setLoopCount(loopFive);
                                        suveryAnswer.setLoopNumber(5);
                                        answers.push(suveryAnswer);
                                        break;
                                    }
                                }
                                if (!updateItem) {
                                    suveryAnswer.setLoopCount(loopFive);
                                    suveryAnswer.setLoopNumber(5);
                                    answers.add(suveryAnswer);
                                }
                            } else {
                                boolean updateItem = false;
                                for (AnswerModel.SuveryAnswer temp : answers) {
                                    if (temp.getQuestionId().equals(suveryAnswer.getQuestionId())) {
                                        updateItem = true;
                                        answers.remove(temp);
                                        answers.push(suveryAnswer);
                                        break;
                                    }
                                }
                                if (!updateItem) {
                                    answers.add(suveryAnswer);
                                }
                            }
                        } else {
                            boolean updateItem = false;
                            for (AnswerModel.SuveryAnswer temp : answers) {
                                if (temp.getQuestionId().equals(suveryAnswer.getQuestionId())) {
                                    updateItem = true;
                                    answers.remove(temp);
                                    answers.push(suveryAnswer);
                                    break;
                                }
                            }
                            if (!updateItem) {
                                answers.add(suveryAnswer);
                            }
                        }

//                    if (!prevSteps.contains(nextPosition)) {
                        prevSteps.push(nextPosition);
//                    }
                        lastPosition1 = currantPosition;

                        viewPager.setCurrentItem(nextPosition);
                    } else {
                        Stack<AnswerModel.SuveryAnswer> answers = answerModel.getSuveryAnswers();
                        answers.add(suveryAnswer);
//                    if (!prevSteps.contains(nextPosition)) {
                        prevSteps.push(nextPosition);
//                    }

                        backgroundStart();
                        lastPosition1 = currantPosition;

                        if (nextPosition == 0) {
                        } else
                            viewPager.setCurrentItem(nextPosition);
                    }

                    break;
                case R.id.prev:
                    resumeFlag = true;
                    if (!prevSteps.isEmpty()) {
                        if (nextPosition == prevSteps.peek()) {
                            //    prevSteps.pop();
                        }
                        nextSteps.push(prevSteps.peek());
                        viewPager.setCurrentItem(prevSteps.pop());
                    } else {
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
        } catch (Exception e) {

        }
    }

    private void skyForce(String id) {
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle.containsKey(id)) {
                int lastPosition = bundle.getInt(id);
                if (lastPosition == nextPosition)
                    resumeFlag = true;
                else
                    resumeFlag = false;
            } else
                resumeFlag = false;
        } catch (Exception e) {
            resumeFlag = false;
        } finally {
            Intent intent = getIntent();
            intent.putExtra(id, nextPosition);
            setIntent(intent);
        }

    }


    public List<PersonModel> getPersonModels() throws Exception {
        return personModels;
    }

    int personcount = 0;
    int childcount = 0;
    int loopthreeCount = 0;
    int loopFourCount = 0;

    public Map<String, List<PersonModel>> getPersionsList() {
        personcount = 0;
        childcount = 0;
        loopthreeCount = 0;
        loopFourCount = 0;
        Map<String, List<PersonModel>> stringPersonModelMap = new HashMap<>();
        List<PersonModel> mansList = new ArrayList<>();
        List<PersonModel> childsList = new ArrayList<>();
        List<PersonModel> loopThree = new ArrayList<>();
        List<PersonModel> loopFour = new ArrayList<>();

        for (PersonModel personModel : personModels) {
            if (personModel.getTypo().equals(persons)) {
                personcount++;
                personModel.setLoopCount(personcount);
                mansList.add(personModel);
            } else if (personModel.getTypo().equals(child)) {
                childcount++;
                personModel.setLoopCount(childcount);
                childsList.add(personModel);
            } else if (personModel.getTypo().equals(loopThreesPerson)) {
                loopthreeCount++;
                personModel.setLoopCount(loopthreeCount);
                loopThree.add(personModel);
            } else if (personModel.getTypo().equals(loopFourPerson)) {
                loopFourCount++;
                personModel.setLoopCount(loopFourCount);
                loopFour.add(personModel);
            }
        }
        stringPersonModelMap.put(persons, mansList);
        stringPersonModelMap.put(child, childsList);
        stringPersonModelMap.put(loopThreesPerson, loopThree);
        stringPersonModelMap.put(loopFourPerson, loopFour);
        return stringPersonModelMap;
    }


    public void executeAnswers() {
        loopFive = 0;
        PersonModel personModel = null;
        List<AnswerModel.SuveryAnswer> suveryAnswer = answerModel.getSuveryAnswers();
        Collections.reverse(suveryAnswer);
        for (AnswerModel.SuveryAnswer answer : suveryAnswer) {
            if (answer.getQuestionId().equals("hid_148") && !TextUtils.isEmpty(answer.getAnswer())) {
                if (!answer.isLoadedName()) {
                    answer.setLoadedName(true);
                    personModel = new PersonModel();
                    personModel.setId("" + new Random().nextInt(99));
                    personModel.setTypo("hh_person");
                    personModel.setAge(answer.getAnswer());
                    loopSexy:
                    for (AnswerModel.SuveryAnswer innerAnswer : suveryAnswer) {
                        if (innerAnswer.getQuestionId().equals("hid_144") && !TextUtils.isEmpty(answer.getAnswer())) {
                            if (!innerAnswer.isLoadedSex()) {
                                innerAnswer.setLoadedSex(true);
                                personModel.setSex(innerAnswer.getAnswer());
                                break loopSexy;
                            }
                        }
                    }
                    loopName:
                    for (AnswerModel.SuveryAnswer innerAnswer : suveryAnswer) {
                        if (innerAnswer.getQuestionId().equals("hid_140") && !TextUtils.isEmpty(answer.getAnswer())) {
                            if (!innerAnswer.isLoadedName()) {
                                innerAnswer.setLoadedName(true);
                                personModel.setName(innerAnswer.getAnswer());
                                break loopName;
                            }
                        }
                        if (!personModels.contains(personModel)) {
                            personModels.add(personModel);
                            //loopOne++;
                            areaModel.setMemberRtfModels(personModels);
                        }
                    }
                }

            }
            if (answer.getQuestionId().equals("hid_180") && !TextUtils.isEmpty(answer.getAnswer())) {
                if (!answer.isLoadedName()) {
                    answer.setLoadedName(true);
                    personModel = new PersonModel();
                    personModel.setTypo("hh_children");
                    personModel.setId("" + new Random().nextInt(99));
                    personModel.setAge(answer.getAnswer());
                    innerLoop:
                    for (AnswerModel.SuveryAnswer innerAnswer : suveryAnswer) {
                        if (innerAnswer.getQuestionId().equals("hid_176") && !TextUtils.isEmpty(answer.getAnswer())) {
                            if (!innerAnswer.isLoadedSex()) {
                                innerAnswer.setLoadedSex(true);
                                personModel.setSex(innerAnswer.getAnswer());
                                break innerLoop;
                            }
                        }
                    }
                    if (!personModels.contains(personModel)) {
                        personModels.add(personModel);
                        areaModel.setMemberRtfModels(personModels);
                    }
                }
            }

            if (answer.getQuestionId().equals("hid_264") && !TextUtils.isEmpty(answer.getAnswer())) {
                if (!answer.isDie_with_tb_drug_()) {
                    answer.setDie_with_tb_drug_(true);
                    personModel = new PersonModel();
                    personModel.setTypo(loopThreesPerson);
                    personModel.setId("" + new Random().nextInt(99));
                    personModel.setDie_with_tb_drug(answer.getAnswer());
                    innerLoop:
                    for (AnswerModel.SuveryAnswer innerAnswer : suveryAnswer) {
                        if (innerAnswer.getQuestionId().equals("hid_260") && !TextUtils.isEmpty(answer.getAnswer())) {
                            if (!innerAnswer.isDie_after_six_week_abortion_()) {
                                innerAnswer.setDie_after_six_week_abortion_(true);
                                personModel.setDie_after_six_week_abortion(innerAnswer.getAnswer());
                                break innerLoop;
                            }
                        }
                    }

                    for (AnswerModel.SuveryAnswer innerAnswer : suveryAnswer) {
                        if (innerAnswer.getQuestionId().equals("hid_256") && !TextUtils.isEmpty(answer.getAnswer())) {
                            if (!innerAnswer.isDie_when_ansent_()) {
                                innerAnswer.setDie_when_ansent_(true);
                                personModel.setDie_when_ansent(innerAnswer.getAnswer());
                                break;
                            }
                        }
                    }


                    for (AnswerModel.SuveryAnswer innerAnswer : suveryAnswer) {
                        if (innerAnswer.getQuestionId().equals("hid_252") && !TextUtils.isEmpty(answer.getAnswer())) {
                            if (!innerAnswer.isDod_()) {
                                innerAnswer.setDod_(true);
                                personModel.setDod(innerAnswer.getAnswer());
                                break;
                            }
                        }
                    }

                    for (AnswerModel.SuveryAnswer innerAnswer : suveryAnswer) {
                        if (innerAnswer.getQuestionId().equals("hid_248") && !TextUtils.isEmpty(answer.getAnswer())) {
                            if (!innerAnswer.isDob_()) {
                                innerAnswer.setDob_(true);
                                personModel.setDob(innerAnswer.getAnswer());
                                break;
                            }
                        }
                    }


                    for (AnswerModel.SuveryAnswer innerAnswer : suveryAnswer) {
                        if (innerAnswer.getQuestionId().equals("hid_244") && !TextUtils.isEmpty(answer.getAnswer())) {
                            if (!innerAnswer.isLoadedSex()) {
                                innerAnswer.setLoadedSex(true);
                                personModel.setSex(innerAnswer.getAnswer());
                                break;
                            }
                        }
                    }


                    if (!personModels.contains(personModel)) {
                        personModels.add(personModel);
                        areaModel.setMemberRtfModels(personModels);
                    }
                }

            }


            //// TODO: 16/07/16  loop four handling  for storing data
            if (answer.getQuestionId().equals("hid_296") && !TextUtils.isEmpty(answer.getAnswer())) {
                if (!answer.isDie_with_tb_drug_()) {
                    answer.setDie_with_tb_drug_(true);
                    personModel = new PersonModel();
                    personModel.setTypo(loopFourPerson);
                    personModel.setId("" + new Random().nextInt(99));
                    personModel.setDie_with_tb_drug(answer.getAnswer());
                    innerLoop:
                    for (AnswerModel.SuveryAnswer innerAnswer : suveryAnswer) {
                        if (innerAnswer.getQuestionId().equals("hid_292") && !TextUtils.isEmpty(answer.getAnswer())) {
                            if (!innerAnswer.isDie_after_six_week_abortion_()) {
                                innerAnswer.setDie_after_six_week_abortion_(true);
                                personModel.setDie_after_six_week_abortion(innerAnswer.getAnswer());
                                break innerLoop;
                            }
                        }
                    }

                    for (AnswerModel.SuveryAnswer innerAnswer : suveryAnswer) {
                        if (innerAnswer.getQuestionId().equals("hid_288") && !TextUtils.isEmpty(answer.getAnswer())) {
                            if (!innerAnswer.isDie_when_ansent_()) {
                                innerAnswer.setDie_when_ansent_(true);
                                personModel.setDie_when_ansent(innerAnswer.getAnswer());
                                break;
                            }
                        }
                    }


                    for (AnswerModel.SuveryAnswer innerAnswer : suveryAnswer) {
                        if (innerAnswer.getQuestionId().equals("hid_284") && !TextUtils.isEmpty(answer.getAnswer())) {
                            if (!innerAnswer.isDob_()) {
                                innerAnswer.setDob_(true);
                                personModel.setDob(innerAnswer.getAnswer());
                                break;
                            }
                        }
                    }


                    for (AnswerModel.SuveryAnswer innerAnswer : suveryAnswer) {
                        if (innerAnswer.getQuestionId().equals("hid_280") && !TextUtils.isEmpty(answer.getAnswer())) {
                            if (!innerAnswer.isLoadedSex()) {
                                innerAnswer.setLoadedSex(true);
                                personModel.setSex(innerAnswer.getAnswer());
                                break;
                            }
                        }
                    }


                    if (!personModels.contains(personModel)) {
                        personModels.add(personModel);
                        areaModel.setMemberRtfModels(personModels);
                    }
                }


            }


            if (answer.getQuestionId().equals("hid_140") && !TextUtils.isEmpty(answer.getAnswer())) {
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
                //loopTwo++;
            } else if (answer.getQuestionId().equals("hid_244") && !TextUtils.isEmpty(answer.getAnswer())) {
//                loopThree++;
            } else if (answer.getQuestionId().equals("hid_280") && !TextUtils.isEmpty(answer.getAnswer())) {
                //     loopFour++;
            } else if (answer.getQuestionId().equals("hid_328") && !TextUtils.isEmpty(answer.getAnswer())) {
                loopFive++;
            }
        }

    }


    void backgroundStart() throws Exception {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    SyncData();
                    if (status != null) {
                        if (status.equalsIgnoreCase("fini"))
                            clearSaveState(key, getIntent().getExtras().getString("SURVEY").equals("ONE"));
                        else
                            pauseSurvey();
                    }
                } catch (Exception e) {

                }
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
                            if (status.equalsIgnoreCase("fini"))
                                clearSaveState(key, getIntent().getExtras().getString("SURVEY").equals("ONE"));
                            else
                                pauseSurvey();

//                            Intent intent = new Intent(FragmentControler.this, SurveyTwo.class);
                            //                          intent.putExtras(getIntent());
                            //                        startActivity(intent);
                            finish();
                        } else {
                            if (status.equalsIgnoreCase("fini"))
                                removePerson(key, getIntent().getStringExtra("Name"));
                            else
                                pauseSurvey();
                            findViewById(R.id.main_layout).setVisibility(View.GONE);
                            findViewById(R.id.finish_this).setVisibility(View.VISIBLE);
                            findViewById(R.id.finish_this).setOnClickListener(FragmentControler.this);
                        }
                    }
                });

            }
        }.execute(null, null, null);
    }


    private void SyncData() throws Exception {


        if (areaModel != null && personModels != null) {
            areaModel.setMemberRtfModels(personModels);
            answerModel.setAreaModel(areaModel);
            String string = new Gson().toJson(areaModel);
            Log.d("AREAMODEL", string);
        }

        if (status != null) {
            answerModel.setStatus(status);
        }

        answerModel.setGenerated_survey(key);
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
                        clearSaveState(key, getIntent().getExtras().getString("SURVEY").equals("ONE"));
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

    boolean executed = false;
    int order = 0;

    @Override
    public void getNextPosition(int position, QuestionsModel questionsModel, String answer, boolean isNew, Object object, boolean localIsRepeater) {
        try {

            Log.d("Answer", answer);
            Log.d("Position", "" + position);

            order = position;
            Log.d("question Id ==", questionsModel.getQuestionId());
            int next = executeQuestionId(position);
            Log.d("Executed Position", "" + next);

//            positionsMap.put(currantPosition,new Positions(nextPosition,lastPosition1));
//            System.out.println("C = "+currantPosition+" L ="+lastPosition1+" N ="+next);


            if (questionsModel.getQuestionId().equals("hid_160") || questionsModel.getQuestionId().equals("hid_164")) {
                if (answer.equals("1")) {
                    loopOne = getPersionsList().get(FragmentControler.persons).size() + 1;
                }
            }
            if (questionsModel.getQuestionId().equals("hid_188") || questionsModel.getQuestionId().equals("hid_192")) {
                if (answer.equals("1")) {
                    loopTwo = getPersionsList().get(FragmentControler.child).size() + 1;
                }
            }


            if (questionsModel.getQuestionId().equals("hid_268")) {
                if (answer.equals("1")) {
                    executeAnswers();
                    loopThree = getPersionsList().get(FragmentControler.loopThreesPerson).size() + 1;
                }
            }
            if (questionsModel.getQuestionId().equals("hid_300")) {
                if (answer.equals("1")) {
                    executeAnswers();
                    loopFour = getPersionsList().get(FragmentControler.loopFourPerson).size() + 1;
                }
            }


            int childSize = 0;

            List<PersonModel> personModels = getPersionsList().get(child);
            for (PersonModel personModel : personModels) {
                if (Integer.parseInt(personModel.getAge()) <= 5) {
                    childSize++;
                }
            }


            if (questionsModel.getQuestionId().equals("hid_148")) {
                try {
                    int age = Integer.parseInt(answer);
                    if (age > 55) {
                        nextPosition = executeQuestionId(160);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (questionsModel.getQuestionId().equals("hid_276")) {
                if (answer.equals("1")) {
                    nextPosition = executeQuestionId(277);
                    for (QuestionsModel.Answer x : questionsModel.getAnswers())
                        x.setOptionNext(277);

                } else {
                    if (childSize == 0) {
                        nextPosition = executeQuestionId(365);
                        for (QuestionsModel.Answer x : questionsModel.getAnswers())
                            x.setOptionNext(365);
                    } else if (childSize == 1) {
                        nextPosition = executeQuestionId(305);
                        for (QuestionsModel.Answer x : questionsModel.getAnswers())
                            x.setOptionNext(305);
                    } else if (childSize >= 2) {
                        nextPosition = executeQuestionId(305);
                        for (QuestionsModel.Answer x : questionsModel.getAnswers()) {
                            x.setOptionNext(305);
                        }
                    } else {

                    }
                }
            }

            fetchViewData(nextPosition, answer, questionsModel);

            if (localIsRepeater) {
                isRepeater = localIsRepeater;
            }

            findViewById(R.id.next).setVisibility(View.VISIBLE);
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

            if (isNew)
                runtime(answer, questionsModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    private void runtime(String ans, QuestionsModel questionsModel) throws Exception {
        //hid_148 //

        try {
            if (ans.contains("{")) {
                HashMap<String, String> hashMap = new Gson().fromJson(ans, HashMap.class);
                if (hashMap.containsKey("text")) {
                    ans = hashMap.get("text");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (questionsModel.getQuestionId().equals("hid_180")) {
            int answer = Integer.parseInt(ans);
            if (answer >= 12 && answer <= 55) {
                List<AnswerModel.SuveryAnswer> reverseList = answerModel.getSuveryAnswers();
                //Collections.reverse(reverseList);
                for (AnswerModel.SuveryAnswer suveryAnswer : reverseList) {
                    if (suveryAnswer.getQuestionId().equals("hid_176")) {
                        if (suveryAnswer.getAnswer().equals("1")) {
                            nextPosition = executeQuestionId(188);
                        } else {
                            nextPosition = executeQuestionId(184);
                        }
                        break;
                    }
                }
            } else {
                nextPosition = executeQuestionId(188);
            }
        } else if (questionsModel.getQuestionId().equals("hid_284")) {
            boolean zero = false;
            if (ans.contains("{")) {
                HashMap<Integer, String> selectMap = new Gson().fromJson(ans, HashMap.class);
                if (selectMap.containsKey("1")) {
                    ans = selectMap.get("1");
                }
                if (selectMap.containsKey("0")) {
                    ans = selectMap.get("0");
                    zero = true;
                }
            }


            int answer = Integer.parseInt(ans);
            if (answer == 99) {
                nextPosition = executeQuestionId(296);
            } else if (((answer >= 12 && answer <= 55) || answer == 88) && !zero) {
                if (MainYear) {
                    Stack<AnswerModel.SuveryAnswer> reverseList = answerModel.getSuveryAnswers();
//                    Collections.reverse(reverseList);
                    for (AnswerModel.SuveryAnswer suveryAnswer : reverseList) {
                        if (suveryAnswer.getQuestionId().equals("hid_280")) {
                            Log.d("MAPAnswer", suveryAnswer.getAnswer());
                            if (suveryAnswer.getAnswer().equals("1")) {
                                nextPosition = executeQuestionId(296);
                            } else {
                                nextPosition = executeQuestionId(288);
                            }
                            break;
                        }
                    }
                } else {
                    nextPosition = executeQuestionId(296);
                }
            } else {
                nextPosition = executeQuestionId(296);
            }
        } else if (questionsModel.getQuestionId().equals("iid_468")) {

            try {

                boolean isYear = false;
                if (ans.contains("{")) {
                    HashMap<Integer, String> selectMap = new Gson().fromJson(ans, HashMap.class);
                    if (selectMap.containsKey("1")) {
                        ans = selectMap.get("1");
                        DynamicFragment.spinners.get(0).setSelection(0);
                        DynamicFragment.spinners.get(2).setSelection(0);
                    }
                    if (selectMap.containsKey("0")) {
                        ans = selectMap.get("0");
                        DynamicFragment.spinners.get(1).setSelection(0);
                        DynamicFragment.spinners.get(2).setSelection(0);

                    }
                    if (selectMap.containsKey("2")) {
                        ans = selectMap.get("2");
                        DynamicFragment.spinners.get(0).setSelection(0);
                        DynamicFragment.spinners.get(1).setSelection(0);

                        isYear = true;
                    }
                }


                int answer = Integer.parseInt(ans);
                if (isYear) {
                    if (answer > 5) {
                        nextPosition = executeQuestionId(476);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (questionsModel.getQuestionId().equals("iid_424")) {
            Stack<AnswerModel.SuveryAnswer> reverseList = answerModel.getSuveryAnswers();
//                    Collections.reverse(reverseList);
            for (AnswerModel.SuveryAnswer suveryAnswer : reverseList) {
                if (suveryAnswer.getQuestionId().equals("iid_176")) {
                    if (suveryAnswer.getAnswer().equals("1")) {
                        if (looperHandler(reverseList, "iid_128"))
                            if (looperHandler(reverseList, "iid_132"))
                                nextPosition = executeQuestionId(532);
                            else
                                nextPosition = executeQuestionId(488);
                        else
                            nextPosition = executeQuestionId(488);


                    } else {
                        nextPosition = executeQuestionId(428);
                    }
                    break;
                }
            }
        } else if (questionsModel.getQuestionId().equals("iid_436")) {
            int answer = Integer.parseInt(ans);
            if (answer == 99 || answer == 1) {
                nextPosition = executeQuestionId(440);
            } else if (answer == 2) {
                Stack<AnswerModel.SuveryAnswer> reverseList = answerModel.getSuveryAnswers();
                Collections.reverse(reverseList);
                for (AnswerModel.SuveryAnswer suveryAnswer : reverseList) {
                    if (suveryAnswer.getQuestionId().equals("iid_128")) {
                        if (suveryAnswer.getAnswer().equals("1"))
                            if (looperHandler(reverseList, "iid_132"))
                                nextPosition = executeQuestionId(532);
                            else
                                nextPosition = executeQuestionId(488);
                        else
                            nextPosition = executeQuestionId(488);
                        break;
                    }
                }
            } else {
                nextPosition = executeQuestionId(488);
            }
        } else if (questionsModel.getQuestionId().equals("iid_484")) {
            Stack<AnswerModel.SuveryAnswer> reverseList = answerModel.getSuveryAnswers();
            Collections.reverse(reverseList);
            for (AnswerModel.SuveryAnswer suveryAnswer : reverseList) {
                if (suveryAnswer.getQuestionId().equals("iid_128")) {
                    if (suveryAnswer.getAnswer().equals("1"))
                        if (looperHandler(reverseList, "iid_132"))
                            nextPosition = executeQuestionId(532);
                        else
                            nextPosition = executeQuestionId(488);
                    else
                        nextPosition = executeQuestionId(488);
                    break;
                }
            }
        } else if (questionsModel.getQuestionId().equals("hid_248")) {
            Log.d("answer", ans);
            int answer = Integer.parseInt(ans);
            if (answer >= 18 && answer <= 55) {
                for (AnswerModel.SuveryAnswer suveryAnswer : answerModel.getSuveryAnswers()) {
                    if (suveryAnswer.getQuestionId().equals("hid_244")) {
                        if (suveryAnswer.getAnswer().equals("1")) {
                            Log.d("if", ans);
                            nextPosition = executeQuestionId(264);
                        } else {
                            Log.d("else", ans);
                            nextPosition = executeQuestionId(256);
                        }
                        break;
                    }
                }
            } else {
                Log.d(" outer else", ans);
                nextPosition = executeQuestionId(264);
            }
        } else if (questionsModel.getQuestionId().equals("iid_108")) {
            int answer = Integer.parseInt(ans);
            if (answer < 18) {
                nextPosition = 0;
                status = "Ineligible";
            }

        } else if (questionsModel.getQuestionId().equals("iid_448")) {
            int answer = Integer.parseInt(ans);
            if (answer == 0) {
                nextPosition = executeQuestionId(476);
            } else {
                nextPosition = executeQuestionId(452);
            }
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


    boolean looperHandler(List<AnswerModel.SuveryAnswer> reverseList, String id) {

        for (AnswerModel.SuveryAnswer suveryAnswer : reverseList) {
            if (suveryAnswer.getQuestionId().equals(id)) {
                if (suveryAnswer.getAnswer().equals("1")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void hideNext() {
        findViewById(R.id.next).setVisibility(View.INVISIBLE);
    }

    @Override
    public void hidePrev() {
        findViewById(R.id.prev).setVisibility(View.INVISIBLE);
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


    private void warning() throws Exception {
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
