package com.example.alex.quickvote;

import java.net.URISyntaxException;

import io.deepstream.DeepstreamClient;


public class DeepstreamService {

    private static DeepstreamService service = new DeepstreamService();
    private DeepstreamClient deepstreamClient;

    public static DeepstreamService getInstance() {
        return service;
    }

    private DeepstreamService() {
        try {
            this.deepstreamClient = new DeepstreamClient( "localhost:6021" );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    DeepstreamClient getDeepstreamClient() {
        return this.deepstreamClient;
    }
}