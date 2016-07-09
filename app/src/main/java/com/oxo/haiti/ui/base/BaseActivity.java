package com.oxo.haiti.ui.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.oxo.haiti.R;
import com.oxo.haiti.model.AreaModel;
import com.oxo.haiti.model.PersonModel;
import com.oxo.haiti.storage.ContentStorage;
import com.oxo.haiti.storage.SnappyNoSQL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wadali on 5/17/2016.
 */
public abstract class BaseActivity extends AppCompatActivity implements DialogInterface.OnClickListener {


    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        messageToast(getString(R.string.press_again));
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    ProgressDialog progressDialog = null;

    protected void showProgress() {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.pleasewait));
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void hideBar() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void messageToast(String message) {
        try {
            final Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void showDialogMessage(String message) {
        messageCallback(false);
        if (!TextUtils.isEmpty(message)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(message);
            alert.setPositiveButton("OK", this);
            alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    messageCallback(true);
                }
            });
            alert.show();
        }
    }

    protected void clearSaveState(String key, boolean vanishRecord) {
//        if (vanishRecord) {
        ContentStorage.getInstance(this).savePositionSurveyOne(0, key);
//        }
        SnappyNoSQL.getInstance().removeSaveState(key);
        SnappyNoSQL.getInstance().removeStack(key);
    }

    protected void removePerson(String key, String Name) {
        try {
            if (key != null) {
                key=key.replace("mmmm","");
                key=key.replace("xxxx","");
                key = key.replace("TWO", "ONE");
            }


            AreaModel areaModel = SnappyNoSQL.getInstance().getArea(key);
            List<PersonModel> rtfModels = new ArrayList<>(areaModel.getMemberRtfModels());
            for (PersonModel rtfModel : rtfModels) {
                if (rtfModel.getName().equals(Name)) {
                    areaModel.getMemberRtfModels().remove(rtfModel);
                }
            }
            if (rtfModels.size() == 0) {
                clearSaveState(key, true);
            }
            SnappyNoSQL.getInstance().saveArea(areaModel, key);
        } catch (Exception e) {

        }

    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        //finish();
        messageCallback(true);
    }

    protected abstract void messageCallback(boolean flag);

}
