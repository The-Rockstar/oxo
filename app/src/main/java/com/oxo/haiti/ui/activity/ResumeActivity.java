package com.oxo.haiti.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oxo.haiti.R;
import com.oxo.haiti.model.AnswerModel;
import com.oxo.haiti.model.AreaModel;
import com.oxo.haiti.model.PersonModel;
import com.oxo.haiti.storage.SnappyNoSQL;

import java.util.ArrayList;
import java.util.List;

public class ResumeActivity extends AppCompatActivity {

    private List<AreaModel> areaModels = new ArrayList<>();
    private List<String> sOneKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            areaModels.clear();
            List<String> keys = SnappyNoSQL.getInstance().getKeysArea();
            for (String key : keys) {
                AreaModel areaModel = SnappyNoSQL.getInstance().getArea(key);
                List<PersonModel> rtfModels = null;
                try {
                    rtfModels = areaModel.getMemberRtfModels();
                } catch (Exception e) {
                    rtfModels = null;
                } finally {
                    if (rtfModels != null) {
                        if (areaModel.getMemberRtfModels().size() > 0) {
                            areaModels.add(areaModel);
                        } else {
                            SnappyNoSQL.getInstance().removeArea(key);
                        }
                    } else {
                        SnappyNoSQL.getInstance().removeArea(key);
                    }

                }

            }
            ListView listView = (ListView) findViewById(R.id.datalist);

            problemSolver();
            for (String key : sOneKeys) {
                for (AreaModel areaModel : areaModels) {
                    if (areaModel.get_id().contains(key)){
                        areaModels.remove(areaModel);
                    }
                }
            }
            LocalAdapter localAdapter = new LocalAdapter(areaModels, sOneKeys);
            listView.setAdapter(localAdapter);
            if ((areaModels.size() + sOneKeys.size()) == 0) {
                findViewById(R.id.max_mus).setVisibility(View.VISIBLE);
            } else
                findViewById(R.id.max_mus).setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void problemSolver() {
        sOneKeys.clear();
        List<String> keys = SnappyNoSQL.getInstance().getKeys();
        for (String key : keys) {
            if (key.contains("ONE") && !key.contains("StopStatus")) {
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
                hh.setText("" + areaModel.get(position).getSit() + areaModel.get(position).getBlock() + areaModel.get(position).getGps() + areaModel.get(position).getHH());
                indu.setText(areaModel.get(position).get_id());
                int count = 0;

                List<PersonModel> rtfModels = areaModel.get(position).getMemberRtfModels();
                for (PersonModel users : rtfModels) {
                    if (!one.getText().equals(users.getName()) && !TextUtils.isEmpty(users.getName())) {
                        if (count == 0) {
                            linearLayout1.setVisibility(View.VISIBLE);
                            one.setText(users.getName());
                            one.setTag(areaModel.get(position).get_id().replace("ONE", "TWO"));

                        } else {
                            linearLayout2.setVisibility(View.VISIBLE);
                            two.setText(users.getName());
                            two.setTag(areaModel.get(position).get_id().replace("ONE", "TWO"));
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
                        intent.putExtra("key", id + "xxxx");
                        intent.putExtra("SURVEY", "TWO");
                        intent.putExtra("mainId", id);
                        intent.putExtra("RESUME", true);
                        intent.putExtra("Name", one.getText());

                        startActivity(intent);
                    }
                });
                two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = (String) v.getTag();
                        Intent intent = new Intent(ResumeActivity.this, FragmentControler.class);
                        intent.putExtra("key", id + "mmmm");
                        intent.putExtra("SURVEY", "TWO");
                        intent.putExtra("mainId", id);
                        intent.putExtra("RESUME", true);
                        intent.putExtra("Name", two.getText());
                        startActivity(intent);

                    }
                });
            } else {


                AnswerModel answerModel = SnappyNoSQL.getInstance().getSaveState(surveyOne.get(position - areaModel.size()));
                if (answerModel != null) {
                    AreaModel areaModel = executer(answerModel);
                    linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_survey_two, null);
                    TextView sit = (TextView) linearLayout.findViewById(R.id.sit);
                    TextView gps = (TextView) linearLayout.findViewById(R.id.gps);
                    TextView block = (TextView) linearLayout.findViewById(R.id.block);
                    TextView hh = (TextView) linearLayout.findViewById(R.id.hh);
                    TextView indu = (TextView) linearLayout.findViewById(R.id.ind);
                    indu.setVisibility(View.VISIBLE);
                    final LinearLayout linearLayout1 = (LinearLayout) linearLayout.findViewById(R.id.xmen);
                    linearLayout1.setVisibility(View.GONE);
                    final LinearLayout linearLayou = (LinearLayout) linearLayout.findViewById(R.id.mainlayout_one);
                    linearLayou.setVisibility(View.GONE);
                    final LinearLayout linearLayout2 = (LinearLayout) linearLayout.findViewById(R.id.mainlayout_two);
                    linearLayout2.setVisibility(View.GONE);

                    final TextView one = (TextView) linearLayout.findViewById(R.id.one);
                    final TextView two = (TextView) linearLayout.findViewById(R.id.two);


                    sit.setText(areaModel.getSit());
                    gps.setText(areaModel.getGps());
                    block.setText(areaModel.getBlock());
                    hh.setText("" + areaModel.getSit() + areaModel.getBlock() + areaModel.getGps() + areaModel.getHH());
                    indu.setText(answerModel.getSurveryId());
                    int count = 0;

                    hh.setTag(answerModel.getGenerated_survey());
                    hh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id = (String) v.getTag();
                            Intent intent = new Intent(ResumeActivity.this, FragmentControler.class);

                            if (!id.contains("ONE")) {
                                id = "ONE" + id;
                            }
                            intent.putExtra("key", id);
                            intent.putExtra("SURVEY", "ONE");
                            intent.putExtra("mainId", id);
                            intent.putExtra("RESUME", true);
                            intent.putExtra("Name", one.getText());

                            startActivity(intent);
                        }
                    });
                } else
                    linearLayout.setVisibility(View.INVISIBLE);

            }
            return linearLayout;
        }
    }


    private AreaModel executer(AnswerModel answerModel) {
        AreaModel areaModel = new AreaModel();
        for (AnswerModel.SuveryAnswer answer : answerModel.getSuveryAnswers()) {

            if (answer.getQuestionId().equals("hid_140") && !TextUtils.isEmpty(answer.getAnswer())) {

            } else if (answer.getQuestionId().equals("hid_2") || answer.getQuestionId().equals("hid_3") || answer.getQuestionId().equals("hid_4") || answer.getQuestionId().equals("hid_5") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.setBlock(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_1") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.setSit(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_7") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.setHH(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_6") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.setGps(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_8") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.setLat(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_10") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.set_long(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_14") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.setName(answer.getAnswer());
            } else if (answer.getQuestionId().equals("hid_15") && !TextUtils.isEmpty(answer.getAnswer())) {
                areaModel.setDesc(answer.getAnswer());
            }
        }
        return areaModel;
    }


}
