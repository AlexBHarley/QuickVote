package com.example.alex.quickvote;


import java.util.ArrayList;

public class PollObject {

    String name;
    ArrayList<String> options;

    public PollObject(String name, ArrayList<String> optionNames) {
        this.name = name;
        this.options = optionNames;
    }
}
