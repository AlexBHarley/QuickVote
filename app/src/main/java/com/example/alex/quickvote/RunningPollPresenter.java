package com.example.alex.quickvote;


import android.util.Log;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.UUID;

import io.deepstream.DeepstreamClient;
import io.deepstream.List;
import io.deepstream.Record;
import io.deepstream.RecordChangedCallback;

public class RunningPollPresenter {

    DeepstreamService service;

    private RunningPollActivity context;

    public RunningPollPresenter() {
        service = DeepstreamService.getInstance();
    }

    public void takeView(RunningPollActivity context) {
        this.context = context;
    }

    public void showPoll(String pollName) {
        DeepstreamClient client = service.getDeepstreamClient();

        List recordList = client.record.getList("poll/" + pollName);

        ArrayList<Record> options = new ArrayList<>();
        Log.d("Deepstream", recordList.getEntries().toString());
        for (String recordName : recordList.getEntries()) {
            Record r = client.record.getRecord(pollName + "/" + recordName);
            r.subscribe(new RecordChangedCallback() {
                @Override
                public void onRecordChanged(String s, JsonElement jsonElement) {
                    updatePoll(s, jsonElement);
                }

                @Override
                public void onRecordChanged(String s, String s1, Object o) {

                }
            });
            options.add(r);
        }
    }

    private void updatePoll(String s, JsonElement jsonElement) {
        Log.d("Deepstream", "Updated value: " + s + " with data " + jsonElement.toString());
    }
}
