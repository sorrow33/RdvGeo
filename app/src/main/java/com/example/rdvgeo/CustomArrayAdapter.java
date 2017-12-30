package com.example.rdvgeo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class CustomArrayAdapter extends ArrayAdapter {

    List<Rendezvous> rdvList;

    public CustomArrayAdapter(Context context, List<Rendezvous> list) {
        super(context, 0, list);
        rdvList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.row, parent, false);
            // inflate custom layout called row
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.textView1);
            // initialize textview
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Rendezvous rdv = (Rendezvous) rdvList.get(position);
        holder.tv.setText(rdv.getTitre());
        // set the name to the text;

        return convertView;

    }

    static class ViewHolder {

        TextView tv;
    }
}