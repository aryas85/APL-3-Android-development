package com.app.auth;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.auth.utils.FirebaseAuthHelper;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class AuthViewModel extends AndroidViewModel {

    private final FirebaseAuthHelper authHelper;
    private final MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authHelper = new FirebaseAuthHelper(application);
        userLiveData.setValue(authHelper.getCurrentUser());
    }

    public LiveData<FirebaseUser> getUserLiveData() { return userLiveData; }
    public LiveData<String> getErrorLiveData() { return errorLiveData; }
    public LiveData<Boolean> getLoadingLiveData() { return loadingLiveData; }

    public void register(String email, String password, String displayName) {
        loadingLiveData.setValue(true);
        authHelper.registerWithEmailAndPassword(email, password, displayName, new FirebaseAuthHelper.AuthListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                loadingLiveData.setValue(false);
                userLiveData.setValue(user);
            }

            @Override
            public void onFailure(Exception e) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue(e.getMessage());
            }
        });
    }

    public void login(String email, String password) {
        loadingLiveData.setValue(true);
        authHelper.loginWithEmailAndPassword(email, password, new FirebaseAuthHelper.AuthListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                loadingLiveData.setValue(false);
                userLiveData.setValue(user);
            }

            @Override
            public void onFailure(Exception e) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue(e.getMessage());
            }
        });
    }

    public void signInWithGoogle(String idToken) {
        loadingLiveData.setValue(true);
        authHelper.firebaseAuthWithGoogle(idToken, new FirebaseAuthHelper.AuthListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                loadingLiveData.setValue(false);
                userLiveData.setValue(user);
            }

            @Override
            public void onFailure(Exception e) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue(e.getMessage());
            }
        });
    }

    public Intent getGoogleSignInIntent() {
        return authHelper.getGoogleSignInIntent();
    }

    public void verifyPhoneNumber(Activity activity, String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {
        authHelper.startPhoneNumberVerification(activity, phoneNumber, callbacks);
    }

    public void signInWithPhoneCredential(PhoneAuthCredential credential, String phoneNumber) {
        loadingLiveData.setValue(true);
        authHelper.signInWithPhoneAuthCredential(credential, phoneNumber, new FirebaseAuthHelper.AuthListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                loadingLiveData.setValue(false);
                userLiveData.setValue(user);
            }

            @Override
            public void onFailure(Exception e) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue(e.getMessage());
            }
        });
    }

    public void signOut() {
        authHelper.signOut();
        userLiveData.setValue(null);
    }
}
