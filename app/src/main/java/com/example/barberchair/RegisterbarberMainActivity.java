package com.example.barberchair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.backup.BackupAgent;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterbarberMainActivity extends AppCompatActivity {
    public EditText nfullname,nemail,npassword,nphonenumber;
    public Button nsignup;
    FirebaseAuth nAuth;
    public TextView nlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerbarber_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Register As a Barber");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        nfullname = findViewById(R.id.fullnametext);
        nemail = findViewById(R.id.emailtext);
        npassword = findViewById(R.id.password1);

        nphonenumber = findViewById(R.id.phonenum);
        nsignup = findViewById(R.id.signupbtn);
        nlogin = findViewById(R.id.logintv);
        nAuth = FirebaseAuth.getInstance();



        //if(nAuth.getCurrentUser() != null){
           // startActivity(new Intent(getApplicationContext(),HomepageBarberMainActivity.class));
          //  finish();
       // }

        //click on the signup Button

        nsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = nemail.getText().toString();
                String password1 = npassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    nemail.setError("Email is Required");
                    nemail.requestFocus();

                }


                else if (TextUtils.isEmpty(password1)){
                    npassword.setError("Password is required");
                    npassword.requestFocus();
                }
                else if( password1.length() < 8 ){
                    npassword.setError("The Password must exceed 8 characters ");
                    npassword.requestFocus();
                }

                //Register the user

                nAuth.createUserWithEmailAndPassword(email,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = nAuth.getCurrentUser();
                            // get userid and email from authentication
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


                            Toast.makeText(RegisterbarberMainActivity.this,"Successful Registration",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));

                            //get the userid and the email and store it.
                        }
                        else {
                            Toast.makeText(RegisterbarberMainActivity.this,"ERROR !!"+ Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        nlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(RegisterbarberMainActivity.this,LoginBarberActivity.class);
               startActivity(intent);
               finish();
            }
        });
    }
}
