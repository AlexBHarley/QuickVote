package com.example.alex.quickvote;


import java.util.ArrayList;
import java.util.UUID;

import io.deepstream.DeepstreamClient;
import io.deepstream.List;
import io.deepstream.Record;

public class CreatePollPresenter {

    DeepstreamService service;

    private CreatePollActivity context;

    public CreatePollPresenter() {
        service = DeepstreamService.getInstance();
    }

    public void takeView(CreatePollActivity context) {
        this.context = context;
    }

    public void createPoll(String pollName, int pollDuration, ArrayList<PollOption> pollOptions) {
        DeepstreamClient client = service.getDeepstreamClient();

        List recordList = client.record.getList("poll/" + pollName);
        ArrayList<String> options = new ArrayList<>();
        for (PollOption option : pollOptions) {
            Record r = client.record.getRecord(pollName + "/" + option.name);
            options.add(r.name());
        }
        recordList.setEntries(options);
    }
}
