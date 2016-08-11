package com.example.alex.quickvote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;

import io.deepstream.DeepstreamClient;
import io.deepstream.Record;
import io.deepstream.RecordChangedCallback;


public class RunningPollActivity extends AppCompatActivity {

    ListView mListView;
    String pollName;
    SimpleAdapter mAdapter;
    TextView mPollName;
    ArrayList<PollOption> currentPollOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_poll);

        Intent i = getIntent();
        pollName = i.getStringExtra("pollName");
        mPollName = (TextView) findViewById(R.id.running_poll_name);
        mPollName.setText(pollName);
        mListView = (ListView) findViewById(R.id.poll_options);
        currentPollOptions = new ArrayList<>();
        mAdapter = new SimpleAdapter(this, currentPollOptions);
        mListView.setAdapter(mAdapter);

        populateList();

        //updateList("pork", 5);
    }

    private void populateList() {
        final DeepstreamClient client = DeepstreamService.getInstance().getDeepstreamClient();
        Record r = client.record.getRecord("poll/" + pollName);

        final JsonArray options = r.get("options").getAsJsonArray();

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Record re1 = client.record.getRecord(options.get(0).getAsString());
                Record re2 = client.record.getRecord(options.get(1).getAsString());
                Record re3 = client.record.getRecord(options.get(2).getAsString());
                Record re4 = client.record.getRecord(options.get(3).getAsString());
                Record re5 = client.record.getRecord(options.get(4).getAsString());

                re1.set("votes", 1);
                re2.set("votes", 1);
                re3.set("votes", 1);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                re1.set("votes", 2);
                re2.set("votes", 1);
                re3.set("votes", 2);
                re4.set("votes", 1);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                re1.set("votes", 3);
                re4.set("votes", 2);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                re1.set("votes", 4);
                re2.set("votes", 2);
                re4.set("votes", 2);
                re5.set("votes", 1);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
