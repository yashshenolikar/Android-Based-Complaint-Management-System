package com.example.complaint;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.Objects;

public class AddComplaintActivity extends AppCompatActivity {

    private EditText descriptionEdt,locationEdt;
    private Button submitBtn;
    private Spinner spinner;
    private String[] departments;
    private String departmentValue;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private String userId;
    private Integer count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaint);

        descriptionEdt =findViewById(R.id.idDescription);
        locationEdt = findViewById(R.id.idLocationEdt);
        submitBtn = findViewById(R.id.idSubmitBtn);
        mAuth = FirebaseAuth.getInstance();

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser!=null)
            userId = mAuth.getCurrentUser().getUid();

        spinner = findViewById(R.id.idDepartment);

        departments= new String[]{"Ministry of Railway","Ministry of Education","Ministry of Finance"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddComplaintActivity.this, android.R.layout.simple_spinner_item,departments);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departmentValue = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComplaint();
            }
        });
    }

    private void addComplaint(){
        if(!validateComplain() | ! validateLocation()){
            return;
        }
        else{
            count=count+1;
            String countComplain = count.toString();
            String description = descriptionEdt.getText().toString();
            String location = locationEdt.getText().toString();
            String department = departmentValue;

            Complain complain = new Complain(userId,department,location,description);
            firestore.collection("User").document(userId).collection("Complaint").document(countComplain).set(complain).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(AddComplaintActivity.this, "Complain Added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddComplaintActivity.this,"Error"+e.toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Boolean validateComplain(){
        String val = descriptionEdt.getText().toString();

        if(val.isEmpty()){
            descriptionEdt.setError("Field Cannot be Empty..");
            return false;
        }
        else{
            descriptionEdt.setError(null);
            return true;
        }
    }

    private Boolean validateLocation(){
        String val = locationEdt.getText().toString();

        if(val.isEmpty()){
            locationEdt.setError("Field Cannot be Empty..");
            return false;
        }
        else{
            locationEdt.setError(null);
            return true;
        }
    }
}