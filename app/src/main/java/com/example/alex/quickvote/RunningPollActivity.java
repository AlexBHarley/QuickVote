package com.example.alex.quickvote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import io.deepstream.DeepstreamClient;
import io.deepstream.List;
import io.deepstream.Record;
import io.deepstream.RecordChangedCallback;


public class RunningPollActivity extends AppCompatActivity {

    ListView mListView;
    String mPollName;
    SimpleAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_poll);

        Intent i = getIntent();
        mPollName = i.getStringExtra("pollName");

        mListView = (ListView) findViewById(R.id.poll_options);
        mAdapter = new SimpleAdapter(this);
        mListView.setAdapter(mAdapter);

        populateList();
    }

    private void populateList() {
        DeepstreamClient client = DeepstreamService.getInstance().getDeepstreamClient();
        Record r = client.record.getRecord(mPollName);

        JsonArray options = r.get("options").getAsJsonArray();

        Log.d("DS", "Getting options");

        ArrayList<PollOption> pollOptions = new ArrayList<>();
        for (JsonElement option : options) {
            Record optionRec = client.record.getRecord(option.getAsString());
            optionRec.subscribe("votes", new RecordChangedCallback() {
                @Override
                public void onRecordChanged(String s, JsonElement jsonElement) {
                    Log.d("DEBUG", "Shouldn't be here");
                }

                @Override
                public void onRecordChanged(String s, String s1, Object o) {
                    Log.d("RECORD CHANGE: " + s, "Path: " + s1 + " data " + o.toString());
                }
            });
            String recName = optionRec.get("name").getAsString();
            Log.d("Name", recName);
            int recVotes = optionRec.get("votes").getAsInt();
            Log.d("Votes", String.valueOf(recVotes));
            pollOptions.add(new PollOption(recName, recVotes));
        }

        mAdapter.add(pollOptions);
        Log.d("Count", String.valueOf(mAdapter.getCount()));
        mAdapter.notifyDataSetChanged();
    }
}
