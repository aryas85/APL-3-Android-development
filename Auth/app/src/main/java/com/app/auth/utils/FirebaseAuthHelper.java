package com.app.auth.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.app.auth.models.UserProfile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class FirebaseAuthHelper {

    private final FirebaseAuth mAuth;
    private final FirebaseFirestore mDb;
    private final GoogleSignInClient mGoogleSignInClient;

    public interface AuthListener {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }

    public FirebaseAuthHelper(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("YOUR_WEB_CLIENT_ID") // Replace with your web client ID
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut();
    }

    public void registerWithEmailAndPassword(String email, String password, String displayName, AuthListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            createUserProfile(user.getUid(), email, displayName, null, listener);
                        } else {
                            listener.onFailure(new Exception("User is null after registration"));
                        }
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }

    public void loginWithEmailAndPassword(String email, String password, AuthListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(mAuth.getCurrentUser());
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }

    public Intent getGoogleSignInIntent() {
        return mGoogleSignInClient.getSignInIntent();
    }

    public void firebaseAuthWithGoogle(String idToken, AuthListener listener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Check if user is new
                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                createUserProfile(user.getUid(), user.getEmail(), user.getDisplayName(), null, listener);
                            } else {
                                listener.onSuccess(user);
                            }
                        } else {
                            listener.onFailure(new Exception("User is null after Google Sign-In"));
                        }
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }

    public void sendPasswordResetEmail(String email, OnCompleteListener<Void> listener) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(listener);
    }

    public void startPhoneNumberVerification(Activity activity, String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthProvider.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(callbacks)
                        .build());
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential, String phoneNumber, AuthListener listener) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                createUserProfile(user.getUid(), null, null, phoneNumber, listener);
                            } else {
                                listener.onSuccess(user);
                            }
                        } else {
                            listener.onFailure(new Exception("User is null after phone auth"));
                        }
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }

    private void createUserProfile(String uid, String email, String displayName, String phoneNumber, AuthListener listener) {
        UserProfile userProfile = new UserProfile(uid, email, displayName, phoneNumber);
        mDb.collection("users").document(uid)
                .set(userProfile)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(mAuth.getCurrentUser());
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }
}
