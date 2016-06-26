package com.oxo.haiti.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.oxo.haiti.R;
import com.oxo.haiti.model.AreaModel;
import com.oxo.haiti.model.PersonModel;
import com.oxo.haiti.storage.SnappyNoSQL;

import java.util.Collections;
import java.util.List;

public class SurveyTwo extends AppCompatActivity implements View.OnClickListener {

    AreaModel areaModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_two);

        TextView one = (TextView) findViewById(R.id.one);
        TextView two = (TextView) findViewById(R.id.two);
        areaModel = SnappyNoSQL.getInstance().getArea(getIntent().getStringExtra("key"));
        int count = 0;
        List<PersonModel> rtfModels = areaModel.getMemberRtfModels();
        if (rtfModels != null)
            Collections.shuffle(rtfModels);
        for (PersonModel users : rtfModels) {
            if (!one.getText().equals(users.getName()) && !TextUtils.isEmpty(users.getName())) {
                if (count == 0)
                    one.setText(users.getName());
                else
                    two.setText(users.getName());
                count++;
            }
            if (count == 2) {
                break;
            }

        }
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        showArea();
    }


    void showArea() {
        TextView sit = (TextView) findViewById(R.id.sit);
        TextView gps = (TextView) findViewById(R.id.gps);
        TextView block = (TextView) findViewById(R.id.block);
        TextView hh = (TextView) findViewById(R.id.hh);
        TextView indu = (TextView) findViewById(R.id.ind);
        sit.setText(areaModel.getSit());
        gps.setText(areaModel.getGps());
        block.setText(areaModel.getBlock());
        hh.setText(areaModel.getHH());
        indu.setText(areaModel.get_id());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, FragmentControler.class);
        intent.putExtra("key", "TWO" + getIntent().getStringExtra("key"));
        intent.putExtra("SURVEY", "TWO");
        startActivity(intent);
    }
}
