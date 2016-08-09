package com.example.alex.quickvote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import io.deepstream.DeepstreamClient;
import io.deepstream.List;
import io.deepstream.Record;


public class RunningPollActivity extends AppCompatActivity {

    ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_poll);
        Intent i = getIntent();
        String pollName = i.getStringExtra("pollName");

        mListView = (ListView) findViewById(R.id.poll_options);

        PollOption[] poa = new PollOption[]{
                new PollOption("option1", 0),
                new PollOption("option2", 0),
                new PollOption("option3", 0)
        };

        mListView.setAdapter(new SimpleAdapter(this, poa));


    }

    private void populateList() {
        DeepstreamClient client = DeepstreamService.getInstance().getDeepstreamClient();

        Record r = client.record.getRecord("");//pollName);

        String listName = r.get("optionListName").getAsString();
        Log.d("DS", "Getting " + listName);
        List options = client.record.getList(listName);

        for (String e : options.getEntries()) {
            Log.d("Entry", e);
        }
    }
}
