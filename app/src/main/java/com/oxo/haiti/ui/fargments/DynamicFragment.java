package com.oxo.haiti.ui.fargments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.oxo.haiti.R;
import com.oxo.haiti.model.QuestionsModel;
import com.oxo.haiti.utils.CommonInterface;

/**
 * Created by jaswinderwadali on 17/05/16.
 */
public class DynamicFragment extends Fragment {


    private int position = 0;
    private LinearLayout answerContainer;
    private TextView headerTextView;
    private CommonInterface commonInterface;
    private QuestionsModel questionsModel;
    private TextView questionDec;

    public static DynamicFragment getFragment(int position, CommonInterface commonInterface, QuestionsModel questionsModel) {
        DynamicFragment dynamicFragment = new DynamicFragment();
        dynamicFragment.position = position;
        dynamicFragment.commonInterface = commonInterface;
        dynamicFragment.questionsModel = questionsModel;
        return dynamicFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dynamic_fragment, container, false);
        headerTextView = (TextView) view.findViewById(R.id.header);
        answerContainer = (LinearLayout) view.findViewById(R.id.ans_container);
        questionDec = (TextView) view.findViewById(R.id.questionDec);
        headerTextView.setText(questionsModel.getQuestionText());
        if (questionsModel.getQuestionType().equals("radio"))
            generateRadioButtonView();
        else
            generateEt();


        if (questionsModel.getQuestionDesc() != null)
            questionDec.setText(questionsModel.getQuestionDesc());
        return view;
    }

    private void generateRadioButtonView() {
        final RadioButton[] rb = new RadioButton[questionsModel.getAnswers().size()];
        final RadioGroup rg = new RadioGroup(getContext()); //create the RadioGroup
        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for (int i = 0; i < questionsModel.getAnswers().size(); i++) {
            rb[i] = new RadioButton(getContext());
            rg.addView(rb[i]);
            rb[i].setText(questionsModel.getAnswers().get(i).getOptionText());
            rb[i].setId(i + 100);
            rb[i].setTag(i);
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) rg.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {


                    if (questionsModel.getAnswers().get((Integer) checkedRadioButton.getTag()).getOptionPrompt() != null)
                        showDialogMessage(questionsModel.getAnswers().get((Integer) checkedRadioButton.getTag()).getOptionPrompt().toString());


                    // Changes the textview's text to "Checked: example radiobutton text"
                    //headerTextView.setText("Checked:" + checkedRadioButton.getText());
                    int radioPosition = (Integer) checkedRadioButton.getTag();
                    commonInterface.getNextPosition(questionsModel.getAnswers().get(radioPosition).getOptionNext() - 1, questionsModel, questionsModel.getAnswers().get(radioPosition).getOptionValue());
                }
            }
        });
        answerContainer.addView(rg);
    }


    private void generateEt() {
        View view = getLayoutInflater(getArguments()).inflate(R.layout.input_text, null);
        EditText editText = (EditText) view.findViewById(R.id.text_et);
        if (questionsModel.getQuestionType().equals("number"))
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    commonInterface.getNextPosition(questionsModel.getAnswers().get(0).getOptionNext() - 1, questionsModel, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        answerContainer.addView(view);

    }


    protected void showDialogMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            //alert.setTitle("Doctor");
            alert.setMessage(message);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                finish();
                }
            });
            alert.show();
        }
    }


}
