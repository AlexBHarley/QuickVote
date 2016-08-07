package com.example.alex.quickvote;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alex on 7/08/2016.
 */
public class PollOptionAdapter extends RecyclerView.Adapter<PollOptionAdapter.PollOptionViewHolder> {

    private ArrayList<PollOption> options;

    public PollOptionAdapter(ArrayList<PollOption> options) {
        this.options = options;
    }

    @Override
    public PollOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        PollOptionViewHolder vh;

        switch (viewType) {
            /*case 0: // create poll option
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.create_poll_option, parent, false);
                vh = new Po(v);
                return vh;*/

            default: // poll option
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.poll_option, parent, false);
                vh = new PollOptionViewHolder(v, 1);
                return vh;
        }
    }

    @Override
    public void onBindViewHolder(PollOptionViewHolder holder, int position) {
        Log.d("DEBUG", "onBindViewHolder");
        PollOption item = options.get(position);
        holder.option.setText(item.name);
    }

    @Override
    public int getItemViewType(int position) {
        if( position == options.size() ) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    class PollOptionViewHolder extends RecyclerView.ViewHolder {

        TextView option;

        public PollOptionViewHolder(View itemView, int type) {
            super(itemView);
            option = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
