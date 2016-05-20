package com.oxo.haiti.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.oxo.haiti.model.AnswerModel;
import com.oxo.haiti.model.QuestionsModel;
import com.oxo.haiti.ui.fargments.DynamicFragment;
import com.oxo.haiti.utils.CommonInterface;

import java.util.List;

/**
 * Created by jaswinderwadali on 17/05/16.
 */
public class QuestionAdapter extends FragmentStatePagerAdapter {

    private List<QuestionsModel> questionsModels;
    private CommonInterface commonInterface;
    private AnswerModel answerModel;

    public QuestionAdapter(FragmentManager fm, CommonInterface commonInterface, List<QuestionsModel> questionsModels, AnswerModel answerModel) {
        super(fm);
        this.commonInterface = commonInterface;
        this.questionsModels = questionsModels;
        this.answerModel = answerModel;
    }

    @Override
    public Fragment getItem(int position) {
        return DynamicFragment.getFragment(position, commonInterface, questionsModels.get(position), answerModel);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }

    @Override
    public int getCount() {
        return questionsModels.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
