package com.example.alex.quickvote;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.UUID;

import io.deepstream.DeepstreamClient;
import io.deepstream.DeepstreamLoginException;
import io.deepstream.DeepstreamRuntimeErrorHandler;
import io.deepstream.List;
import io.deepstream.LoginResult;
import io.deepstream.Record;
import io.deepstream.constants.Event;
import io.deepstream.constants.Topic;


public class CreatePollActivity extends AppCompatActivity {

    private CreatePollPresenter presenter;
    private RecyclerView pollOptionRecyclerView;
    private PollOptionAdapter mPollAdapter;

    private EditText mPollNameField;
    private EditText mPollDurationField;
    private EditText mNewPollNameField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        setUpRecyclerView();

        mPollNameField = (EditText) findViewById(R.id.poll_name);
        mPollDurationField = (EditText) findViewById(R.id.poll_duration);
        mNewPollNameField = (EditText) findViewById(R.id.new_poll_option_name);
        mNewPollNameField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    tryAddPollOption();
                    return true;
                }
                return false;
            }
        });
        Button mCreatePollButton = (Button) findViewById(R.id.create_poll_button);
        mCreatePollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptCreatePoll();
            }
        });
    }

    private void tryAddPollOption() {
        if(mPollAdapter.getOptions().size() >= 10) {
            Toast.makeText(CreatePollActivity.this, "Too many poll options, please remove one", Toast.LENGTH_LONG).show();
            //todo: implement removing
            return;
        }
        mPollAdapter.addOption(mNewPollNameField.getText().toString());
        mNewPollNameField.setText("");
    }

    public void notifyChanges() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        pollOptionRecyclerView.scrollToPosition(0);
    }

    private void setUpRecyclerView() {
        pollOptionRecyclerView = (RecyclerView) findViewById(R.id.poll_options);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mPollAdapter = new PollOptionAdapter(CreatePollActivity.this);
        pollOptionRecyclerView.setLayoutManager(layoutManager);
        pollOptionRecyclerView.setAdapter(mPollAdapter);
        pollOptionRecyclerView.setHasFixedSize(true);
    }

    private void attemptCreatePoll() {
        Log.d("CreatePollActivity", "attemptCreatePoll");
        String pollName = mPollNameField.getText().toString();
        String pollDuration = mPollDurationField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(pollName) ) {
            mPollNameField.setError(getString(R.string.error_field_required));
            focusView = mPollNameField;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(pollDuration)) {
            mPollDurationField.setError(getString(R.string.error_field_required));
            focusView = mPollDurationField;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            new CreatePollTask(pollName, Integer.valueOf(pollDuration), mPollAdapter.getOptions()).execute();
        }
    }

    private class CreatePollTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPollName;
        private final int mPollDuration;
        private final ArrayList<PollOption> mPollOptions;

        CreatePollTask(String pollName, int pollDuration, ArrayList<PollOption> pollOptions) {
            Log.d("CreatePollTask", "constructor");
            this.mPollName = pollName;
            this.mPollDuration = pollDuration;
            this.mPollOptions = pollOptions;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            LoginResult loginResult;
            Log.d("CreatePollTask", "doInBackground");

            DeepstreamClient client = DeepstreamService.getInstance().getDeepstreamClient();

            client.setRuntimeErrorHandler(new DeepstreamRuntimeErrorHandler() {
                @Override
                public void onException(Topic topic, Event event, String s) {

                }
            });

            try {
                loginResult = client.login(new JsonObject());
            } catch (DeepstreamLoginException e) {
                Toast.makeText(CreatePollActivity.this, "Error creating connection, try again later", Toast.LENGTH_LONG).show();
                return false;
            }

            if( !loginResult.loggedIn() ) {
                Toast.makeText(CreatePollActivity.this, "Error logging in, try again later", Toast.LENGTH_LONG).show();
                return false;
            }

            Record pollRecord = client.record.getRecord("poll/" + mPollName);
            PollObject po = new PollObject(mPollName, mPollName + "-options/");
            pollRecord.set(po);
            //create record for each poll option
            for (PollOption p : this.mPollOptions) {
                Record r = client.record.getRecord(po.optionListName + p.name);
                r.set(p);
            }

            //create list of option names
            ArrayList<String> pollNames = new ArrayList<>();
            for (PollOption p : mPollOptions) {
                pollNames.add(p.name);
            }
            List pollOptionList = client.record.getList(po.optionListName);
            pollOptionList.setEntries(pollNames);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean loggedIn) {
            if( !loggedIn ) {
                return;
            }
            Log.d("CreatePollTask", "starting new activity");
            Intent i = new Intent(CreatePollActivity.this, RunningPollActivity.class);
            i.putExtra("pollName", "poll/" + mPollName);
            startActivity(i);
        }
    }
}
