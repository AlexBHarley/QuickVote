package com.example.alex.quickvote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;

import io.deepstream.DeepstreamClient;
import io.deepstream.Record;
import io.deepstream.RecordChangedCallback;


public class RunningPollActivity extends AppCompatActivity {

    ListView mListView;
    String mPollName;
    SimpleAdapter mAdapter;
    ArrayList<PollOption> currentPollOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_poll);

        Intent i = getIntent();
        mPollName = i.getStringExtra("pollName");
        mListView = (ListView) findViewById(R.id.poll_options);
        currentPollOptions = new ArrayList<>();
        mAdapter = new SimpleAdapter(this, currentPollOptions);
        mListView.setAdapter(mAdapter);

        populateList();
    }

    private void populateList() {
        DeepstreamClient client = DeepstreamService.getInstance().getDeepstreamClient();
        Record r = client.record.getRecord(mPollName);

        JsonArray options = r.get("options").getAsJsonArray();

        ArrayList<PollOption> pollOptions = new ArrayList<>();
        for (JsonElement option : options) {
            Record optionRec = client.record.getRecord(option.getAsString());

            final String recName = (optionRec.get("name")).getAsString();
            int recVotes = optionRec.get("votes").getAsInt();

            optionRec.subscribe("votes", new RecordChangedCallback() {
                @Override
                public void onRecordChanged(String s, JsonElement jsonElement) {
                    Log.d("DEBUG", "Shouldn't be here");
                }

                @Override
                public void onRecordChanged(String s, String s1, Object o) {
                    Log.d("RECORD CHANGE: " + s, "Path: " + s1 + " data " + o.toString());
                    updateList(recName, ((JsonPrimitive) o).getAsInt());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
            currentPollOptions.add(new PollOption(recName, recVotes));
        }
        mAdapter.notifyDataSetChanged();
    }

    private void updateList(String optionName, int number) {
        for (PollOption p : currentPollOptions) {
            if(p.name.equals(optionName)) {
                p.votes = number;
                break;
            }
        }
    }
}
