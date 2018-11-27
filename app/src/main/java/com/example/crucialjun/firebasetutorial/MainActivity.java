package com.example.crucialjun.firebasetutorial;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText childValueEditText;
    Button addButton;
    Button removeButton;
    private TextView childValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = firebaseDatabase.getReference("crucial");

        childValueTextView = findViewById(R.id.childValueTextView);
        childValueEditText = findViewById(R.id.childValueEditText);
        addButton = findViewById(R.id.addButton);
        removeButton = findViewById(R.id.removeButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String childValue = childValueEditText.getText().toString();
                mRef.setValue(childValue);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.removeValue();
            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String childVaue = String.valueOf(dataSnapshot.getValue());
                childValueTextView.setText(childVaue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
