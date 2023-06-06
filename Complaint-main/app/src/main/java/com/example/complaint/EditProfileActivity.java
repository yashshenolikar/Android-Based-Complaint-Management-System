package com.example.complaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    private EditText userNameEdt,passwordEdt,cnfPasswordEdt,userAddressEdt;
    private Button changePassBtn;
    private FirebaseAuth auth;
    private String userId;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fdb;
    private FirebaseUser firebaseUser;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        loadingPB = findViewById(R.id.idLoadingPB);
        userNameEdt = findViewById(R.id.idUserName);
        passwordEdt = findViewById(R.id.idChangePass);
        changePassBtn = findViewById(R.id.idChangePassBtn);
        cnfPasswordEdt = findViewById(R.id.idPassCnf);
        userAddressEdt = findViewById(R.id.idUserAddress);
        mAuth = FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();
        fdb= FirebaseFirestore.getInstance();

        firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser!=null)
            userId= firebaseUser.getUid();
        DocumentReference documentReference = fdb.collection("User").document(userId);
        documentReference.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userNameEdt.setText(value.getString("Name"));
                userAddressEdt.setText(value.getString("Address"));
            }
        });

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                passChange();
            }
        });
    }

    private void passChange(){
        if(!validateCnfPass() | !validatePassword()){
            loadingPB.setVisibility(View.GONE);
            return;
        }
        else{
            String userName = userNameEdt.getText().toString();
            String userAddress = userAddressEdt.getText().toString();
            String newPassword = passwordEdt.getText().toString();
            String cnfPassword = cnfPasswordEdt.getText().toString();

            firebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("Password",newPassword);
                        fdb.collection("User").document(userId).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(EditProfileActivity.this, "Password Changed Successfully..", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                    else{
                        loadingPB.setVisibility(View.GONE);
                        Toast.makeText(EditProfileActivity.this, "Failed to change password..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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

    private Boolean validateCnfPass(){
        String val = cnfPasswordEdt.getText().toString();

        if(val.isEmpty()){
            cnfPasswordEdt.setError("Field Cannot be Empty..");
            return false;
        }
        else if(val.equals("passwordEdt.getText().toString()")){
            cnfPasswordEdt.setError("Pass do not Match..");
            return false;
        }
        else{
            cnfPasswordEdt.setError(null);
            return true;
        }
    }

}