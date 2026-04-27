package com.app.seekbar;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar seekBar;
    TextView textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textMessage = findViewById(R.id.t);
        seekBar = findViewById(R.id.seekbar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Set minimum text size to avoid 0
                textMessage.setTextSize(12 + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional
            }
        });
    }
}