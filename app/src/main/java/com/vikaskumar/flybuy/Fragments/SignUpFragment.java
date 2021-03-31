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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vikaskumar.flybuy.MainActivity;
import com.vikaskumar.flybuy.Models.User;
import com.vikaskumar.flybuy.R;

public class SignUpFragment extends Fragment {
    private Button alReadyAcc;
    private FrameLayout parentFrameLout;
    private FirebaseAuth mAuth;
    private String emailStr, passStr, confirmPassStr, nameStr;
    ProgressDialog dialog;
    FirebaseFirestore database;
    private TextInputEditText fullName, email, password, confPassword;
    private Button signup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        alReadyAcc = view.findViewById(R.id.backLogin);
        fullName = view.findViewById(R.id.signupName);
        email = view.findViewById(R.id.signupEmail);
        password = view.findViewById(R.id.signupPassword);
        confPassword = view.findViewById(R.id.signupConfirmPassword);
        signup = view.findViewById(R.id.signup);

        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Creating new account...");

        parentFrameLout = getActivity().findViewById(R.id.register_framelayput);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alReadyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private boolean validateName() {
        String val = fullName.getEditableText().toString();

        if (val.isEmpty()) {
            fullName.setError("Field cannot be empty");
            return false;
        } else {
            fullName.setError(null);
            return true;
        }
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

    private Boolean validatePassword() {
        String val = password.getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    private Boolean validateConfirmPassword() {
        String val = confPassword.getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            confPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            confPassword.setError("Password is too weak");
            return false;
        } else {
            confPassword.setError(null);
            return true;
        }
    }

    private void signUp() {
        if (!validateName() | !validateEmail() | !validatePassword() | !validateConfirmPassword()) {
            return;
        }
        nameStr = fullName.getEditableText().toString().trim();
        emailStr = email.getText().toString().trim();
        passStr = password.getText().toString().trim();
        confirmPassStr = confPassword.getText().toString().trim();

        if (validate()) {
            dialog.show();
            signupUser(nameStr, emailStr, passStr);
        }
    }

    private void signupUser(String nameStr, String emailStr, String passStr) {
        final User user = new User(nameStr, emailStr, passStr);

        mAuth.createUserWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    String uid = task.getResult().getUser().getUid();
                    database
                            .collection("users")
                            .document(uid)
                            .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                dialog.dismiss();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private boolean validate() {
        if (passStr.compareTo(confirmPassStr) != 0)
        {
            Toast.makeText(getContext(), "Password and Confirm password Should be Same !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.top_animation, R.anim.bottom_animation);
        fragmentTransaction.replace(parentFrameLout.getId(),fragment);
        fragmentTransaction.commit();
    }
}