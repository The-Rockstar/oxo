package com.oxo.haiti.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxo.haiti.R;
import com.oxo.haiti.model.QuestionsModel;
import com.oxo.haiti.model.UsersModel;
import com.oxo.haiti.service.RestAdapter;
import com.oxo.haiti.storage.ContentStorage;
import com.oxo.haiti.storage.SnappyNoSQL;
import com.oxo.haiti.ui.base.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {
    /**
     * Created by jaswinderwadali on 5/16/2016.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ContentStorage.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, ControlActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Button button = (Button) findViewById(R.id.login_bt);
        button.setOnClickListener(this);

        if (!isActive()) {
            changeStatus(false);
            fetchUsers();
        } else
            changeStatus(true);
    }


    private boolean isActive() {
        if (SnappyNoSQL.getInstance().getUsers() == null)
            return false;
        else
            return true;

    }

    private void fetchUsers() {
        showProgress();
        Call<UsersModel> usersModelCall = RestAdapter.getInstance(this).getApiService().getUsers();
        usersModelCall.enqueue(new Callback<UsersModel>() {
            @Override
            public void onResponse(Call<UsersModel> call, Response<UsersModel> response) {
                SnappyNoSQL.getInstance().saveUsers(response.body());
                fetchSurveyOne("1");
            }

            @Override
            public void onFailure(Call<UsersModel> call, Throwable t) {
                hideBar();
                messageToast(getString(R.string.retry));
            }
        });
    }

    private void fetchSurveyOne(final String id) {
        Call<List<QuestionsModel>> usersModelCall = RestAdapter.getInstance(this).getApiService().getFirstSurvey(id);
        usersModelCall.enqueue(new Callback<List<QuestionsModel>>() {
            @Override
            public void onResponse(Call<List<QuestionsModel>> call, Response<List<QuestionsModel>> response) {
                if (id.equals("1")) {
                    SnappyNoSQL.getInstance().saveSurveyQuestionsOne(response.body());
                    fetchSurveyOne("2");
                } else {
                    SnappyNoSQL.getInstance().saveSurveyQuestionsTwo(response.body());
                    hideBar();
                    changeStatus(true);
                }
            }

            @Override
            public void onFailure(Call<List<QuestionsModel>> call, Throwable t) {
                hideBar();
                messageToast(getString(R.string.retry));
            }
        });


    }

    private void changeStatus(boolean status) {
        ImageView imageView = (ImageView) findViewById(R.id.sync_iv);
        TextView textView = (TextView) findViewById(R.id.status_tv);
        if (status) {
            imageView.setImageResource(R.drawable.ic_done_all_50dp);
            textView.setText(getString(R.string.activr));
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setImageResource(R.drawable.ic_sync_problem_50dp);
            textView.setText(getString(R.string.notactiviated));
        }
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sync_iv:
                messageToast(getString(R.string.pleasewait));
                fetchUsers();
                break;
            case R.id.login_bt:
                processLogin();
                break;
        }
    }


    private void processLogin() {
        EditText userNameEditText = (EditText) findViewById(R.id.username_et);
        EditText passwordEditText1 = (EditText) findViewById(R.id.password_et);
        if (TextUtils.isEmpty(userNameEditText.getText().toString())) {
            messageToast(getString(R.string.invaliduser));
        } else if (TextUtils.isEmpty(passwordEditText1.getText().toString())) {
            messageToast(getString(R.string.invalidpassword));
        } else {
            boolean auth = SnappyNoSQL.getInstance().loginAuth(userNameEditText.getText().toString(), passwordEditText1.getText().toString());
            if (auth) {
                messageToast(getString(R.string.success));
                ContentStorage.getInstance(this).loggedIn(true);
                ContentStorage.getInstance(this).loggedIn(userNameEditText.getText().toString(), passwordEditText1.getText().toString());
                startActivity(new Intent(this, ControlActivity.class));
                finish();
            } else {
                messageToast(getString(R.string.invalid_userpass));
            }
        }
    }
}
