package com.example.alex.quickvote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class CreatePollActivity extends AppCompatActivity {

    private CreatePollPresenter presenter;
    private RecyclerView pollOptionRecyclerView;
    private PollOptionAdapter mPollAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        setUpRecyclerView();

        Button mCreatePollButton = (Button) findViewById(R.id.create_poll_button);
        mCreatePollButton.setOnClickListener(new CreatePollClickListener());

        if (presenter == null) presenter = new CreatePollPresenter();
        presenter.takeView(this);
    }

    public void notifyChanges() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        pollOptionRecyclerView.scrollToPosition(0);
    }

    private void setUpRecyclerView() {
        pollOptionRecyclerView = (RecyclerView) findViewById(R.id.poll_options);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mPollAdapter = new PollOptionAdapter(CreatePollActivity.this);
        pollOptionRecyclerView.setLayoutManager(layoutManager);
        pollOptionRecyclerView.setAdapter(mPollAdapter);
        pollOptionRecyclerView.setHasFixedSize(true);
    }

    private class CreatePollClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String pollName = ((TextView) findViewById(R.id.poll_name)).getText().toString();
            int pollDuration = Integer.valueOf(((TextView) findViewById(R.id.poll_duration)).getText().toString());
            presenter.createPoll(pollName, pollDuration, mPollAdapter.getOptions());
            Intent i = new Intent(CreatePollActivity.this, RunningPollActivity.class);
            i.putExtra("pollName", pollName);
            startActivity(i);
        }
    }
}
