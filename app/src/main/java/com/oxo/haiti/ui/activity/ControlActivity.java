package com.oxo.haiti.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oxo.haiti.R;
import com.oxo.haiti.model.CommonModel;
import com.oxo.haiti.service.RestAdapter;
import com.oxo.haiti.storage.ContentStorage;
import com.oxo.haiti.storage.SnappyNoSQL;
import com.oxo.haiti.ui.base.BaseActivity;
import com.oxo.haiti.utils.ExpandableLayout;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlActivity extends BaseActivity implements View.OnClickListener {
    /**
     * Created by jaswinderwadali on 5/16/2016.
     */
    private Toolbar toolbar;
    private List<String> sOneKeys = new ArrayList<>();
    private List<String> sTwoKeys = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.findViewById(R.id.settings).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        problemSolver();
        layoutManager();
    }

    private void layoutManager() {
        ExpandableLayout expandableLayoutOne = (ExpandableLayout) findViewById(R.id.survey_one);
        ExpandableLayout expandableLayoutTwo = (ExpandableLayout) findViewById(R.id.survey_two);
        expandableLayoutOne.setOnChildToggleListener(expandableLayoutChildToggleListener);
        expandableLayoutTwo.setOnChildToggleListener(expandableLayoutChildToggleListener);

        TextView textView = (TextView) expandableLayoutTwo.findViewById(R.id.header_tv);
        textView.setText(getString(R.string.survey_two));

        expandableLayoutOne.findViewById(R.id.new_survey).setTag(0);
        expandableLayoutTwo.findViewById(R.id.new_survey).setTag(1);
        expandableLayoutOne.findViewById(R.id.resume_survey).setTag(0);
        expandableLayoutTwo.findViewById(R.id.resume_survey).setTag(1);

        expandableLayoutOne.findViewById(R.id.new_survey).setOnClickListener(newSurvey);
        expandableLayoutTwo.findViewById(R.id.new_survey).setOnClickListener(newSurvey);
        expandableLayoutOne.findViewById(R.id.resume_survey).setOnClickListener(resumeSurvey);
        expandableLayoutTwo.findViewById(R.id.resume_survey).setOnClickListener(resumeSurvey);


        if (sOneKeys.size() > 0) {
            expandableLayoutOne.findViewById(R.id.resume_survey).setVisibility(View.VISIBLE);
        } else {
            expandableLayoutOne.findViewById(R.id.resume_survey).setVisibility(View.GONE);

        }


        if (sTwoKeys.size() > 0) {
            expandableLayoutTwo.findViewById(R.id.resume_survey).setVisibility(View.VISIBLE);
        } else {
            expandableLayoutTwo.findViewById(R.id.resume_survey).setVisibility(View.GONE);

        }


    }

    ExpandableLayout.OnChildToggleListener expandableLayoutChildToggleListener = new ExpandableLayout.OnChildToggleListener() {
        @Override
        public void onToggled(ExpandableLayout layout, boolean opened) {

        }
    };


    View.OnClickListener resumeSurvey = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(ControlActivity.this, FragmentControler.class);
            switch ((int) v.getTag()) {
                case 0:
                    intent.putExtra("SURVEY", "ONE");
                    alertBuilder(sOneKeys);
                    intent.putExtra("RESUME", true);

                    break;
                case 1:
                    intent.putExtra("SURVEY", "TWO");
                    alertBuilder(sTwoKeys);
                    intent.putExtra("RESUME", true);
                    break;
            }
//            startActivity(intent);
        }
    };


    private void problemSolver() {
        List<String> keys = SnappyNoSQL.getInstance().getKeys();
        for (String key : keys) {
            if (key.contains("ONE"))
                sOneKeys.add(key);
            else
                sTwoKeys.add(key);
        }
    }

    private View.OnClickListener newSurvey = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ControlActivity.this, FragmentControler.class);
            intent.putExtra("RESUME", false);
            switch ((int) v.getTag()) {
                case 0:
                    intent.putExtra("key", "ONE" + new Random(100));
                    intent.putExtra("SURVEY", "ONE");
                    break;
                case 1:
                    intent.putExtra("key", "TWO" + new Random(100));
                    intent.putExtra("SURVEY", "TWO");
                    break;
            }
            startActivity(intent);


        }
    };

    @Override
    public void onClick(View v) {
        optionMenu();
    }

    private void optionMenu() {
        PopupMenu popup = new PopupMenu(this, toolbar.findViewById(R.id.settings));
        popup.getMenuInflater().inflate(R.menu.logout, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                ContentStorage.getInstance(ControlActivity.this).loggedIn(false);
                Intent intent = new Intent(ControlActivity.this, AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            }
        });
        popup.show();
    }


    private void alertBuilder(List<String> list) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Select One Name:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.addAll(list);

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        dialog.dismiss();
                    }
                });
        builderSingle.show();
    }

}
