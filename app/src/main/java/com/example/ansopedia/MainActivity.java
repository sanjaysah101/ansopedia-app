package com.example.ansopedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ansopedia.Model.SharedPrefManager;
import com.example.ansopedia.Model.UserModel;

public class MainActivity extends AppCompatActivity {
    TextView textViewId, textViewUsername, textViewEmail, textViewFirstName, textViewLastName, textViewMiddleName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getSupportActionBar().hide();
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }

        textViewId = (TextView) findViewById(R.id.textViewId);
        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewFirstName = findViewById(R.id.textViewGender);
        textViewMiddleName = findViewById(R.id.middleName);
        textViewLastName= findViewById(R.id.textViewLastName);

        //getting the current user
        UserModel user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textViews
        textViewId.setText(String.valueOf(user.getId()));
        textViewUsername.setText(user.getUserName());
        textViewEmail.setText(user.getEmail());
        textViewFirstName.setText(user.getFirstName());
        textViewMiddleName.setText(user.getMiddleName());
        textViewLastName.setText(user.getLastName());


        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });

    }
}