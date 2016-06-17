package com.oxo.haiti.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oxo.haiti.R;
import com.oxo.haiti.model.AreaModel;
import com.oxo.haiti.model.RtfModel;
import com.oxo.haiti.storage.SnappyNoSQL;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResumeActivity extends AppCompatActivity {

    List<AreaModel> areaModels = new ArrayList<>();
    private List<String> sOneKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

    }

    @Override
    protected void onResume() {
        super.onResume();
        areaModels.clear();
        List<String> keys = SnappyNoSQL.getInstance().getKeysArea();
        for (String key : keys) {
            AreaModel areaModel = SnappyNoSQL.getInstance().getArea(key);
            if (areaModel.getMemberRtfModels().size() > 0) {
                areaModels.add(areaModel);
            } else {
                SnappyNoSQL.getInstance().removeArea(key);
            }
        }
        ListView listView = (ListView) findViewById(R.id.datalist);

        problemSolver();
        listView.setAdapter(new LocalAdapter(areaModels, sOneKeys));
    }


    private void problemSolver() {
        sOneKeys.clear();
        List<String> keys = SnappyNoSQL.getInstance().getKeys();
        for (String key : keys) {
            if (key.contains("ONE")) {
                sOneKeys.add(key);
                Log.d("TAG===", key);
            }
        }
    }


    public class LocalAdapter extends BaseAdapter {
        List<AreaModel> areaModel;
        List<String> surveyOne;

        LocalAdapter(List<AreaModel> areaModel, List<String> surveyOne) {
            this.surveyOne = surveyOne;
            this.areaModel = areaModel;
        }

        @Override
        public int getCount() {
            return areaModel.size() + surveyOne.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_survey_two, null);
            if (position < areaModels.size()) {
                linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_survey_two, null);
                TextView sit = (TextView) linearLayout.findViewById(R.id.sit);
                TextView gps = (TextView) linearLayout.findViewById(R.id.gps);
                TextView block = (TextView) linearLayout.findViewById(R.id.block);
                TextView hh = (TextView) linearLayout.findViewById(R.id.hh);
                TextView indu = (TextView) linearLayout.findViewById(R.id.ind);
                final LinearLayout linearLayout1 = (LinearLayout) linearLayout.findViewById(R.id.mainlayout_one);
                final LinearLayout linearLayout2 = (LinearLayout) linearLayout.findViewById(R.id.mainlayout_two);


                final TextView one = (TextView) linearLayout.findViewById(R.id.one);
                final TextView two = (TextView) linearLayout.findViewById(R.id.two);


                sit.setText(areaModel.get(position).getSit());
                gps.setText(areaModel.get(position).getGps());
                block.setText(areaModel.get(position).getBlock());
                hh.setText(areaModel.get(position).getHH());
                indu.setText(areaModel.get(position).get_id());
                int count = 0;

                List<RtfModel> rtfModels = areaModel.get(position).getMemberRtfModels();
                for (RtfModel users : rtfModels) {
                    if (!one.getText().equals(users.getName()) && !TextUtils.isEmpty(users.getName())) {
                        if (count == 0) {
                            linearLayout1.setVisibility(View.VISIBLE);
                            one.setText(users.getName());
                            one.setTag(areaModel.get(position).get_id());

                        } else {
                            linearLayout2.setVisibility(View.VISIBLE);
                            two.setText(users.getName());
                            two.setTag(areaModel.get(position).get_id());
                        }
                        count++;
                    }
                    if (count == 2) {
                        break;
                    }

                }
                one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = (String) v.getTag();
                        Intent intent = new Intent(ResumeActivity.this, FragmentControler.class);
                        intent.putExtra("key", id);
                        intent.putExtra("SURVEY", "TWO");
                        intent.putExtra("mainId", id);
                        intent.putExtra("Name", one.getText());

                        startActivity(intent);
                    }
                });
                two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = (String) v.getTag();
                        Intent intent = new Intent(ResumeActivity.this, FragmentControler.class);
                        intent.putExtra("key", id);
                        intent.putExtra("SURVEY", "TWO");
                        intent.putExtra("mainId", id);
                        intent.putExtra("Name", two.getText());
                        startActivity(intent);

                    }
                });
            } else {
                linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_survey_two, null);
                LinearLayout linearLayout2 = (LinearLayout) linearLayout.findViewById(R.id.maxproblem);
                linearLayout2.setVisibility(View.GONE);

                TextView sit = (TextView) linearLayout.findViewById(R.id.sit);
                TextView gps = (TextView) linearLayout.findViewById(R.id.gps);
                TextView block = (TextView) linearLayout.findViewById(R.id.block);
                TextView hh = (TextView) linearLayout.findViewById(R.id.hh);
                TextView indu = (TextView) linearLayout.findViewById(R.id.ind);
                sit.setVisibility(View.GONE);
                gps.setVisibility(View.GONE);
                block.setVisibility(View.GONE);
                hh.setVisibility(View.GONE);
                indu.setVisibility(View.GONE);
                final LinearLayout linearLayout1 = (LinearLayout) linearLayout.findViewById(R.id.mainlayout_one);
                linearLayout1.setVisibility(View.VISIBLE);
                final TextView one = (TextView) linearLayout.findViewById(R.id.one);
                one.setText(surveyOne.get(position - areaModel.size()));
                one.setTag(surveyOne.get(position - areaModel.size()));
                one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = (String) v.getTag();
                        Intent intent = new Intent(ResumeActivity.this, FragmentControler.class);
                        intent.putExtra("key", id);
                        intent.putExtra("SURVEY", "ONE");
                        intent.putExtra("mainId", id);
                        intent.putExtra("RESUME", true);
                        intent.putExtra("Name", one.getText());

                        startActivity(intent);
                    }
                });


            }
            return linearLayout;
        }
    }
}
