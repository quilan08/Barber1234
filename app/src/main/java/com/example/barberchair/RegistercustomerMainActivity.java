package com.example.barberchair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegistercustomerMainActivity extends AppCompatActivity {
    public EditText nemail,npassword;
    public Button nsignup;
   public   FirebaseAuth mAuth;
    public TextView nlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registercustomer_main);


        nemail = findViewById(R.id.emailtextcust);
        npassword = findViewById(R.id.passwordcust);
        nsignup = findViewById(R.id.signupbtncust);
        nlogin = findViewById(R.id.logintv);
        mAuth = FirebaseAuth.getInstance();


        nsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String  emailwords = nemail.getText().toString();
               String passwords = npassword.getText().toString();

                if(TextUtils.isEmpty(emailwords)){
                    nemail.setError("Email is Required");
                    nemail.requestFocus();
                }

                if (TextUtils.isEmpty(passwords)){
                    npassword.setError("Password is Required");
                    npassword.requestFocus();
                }
                if (passwords.length() > 6){
                    npassword.setError("Password too short");
                    npassword.requestFocus();
                }

                mAuth.createUserWithEmailAndPassword(emailwords,passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();


                            String emailwords = user.getEmail();
                            String userid = user.getUid();

                            if(task.getResult().getAdditionalUserInfo().isNewUser()){

                            HashMap<Object,String> hashMap = new HashMap<>();
                            // info  in hashmap
                            hashMap.put("email",emailwords);
                            hashMap.put("userid",userid);
                            hashMap.put("name","");
                            hashMap.put("phone","");
                            hashMap.put("image","");
                            hashMap.put("cover","");

                            //we need to put this in the firebase dATAbase and store it

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = database.getReference("Users");

                            databaseReference.child(userid).setValue(hashMap);


                            }
                        Toast.makeText(getApplication(),"Successful Registration",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),CustomerDashboardActivity.class));
                        }

                        else {
                            Toast.makeText(RegistercustomerMainActivity.this,"ERROR !!"+ Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                        }

                        }



                    });



            }
        });
        nlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistercustomerMainActivity.this,LoginCustomerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
