package com.example.alex.quickvote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Alex on 6/08/2016.
 */
public class CreatePollActivity extends AppCompatActivity {

    private CreatePollPresenter presenter;
    private RecyclerView pollOptionRecyclerView;
    private PollOptionAdapter mPollAdapter;
    private ArrayList<PollOption> options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        options = new ArrayList<>();
        pollOptionRecyclerView = (RecyclerView) findViewById(R.id.poll_options);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mPollAdapter = new PollOptionAdapter(options);
        pollOptionRecyclerView.setLayoutManager(layoutManager);
        pollOptionRecyclerView.setAdapter(mPollAdapter);
        
        addData();



    }

    private void addData() {
        for(int i = 0; i < 10; i++) {
            PollOption p = new PollOption(String.valueOf(i));
            options.add(p);
        }
        mPollAdapter.notifyDataSetChanged();
    }

}
