package com.oxo.haiti.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.oxo.haiti.model.QuestionsModel;
import com.oxo.haiti.ui.fargments.DynamicFragment;
import com.oxo.haiti.utils.CommonInterface;

import java.util.List;

/**
 * Created by jaswinderwadali on 17/05/16.
 */
public class QuestionAdapter extends FragmentStatePagerAdapter  {

    private List<QuestionsModel> questionsModels;
    private CommonInterface commonInterface;

    public QuestionAdapter(FragmentManager fm, CommonInterface commonInterface, List<QuestionsModel> questionsModels) {
        super(fm);
        this.commonInterface = commonInterface;
        this.questionsModels = questionsModels;
    }

    @Override
    public Fragment getItem(int position) {
        return DynamicFragment.getFragment(position, commonInterface, questionsModels.get(position));
    }

    @Override
    public int getCount() {
        return questionsModels.size();
    }


}
