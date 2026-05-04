package com.example.smartqueueapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Spinner officeSpinner, serviceSpinner;
    Button tokenBtn;
    TextView queueCountText, avgWaitText, greetingText;

    private final int[] queueCounts = {0, 23, 12, 8, 31, 5};
    private final int[] avgWaits    = {0, 35, 18, 12, 45, 8};

    private final String[][] officeServices = {
            {"Select Service"},
            {"Select Service","Driving Licence (New)","Driving Licence (Renewal)",
                    "Vehicle Registration","RC Transfer","Learner's Licence",
                    "Fitness Certificate","NOC Request","Duplicate RC"},
            {"Select Service","Birth Certificate","Death Certificate",
                    "Property Tax Payment","Trade Licence","Building Plan Approval",
                    "Water Connection","Marriage Certificate","Khata Transfer"},
            {"Select Service","New Aadhaar Enrolment","Aadhaar Update (Name/DOB)",
                    "Address Update","Mobile Number Link","Biometric Update",
                    "Aadhaar PVC Card","Email Link"},
            {"Select Service","Fresh Passport","Passport Renewal",
                    "Tatkal Passport","Minor Passport","Police Clearance Certificate",
                    "Lost Passport Reissue","Address Change"},
            {"Select Service","Account Opening","Loan Application",
                    "Fixed Deposit","KYC Update","Cheque Book Request",
                    "DD / Pay Order","Grievance / Complaint"}
    };

    private boolean spinnerReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        officeSpinner  = findViewById(R.id.officeSpinner);
        serviceSpinner = findViewById(R.id.serviceSpinner);
        tokenBtn       = findViewById(R.id.tokenBtn);
        queueCountText = findViewById(R.id.queueCountText);
        avgWaitText    = findViewById(R.id.avgWaitText);
        greetingText   = findViewById(R.id.greetingText);

        String phone = getIntent().getStringExtra("phone");
        if (phone != null) greetingText.setText("Welcome, " + phone);

        String[] offices = {
                "Select Office","RTO Office","Municipal Corporation",
                "Aadhaar Center","Passport Office","Bank"
        };

        ArrayAdapter<String> officeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, offices);
        officeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        officeSpinner.setAdapter(officeAdapter);

        updateServiceSpinner(0);
        spinnerReady = true;

        officeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view,
                                       int pos, long id) {
                if (!spinnerReady) return;
                updateServiceSpinner(pos);
                if (pos > 0) {
                    queueCountText.setText(String.valueOf(queueCounts[pos]));
                    avgWaitText.setText(avgWaits[pos] + " min");
                } else {
                    queueCountText.setText("-");
                    avgWaitText.setText("-");
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        tokenBtn.setOnClickListener(v -> {
            String selectedOffice  = officeSpinner.getSelectedItem().toString();
            String selectedService = serviceSpinner.getSelectedItem().toString();

            if (selectedOffice.equals("Select Office")) {
                Toast.makeText(this, "Please select an office", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedService.equals("Select Service")) {
                Toast.makeText(this, "Please select a service", Toast.LENGTH_SHORT).show();
                return;
            }

            int officePos   = officeSpinner.getSelectedItemPosition();
            int peopleAhead = queueCounts[officePos];
            int waitTime    = avgWaits[officePos];

            Intent intent = new Intent(HomeActivity.this, TokenActivity.class);
            intent.putExtra("office",      selectedOffice);
            intent.putExtra("service",     selectedService);
            intent.putExtra("peopleAhead", peopleAhead);
            intent.putExtra("waitTime",    waitTime);
            startActivity(intent);
        });
    }

    private void updateServiceSpinner(int index) {
        String[] services = (index >= 0 && index < officeServices.length)
                ? officeServices[index] : officeServices[0];
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, services);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(adapter);
        serviceSpinner.setSelection(0);
    }
}