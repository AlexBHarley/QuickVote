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

    private static final int POLL_OPTION = 0;
    private static final int CREATE_POLL_OPTION = 1;

    private ArrayList<PollOption> options;
    private CreatePollActivity context;

    public PollOptionAdapter(CreatePollActivity context) {
        this.options = new ArrayList<>();
        this.options.add(new PollOption("Dummy")); //hack because last element is a edittext
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        int layoutRes;

        switch (viewType) {
            case CREATE_POLL_OPTION:
                layoutRes = R.layout.create_poll_option;
                view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
                return new CreatePollOptionViewHolder(view);
            default:
                layoutRes = R.layout.poll_option;
                view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
                return new PollOptionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("onBindViewHolder", "item type: " + String.valueOf(holder.getItemViewType()));
        switch (holder.getItemViewType()) {

            case POLL_OPTION:
                PollOptionViewHolder pollOptionViewHolder = (PollOptionViewHolder) holder;
                pollOptionViewHolder.option.setText(options.get(position).name);
                break;

            case CREATE_POLL_OPTION:
                CreatePollOptionViewHolder viewHolder = (CreatePollOptionViewHolder) holder;
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == options.size() - 1 ? CREATE_POLL_OPTION : POLL_OPTION;
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    private void addOption(String s) {
        this.options.add(options.size() - 1, new PollOption(s)); // hack
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

    class CreatePollOptionViewHolder extends RecyclerView.ViewHolder {

        EditText option;

        public CreatePollOptionViewHolder(View itemView) {
            super(itemView);
            option = (EditText) itemView.findViewById(R.id.create_poll_option);
            option.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        addOption(option.getText().toString());
                        option.setText("");
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}
