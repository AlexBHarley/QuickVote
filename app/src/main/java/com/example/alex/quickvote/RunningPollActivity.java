package com.example.alex.quickvote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import io.deepstream.DeepstreamClient;
import io.deepstream.List;


public class RunningPollActivity extends AppCompatActivity {

    RunningPollPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_poll);
        Intent i = getIntent();
        String pollName = i.getStringExtra("pollName");

        DeepstreamClient client = DeepstreamService.getInstance().getDeepstreamClient();

        List options = client.record.getList(pollName);

        for (String e : options.getEntries()) {
            Log.d("Entry", e);
        }
    }
}
