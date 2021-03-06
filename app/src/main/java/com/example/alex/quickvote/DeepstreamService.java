package com.example.alex.quickvote;

import java.net.URISyntaxException;

import io.deepstream.DeepstreamClient;


public class DeepstreamService {

    private static DeepstreamService ourInstance = new DeepstreamService();
    DeepstreamClient deepstreamClient;

    public static DeepstreamService getInstance() {
        return ourInstance;
    }

    DeepstreamService() {
        try {
            this.deepstreamClient = new DeepstreamClient( "10.0.2.2:6021" );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    DeepstreamClient getDeepstreamClient() {
        return this.deepstreamClient;
    }
}