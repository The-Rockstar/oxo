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
import com.oxo.haiti.storage.ContentStorage;
import com.oxo.haiti.storage.SnappyNoSQL;
import com.oxo.haiti.ui.base.BaseActivity;
import com.oxo.haiti.utils.ExpandableLayout;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

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

        Intent intent = new Intent(this, FragmentControler.class);
        intent.putExtra("key", "asdsad");
        intent.putExtra("SURVEY", "TWO");
        intent.putExtra("mainId", "asdsad");
        intent.putExtra("Name", "jas");
//        startActivity(intent);

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

        TextView bodynewtwo = (TextView) expandableLayoutTwo.findViewById(R.id.new_survey);
        bodynewtwo.setText(getString(R.string.nouvomoun));

        TextView alreadyStartedTwo = (TextView) expandableLayoutTwo.findViewById(R.id.resume_survey);
        alreadyStartedTwo.setText(R.string.alreadystartedtwo);

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
            expandableLayoutOne.findViewById(R.id.resume_survey).setVisibility(View.VISIBLE);
        }

        if (SnappyNoSQL.getInstance().getKeysArea().size() > 0) {
            expandableLayoutTwo.findViewById(R.id.resume_survey).setVisibility(View.VISIBLE);
        } else {
            expandableLayoutTwo.findViewById(R.id.resume_survey).setVisibility(View.VISIBLE);
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
            Intent intent = new Intent(ControlActivity.this, ResumeActivity.class);
            startActivity(intent);
        }
    };


    private void problemSolver() {
        sOneKeys.clear();
        sTwoKeys.clear();
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
                    intent.putExtra("key", "ONE" + new DateTime().toString("yyyy-MM-dd hh:mm:ss"));
                    intent.putExtra("SURVEY", "ONE");
                    break;
                case 1:
                    intent.putExtra("key", "TWO" + new DateTime().toString("yyyy-MM-dd hh:mm:ss"));
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


    private void alertBuilder(final List<String> pureList) {
        List<String> list = new ArrayList<>(pureList);
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).contains("ONE")) {
                flag = true;
            }
            list.set(i, list.get(i).replace("ONE", ""));
            list.set(i, list.get(i).replace("TWO", ""));
        }
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(getString(flag ? R.string.alreadystartedone : R.string.alreadystartedtwo).concat(" : "));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1);
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
                        String keyValue = pureList.get(which);
                        Intent intent = new Intent(ControlActivity.this, FragmentControler.class);
                        intent.putExtra("SURVEY", keyValue.contains("ONE") ? "ONE" : "TWO");
                        intent.putExtra("RESUME", true);
                        intent.putExtra("key", keyValue);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }

        );
        builderSingle.show();
    }

    @Override
    protected void messageCallback(boolean flag) {

    }
}
