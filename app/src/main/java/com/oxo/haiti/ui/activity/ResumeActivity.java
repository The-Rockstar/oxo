package com.oxo.haiti.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResumeActivity extends AppCompatActivity {

    List<AreaModel> areaModels = new ArrayList<>();

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
        listView.setAdapter(new LocalAdapter(areaModels));
    }

    public class LocalAdapter extends BaseAdapter {
        List<AreaModel> areaModel;

        LocalAdapter(List<AreaModel> areaModel) {
            this.areaModel = areaModel;
        }

        @Override
        public int getCount() {
            return areaModel.size();
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
            TextView sit = (TextView) linearLayout.findViewById(R.id.sit);
            TextView gps = (TextView) linearLayout.findViewById(R.id.gps);
            TextView block = (TextView) linearLayout.findViewById(R.id.block);
            TextView hh = (TextView) linearLayout.findViewById(R.id.hh);
            TextView indu = (TextView) linearLayout.findViewById(R.id.ind);

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
                        one.setText(users.getName());
                        one.setTag(areaModel.get(position).get_id());
                    } else {
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
            return linearLayout;
        }
    }
}
