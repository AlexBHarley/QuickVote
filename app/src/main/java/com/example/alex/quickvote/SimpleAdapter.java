package com.example.alex.quickvote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SimpleAdapter extends ArrayAdapter<PollOption> {

    private final Context context;
    private ArrayList<PollOption> values;

    public SimpleAdapter(Context context, ArrayList<PollOption> values) {
        super(context, -1, new PollOption[]{});
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.running_poll_option, parent, false);
        TextView optionName = (TextView) rowView.findViewById(R.id.poll_option_name);
        TextView optionVotes = (TextView) rowView.findViewById(R.id.poll_option_votes);
        optionName.setText(values.get(position).name);
        optionVotes.setText(Integer.toString(values.get(position).votes));

        return rowView;
    }
}
