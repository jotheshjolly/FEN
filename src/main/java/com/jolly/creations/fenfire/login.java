package com.jolly.creations.fenfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class login extends AppCompatActivity {
    String uname,pass;
    EditText username,password;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        users = FirebaseDatabase.getInstance().getReference("users");

    }

    public void getdetials()
    {
        uname= username.getText().toString();
        pass= password.getText().toString();

    }

    public void login(View view) {
        getdetials();

        if(uname.equals("") || uname.length()<3 )
        {
            username.setError("not be empty or less than 3");
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

                            if(dataSnapshot.hasChild(uname))
                            {
                                if(dataSnapshot.child(uname).child("password").getValue().toString().equals(pass))
                                {
                                    Toast.makeText(getApplicationContext(),"login successful",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(login.this,MainActivity.class));
                                }

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"username or password is wrong",Toast.LENGTH_SHORT).show();
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
