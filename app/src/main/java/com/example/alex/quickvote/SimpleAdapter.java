package com.example.alex.quickvote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Alex on 10/08/2016.
 */
public class SimpleAdapter extends ArrayAdapter<PollOption> {

    private final Context context;
    private final PollOption[] values;

    public SimpleAdapter(Context context, PollOption[] objects) {
        super(context, -1, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.running_poll_option, parent, false);
        TextView optionName = (TextView) rowView.findViewById(R.id.poll_option_name);
        TextView optionVotes = (TextView) rowView.findViewById(R.id.poll_option_votes);
        optionName.setText(values[position].name);
        optionVotes.setText(Integer.toString(values[position].votes));

        return rowView;
    }
}
