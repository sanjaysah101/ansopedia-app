package com.example.ansopedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ansopedia.Model.RequestHandler;
import com.example.ansopedia.Model.SharedPrefManager;
import com.example.ansopedia.Model.URLs;
import com.example.ansopedia.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    TextView signInTextBtn;
    private EditText textInputEditTextUserName, textInputEditTextEmail, textInputEditTextPassword, textInputEditTextConfirmPassword;
    private ProgressBar progressBar;
    private Button signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        signInTextBtn = findViewById(R.id.signInTextBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
        textInputEditTextUserName = findViewById(R.id.userName);
        textInputEditTextEmail = findViewById(R.id.userEmail);
        textInputEditTextPassword = findViewById(R.id.password);
        textInputEditTextConfirmPassword = findViewById(R.id.confirmPassword);
        progressBar = findViewById(R.id.progressBar);
        signInTextBtn = findViewById(R.id.signInTextBtn);

        signInTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }
    public void registerUser(){
        String username, email, password, confirmPassword;
        username = textInputEditTextUserName.getText().toString().trim();
        email = textInputEditTextEmail.getText().toString().trim();
        password = textInputEditTextPassword.getText().toString().trim();
        confirmPassword = textInputEditTextConfirmPassword.getText().toString().trim();

        if(username.isEmpty()){
            textInputEditTextUserName.setError("First name is required");
            textInputEditTextUserName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            textInputEditTextEmail.setError("Email is required");
            textInputEditTextEmail.requestFocus();
            return;
        }if(password.isEmpty()){
            textInputEditTextPassword.setError("Password is required");
            textInputEditTextPassword.requestFocus();
            return;
        }if(confirmPassword.isEmpty()){
            textInputEditTextConfirmPassword.setError("Confirm password is required");
            textInputEditTextConfirmPassword.requestFocus();
            return;
        }if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textInputEditTextConfirmPassword.setError("Invalid email Address");
            textInputEditTextConfirmPassword.requestFocus();
            return;
        }if(password.length() < 6  ){
            textInputEditTextPassword.setError("Password must be greater than 6 characters");
            textInputEditTextConfirmPassword.requestFocus();
            return;
        }if(!confirmPassword.equals(password)){
            textInputEditTextConfirmPassword.setError("Confirm password did not match with password");
            textInputEditTextConfirmPassword.requestFocus();
            return;
        }

//        Toast.makeText(this, userName +" "+ email+" " +password + " "+confirmPassword, Toast.LENGTH_SHORT).show();
//        progressBar.setVisibility(View.VISIBLE);

        //if it passes all the validations
        class RegisterUser extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);

                // Returning the response
                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
    //            Toast.makeText(SignupActivity.this, "before try", Toast.LENGTH_SHORT).show();
                try {
                    //converting response to json object
                    //############################## Not working from here ###################################
                    JSONObject obj = new JSONObject(s);
                    Toast.makeText(SignupActivity.this, "Inside try", Toast.LENGTH_SHORT).show();
                    //if no error in response
                    if (obj.getInt("success") == 1) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        UserModel user = new UserModel(
                                userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("email")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
        }
    }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();


    }
}