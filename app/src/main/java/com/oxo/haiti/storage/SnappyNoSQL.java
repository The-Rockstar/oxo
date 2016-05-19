package com.oxo.haiti.storage;

import android.content.Context;
import android.content.res.TypedArray;

import com.google.gson.reflect.TypeToken;
import com.oxo.haiti.model.QuestionsModel;
import com.oxo.haiti.model.UsersModel;
import com.snappydb.DB;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wadali on 5/17/2016.
 */
public class SnappyNoSQL {

    private static SnappyNoSQL instance;
    private DB snappyDB;
    private static final String ROW = "USER_X";
    private static final String SURVEY_ONE = "SURVEY_ONE";
    private static final String SURVEY_TWO = "SURVEY_TWO";
    private static final String SURVEY_DATA = "SURVEY_DATA";


    public static void init(Context context) {
        if (instance == null)
            instance = new SnappyNoSQL(context);
    }

    public static SnappyNoSQL getInstance() {
        if (instance == null)
            throw new RuntimeException("Call init before getInstance");
        return instance;
    }

    private SnappyNoSQL(Context context) {
        initInternal(context);
    }

    private void initInternal(Context context) {
        try {
            this.snappyDB = new SnappyDB.Builder(context)
                    //.directory(Environment.getExternalStorageDirectory().getAbsolutePath()) //optional
                    .name("USER_DB")//optional
                    .build();
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public void saveUsers(UsersModel usersModel) {
        try {
            snappyDB.put(ROW, usersModel);
            for (UsersModel.User user : usersModel.getUsers()) {
                snappyDB.put(user.getUsername(), user);
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public void saveSurveyQuestionsOne(List<QuestionsModel> questionsModels) {
        try {
            snappyDB.put(SURVEY_ONE, questionsModels);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public List<QuestionsModel> getSurveyQuestionsOne() {
        try {
            if (snappyDB.exists(SURVEY_ONE))
                return snappyDB.get(SURVEY_ONE, ArrayList.class);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void saveSurveyQuestionsTwo(List<QuestionsModel> questionsModels) {
        try {
            snappyDB.put(SURVEY_TWO, questionsModels);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public List<QuestionsModel> getSurveyQuestionsTwo() {
        try {
            if (snappyDB.exists(SURVEY_TWO))
                return snappyDB.get(SURVEY_TWO, ArrayList.class);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return null;

    }


    public boolean loginAuth(String userName, String password) {
        try {
            if (snappyDB.exists(userName)) {
                UsersModel.User user = snappyDB.get(userName, UsersModel.User.class);
                return user.getPassword().equals(password);
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return false;
    }


    public UsersModel getUsers() {
        try {
            if (snappyDB.exists(ROW))
                return snappyDB.get(ROW, UsersModel.class);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAllUsers(Context context) {
        try {
            snappyDB.destroy();
            initInternal(context);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }


    public void saveOfflineSurvey(String data) {
        try {
            List<String> stringList = getSurveyData();
            stringList.add(data);
            snappyDB.put(SURVEY_DATA, stringList);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public void saveOfflineSurvey(List<String> stringList) {
        try {
            snappyDB.put(SURVEY_DATA, stringList);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }


    public List<String> getSurveyData() {
        try {
            if (snappyDB.exists(SURVEY_DATA))
                return snappyDB.get(SURVEY_DATA, ArrayList.class);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void removeData(String data) {
        List<String> stringList = getSurveyData();
        stringList.remove(data);
        saveOfflineSurvey(stringList);
    }

}
