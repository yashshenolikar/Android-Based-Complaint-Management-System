package com.example.complaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class ConsumerActivity extends AppCompatActivity {
    private CardView addComplaint,editProfile,queriesRegistered,queriesSolved;
    private TextView userNameDisplayTV,queriesRegisteredTV;
    private Button signOutBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);

        signOutBtn = findViewById(R.id.idSignOut);
        queriesRegisteredTV = findViewById(R.id.idQueriesRegistered);
        addComplaint = findViewById(R.id.idCdAddComplaint);
        editProfile = findViewById(R.id.idCdEditProfile);
        queriesRegistered = findViewById(R.id.idCdQueriesRegister);
        queriesSolved = findViewById(R.id.idCdQueriesResolved);
        userNameDisplayTV = findViewById(R.id.idUserNameDisplay);
        firestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            userId = mAuth.getCurrentUser().getUid();
        }
        DocumentReference documentReference = firestore.collection("User").document(userId);
        documentReference.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userNameDisplayTV.setText(value.getString("Name"));
            }
        });

//        Query query = firestore.collection("Complaint");
//        AggregateQuery countQuery = query.count();
//        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    AggregateQuerySnapshot snapshot = task.getResult();
//                    int count = Math.toIntExact(snapshot.getCount());
//                    queriesRegisteredTV.setText(count);
//                }
//            }
//        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(ConsumerActivity.this,LoginActivity.class));
                finish();
            }
        });
        addComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConsumerActivity.this,AddComplaintActivity.class));
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConsumerActivity.this,EditProfileActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}