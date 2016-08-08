package com.example.alex.quickvote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class RunningPollActivity extends AppCompatActivity {

    RunningPollPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_poll);
        presenter = new RunningPollPresenter();
        presenter.takeView(this);

        Intent i = getIntent();

        String pollName = i.getStringExtra("pollName");

        presenter.showPoll(pollName);
    }
}
