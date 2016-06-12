package com.oxo.haiti.ui.fargments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oxo.haiti.R;
import com.oxo.haiti.model.AnswerModel;
import com.oxo.haiti.model.AreaModel;
import com.oxo.haiti.model.QuestionsModel;
import com.oxo.haiti.ui.activity.FragmentControler;
import com.oxo.haiti.ui.base.BaseActivity;
import com.oxo.haiti.utils.CommonInterface;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jaswinderwadali on 17/05/16.
 */
public class DynamicFragment extends Fragment {

    private int UPPER_LIMIT = 10;
    private int LOWER_LIMIT = 0;
    private LinearLayout answerContainer;
    private TextView headerTextView;
    private CommonInterface commonInterface;
    private QuestionsModel questionsModel;
    private TextView questionDec;
    private View view = null;
    private AnswerModel answerModel;
    private AnswerModel.SuveryAnswer suveryAnswer = null;
    private List<String> checkBoxAnswers = null;
    private Map<Integer, CheckBox> checkboxlist;
    private LinearLayout editTextInflatedView = null;
    private CheckBox otherCheckBox = null;
    private List<View> editTextIds = new ArrayList<>();
    private Map<Integer, String> editTextAnswers = new HashMap<>();


    public static DynamicFragment getFragment(int position, CommonInterface commonInterface, QuestionsModel questionsModel, AnswerModel answerModel) {
        DynamicFragment dynamicFragment = new DynamicFragment();
        dynamicFragment.commonInterface = commonInterface;
        dynamicFragment.questionsModel = questionsModel;
        dynamicFragment.answerModel = answerModel;
        return dynamicFragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        answerContainer.removeAllViews();
                        FragmentControler fragmentControler = (FragmentControler) getActivity();
                        if (null == matchQuestion(questionsModel.getQuestionId()) || !fragmentControler.getIntent().getExtras().getString("SURVEY").equals("ONE")) {
                            for (AnswerModel.SuveryAnswer suveryAnswer : answerModel.getSuveryAnswers()) {
                                if (suveryAnswer != null && questionDec != null && questionsModel.getQuestionId().equals(suveryAnswer.getQuestionId())) {
                                    DynamicFragment.this.suveryAnswer = suveryAnswer;
                                }
                            }
                        } else {
//                            fragmentControler.hideNext();
                        }


                        if (questionsModel.getQuestionType().equals("radio"))
                            generateRadioButtonView();
                        else if (questionsModel.getQuestionType().equals("message"))
                            setMessageType();
                        else if (questionsModel.getQuestionType().equals("checkbox"))
                            checkBoxInput();
                        else if (questionsModel.getQuestionType().equals("checkbox_radio"))
                            checkBox_radio();
                        else if (questionsModel.getQuestionType().equals("number_radio"))
                            dynamicTextInput();
                        else if (questionsModel.getQuestionType().equals("number_number_radio"))
                            dynamicTextInput();
                        else if (questionsModel.getQuestionType().equals("text_text"))
                            dynamicTextInput();
                        else if (questionsModel.getQuestionType().equals("text_text_radio"))
                            dynamicTextInput();
                        else if (questionsModel.getQuestionType().equals("text_radio"))
                            dynamicTextInput();
                        else if (questionsModel.getQuestionType().equals("select"))
                            selector();
                        else if (questionsModel.getQuestionType().equals("hh_profile"))
                            showAreaView();
                        else
                            generateEt();


                        if (!TextUtils.isEmpty(questionsModel.getQuestionDesc()))
                            questionDec.setText(Html.fromHtml(questionsModel.getQuestionDesc()));
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                        showDialogMessage("Something went wrong need to contact developer");
                    } finally {
                    }


                }
            }, 500);
        } else {
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dynamic_fragment, container, false);

        try {
            headerTextView = (TextView) view.findViewById(R.id.header);
            answerContainer = (LinearLayout) view.findViewById(R.id.ans_container);
            questionDec = (TextView) view.findViewById(R.id.questionDec);
            insertDate();
            headerTextView.setText(Html.fromHtml(questionsModel.getQuestionText()));


//            headerTextView = (TextView) view.findViewById(R.id.header);
//            answerContainer = (LinearLayout) view.findViewById(R.id.ans_container);
//            questionDec = (TextView) view.findViewById(R.id.questionDec);
//            insertDate();
//            headerTextView.setText(Html.fromHtml(questionsModel.getQuestionText()));
//
//            FragmentControler fragmentControler = (FragmentControler) getActivity();
//            if (!fragmentControler.getRepeater())
//                for (AnswerModel.SuveryAnswer suveryAnswer : answerModel.getSuveryAnswers()) {
//                    if (suveryAnswer != null && questionDec != null && questionsModel.getQuestionId().equals(suveryAnswer.getQuestionId())) {
//                        this.suveryAnswer = suveryAnswer;
//                    }
//                }
//
//
//            if (questionsModel.getQuestionType().equals("radio"))
//                generateRadioButtonView();
//            else if (questionsModel.getQuestionType().equals("message"))
//                setMessageType();
//            else if (questionsModel.getQuestionType().equals("checkbox"))
//                checkBoxInput();
//            else if (questionsModel.getQuestionType().equals("checkbox_radio"))
//                checkBox_radio();
//            else if (questionsModel.getQuestionType().equals("number_radio"))
//                dynamicTextInput();
//            else if (questionsModel.getQuestionType().equals("number_number_radio"))
//                dynamicTextInput();
//            else if (questionsModel.getQuestionType().equals("text_text"))
//                dynamicTextInput();
//            else if (questionsModel.getQuestionType().equals("text_text_radio"))
//                dynamicTextInput();
//            else if (questionsModel.getQuestionType().equals("text_radio"))
//                dynamicTextInput();
//            else if (questionsModel.getQuestionType().equals("select"))
//                selector();
//            else if (questionsModel.getQuestionType().equals("hh_profile"))
//                showAreaView();
//            else
//                generateEt();
//
//
//            if (!TextUtils.isEmpty(questionsModel.getQuestionDesc()))
//                questionDec.setText(Html.fromHtml(questionsModel.getQuestionDesc()));
        } catch (Exception e) {
            showDialogMessage("Something went wrong need to contact developer");
        } finally {
            return view;
        }
    }

    private void showAreaView() {
        try {
            answerContainer.removeAllViews();
            LinearLayout linearLayout = (LinearLayout) getLayoutInflater(getArguments()).inflate(R.layout.show_area, null);
            TextView sit = (TextView) linearLayout.findViewById(R.id.sit);
            TextView gps = (TextView) linearLayout.findViewById(R.id.gps);
            TextView block = (TextView) linearLayout.findViewById(R.id.block);
            TextView hh = (TextView) linearLayout.findViewById(R.id.hh);
            TextView indu = (TextView) linearLayout.findViewById(R.id.ind);
            TextView lat = (TextView) linearLayout.findViewById(R.id.lat);
            TextView long_ = (TextView) linearLayout.findViewById(R.id.long_);
            TextView desc = (TextView) linearLayout.findViewById(R.id.decs_);
            TextView name = (TextView) linearLayout.findViewById(R.id.name_);
            FragmentControler fragmentControler = (FragmentControler) getActivity();
            AreaModel areaModel = fragmentControler.getAreaModel();
            sit.setText(areaModel.getSit());
            gps.setText(areaModel.getGps());
            block.setText(areaModel.getBlock());
            hh.setText(areaModel.getHH());
            indu.setText(areaModel.get_id());
            lat.setText(areaModel.getLat());
            long_.setText(areaModel.get_long());
            desc.setText(areaModel.getDesc());
            name.setText(areaModel.getName());
            answerContainer.addView(linearLayout);
        } catch (Exception e) {
            Log.d("OXO", "Something went wrong");
        }
    }

    private void selector() {
        ArrayList<String> spinnerArray = new ArrayList<String>();

        for (int i = 0; i < questionsModel.getAnswers().size(); i++) {
            spinnerArray.add(questionsModel.getAnswers().get(i).getOptionText());
        }

        Spinner spinner = new Spinner(getContext());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);
        answerContainer.addView(spinner);

