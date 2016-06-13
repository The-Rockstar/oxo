package com.oxo.haiti.storage;

import android.content.Context;

import com.oxo.haiti.model.AnswerModel;
import com.oxo.haiti.model.AreaModel;
import com.oxo.haiti.model.QuestionsModel;
import com.oxo.haiti.model.RtfModel;
import com.oxo.haiti.model.UserModel;
import com.snappydb.DB;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

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
    private static final String KEYSTORE = "KEYSTORE";


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

    public void saveUsers(UserModel usersModel) {
        try {
            snappyDB.put(ROW, usersModel);
            for (UserModel.User user : usersModel.getUsers()) {
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
                UserModel.User user = snappyDB.get(userName, UserModel.User.class);
                return user.getPassword().equals(password);
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return false;
    }


    public UserModel getUsers() {
        try {
            if (snappyDB.exists(ROW))
                return snappyDB.get(ROW, UserModel.class);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteDataBase(Context context) {
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


//    public void saveState(AnswerModel answerModel, boolean isOne) {
//        try {
//            snappyDB.put(isOne ? SAVE_STATE_ONE : SAVE_STATE_TWO, answerModel);
//        } catch (SnappydbException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public AnswerModel getSaveState(boolean isOne) {
//        try {
//            if (snappyDB.exists(isOne ? SAVE_STATE_ONE : SAVE_STATE_TWO))
//                return snappyDB.get(isOne ? SAVE_STATE_ONE : SAVE_STATE_TWO, AnswerModel.class);
//        } catch (SnappydbException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    public void removeSaveState(boolean isOne) {
//        try {
//            if (snappyDB.exists(isOne ? SAVE_STATE_ONE : SAVE_STATE_TWO))
//                snappyDB.del(isOne ? SAVE_STATE_ONE : SAVE_STATE_TWO);
//        } catch (SnappydbException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void saveStack(Stack<Integer> stack, boolean isOne) {
//        try {
//            snappyDB.put(isOne ? SAVE_STACK_ONE : SAVE_STACK_TWO, stack);
//        } catch (SnappydbException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public Stack<Integer> getStack(boolean isOne) {
//        try {
//            if (snappyDB.exists(isOne ? SAVE_STACK_ONE : SAVE_STACK_TWO))
//                return snappyDB.get(isOne ? SAVE_STACK_ONE : SAVE_STACK_TWO, Stack.class);
//        } catch (SnappydbException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void removeStack(boolean isOne) {
//        try {
//            if (snappyDB.exists(isOne ? SAVE_STACK_ONE : SAVE_STACK_TWO))
//                snappyDB.del(isOne ? SAVE_STACK_ONE : SAVE_STACK_TWO);
//        } catch (SnappydbException e) {
//            e.printStackTrace();
//        }
//    }


    // key value pair ONE2016-06-01 01:03:49 ONE2016-06-01 01:03:49


    public void saveState(AnswerModel answerModel, String key) {
        try {
            snappyDB.put(key, answerModel);
        } catch (SnappydbException e) {
            e.printStackTrace();
        } finally {
            storeKey(key);
        }
    }

    public AnswerModel getSaveState(String key) {
        try {
            if (snappyDB.exists(key))
                return snappyDB.get(key, AnswerModel.class);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void removeSaveState(String key) {
        try {
            if (snappyDB.exists(key))
                snappyDB.del(key);
        } catch (SnappydbException e) {
            e.printStackTrace();
        } finally {
            removeKey(key);
        }
    }


    public void saveStack(Stack<Integer> stack, String key) {
        try {
            snappyDB.put(key + "Stack", stack);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public Stack<Integer> getStack(String key) {
        try {
            if (snappyDB.exists(key + "Stack"))
                return snappyDB.get(key + "Stack", Stack.class);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeStack(String key) {
        try {
            if (snappyDB.exists(key + "Stack"))
                snappyDB.del(key + "Stack");
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }


    private void storeKey(String key) {
        try {
            List<String> keys = getKeys();
            if (!keys.contains(key))
                keys.add(key);
            snappyDB.put(KEYSTORE, keys);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    private void removeKey(String key) {
        try {
            if (snappyDB.exists(KEYSTORE)) {
                List<String> keys = getKeys();
                keys.remove(key);
                snappyDB.put(KEYSTORE, keys);
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public List<String> getKeys() {
        try {
            if (snappyDB.exists(KEYSTORE))
                return snappyDB.get(KEYSTORE, ArrayList.class);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public void saveArea(AreaModel answerModel, String key) {
        try {
            List<RtfModel> rtfModels = answerModel.getMemberRtfModels();
            Collections.shuffle(rtfModels);
            List<RtfModel> temp = new ArrayList<>();
            if (rtfModels.size() > 1) {
                temp.add(rtfModels.get(0));
                temp.add(rtfModels.get(1));
            } else {
                temp.addAll(rtfModels);
            }
            rtfModels.clear();
            answerModel.setMemberRtfModels(temp);
            snappyDB.put("AREA" + key, answerModel);
            storeKeyArea(key);
        } catch (SnappydbException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            storeKey(key);

        }
    }

    public AreaModel getArea(String key) {
        try {
            if (snappyDB.exists("AREA" + key))
                return snappyDB.get("AREA" + key, AreaModel.class);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeArea(String key) {
        try {
            if (snappyDB.exists("AREA" + key))
                snappyDB.del("AREA" + key);
        } catch (SnappydbException e) {
            e.printStackTrace();
        } finally {
            removeKeyArea(key);
        }
    }


    private void storeKeyArea(String key) {
        try {
            List<String> keys = getKeysArea();
            if (!keys.contains(key))
                keys.add(key);
            snappyDB.put("AREA_KEY" + KEYSTORE, keys);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    private void removeKeyArea(String key) {
        try {
            if (snappyDB.exists("AREA_KEY" + KEYSTORE)) {
                List<String> keys = getKeysArea();
                keys.remove(key);
                snappyDB.put("AREA_KEY" + KEYSTORE, keys);
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public List<String> getKeysArea() {
        try {
            if (snappyDB.exists("AREA_KEY" + KEYSTORE))
                return snappyDB.get("AREA_KEY" + KEYSTORE, ArrayList.class);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}
