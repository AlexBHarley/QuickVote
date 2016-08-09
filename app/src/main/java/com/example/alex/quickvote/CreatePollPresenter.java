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
        //service = DeepstreamService.getInstance();
    }

    public void takeView(CreatePollActivity context) {
        this.context = context;
    }


}
