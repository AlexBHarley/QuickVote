package com.example.alex.quickvote;


public class PollOption {

    public String name;
    public int votes;

    public PollOption(String name) {
        this.name = name;
        this.votes = 0;
    }

    public PollOption(String name, int votes) {
        this.name = name;
        this.votes = votes;
    }
}
