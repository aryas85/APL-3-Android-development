package com.example.smartqueueapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class TokenActivity extends AppCompatActivity {

    TextView tokenText, officeName, serviceType,
            peopleAheadText, waitTimeText, nowServingText;
    ProgressBar queueProgress;
    Button notifyBtn, homeBtn;
    DatabaseHelper dbHelper;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private int currentServing;
    private int myToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        tokenText       = findViewById(R.id.tokenText);
        officeName      = findViewById(R.id.officeName);
        serviceType     = findViewById(R.id.serviceType);
        peopleAheadText = findViewById(R.id.peopleAheadText);
        waitTimeText    = findViewById(R.id.waitTimeText);
        nowServingText  = findViewById(R.id.nowServingText);
        queueProgress   = findViewById(R.id.queueProgress);
        notifyBtn       = findViewById(R.id.notifyBtn);
        homeBtn         = findViewById(R.id.homeBtn);

        dbHelper = new DatabaseHelper(this);

        String office   = getIntent().getStringExtra("office");
        String service  = getIntent().getStringExtra("service");
        int peopleAhead = getIntent().getIntExtra("peopleAhead", 10);
        int waitTime    = getIntent().getIntExtra("waitTime", 20);

        currentServing = 20;
        myToken        = currentServing + peopleAhead;

        officeName.setText(office);
        serviceType.setText(service);
        tokenText.setText("A-" + myToken);
        peopleAheadText.setText(String.valueOf(peopleAhead));
        waitTimeText.setText(waitTime + " min");
        nowServingText.setText("A-" + currentServing);
        queueProgress.setMax(myToken);
        queueProgress.setProgress(currentServing);

        dbHelper.insertToken(myToken, service, waitTime);

        notifyBtn.setOnClickListener(v ->
                Toast.makeText(this, "You'll be notified when your turn is near!",
                        Toast.LENGTH_SHORT).show());

        homeBtn.setOnClickListener(v -> finish());

        startQueueSimulation(peopleAhead, waitTime);
    }

    private void startQueueSimulation(int initialAhead, int initialWait) {
        final int[] ahead = {initialAhead};
        final int[] wait  = {initialWait};

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ahead[0] > 0) {
                    ahead[0]--;
                    wait[0] = Math.max(wait[0] - 5, 0);
                    currentServing++;

                    peopleAheadText.setText(String.valueOf(ahead[0]));
                    waitTimeText.setText(wait[0] + " min");
                    nowServingText.setText("A-" + currentServing);
                    queueProgress.setProgress(currentServing);

                    handler.postDelayed(this, 5000);
                } else {
                    Toast.makeText(TokenActivity.this,
                            "Your turn is next!", Toast.LENGTH_LONG).show();
                }
            }
        }, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}