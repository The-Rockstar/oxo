package com.oxo.haiti.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oxo.haiti.R;
import com.oxo.haiti.model.CommonModel;
import com.oxo.haiti.service.RestAdapter;
import com.oxo.haiti.ui.base.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlActivity extends BaseActivity implements View.OnClickListener {
    /**
     * Created by jaswinderwadali on 5/16/2016.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.one).setOnClickListener(this);
        findViewById(R.id.two).setOnClickListener(this);
       }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, FragmentControler.class);
        switch (v.getId()) {
            case R.id.one:
                intent.putExtra("SURVEY", "ONE");
                break;
            case R.id.two:
                intent.putExtra("SURVEY", "TWO");
                break;
        }
        startActivity(intent);
    }

}
