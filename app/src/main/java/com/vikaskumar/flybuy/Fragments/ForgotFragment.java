package com.vikaskumar.flybuy.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.vikaskumar.flybuy.R;
import com.vikaskumar.flybuy.RegisterActivity;

public class ForgotFragment extends Fragment {
    private FrameLayout parentLayout;
    private Button forgotPassword;
    FirebaseAuth auth;
    ProgressDialog dialog;
    ImageView back;
    private TextInputEditText email;



    public ForgotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot, container, false);
        forgotPassword = view.findViewById(R.id.set_new_password_btn);
        email = view.findViewById(R.id.emailForgot);
        parentLayout = getActivity().findViewById(R.id.register_framelayput);
        back = view.findViewById(R.id.backSignIn);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading.....");


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());

            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword();
            }
        });
    }
    private Boolean validateEmail() {
        String val = email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }
    private void newPassword() {
        if (!validateEmail()) {
            return;
        }
        dialog.show();
        auth.sendPasswordResetEmail(email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(getActivity(), RegisterActivity.class));
                            Toast.makeText(getActivity(), "Please Check Your Email Address", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                        else {
                            Toast.makeText(getActivity(), "Enter your correct email", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentLayout.getId(),fragment);
        fragmentTransaction.commit();

    }
}