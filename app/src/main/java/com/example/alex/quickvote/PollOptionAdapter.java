package com.example.alex.quickvote;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class PollOptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PollOption> options;
    private CreatePollActivity context;

    public PollOptionAdapter(CreatePollActivity context) {
        this.options = new ArrayList<>();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        int layoutRes;

        layoutRes = R.layout.poll_option;
        view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new PollOptionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("onBindViewHolder", "item type: " + String.valueOf(holder.getItemViewType()));

        PollOptionViewHolder pollOptionViewHolder = (PollOptionViewHolder) holder;
        pollOptionViewHolder.option.setText(options.get(position).name);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public void addOption(String s) {
        this.options.add(new PollOption(s)); // hack
        notifyItemInserted(options.size());
        context.notifyChanges();
    }

    public ArrayList<PollOption> getOptions() {
        return options;
    }

    class PollOptionViewHolder extends RecyclerView.ViewHolder {

        TextView option;

        public PollOptionViewHolder(View itemView) {
            super(itemView);
            option = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
