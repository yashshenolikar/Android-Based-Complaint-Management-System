package com.example.complaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText userNameEdt, passwordEdt;
    private Button loginBtn;
    private ProgressBar loadingPB;
    private TextView registerTV;
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameEdt = findViewById(R.id.idUser);
        passwordEdt = findViewById(R.id.idPass);
        loginBtn = findViewById(R.id.idloginBtn);
        loadingPB = findViewById(R.id.idLoadingPB);
        registerTV = findViewById(R.id.idRegisterTV);
        mAuth = FirebaseAuth.getInstance();

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                loginUser();
            }
        });
    }
    @Override
    protected void onStart() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser!=null){
            startActivity(new Intent(this,ConsumerActivity.class));
        }
        super.onStart();
    }

    private Boolean validateUsername(){
        String val = userNameEdt.getText().toString();

        if(val.isEmpty()){
            userNameEdt.setError("Field Cannot be Empty..");
            return false;
        }
        else{
            userNameEdt.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = passwordEdt.getText().toString();
        if(val.isEmpty()){
            passwordEdt.setError("Field Cannot be Empty");
            return false;
        }
        else{
            passwordEdt.setError(null);
            return true;
        }
    }

    public void loginUser(){
        if(!validatePassword() | ! validateUsername()){
            loadingPB.setVisibility(View.GONE);
            return;
        }
        else{
            isUser();
        }
    }

    private void isUser(){
        loadingPB.setVisibility(View.VISIBLE);
        String userName = userNameEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        mAuth.signInWithEmailAndPassword(userName,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,ConsumerActivity.class));
                    finish();
                }
                else{
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}