//        for (int i = 0; i < questionsModel.getAnswers().size(); i++) {
//            if (suveryAnswer != null)
//                if (suveryAnswer.getAnswer().equals(questionsModel.getAnswers().get(i).getOptionValue()))
//                    spinner.setSelection(i);
//
//        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    BaseActivity baseActivity = (BaseActivity) getActivity();
                    baseActivity.messageToast("Please select Somthing");
                    commonInterface.hideNext();
                } else {
                    Object status = questionsModel.getAnswers().get(position).getOptionStatus();
                    commonInterface.getNextPosition(questionsModel.getAnswers().get(position).getOptionNext(), questionsModel, questionsModel.getAnswers().get(position).getOptionValue(), true, status, questionsModel.getAnswers().get(position).isRepeater());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    RadioGroup rg = null;

    private void dynamicTextInput() {
        final RadioButton[] rb = new RadioButton[questionsModel.getAnswers().size()];
        rg = new RadioGroup(getContext()); //create the RadioGroup
        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for (int i = 0; i < questionsModel.getAnswers().size(); i++) {
            if (questionsModel.getAnswers().get(i).getOptionType().equals("number")) {
                View id = generateEt(questionsModel.getAnswers().get(i).getMax(), questionsModel.getAnswers().get(i).getMin(), i);
                editTextIds.add(id);
            }
            if (questionsModel.getAnswers().get(i).getOptionType().equals("radio")) {
                rb[i] = new RadioButton(getContext());
                rg.addView(rb[i]);
                rb[i].setText(Html.fromHtml(questionsModel.getAnswers().get(i).getOptionText()));
                rb[i].setId(i + 100);
                rb[i].setTag(i);
                if (suveryAnswer != null)
                    if (questionsModel.getAnswers().get(i).getOptionValue().equals(suveryAnswer.getAnswer())) {
                        ((RadioButton) rg.getChildAt(i)).setChecked(true);
                    }
            }
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                              @Override
                                              public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                  RadioButton checkedRadioButton = (RadioButton) rg.findViewById(checkedId);
                                                  boolean isChecked = checkedRadioButton.isChecked();
                                                  if (isChecked) {
                                                      for (View id : editTextIds) {
                                                          EditText editText = (EditText) id;
                                                          if (editText != null)
                                                              editText.setText("");
                                                      }
                                                      int position = (Integer) checkedRadioButton.getTag();
                                                      if (questionsModel.getAnswers().get((Integer) checkedRadioButton.getTag()).getOptionPrompt() != null)
                                                          showDialogMessage(questionsModel.getAnswers().get((Integer) checkedRadioButton.getTag()).getOptionPrompt().toString());
                                                      int radioPosition = (Integer) checkedRadioButton.getTag();
                                                      Object status = questionsModel.getAnswers().get(radioPosition).getOptionStatus();
                                                      commonInterface.getNextPosition(questionsModel.getAnswers().get(radioPosition).getOptionNext(), questionsModel, questionsModel.getAnswers().get(radioPosition).getOptionValue(), true, status, questionsModel.getAnswers().get(position).isRepeater());
                                                      if (!TextUtils.isEmpty(questionsModel.getAnswers().get(position).getOptionValue()) && questionsModel.getAnswers().get(position).getOptionValue().equals("66")) {
                                                          commonInterface.hideNext();
                                                          if (editTextInflatedView != null) {
                                                              //    editTextInflatedView.removeAllViews();
                                                          }
                                                          generateEt();
                                                      }
                                                  } else {
                                                      //   editTextInflatedView.removeAllViews();
                                                  }
                                              }
                                          }

            );
        }
        answerContainer.addView(rg);
    }

    void insertDate() {
        String str = questionsModel.getQuestionText();
        if (str.contains("##")) {
            String[] result = str.split("##");
            if (!TextUtils.isEmpty(result[1])) {
                String finalDate = new DateTime().minusSeconds(Integer.parseInt(result[1])).toString("MM/yyyy");
                String s = str.replace("##" + result[1] + "##", finalDate);
                questionsModel.setQuestionText(s);
            }
        }
    }


    private void generateRadioButtonView() {
        final RadioButton[] rb = new RadioButton[questionsModel.getAnswers().size()];
        final RadioGroup rg = new RadioGroup(getContext()); //create the RadioGroup
        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for (int i = 0; i < questionsModel.getAnswers().size(); i++) {
            rb[i] = new RadioButton(getContext());
            rg.addView(rb[i]);
            rb[i].setText(Html.fromHtml(questionsModel.getAnswers().get(i).getOptionText()));
            rb[i].setId(i + 100);
            rb[i].setTag(i);
            if (suveryAnswer != null)
                if (questionsModel.getAnswers().get(i).getOptionValue().equals(suveryAnswer.getAnswer())) {
                    ((RadioButton) rg.getChildAt(i)).setChecked(true);
                }

        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                          @Override
                                          public void onCheckedChanged(RadioGroup group, int checkedId) {
                                              RadioButton checkedRadioButton = (RadioButton) rg.findViewById(checkedId);
                                              boolean isChecked = checkedRadioButton.isChecked();
                                              if (isChecked) {
                                                  int position = (Integer) checkedRadioButton.getTag();
                                                  if (questionsModel.getAnswers().get((Integer) checkedRadioButton.getTag()).getOptionPrompt() != null)
                                                      showDialogMessage(questionsModel.getAnswers().get((Integer) checkedRadioButton.getTag()).getOptionPrompt().toString());
                                                  int radioPosition = (Integer) checkedRadioButton.getTag();
                                                  Object status = questionsModel.getAnswers().get(radioPosition).getOptionStatus();
                                                  commonInterface.getNextPosition(questionsModel.getAnswers().get(radioPosition).getOptionNext(), questionsModel, questionsModel.getAnswers().get(radioPosition).getOptionValue(), true, status, questionsModel.getAnswers().get(position).isRepeater());
                                                  if (!TextUtils.isEmpty(questionsModel.getAnswers().get(position).getOptionValue()) && questionsModel.getAnswers().get(position).getOptionValue().equals("66")) {
                                                      commonInterface.hideNext();
                                                      if (editTextInflatedView != null) {
                                                          editTextInflatedView.removeAllViews();
                                                      }
                                                      generateEt();
                                                  } else {
                                                      if (editTextInflatedView != null) {
                                                          editTextInflatedView.removeAllViews();
                                                      }
                                                  }
                                              } else {
                                                  if (editTextInflatedView != null)
                                                      editTextInflatedView.removeAllViews();
                                              }
                                          }
                                      }

        );
        answerContainer.addView(rg);
    }


    private void generateEt() {
        editTextInflatedView = (LinearLayout) getLayoutInflater(getArguments()).inflate(R.layout.input_text, null);
        EditText editText = (EditText) editTextInflatedView.findViewById(R.id.text_et);
        if (questionsModel.getQuestionType().contains("number")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            UPPER_LIMIT = questionsModel.getMax();
            LOWER_LIMIT = questionsModel.getMin();

            UPPER_LIMIT = questionsModel.getMax();
            LOWER_LIMIT = questionsModel.getMin();


        }
        if (suveryAnswer != null)
            editText.setText(suveryAnswer.getAnswer());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    boolean patternFlag = true;
                    if (questionsModel.getPattern() != null) {
                        patternFlag = s.toString().matches(questionsModel.getPattern());
                    }
                    boolean matchCase = true;
                    if (!TextUtils.isEmpty(questionsModel.getMatch_with()) && matchQuestion(questionsModel.getMatch_with()) != null) {
                        matchCase = s.toString().equals(matchQuestion(questionsModel.getMatch_with()));
                    }
                    if (matchCase) {
                        if (patternFlag) {
                            if (questionsModel.getQuestionType().contains("number")) {
                                if (UPPER_LIMIT < Integer.parseInt(s.toString()) || Integer.parseInt(s.toString()) < LOWER_LIMIT) {
                                    commonInterface.hideNext();
                                    BaseActivity baseActivity = (BaseActivity) getActivity();
                                    baseActivity.messageToast("Antre yon nimewo/kantite antre " + LOWER_LIMIT + " a " + UPPER_LIMIT + ".");
                                } else {
                                    Object status = questionsModel.getAnswers().get(0).getOptionStatus();
                                    commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext(), questionsModel, s.toString(), true, status, questionsModel.getAnswers().get(0).isRepeater());
                                }
                            } else {
                                Object status = questionsModel.getAnswers().get(0).getOptionStatus();
                                commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext(), questionsModel, s.toString(), true, status, questionsModel.getAnswers().get(0).isRepeater());
                                commonInterface.questionId(String.valueOf(questionsModel.getAnswers().get(0).getOptionNext()), questionsModel, s.toString());
// view.setTag(new Temp(questionsModel.getAnswers().get(0).getOptionNext()  , questionsModel, s.toString(), true));
                            }
                        } else {
                            BaseActivity baseActivity = (BaseActivity) getActivity();
                            baseActivity.messageToast("Pattern not match");
                        }
                    } else {
                        BaseActivity baseActivity = (BaseActivity) getActivity();
                        baseActivity.messageToast("not match with prev");
                    }
                } else
                    commonInterface.hideNext();
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        answerContainer.addView(editTextInflatedView);

    }


    private View generateEt(final int max, final int min, final int position) {
        editTextInflatedView = (LinearLayout) getLayoutInflater(getArguments()).inflate(R.layout.input_text, null);
        editTextInflatedView.setId(new Random(100).nextInt());
        if (suveryAnswer != null) {
            try {
                editTextAnswers = new Gson().fromJson(suveryAnswer.getAnswer(), HashMap.class);
            } catch (Exception e) {

            }
        }
        EditText editText = (EditText) editTextInflatedView.findViewById(R.id.text_et);
        if (editTextAnswers.containsKey(position)) {
            editText.setText(editTextAnswers.get(position));
        }
        editText.setId(new Random(67).nextInt());
        if (questionsModel.getQuestionType().contains("number")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            UPPER_LIMIT = max;
            LOWER_LIMIT = min;
        }
        if (suveryAnswer != null)
            editText.setText(suveryAnswer.getAnswer());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (rg != null) {
                        int id = rg.getCheckedRadioButtonId();
                        if (id != -1) {
                            RadioButton radioButton = (RadioButton) rg.findViewById(id);
                            radioButton.setChecked(false);
                        }
                    }
                    if (questionsModel.getQuestionType().contains("number")) {
                        if (max < Integer.parseInt(s.toString()) || Integer.parseInt(s.toString()) < min) {
                            commonInterface.hideNext();
                            BaseActivity baseActivity = (BaseActivity) getActivity();
                            baseActivity.messageToast("Antre yon nimewo/kantite antre " + min + " a " + max + ".");
                        } else {
                            editTextAnswers.put(position, s.toString());
                            Object status = questionsModel.getAnswers().get(position).getOptionStatus();
                            commonInterface.getNextPosition(questionsModel.getAnswers().get(position).getOptionNext(), questionsModel, new Gson().toJson(editTextAnswers, HashMap.class), true, status, questionsModel.getAnswers().get(position).isRepeater());
                        }
                    } else {
                        Object status = questionsModel.getAnswers().get(position).getOptionStatus();
                        commonInterface.getNextPosition(questionsModel.getAnswers().get(position).getOptionNext(), questionsModel, s.toString(), true, status, questionsModel.getAnswers().get(position).isRepeater());
//                    view.setTag(new Temp(questionsModel.getAnswers().get(0).getOptionNext()  , questionsModel, s.toString(), true));
                    }
                } else
                    commonInterface.hideNext();
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        answerContainer.addView(editTextInflatedView);
        return editText;
    }


    private void checkBoxInput() {
        checkBoxAnswers = new ArrayList<>();
        checkboxlist = new ConcurrentHashMap<>();

        checkBoxCheck();
        for (int i = 0; i < questionsModel.getAnswers().size(); i++) {
            final CheckBox checkBox = new CheckBox(getContext());
            checkBox.setId(130 + i);
            checkBox.setTag(questionsModel.getAnswers().get(i).getOptionValue());
            checkBox.setTag(R.string.success, i);
            checkBox.setText(Html.fromHtml(questionsModel.getAnswers().get(i).getOptionText()));
            answerContainer.addView(checkBox);

            if (suveryAnswer != null)
                if (checkBoxAnswers.contains(questionsModel.getAnswers().get(i).getOptionValue())) {
                    checkBox.setChecked(true);
                }


            if (questionsModel.getAnswers().get(i).getOptionText().contains("Lòt") || questionsModel.getAnswers().get(i).getOptionText().contains("lòt") || questionsModel.getAnswers().get(i).getOptionText().toLowerCase().indexOf("lòt") != -1) {
                otherCheckBox = checkBox;
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            commonInterface.hideNext();
                            if (!checkboxlist.isEmpty()) {
                                for (Integer id : checkboxlist.keySet()) {
                                    CheckBox checkBox = checkboxlist.get(id);
                                    checkBox.setChecked(false);
                                }
                                if (editTextInflatedView != null) {
                                    editTextInflatedView.removeAllViews();
                                }
                                checkBoxAnswers.clear();
                            }
                            generateEt();
                        } else {

                            editTextInflatedView.removeAllViews();
                        }
                    }
                });
            } else
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String value = (String) buttonView.getTag();
                        if (isChecked) {
                            if (questionsModel.getAnswers().get((Integer) buttonView.getTag(R.string.success)).getOptionPrompt() != null)
                                showDialogMessage(questionsModel.getAnswers().get((Integer) buttonView.getTag(R.string.success)).getOptionPrompt().toString());


                            if (editTextInflatedView != null) {
                                editTextInflatedView.removeAllViews();
                            }
                            if (otherCheckBox != null) {
                                otherCheckBox.setChecked(false);
                            }

                            checkboxlist.put(buttonView.getId(), (CheckBox) buttonView);
                            checkBoxAnswers.add(value);
                            Object status = questionsModel.getAnswers().get(0).getOptionStatus();

                            commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext(), questionsModel, /*TextUtils.join(",", checkBoxAnswers)*/checkBoxAnswers.toString(), true, status, questionsModel.getAnswers().get(0).isRepeater());
                        } else {
                            checkboxlist.remove(buttonView.getId());
                            checkBoxAnswers.remove(value);
                            if (checkBoxAnswers.size() <= 0) {
                                commonInterface.hideNext();
                            }

                        }

                    }
                });
        }

    }


    private void setMessageType() {
        //  commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext()  , questionsModel, "", true);

    }


    protected void showDialogMessage(String message) {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.showDialogMessage(message);
    }


    private void checkBox_radio() {
        editTextIds.clear();
        checkboxlist = new ConcurrentHashMap<>();
        checkBoxAnswers = new ArrayList<>();
        checkBoxCheck();
        final RadioButton[] rb = new RadioButton[questionsModel.getAnswers().size()];
        final RadioGroup rg = new RadioGroup(getContext()); //create the RadioGroup
        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL


        for (int i = 0; i < questionsModel.getAnswers().size(); i++) {
            if (questionsModel.getAnswers().get(i).getOptionType().equals("checkbox")) {
                final CheckBox checkBox = new CheckBox(getContext());
                checkBox.setId(130 + i);
                checkBox.setTag(questionsModel.getAnswers().get(i).getOptionValue());
                checkBox.setText(Html.fromHtml(questionsModel.getAnswers().get(i).getOptionText()));
                answerContainer.addView(checkBox);

                if (questionsModel.getAnswers().get(i).getOptionText().contains("Lòt")) {
                    otherCheckBox = checkBox;
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                for (View radioButton : editTextIds) {
                                    ((RadioButton) radioButton).setChecked(false);
                                }
                                commonInterface.hideNext();
                                if (!checkboxlist.isEmpty()) {
                                    for (Integer id : checkboxlist.keySet()) {
                                        CheckBox checkBox = checkboxlist.get(id);
                                        checkBox.setChecked(false);
                                    }
                                    if (editTextInflatedView != null) {
                                        editTextInflatedView.removeAllViews();
                                    }
                                    checkboxlist.put(buttonView.getId(), (CheckBox) buttonView);
                                    checkBoxAnswers.clear();
                                }
                                generateEt();
                            } else {
                                checkboxlist.remove(buttonView.getId());
                                editTextInflatedView.removeAllViews();
                            }
                        }
                    });
                } else
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            String value = (String) buttonView.getTag();
                            if (isChecked) {
                                for (View radioButton : editTextIds) {
                                    ((RadioButton) radioButton).setChecked(false);
                                }
                                if (otherCheckBox != null) {
                                    otherCheckBox.setChecked(false);
                                }
                                checkboxlist.put(buttonView.getId(), (CheckBox) buttonView);
                                checkBoxAnswers.add(value);
                                Object status = questionsModel.getAnswers().get(0).getOptionStatus();
                                commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext(), questionsModel, /*TextUtils.join(",", checkBoxAnswers)*/checkBoxAnswers.toString(), true, status, questionsModel.getAnswers().get(0).isRepeater());
                            } else {
                                checkBoxAnswers.remove(value);
                                checkboxlist.remove(buttonView.getId());
                                if (checkBoxAnswers.size() <= 0) {
                                    commonInterface.hideNext();
                                }

                            }

                        }
                    });
            } else if (questionsModel.getAnswers().get(i).getOptionType().equals("radio")) {
                rb[i] = new RadioButton(getContext());
                rg.addView(rb[i]);
                rb[i].setText(Html.fromHtml(questionsModel.getAnswers().get(i).getOptionText()));
                rb[i].setId(i + 100);
                rb[i].setTag(i);
                editTextIds.add(rb[i]);
                if (suveryAnswer != null)
                    if (questionsModel.getAnswers().get(i).getOptionValue().equals(suveryAnswer.getAnswer())) {
                        ((RadioButton) rg.getChildAt(i)).setChecked(true);
                    }

            }

            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton checkedRadioButton = (RadioButton) rg.findViewById(checkedId);
                    boolean isChecked = checkedRadioButton.isChecked();
                    if (isChecked) {
                        int position = (Integer) checkedRadioButton.getTag();
                        if (questionsModel.getAnswers().get(position).getOptionPrompt() != null)
                            showDialogMessage(questionsModel.getAnswers().get(position).getOptionPrompt().toString());
                        Object status = questionsModel.getAnswers().get(position).getOptionStatus();
                        commonInterface.getNextPosition(questionsModel.getAnswers().get(position).getOptionNext(), questionsModel, questionsModel.getAnswers().get(position).getOptionValue(), true, status, questionsModel.getAnswers().get(position).isRepeater());

                        if (questionsModel.getAnswers().get(position).getOptionText().contains("Lòt")) {
                            generateEt();
                            commonInterface.hideNext();
                        }

                        if (!checkboxlist.isEmpty()) {
                            for (Integer id : checkboxlist.keySet()) {
                                CheckBox checkBox = checkboxlist.get(id);
                                checkBox.setChecked(false);
                            }
                            if (editTextInflatedView != null) {
                                editTextInflatedView.removeAllViews();
                            }
                            checkBoxAnswers.clear();

                        }
                    } else {
                        if (editTextInflatedView != null)
                            editTextInflatedView.removeAllViews();
                    }
                }

            });
        }
        answerContainer.addView(rg);


    }


    private void checkBoxCheck() {
        if (suveryAnswer != null) {
            String answer = suveryAnswer.getAnswer();
            answer = answer.replaceAll("\\s+", "");
            if (answer.contains("[")) {
                String replace = answer.replace("[", "");
                System.out.println(replace);
                String replace1 = replace.replace("]", "");
                System.out.println(replace1);
                checkBoxAnswers = new ArrayList<>(Arrays.asList(replace1.split(",")));
                System.out.println(checkBoxAnswers.toString());
            }
        }
    }


    String matchQuestion(String question_id) {
        for (AnswerModel.SuveryAnswer answer : answerModel.getSuveryAnswers()) {
            if (answer.getQuestionId().equals(question_id) && !TextUtils.isEmpty(answer.getAnswer())) {
                return answer.getAnswer();
            }
        }
        return null;
    }
}
