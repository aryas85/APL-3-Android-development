package com.app.radiobutton;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioJava, radioHtml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Linking XML IDs
        radioGroup = findViewById(R.id.radioGroup);
        radioJava = findViewById(R.id.radioJava);
        radioHtml = findViewById(R.id.radioHtml);

        // Listener for selection
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.radioJava) {
                Toast.makeText(MainActivity.this,
                        "You selected JAVA",
                        Toast.LENGTH_SHORT).show();
            }

            else if (checkedId == R.id.radioHtml) {
                Toast.makeText(MainActivity.this,
                        "You selected HTML",
                        Toast.LENGTH_SHORT).show();
            }

        });
    }
}
