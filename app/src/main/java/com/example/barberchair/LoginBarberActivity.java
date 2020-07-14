package com.example.barberchair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class LoginBarberActivity extends AppCompatActivity {
public EditText nemail,npassword;
public Button nloginbtn;
public TextView nsignuptv;
private   FirebaseAuth nAuth;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_barber);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login Into a Barber Account");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        nemail = findViewById(R.id.Emailtext);
        npassword = findViewById(R.id.password1);
        nloginbtn = findViewById(R.id.loginbtn);
        nsignuptv=findViewById(R.id.signuptext);

        nAuth = FirebaseAuth.getInstance();

        nloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //when the user clicks on the login button
                String email = nemail.getText().toString();
                String password = npassword.getText().toString();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    nemail.setError("Invalid input");
                    nemail.setFocusable(true);

                }
                else{
                    loginuser(email,password);
                }
            }
        });
        nsignuptv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginBarberActivity.this,RegisterbarberMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in >>>>");


    }

    public void  loginuser(String nemail,String npassword) {
        //show progressdialog
        progressDialog.show();
        nAuth.signInWithEmailAndPassword(nemail, npassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //dimiss progressdialog
                            progressDialog.dismiss();

                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = nAuth.getCurrentUser();

                            String email = user.getEmail();
                            String userid = user.getUid();

                            //when user is registered, user is stored in database realtime.
                            if(task.getResult().getAdditionalUserInfo().isNewUser()){

                                //using hashMap

                                HashMap<Object,String> hashMap = new HashMap<>();
                                // info  in hashmap
                                hashMap.put("email",email);
                                hashMap.put("userid",userid);
                                hashMap.put("name","");
                                hashMap.put("phone","");
                                hashMap.put("image","");
                                hashMap.put("cover","");
                                //get the firbasedatabase instance

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                // we need to put this in the database and store it at the Users
                                DatabaseReference databaseReference = database.getReference("Users");
                                databaseReference.child(userid).setValue(hashMap);

                            }


                            Intent intent = new Intent(LoginBarberActivity.this,DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginBarberActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginBarberActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    public boolean onSupportNavigateUp(){
        onBackPressed(); // keep the previous activity
        return onSupportNavigateUp();
    }


}