package com.example.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {
    TextView signInTextBtn;
    private EditText textInputEditTextUserName, textInputEditTextPassword;
    private ProgressBar progressBar;
    private Button signInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);



        TextView textViewForgetPassword = findViewById(R.id.textViewForgetPassword);
        TextView textViewSignUp = findViewById(R.id.textViewSignUp);

//        signInTextBtn = findViewById(R.id.signInTextBtn);
        signInBtn = findViewById(R.id.loginbtn);
        textInputEditTextUserName = findViewById(R.id.userEmail);
        textInputEditTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);

        textViewForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

    }
    private void userLogin() {
    //first getting the values
    final String userName = textInputEditTextUserName.getText().toString();
    final String password = textInputEditTextPassword.getText().toString();
        if(userName.isEmpty()){
            textInputEditTextUserName.setError("First name is required");
            textInputEditTextUserName.requestFocus();
            return;
        }
        if(password.isEmpty()){
            textInputEditTextPassword.setError("Password is required");
            textInputEditTextPassword.requestFocus();
            return;
        }

        class UserLogin extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", userName);
                params.put("password", password);

                //Returning the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
//                    Toast.makeText(SignInActivity.this, "Inside try", Toast.LENGTH_SHORT).show();

                    JSONObject obj = new JSONObject(s);
                    if(obj.getInt("success") == 1){
//                        Toast.makeText(SignInActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");
//                        //creating a new user object

                        /*
                     {
    "success": 1,
    "message": "Login Success",
    "user": {
        "id": "53",
        "username": "rku",
        "email": "sanjay8797421521@gmail.com",
        "slug": "",
        "lastLogin": null,
        "status": "1",
        "displayName": null,
        "registeredAt": "0000-00-00 00:00:00",
        "isVerified": "0",
        "firstName": "Sanjay",
        "lastName": "Sah",
        "middleName": "Kumar",
        "dob": "0000-00-00"
    }
}* */

                        UserModel user = new UserModel(
                            userJson.getInt("id"),
                            userJson.getString("username"),
                            userJson.getString("email"),
                            userJson.getString("displayName"),
                            userJson.getString("isVerified"),
                            userJson.getString("firstName"),
                            userJson.getString("lastName"),
                            userJson.getString("middleName"),
                            userJson.getString("dob")
                        );
//
//                        //storing the user in shared preferences
                        Toast.makeText(SignInActivity.this,  user.getFirstName(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(SignInActivity.this,  user.getLastName(), Toast.LENGTH_SHORT).show();
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        //starting the profile activity
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
}


        }

            UserLogin ul = new UserLogin();
            ul.execute();
        }

}