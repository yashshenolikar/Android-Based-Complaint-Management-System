package com.example.complaint;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userNameEdt,userMailEdt,passwordEdt,cnfPasswordEdt,userMobileEdt,userAddressEdt;
    private Button registerBtn;
    private ProgressBar loadingPB;
    private FirebaseAuth mAuth;
    private TextView loginTV;

    private FirebaseFirestore firestore;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userNameEdt=findViewById(R.id.idUserName);
        userMobileEdt =findViewById(R.id.idUserMobile);
        userAddressEdt=findViewById(R.id.idUserAddress);
        userMailEdt = findViewById(R.id.idUserReg);
        passwordEdt = findViewById(R.id.idPass);
        cnfPasswordEdt = findViewById(R.id.idCnfPass);
        registerBtn = findViewById(R.id.idRegister);
        loadingPB = findViewById(R.id.idProgressBar);
        loginTV = findViewById(R.id.idLoginTV);
        mAuth = FirebaseAuth.getInstance();
        firestore= FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser()!=null){
            Toast.makeText(this, "user ID"+mAuth.getCurrentUser().getUid().toString(), Toast.LENGTH_SHORT).show();
        }

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                registerUser();
            }
        });

    }
    private Boolean validateUsername(){
        String val = userNameEdt.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if(val.isEmpty()){
            userNameEdt.setError("Field Cannot be Empty..");
            return false;
        }
        else if(val.length()>=15){
            userNameEdt.setError("Username too long");
            return false;
        }
        else if(!val.matches(noWhiteSpace)){
            userNameEdt.setError("White Spaces are not allowed");
            return false;
        }
        else{
            userNameEdt.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = passwordEdt.getText().toString();
        String passwordVal= "^"+"(?=.*[A-Z])"+"(?=.*[@#%^&+=])"+".{4,}"+"$";
        if(val.isEmpty()){
            passwordEdt.setError("Field Cannot be Empty");
            return false;
        }
        else if(!val.matches(passwordVal)){
            passwordEdt.setError("Password too weak");
            return false;
        }
        else{
            passwordEdt.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = userMailEdt.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            userMailEdt.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            userMailEdt.setError("Invalid email address");
            return false;
        } else {
            userMailEdt.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = userMobileEdt.getText().toString();
        if (val.isEmpty()) {
            userMobileEdt.setError("Field cannot be empty");
            return false;
        } else {
            userMobileEdt.setError(null);
            return true;
        }
    }

    public void registerUser() {
        if(!validateUsername() | !validatePassword() | !validatePhoneNo() | !validateEmail() | !validateUsername()){
            return;
        }
        loadingPB.setVisibility(View.GONE);
        String userMail = userMailEdt.getText().toString();
        String userName = userNameEdt.getText().toString();
        String userMobile = userMobileEdt.getText().toString();
        String userAddress = userAddressEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        String cnfPassword = cnfPasswordEdt.getText().toString();
        mAuth.createUserWithEmailAndPassword(userMail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(RegistrationActivity.this, "User Created..", Toast.LENGTH_SHORT).show();
                    userID=mAuth.getCurrentUser().getUid();
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("Mail",userMail);
                    hashMap.put("Name",userName);
                    hashMap.put("Mobile Number",userMobile);
                    hashMap.put("Address",userAddress);
                    hashMap.put("Password",password);
                    firestore.collection("User").document(userID).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                            Log.d(TAG,"onSuccess: user profile created for"+userID);
                        }
                    });
                }
                else{
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(RegistrationActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}