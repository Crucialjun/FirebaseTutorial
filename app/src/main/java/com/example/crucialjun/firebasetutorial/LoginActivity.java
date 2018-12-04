package com.example.crucialjun.firebasetutorial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLogin,passwordLogin,emailRegistration,passwordRegistration,mNameRegistration,
    mAgeRegistration,mSexRegistration,mUserNameRegistration;
    private Button buttonLogin,buttonRegistration;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener firebaseAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        emailRegistration = findViewById(R.id.emailRegistration);
        passwordRegistration = findViewById(R.id.passwordRegistration);
        mNameRegistration = findViewById(R.id.nameRegistration);
        mAgeRegistration = findViewById(R.id.ageRegistration);
        mSexRegistration = findViewById(R.id.sexRegistration);
        mUserNameRegistration = findViewById(R.id.userNameRegistration);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegistration = findViewById(R.id.buttonRegistration);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailRegistration.getText().toString();
                final String password = passwordRegistration.getText().toString();
                final String username = mUserNameRegistration.getText().toString();
                Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users")
                        .orderByChild("username").equalTo(username);
                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() > 0){
                            Toast.makeText(LoginActivity.this,
                                    "Choose a different username",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Sign Up Error", Toast.LENGTH_LONG).show();
                                    }else{
                                        String user_id = mAuth.getCurrentUser().getUid();
                                        final DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                                        final String name = mNameRegistration.getText().toString();
                                        final String age = mAgeRegistration.getText().toString();
                                        final String sex = mSexRegistration.getText().toString();

                                        Map newPost = new HashMap();
                                        newPost.put("name",name);
                                        newPost.put("username",username);
                                        newPost.put("age",age);
                                        newPost.put("sex",sex);

                                        current_user.setValue(newPost);
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailLogin.getText().toString();
                String password = passwordLogin.getText().toString();

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Sign In Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
