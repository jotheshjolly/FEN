package com.jolly.creations.fenfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {
    String uname,pass,em;
    EditText username,password,email;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);

        users = FirebaseDatabase.getInstance().getReference("users");

    }

    public void getdetials()
    {
        uname= username.getText().toString();
        pass= password.getText().toString();
        em= email.getText().toString();
    }

    public void signup(View view) {
        getdetials();

        if(uname.equals("") || uname.length()<3 )
        {
            username.setError("not be empty or less than 3");
        }else
        {
            if(em.equals("") || !emailValidator(em) )
            {
                email.setError("enter valid email");
            }else
            {
                if(pass.equals("") || pass.length()<8 )
                {
                    password.setError("not be empty or less than 8");
                }else
                {
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.

                            if(!dataSnapshot.hasChild(uname))
                            {
                                users.child(uname).child("uname").setValue(uname);
                                users.child(uname).child("email").setValue(em);
                                users.child(uname).child("password").setValue(pass);

                                Toast.makeText(getApplicationContext(),"registered",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(signup.this,login.class));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"try another username",Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value

                        }
                    });




                }
            }
        }
    }

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void login(View view) {
        startActivity(new Intent(signup.this,login.class));
    }
}
