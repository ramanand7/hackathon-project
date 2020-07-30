package com.example.firenotes.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firenotes.MainActivity;
import com.example.firenotes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {
    EditText lEmail,lpassword;
    Button loginNow;
    TextView forgetpass,creatAcc;
    FirebaseAuth fAuth;
    ProgressBar spinner;
    FirebaseUser user;
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login to account");
        lEmail = findViewById(R.id.email);
        lpassword = findViewById(R.id.lPassword);
        loginNow = findViewById(R.id.loginBtn);
        forgetpass = findViewById(R.id.forgotPasword);
        creatAcc= findViewById(R.id.createAccount);
        fAuth = FirebaseAuth.getInstance();
        spinner = findViewById(R.id.progressBar3);

        user= fAuth.getCurrentUser();

        showWarning();

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = lEmail.getText().toString();
                String mPassword = lpassword.getText().toString();
                if (mEmail.isEmpty()||mPassword.isEmpty()){
                    Toast.makeText(login.this, "field are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                spinner.setVisibility(View.VISIBLE);
                if (fAuth.getCurrentUser().isAnonymous()){
                    FirebaseUser user= fAuth.getCurrentUser();
                    fstore.collection("notes").document(user.getUid())
                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(login.this, "All Temp notes are deleted", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(login.this, "Temp user is deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                fAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(login.this, "YOU HAVE SINGED IN", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login.this, "Login failed!" +e.getMessage(), Toast.LENGTH_LONG).show();
                        spinner.setVisibility(View.GONE);
                    }
                });
                finish();
            }
        });
    }

    private void showWarning() {

        AlertDialog.Builder warning = new AlertDialog.Builder(this).setTitle("are you sure").setMessage(
                "Linking existing account will delete all the temp notes .create new to save them ")
                .setPositiveButton("save notes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), register.class));
                        finish();
                    }
                }).setNegativeButton("it's ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       
                    }
                });
        warning.show();
    }
}
