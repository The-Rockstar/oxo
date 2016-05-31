package com.oxo.haiti.utils;

import com.oxo.haiti.model.QuestionsModel;

/**
 * Created by jaswinderwadali on 17/05/16.
 */
public interface CommonInterface {
    void getNextPosition(int position, QuestionsModel questionsModel, String answer, boolean isNew, Object status);

    void hideNext();

    void hidePrev();

    void questionId(String id);

    String getNextId();
}
