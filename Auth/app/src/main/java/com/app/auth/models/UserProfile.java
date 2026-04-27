package com.app.auth.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class UserProfile {
    private String uid;
    private String email;
    private String displayName;
    private String phoneNumber;
    @ServerTimestamp
    private Timestamp createdAt;
    private int screenTimeGoal;

    public UserProfile() {
        // Required for Firebase
    }

    public UserProfile(String uid, String email, String displayName, String phoneNumber) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.screenTimeGoal = 0; // Default value
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public int getScreenTimeGoal() { return screenTimeGoal; }
    public void setScreenTimeGoal(int screenTimeGoal) { this.screenTimeGoal = screenTimeGoal; }
}
