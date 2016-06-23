package com.oxo.haiti.ui.fargments;

import android.graphics.Color;
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
import com.oxo.haiti.adapter.CustomArrayAdapter;
import com.oxo.haiti.model.AnswerModel;
import com.oxo.haiti.model.AreaModel;
import com.oxo.haiti.model.Condition;
import com.oxo.haiti.model.PersonModel;
import com.oxo.haiti.model.QuestionsModel;
import com.oxo.haiti.model.RtfModel;
import com.oxo.haiti.storage.SnappyNoSQL;
import com.oxo.haiti.ui.activity.FragmentControler;
import com.oxo.haiti.ui.base.BaseActivity;
import com.oxo.haiti.utils.CommonInterface;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
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
    private Map<String, String> editTextAnswers = new HashMap<>();
    private int dorpDownYear, dropDownmonth;
    boolean singleSelecter = false;
    boolean selectedFromSpiner = false;


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

                        if (FragmentControler.resumeFlag) {
                            for (AnswerModel.SuveryAnswer suveryAnswer : answerModel.getSuveryAnswers()) {
                                if (suveryAnswer != null && questionDec != null && questionsModel.getQuestionId().equals(suveryAnswer.getQuestionId())) {
                                    DynamicFragment.this.suveryAnswer = suveryAnswer;
                                }
                            }
                        } else {
                            DynamicFragment.this.suveryAnswer = null;
                            editTextAnswers.clear();
                            if (questionsModel.getQuestionType().equals("message") || questionsModel.getQuestionType().equals("hh_profile") || questionsModel.getQuestionType().equals("hh_person") || questionsModel.getQuestionType().equals("hh_children")) {

                            } else
                                fragmentControler.hideNext();
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
                            populateBoth();
                        else if (questionsModel.getQuestionType().equals("text_text"))
                            dynamicTextInput();
                        else if (questionsModel.getQuestionType().equals("text_text_radio"))
                            dynamicTextInput();
                        else if (questionsModel.getQuestionType().equals("text_radio"))
                            dynamicTextInput();
                        else if (questionsModel.getQuestionType().equals("select"))
                            selector();
                        else if (questionsModel.getQuestionType().equals("number_number"))
                            populateBoth();
                        else if (questionsModel.getQuestionType().equals("hh_profile"))
                            showAreaView();
                        else if (questionsModel.getQuestionType().equals("hh_person"))
                            showPerson("hh_person");
                        else if (questionsModel.getQuestionType().equals("hh_children"))
                            showPerson("hh_children");
                        else if (questionsModel.getQuestionType().equals("radio_radio_radio_radio_radio_radio"))
                            genrateRadioGroups();
                        else
                            generateEt();


                        if (questionsModel.getQuestionId().equals("hid_304")) {
                            if (FragmentControler.childCount == 1) {
                                for (QuestionsModel.Answer answer : questionsModel.getAnswers())
                                    answer.setOptionNext(305);
                            }
                        }

                        if (questionsModel.getQuestionId().equals("hid_304")) {
                            if (FragmentControler.childCount == 0) {
                                for (QuestionsModel.Answer answer : questionsModel.getAnswers())
                                    answer.setOptionNext(365);
                            }
                        }

                        if (questionsModel.getQuestionId().equals("hid_328")) {
                            if (FragmentControler.childCount >= 2) {
                                if (!FragmentControler.isLoaded) {
                                    for (QuestionsModel.Answer answer : questionsModel.getAnswers())
                                        answer.setOptionNext(308);
                                    FragmentControler.isLoaded = true;
                                } else {
                                    for (QuestionsModel.Answer answer : questionsModel.getAnswers())
                                        answer.setOptionNext(332);
                                }
                            }
                        }

                        List<Condition> conditions = SnappyNoSQL.getInstance().getConditions();
                        for (Condition condition : conditions) {
                            if (questionsModel.getQuestionId().equals(condition.getKey())) {
                                Stack<AnswerModel.SuveryAnswer> reverseList = answerModel.getSuveryAnswers();
//                                Collections.reverse(reverseList);
                                for (AnswerModel.SuveryAnswer answer : reverseList) {
                                    for (Condition.ConditionMode conditionMode : condition.getValue().getConditions()) {
                                        if (answer.getQuestionId().equals(conditionMode.getQuestionOrder())) {
                                            if (conditionMode.getCompare().equals("==")) {
                                                if (Integer.parseInt(answer.getAnswer()) == conditionMode.getValue()) {
                                                    for (QuestionsModel.Answer answer1 : questionsModel.getAnswers()) {
                                                        answer1.setOptionNext(conditionMode.getQuestionNext());

                                                    }
                                                    break;
                                                }
                                            } else if (conditionMode.getCompare().equals("<")) {
                                                if (Integer.parseInt(answer.getAnswer()) < conditionMode.getValue()) {
                                                    for (QuestionsModel.Answer answer1 : questionsModel.getAnswers()) {
                                                        answer1.setOptionNext(conditionMode.getQuestionNext());
                                                    }
                                                    break;
                                                }
                                            } else if (conditionMode.getCompare().equals(">")) {
                                                if (Integer.parseInt(answer.getAnswer()) > conditionMode.getValue()) {
                                                    for (QuestionsModel.Answer answer1 : questionsModel.getAnswers()) {
                                                        answer1.setOptionNext(conditionMode.getQuestionNext());
                                                    }
                                                    break;
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }

                        if (!TextUtils.isEmpty(questionsModel.getQuestionText())) {

                            String source = questionsModel.getQuestionText();
                            if (questionsModel.getQuestionOrder() == 365) {
                                FragmentControler fragmentControler1 = (FragmentControler) getActivity();
                                fragmentControler1.getAreaModel();
                                AreaModel areaModel = fragmentControler.saveAreax();
                                List<RtfModel> rtfModels = areaModel.getMemberRtfModels();
                                String s = "";
                                if (rtfModels.size() > 0) {
                                    s = addAgeSex(rtfModels.get(0).getName());

                                    if (rtfModels.size() > 1 && !rtfModels.get(1).getName().equals(rtfModels.get(0).getName())) {
                                        s = s.concat(" epi ");
                                        s = s.concat(addAgeSex(rtfModels.get(1).getName()));
                                    }
                                }

                                s = source.replace("random_selected_persons", s);
                                headerTextView.setText(Html.fromHtml(s));

                            } else if (source.contains("#current_person#")) {
                                source = source.replace("#current_person#", getActivity().getIntent().getStringExtra("Name"));
                                headerTextView.setText(Html.fromHtml(source));
                            } else if (source.contains("#n1#") || source.contains("#n2#") || source.contains("#n3#") || source.contains("#n4#")) {
                                FragmentControler fragmentControler1 = (FragmentControler) getActivity();
                                fragmentControler1.executeAnswers();
                                if (source.contains("#n1#")) {
                                    if (questionsModel.getQuestionId().equals("hid_140")) {
                                        int count = FragmentControler.loopOne + 1;
                                        source = source.replace("#n1#", count + "");
                                    } else
                                        source = source.replace("#n1#", FragmentControler.loopOne == 0 ? "" + 1 : "" + FragmentControler.loopOne);

                                } else if (source.contains("#n2#")) {
                                    if (questionsModel.getQuestionId().equals("hid_176")) {
                                        int count = FragmentControler.loopTwo + 1;
                                        source = source.replace("#n2#", count + "");
                                    } else
                                        source = source.replace("#n2#", FragmentControler.loopTwo == 0 ? "" + 1 : "" + FragmentControler.loopTwo);
                                } else if (source.contains("#n3#")) {
                                    if (questionsModel.getQuestionId().equals("hid_244")) {
                                        int count = FragmentControler.loopThree + 1;
                                        source = source.replace("#n3#", count + "");
                                    } else
                                        source = source.replace("#n3#", FragmentControler.loopThree == 0 ? "" + 1 : "" + FragmentControler.loopThree);
                                } else if (source.contains("#n4#")) {
                                    if (questionsModel.getQuestionId().equals("hid_280")) {
                                        int count = FragmentControler.loopFour + 1;
                                        source = source.replace("#n4#", count + "");
                                    } else
                                        source = source.replace("#n4#", FragmentControler.loopFour == 0 ? "" + 1 : "" + FragmentControler.loopFour);
                                }
                                headerTextView.setText(Html.fromHtml(source));

                            } else
                                headerTextView.setText(Html.fromHtml(source));
                        }
                        if (!TextUtils.isEmpty(questionsModel.getQuestionDesc())) {
                            questionDec.setText(questionsModel.getQuestionDesc());
                        }
                        showBloodPresure();

                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                        // showDialogMessage("Something went wrong need to contact developer");
                    } finally {
                    }


                }
            }, 500);
        } else {
        }
    }

    private String addAgeSex(String name) {

        FragmentControler fragmentControler = (FragmentControler) getActivity();
        List<PersonModel> personModels = fragmentControler.getPersonModels();
        for (PersonModel personModel : personModels) {
            if (personModel.getName().equals(name)) {
                return personModel.getName() + " " + personModel.getId() + " " + personModel.getAge() + " " + personModel.getSex();
            }
        }

        return name;
    }


    List<Integer> tags = new ArrayList<>();

    private void genrateRadioGroups() {

        try {
            List<QuestionsModel.Answer> answer = questionsModel.getAnswers();
            generateRadio(answer.subList(0, 6), 1, "a) Nève / Eksite");
            generateRadio(answer.subList(6, 12), 2, "b) Dekouraje / Dezespere");
            generateRadio(answer.subList(12, 18), 3, "c) Ajite");
            generateRadio(answer.subList(18, 24), 4, "d) Dekouraje tèlman ke anyen pa enterese m");
            generateRadio(answer.subList(24, 30), 5, "e) Tout bagay te paret difisil pou ou");
            generateRadio(answer.subList(30, 36), 6, "f) Repliye sou mwenmenm, oubyen santi m’ san valè");
        } catch (Exception e) {

        }


    }

    Map<Integer, Integer> integerIntegerMap = new HashMap<>();

    private void generateRadio(final List<QuestionsModel.Answer> pickeranswer, int x, String jas) {
        integerIntegerMap.clear();
        tags.clear();
        tags.add(x);
        TextView textView = new TextView(getContext());
        textView.setId(new Random().nextInt());

        textView.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));
        textView.setText(jas);
        answerContainer.addView(textView);
        final RadioButton[] rb = new RadioButton[pickeranswer.size()];
        final RadioGroup rg = new RadioGroup(getContext());
        rg.setTag(x);
        rg.setId(x * 13 + 1);
        //create the RadioGroup
        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for (int i = 0; i < pickeranswer.size(); i++) {
            rb[i] = new RadioButton(getContext());
            rg.addView(rb[i]);
            rb[i].setText(Html.fromHtml(pickeranswer.get(i).getOptionText()));
            rb[i].setId(i + 100 + x);
            rb[i].setTag(i);
            rb[i].setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));

            if (suveryAnswer != null)
                if (pickeranswer.get(i).getOptionValue().equals(suveryAnswer.getAnswer())) {
                    ((RadioButton) rg.getChildAt(i)).setChecked(true);
                }

        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                          @Override
                                          public void onCheckedChanged(RadioGroup group, int checkedId) {
                                              RadioButton checkedRadioButton = (RadioButton) rg.findViewById(checkedId);
                                              boolean isChecked = checkedRadioButton.isChecked();
                                              tags.remove((Integer) group.getTag());
                                              int radioPosition = (Integer) checkedRadioButton.getTag();

                                              boolean allSelected = false;
                                              integerIntegerMap.put((Integer) group.getTag(), radioPosition);
                                              if (tags.size() == 0) {
                                                  allSelected = true;
                                              }

                                              if (allSelected) {
                                                  if (isChecked) {
                                                      int position = (Integer) checkedRadioButton.getTag();
                                                      if (pickeranswer.get((Integer) checkedRadioButton.getTag()).getOptionPrompt() != null)
                                                          showDialogMessage(pickeranswer.get((Integer) checkedRadioButton.getTag()).getOptionPrompt().toString());
                                                      Object status = pickeranswer.get(radioPosition).getOptionStatus();
                                                      commonInterface.getNextPosition(pickeranswer.get(radioPosition).getOptionNext(), questionsModel, new Gson().toJson(integerIntegerMap), true, status, pickeranswer.get(position).isRepeater());
                                                      if (!TextUtils.isEmpty(pickeranswer.get(position).getOptionValue()) && pickeranswer.get(position).getOptionValue().equals("66")) {
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
                                              } else {
                                                  //// TODO: 18/06/16  please select all
                                              }
                                          }
                                      }

        );
        answerContainer.addView(rg);
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
            headerTextView.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));
        } catch (Exception e) {
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
            hh.setText("" + areaModel.getSit() + areaModel.getBlock() + areaModel.getGps() + areaModel.getHH());
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


    private void populateBoth() {
        singleSelecter = questionsModel.isHh_single();
        final RadioButton[] rb = new RadioButton[questionsModel.getAnswers().size()];
        rg = new RadioGroup(getContext()); //create the RadioGroup
        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL


        for (int i = 0; i < questionsModel.getAnswers().size(); i++) {
            if (questionsModel.getAnswers().get(i).getOptionType().equals("number"))
                selector_select(questionsModel.getAnswers().get(i).getOptionText(), questionsModel.getAnswers().get(i).getMin(), questionsModel.getAnswers().get(i).getMax(), i);
            else {
                if (questionsModel.getAnswers().get(i).getOptionType().equals("radio")) {
                    rb[i] = new RadioButton(getContext());
                    rg.addView(rb[i]);
                    rb[i].setText(Html.fromHtml(questionsModel.getAnswers().get(i).getOptionText()));
                    rb[i].setId(i + 100);
                    rb[i].setTag(i);
                    rb[i].setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));

                    if (suveryAnswer != null)
                        if (questionsModel.getAnswers().get(i).getOptionValue().equals(suveryAnswer.getAnswer())) {
                            ((RadioButton) rg.getChildAt(i)).setChecked(true);
                        }
                }
            }
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                          @Override
                                          public void onCheckedChanged(RadioGroup group, int checkedId) {
                                              try {
                                                  RadioButton checkedRadioButton = (RadioButton) rg.findViewById(checkedId);
                                                  boolean isChecked = checkedRadioButton.isChecked();
                                                  if (isChecked) {
                                                      for (View id : editTextIds) {
                                                          EditText editText = (EditText) id;
                                                          if (editText != null)
                                                              editText.setText("");
                                                      }
                                                      if (spinners.size() > 0) {
                                                          Spinner spinner = spinners.get(0);
                                                          spinner.setSelection(0);
                                                          if (spinners.size() > 1) {
                                                              Spinner spinner1 = spinners.get(1);
                                                              spinner1.setSelection(0);
                                                          }
                                                          selectedFromSpiner = false;
                                                          dropDownmonth = 0;
                                                          dorpDownYear = 0;
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
                                              } catch (Exception exception) {
                                                  selectedFromSpiner = false;

                                              }
                                          }
                                      }

        );
        answerContainer.addView(rg);


    }

    List<Spinner> spinners = new ArrayList<>();


    private void selector_select(final String text, int min, int max, final int index) {
        FragmentControler.MainYear = false;
        final ArrayList<String> spinnerArray = new ArrayList<String>();

        if (text != null) {
            spinnerArray.add(text);
        }

        for (int i = min; i <= max; i++) {
            spinnerArray.add(i + "");
        }
        if (!singleSelecter)
            if (index == 0) {
                spinnerArray.add("Pa konnen");
            }

        final Spinner spinner = new Spinner(getContext());
        spinner.setId(new Random().nextInt(29 + min));
        spinner.setTag(text != null ? text : "");
        spinners.add(spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new CustomArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);

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
                                              public void onItemSelected(AdapterView<?> parent, View view, int positionx, long id) {
                                                  String s = (String) spinner.getTag();
                                                  if (s.equals("Lane")) {
                                                      FragmentControler.MainYear = true;
                                                  } else {
                                                      FragmentControler.MainYear = false;

                                                  }
                                                  if (singleSelecter) {
                                                      switch (index) {
                                                          case 0:
                                                              spinners.get(1).setSelection(0);
                                                              break;
                                                          case 1:
                                                              spinners.get(0).setSelection(0);
                                                              break;
                                                      }
                                                  }
                                                  if (rg != null) {
                                                      selectedFromSpiner = true;
                                                      int as = rg.getCheckedRadioButtonId();
                                                      if (as > 0) {
                                                          rg.clearCheck();
                                                      }
                                                  }
                                                  if (index == 0 && (positionx == 0 || positionx + 1 == spinnerArray.size())) {

                                                      if (positionx == 0) {
                                                          commonInterface.hideNext();
                                                      }
                                                      if (positionx + 1 == spinnerArray.size()) {
                                                          commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext(), questionsModel, "" + 99, true, "", questionsModel.getAnswers().get(0).isRepeater());
                                                      }

                                                  } else if ((index == 1 && positionx == 0)) {
                                                      commonInterface.hideNext();
                                                  } else {
//                                                      if (text != null) {
//                                                          positionx = positionx + 1;
//                                                      }
                                                      String positionStr = spinnerArray.get(positionx);
                                                      int position = Integer.parseInt(positionStr);
                                                      if (index == 0) {
                                                          dropDownmonth = position;
                                                      } else
                                                          dorpDownYear = position;

                                                      if ((dorpDownYear == new DateTime().getYear() && dropDownmonth > new DateTime().getMonthOfYear()) && !singleSelecter) {
                                                          commonInterface.hideNext();
                                                          if (dropDownmonth == 0 || dorpDownYear == 0)
                                                              commonInterface.hideNext();
                                                      } else {
                                                          commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext(), questionsModel, "" + position, true, "", questionsModel.getAnswers().get(0).isRepeater());
                                                          if ((dropDownmonth == 0 || dorpDownYear == 0) && !singleSelecter)
                                                              commonInterface.hideNext();

                                                      }
                                                  }
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> parent) {

                                              }
                                          }

        );


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
//                    baseActivity.messageToast("Pattern not match with 'Erè'");
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
            if (questionsModel.getAnswers().get(i).getOptionType().equals("number") || questionsModel.getAnswers().get(i).getOptionType().equals("text")) {
                View id = generateEt(questionsModel.getAnswers().get(i).getMax(), questionsModel.getAnswers().get(i).getMin(), i);
                editTextIds.add(id);
            }
            if (questionsModel.getAnswers().get(i).getOptionType().equals("radio")) {
                rb[i] = new RadioButton(getContext());
                rg.addView(rb[i]);
                rb[i].setText(Html.fromHtml(questionsModel.getAnswers().get(i).getOptionText()));
                rb[i].setId(i + 100);
                rb[i].setTag(i);
                rb[i].setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));

                //// TODO: 14/06/16 radio button from map and marke if condtion

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
                                                      if (editTextAnswers.containsKey("text"))
                                                          editTextAnswers.remove("text");
                                                      editTextAnswers.put("radio", "true");

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
            rb[i].setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));

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
        editText.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));

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
                    if (!TextUtils.isEmpty(questionsModel.getMatch_with())) {
                        String s1 = matchQuestion(questionsModel.getMatch_with());
                        if (s1 != null)
                            matchCase = s.toString().equals(s1);
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
                            commonInterface.hideNext();
                            BaseActivity baseActivity = (BaseActivity) getActivity();
                            baseActivity.messageToast("Erè");
                        }
                    } else {
                        commonInterface.hideNext();
                        BaseActivity baseActivity = (BaseActivity) getActivity();
                        baseActivity.messageToast("Erè");
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
        try {
            if (!TextUtils.isEmpty(questionsModel.getAnswers().get(position).getOptionText())) {
                TextView textView = (TextView) editTextInflatedView.findViewById(R.id.text_status);
                textView.setVisibility(View.VISIBLE);
                textView.setText(questionsModel.getAnswers().get(position).getOptionText());
                textView.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));
            }
        } catch (Exception e) {

        }
        EditText editText = (EditText) editTextInflatedView.findViewById(R.id.text_et);
        editText.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));

        if (editTextAnswers.containsKey("text")) {
            editText.setText(editTextAnswers.get("text"));
        }
        editText.setId(new Random(67).nextInt());
        if (questionsModel.getQuestionType().contains("number")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            UPPER_LIMIT = max;
            LOWER_LIMIT = min;
        }


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
                    boolean loopCounter = true;
                    if (questionsModel.getQuestionId().equals("hid_272")) {
                        try {
                            if (FragmentControler.loopThree == Integer.parseInt(s.toString())) {
                                loopCounter = true;
                            } else {
                                loopCounter = false;

                            }
                        } catch (Exception e) {

                        }
                    }


                    if (questionsModel.getQuestionId().equals("hid_304")) {
                        try {
                            if (FragmentControler.loopFour == Integer.parseInt(s.toString())) {
                                loopCounter = true;
                            } else {
                                loopCounter = false;
                            }
                        } catch (Exception e) {

                        }
                    }

                    boolean matchCase = true;
                    if (!TextUtils.isEmpty(questionsModel.getMatch_with())) {
                        String answer = matchQuestion(questionsModel.getMatch_with());
                        HashMap<String, String> hashMap = new Gson().fromJson(answer, HashMap.class);
                        String finalAnswer = hashMap.get("text");
                        matchCase = s.toString().equals(finalAnswer);
                    }
                    if (loopCounter) {
                        if (matchCase) {
                            if (questionsModel.getQuestionType().contains("number")) {
                                if (max < Integer.parseInt(s.toString()) || Integer.parseInt(s.toString()) < min) {
                                    commonInterface.hideNext();
                                    BaseActivity baseActivity = (BaseActivity) getActivity();
                                    baseActivity.messageToast("Antre yon nimewo/kantite antre " + min + " a " + max + ".");
                                } else {
                                    editTextAnswers.put("text", s.toString());
                                    if (editTextAnswers.containsKey("radio"))
                                        editTextAnswers.remove("radio");
                                    Object status = questionsModel.getAnswers().get(position).getOptionStatus();
                                    commonInterface.getNextPosition(questionsModel.getAnswers().get(position).getOptionNext(), questionsModel, new Gson().toJson(editTextAnswers, HashMap.class), true, status, questionsModel.getAnswers().get(position).isRepeater());
                                }
                            } else {
                                Object status = questionsModel.getAnswers().get(position).getOptionStatus();
                                commonInterface.getNextPosition(questionsModel.getAnswers().get(position).getOptionNext(), questionsModel, s.toString(), true, status, questionsModel.getAnswers().get(position).isRepeater());
//                    view.setTag(new Temp(questionsModel.getAnswers().get(0).getOptionNext()  , questionsModel, s.toString(), true));
                            }
                        } else {
                            commonInterface.hideNext();
                            BaseActivity baseActivity = (BaseActivity) getActivity();
                            baseActivity.messageToast("Ere");
                        }
                    } else {
                        commonInterface.hideNext();
                        BaseActivity baseActivity = (BaseActivity) getActivity();
                        String value = questionsModel.getQuestionId().equals("hid_272") ? FragmentControler.loopThree + "" : FragmentControler.loopFour + "";
                        baseActivity.messageToast("Patisipan yo te di ke  " + s.toString() + " mouri, men yo te bay enfomasyon yo pou  " + value + " times B2a1 was answered] moun ki mouri: retounen epi korige erè.");
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
            checkBox.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));
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
                                    //    checkBox.setChecked(false);
                                }
                                if (editTextInflatedView != null) {
                                    editTextInflatedView.removeAllViews();
                                }
                                //checkBoxAnswers.clear();
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
                                //   editTextInflatedView.removeAllViews();
                            }
                            if (otherCheckBox != null) {
                                //     otherCheckBox.setChecked(false);
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


    void showBloodPresure() {
        if (questionsModel.getQuestionId().equals("iid_542")) {
            String questionText = questionsModel.getQuestionText();
            for (AnswerModel.SuveryAnswer answer : answerModel.getSuveryAnswers()) {
                if (answer.getQuestionId().equals("iid_140") || answer.getQuestionId().equals("iid_496")) {
                    questionText = questionText.replace("[B2]", answer.getAnswer());
                } else if (answer.getQuestionId().equals("iid_144") || answer.getQuestionId().equals("iid_500")) {
                    questionText = questionText.replace("[B3]", answer.getAnswer());
                } else if (answer.getQuestionId().equals("iid_152") || answer.getQuestionId().equals("iid_508")) {
                    questionText = questionText.replace("[B5]", answer.getAnswer());
                } else if (answer.getQuestionId().equals("iid_156") || answer.getQuestionId().equals("iid_512")) {
                    questionText = questionText.replace("[B6]", answer.getAnswer());
                } else if (answer.getQuestionId().equals("iid_164") || answer.getQuestionId().equals("iid_520")) {
                    questionText = questionText.replace("[B8]", answer.getAnswer());
                } else if (answer.getQuestionId().equals("iid_168") || answer.getQuestionId().equals("iid_525")) {
                    questionText = questionText.replace("[B9]", answer.getAnswer());
                } else if (answer.getQuestionId().equals("iid_532")) {
                    String value = fetchMapValue(answer.getAnswer());
                    questionText = questionText.replace("[B11]", value);
                } else if (answer.getQuestionId().equals("iid_536")) {
                    String value = fetchMapValue(answer.getAnswer());
                    questionText = questionText.replace("[B12]", value);
                } else if (answer.getQuestionId().equals("iid_540")) {
                    String value = fetchMapValue(answer.getAnswer());
                    questionText = questionText.replace("[B13]", value);
                }

            }
            headerTextView.setText(Html.fromHtml(questionText));
        }
    }

    private String fetchMapValue(String answer) {
        String value = "";
        if (answer.contains("{")) {
            Map<String, String> map = new Gson().fromJson(answer, HashMap.class);
            if (map.containsKey("text")) {
                value = map.get("text");
            } else if (map.containsKey("radio"))
                value = map.get("text");


        }
        return value;
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
        rg = new RadioGroup(getContext()); //create the RadioGroup
        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL


        for (int i = 0; i < questionsModel.getAnswers().size(); i++) {
            if (questionsModel.getAnswers().get(i).getOptionType().equals("checkbox")) {
                final CheckBox checkBox = new CheckBox(getContext());
                checkBox.setId(130 + i);
                checkBox.setTag(questionsModel.getAnswers().get(i).getOptionValue());
                checkBox.setText(Html.fromHtml(questionsModel.getAnswers().get(i).getOptionText()));
                checkBox.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));
                answerContainer.addView(checkBox);


                if (questionsModel.getAnswers().get(i).getOptionText().contains("Lòt")) {
                    otherCheckBox = checkBox;
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                int selectedid = rg.getCheckedRadioButtonId();
                                if (selectedid > 0) {
                                    rg.clearCheck();
                                }
//                                for (View radioButton : editTextIds) {
//                                    ((RadioButton) radioButton).setChecked(false);
//                                }
                                checkboxlist.put(buttonView.getId(), (CheckBox) buttonView);

                                commonInterface.hideNext();
                                if (!checkboxlist.isEmpty()) {
                                    for (Integer id : checkboxlist.keySet()) {
                                        CheckBox checkBox = checkboxlist.get(id);
                                        // checkBox.setChecked(false);
                                    }
                                    if (editTextInflatedView != null) {
                                        //       editTextInflatedView.removeAllViews();
                                    }
                                    //  checkBoxAnswers.clear();
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
                                int selectedid = rg.getCheckedRadioButtonId();
                                if (selectedid > 0) {
                                    rg.clearCheck();
                                }

//                                for (View radioButton : editTextIds) {
//                                    ((RadioButton) radioButton).setChecked(false);
//                                }
                                if (otherCheckBox != null) {
                                    //     otherCheckBox.setChecked(false);
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
                rb[i].setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));

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
                    try {
                        boolean isChecked = checkedRadioButton.isChecked();
                        if (isChecked) {
                            int position = (Integer) checkedRadioButton.getTag();
                            if (questionsModel.getAnswers().get(position).getOptionPrompt() != null)
                                showDialogMessage(questionsModel.getAnswers().get(position).getOptionPrompt().toString());
                            Object status = questionsModel.getAnswers().get(position).getOptionStatus();

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

                                if (otherCheckBox != null) {
                                    otherCheckBox.setChecked(false);
                                }
                                commonInterface.getNextPosition(questionsModel.getAnswers().get(position).getOptionNext(), questionsModel, questionsModel.getAnswers().get(position).getOptionValue(), true, status, questionsModel.getAnswers().get(position).isRepeater());
                                checkBoxAnswers.clear();

                            }
                        } else {
                            if (editTextInflatedView != null)
                                editTextInflatedView.removeAllViews();
                        }
                    } catch (Exception exception) {

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
        List<AnswerModel.SuveryAnswer> suveryAnswers = answerModel.getSuveryAnswers();
//        Collections.reverse(suveryAnswers);
        String answerStr = null;
        for (AnswerModel.SuveryAnswer answer : suveryAnswers) {
            if (answer.getQuestionId().equals(question_id) && !TextUtils.isEmpty(answer.getAnswer())) {
                answerStr = answer.getAnswer();
            }
        }
        return answerStr;
    }


    void showPerson(String type) {
        FragmentControler.childCount = 0;
        editTextInflatedView = (LinearLayout) getLayoutInflater(getArguments()).inflate(R.layout.show_person, null);
        LinearLayout namelayout = (LinearLayout) editTextInflatedView.findViewById(R.id.namelayout);
        LinearLayout sexLayout = (LinearLayout) editTextInflatedView.findViewById(R.id.sexlayout);
        LinearLayout ageLayout = (LinearLayout) editTextInflatedView.findViewById(R.id.agelayout);
        FragmentControler fragmentControler = (FragmentControler) getActivity();
        List<PersonModel> personModels = fragmentControler.getPersonModels();


        switch (type) {
            case "hh_person":

                break;
            case "hh_children":
                namelayout.setVisibility(View.GONE);
                break;
        }
        for (PersonModel personModel : personModels) {
            if (personModel.getTypo().equals(type)) {
                if (!type.equals("hh_children")) {
                    TextView name = new TextView(getContext());
                    name.setTextColor(Color.BLACK);
                    name.setId(new Random().nextInt(23));
                    name.setText(personModel.getName());
                    namelayout.addView(name);
                    name.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));
                }
                TextView sex = new TextView(getContext());
                sex.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));

                TextView age = new TextView(getContext());
                age.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));

                age.setId(new Random().nextInt(43));
                age.setTextColor(Color.BLACK);
                if (type.equals("hh_children")) {
                    try {
                        if (Integer.parseInt(personModel.getAge()) <= 5) {
                            FragmentControler.childCount++;
                        }

                    } catch (Exception e) {

                    }
                }

                sex.setId(new Random().nextInt(54));
                sex.setTextColor(Color.BLACK);

                sex.setText(personModel.getSex().equals("1") ? "Gason" : "Fi");
                age.setText(personModel.getAge());
                sexLayout.addView(sex);
                ageLayout.addView(age);
            }
        }
        answerContainer.addView(editTextInflatedView);
    }
}


