package com.oxo.haiti.ui.fargments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.oxo.haiti.R;
import com.oxo.haiti.model.AnswerModel;
import com.oxo.haiti.model.QuestionsModel;
import com.oxo.haiti.ui.base.BaseActivity;
import com.oxo.haiti.utils.CommonInterface;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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


    public static DynamicFragment getFragment(int position, CommonInterface commonInterface, QuestionsModel questionsModel, AnswerModel answerModel) {
        DynamicFragment dynamicFragment = new DynamicFragment();
        dynamicFragment.commonInterface = commonInterface;
        dynamicFragment.questionsModel = questionsModel;
        dynamicFragment.answerModel = answerModel;
        return dynamicFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dynamic_fragment, container, false);
        headerTextView = (TextView) view.findViewById(R.id.header);
        answerContainer = (LinearLayout) view.findViewById(R.id.ans_container);
        questionDec = (TextView) view.findViewById(R.id.questionDec);
        insertDate();
        headerTextView.setText(Html.fromHtml(questionsModel.getQuestionText()));

        for (AnswerModel.SuveryAnswer suveryAnswer : answerModel.getSuveryAnswers()) {
            if (suveryAnswer != null && questionDec != null && questionsModel.getQuestionId().equals(suveryAnswer.getQuestionId())) {
                this.suveryAnswer = suveryAnswer;
            }
        }

        if (questionsModel.getQuestionType().equals("radio"))
            generateRadioButtonView();
        else if (questionsModel.getQuestionType().equals("message"))
            setMessageType();
        else if (questionsModel.getQuestionType().equals("checkbox"))
            checkBoxInput();
        else if (questionsModel.getQuestionType().equals("checkbox_radio"))
            checkBox_radio();
        else if (questionsModel.getQuestionType().equals("input_radio"))
            input_radio();
        else if (questionsModel.getQuestionType().equals("text_text"))
            dynamicTextInput();
        else
            generateEt();


        if (!TextUtils.isEmpty(questionsModel.getQuestionDesc()))
            questionDec.setText(Html.fromHtml(questionsModel.getQuestionDesc()));
        return view;
    }

    private void dynamicTextInput() {
        for (int i = 0; i < questionsModel.getAnswers().size(); i++) {
            generateEt(questionsModel.getAnswers().get(i).getMax(), questionsModel.getAnswers().get(i).getMin());
        }
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
                    if (questionsModel.getAnswers().get((Integer) checkedRadioButton.getTag()).getOptionPrompt() != null)
                        showDialogMessage(questionsModel.getAnswers().get((Integer) checkedRadioButton.getTag()).getOptionPrompt().toString());
                    int radioPosition = (Integer) checkedRadioButton.getTag();
                    commonInterface.getNextPosition(questionsModel.getAnswers().get(radioPosition).getOptionNext() - 1, questionsModel, questionsModel.getAnswers().get(radioPosition).getOptionValue(), true);

                }
            }
        });
        answerContainer.addView(rg);
    }


    private void generateEt() {
        editTextInflatedView = (LinearLayout) getLayoutInflater(getArguments()).inflate(R.layout.input_text, null);
        EditText editText = (EditText) editTextInflatedView.findViewById(R.id.text_et);
        if (questionsModel.getQuestionType().equals("number")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                    if (questionsModel.getQuestionType().equals("number")) {
                        if (UPPER_LIMIT < Integer.parseInt(s.toString()) || Integer.parseInt(s.toString()) < LOWER_LIMIT) {
                            commonInterface.hideNext();
                            BaseActivity baseActivity = (BaseActivity) getActivity();
                            baseActivity.messageToast("Antre yon nimewo/kantite antre " + LOWER_LIMIT + " a " + UPPER_LIMIT + ".");
                        } else {
                            commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext() - 1, questionsModel, s.toString(), true);
                        }
                    } else
                        commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext() - 1, questionsModel, s.toString(), true);
//                    view.setTag(new Temp(questionsModel.getAnswers().get(0).getOptionNext() - 1, questionsModel, s.toString(), true));
                } else
                    commonInterface.hideNext();
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        answerContainer.addView(editTextInflatedView);

    }


    private void generateEt(final int min, final int max) {
        editTextInflatedView = (LinearLayout) getLayoutInflater(getArguments()).inflate(R.layout.input_text, null);
        EditText editText = (EditText) editTextInflatedView.findViewById(R.id.text_et);
        if (questionsModel.getQuestionType().equals("number")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                    if (questionsModel.getQuestionType().equals("number")) {
                        if (max < Integer.parseInt(s.toString()) || Integer.parseInt(s.toString()) < min) {
                            commonInterface.hideNext();
                            BaseActivity baseActivity = (BaseActivity) getActivity();
                            baseActivity.messageToast("Antre yon nimewo/kantite antre " + max + " a " + min + ".");
                        } else {
                            commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext() - 1, questionsModel, s.toString(), true);
                        }
                    } else
                        commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext() - 1, questionsModel, s.toString(), true);
//                    view.setTag(new Temp(questionsModel.getAnswers().get(0).getOptionNext() - 1, questionsModel, s.toString(), true));
                } else
                    commonInterface.hideNext();
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        answerContainer.addView(editTextInflatedView);

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


            if (questionsModel.getAnswers().get(i).getOptionText().contains("Lòt")) {
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
                            commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext() - 1, questionsModel, /*TextUtils.join(",", checkBoxAnswers)*/checkBoxAnswers.toString(), true);
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
        //  commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext() - 1, questionsModel, "", true);

    }

    protected void showDialogMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            //alert.setTitle("Doctor");
            alert.setMessage(Html.fromHtml(message));
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                finish();
                }
            });
            alert.show();
        }
    }


    private void checkBox_radio() {
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
                            generateEt();
                            commonInterface.hideNext();
                        }
                    });
                } else
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            String value = (String) buttonView.getTag();
                            if (isChecked) {
                                if (otherCheckBox != null) {
                                    otherCheckBox.setChecked(false);
                                }
                                checkboxlist.put(buttonView.getId(), (CheckBox) buttonView);
                                checkBoxAnswers.add(value);
                                commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext() - 1, questionsModel, /*TextUtils.join(",", checkBoxAnswers)*/checkBoxAnswers.toString(), true);
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
                        if (questionsModel.getAnswers().get((Integer) checkedRadioButton.getTag()).getOptionPrompt() != null)
                            showDialogMessage(questionsModel.getAnswers().get((Integer) checkedRadioButton.getTag()).getOptionPrompt().toString());
                        int radioPosition = (Integer) checkedRadioButton.getTag();
                        commonInterface.getNextPosition(questionsModel.getAnswers().get(radioPosition).getOptionNext() - 1, questionsModel, questionsModel.getAnswers().get(radioPosition).getOptionValue(), true);
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

                        editTextInflatedView.removeAllViews();
                    }
                }

            });
            answerContainer.addView(rg);


        }

    }

    private void input_radio() {

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
}
