package com.oxo.haiti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by jaswinderwadali on 13/06/16.
 */
public class CustomArrayAdapter extends ArrayAdapter<String> {


    public CustomArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CustomArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public CustomArrayAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    public CustomArrayAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }


    public CustomArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    public CustomArrayAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }




    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, null);
    }
}
