package com.app.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.auth.databinding.FragmentPhoneAuthBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class PhoneAuthFragment extends Fragment {

    private FragmentPhoneAuthBinding binding;
    private AuthViewModel viewModel;
    private String mVerificationId;
    private String mPhoneNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPhoneAuthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        binding.btnSendOtp.setOnClickListener(v -> sendOtp());
        binding.btnVerifyOtp.setOnClickListener(v -> verifyOtp());
    }

    private void sendOtp() {
        mPhoneNumber = binding.etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mPhoneNumber) || mPhoneNumber.length() < 10) {
            binding.tilPhone.setError("Enter a valid phone number");
            return;
        }

        String fullNumber = "+91" + mPhoneNumber;
        viewModel.verifyPhoneNumber(requireActivity(), fullNumber, callbacks);
    }

    private void verifyOtp() {
        String code = binding.etOtp.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            binding.tilOtp.setError("Enter OTP");
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        viewModel.signInWithPhoneCredential(credential, mPhoneNumber);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            viewModel.signInWithPhoneCredential(credential, mPhoneNumber);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getContext(), "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            mVerificationId = verificationId;
            binding.tilPhone.setVisibility(View.GONE);
            binding.btnSendOtp.setVisibility(View.GONE);
            binding.tilOtp.setVisibility(View.VISIBLE);
            binding.btnVerifyOtp.setVisibility(View.VISIBLE);
            binding.tvResendOtp.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
