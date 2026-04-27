package com.app.digitalclock;
import android.os.Bundle;
import android.widget.AnalogClock;
import android.widget.DigitalClock;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    AnalogClock analogClock;
    DigitalClock digitalClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        analogClock = findViewById(R.id.analogClock1);
        digitalClock = findViewById(R.id.digitalClock1);
    }
}