package com.example.ansopedia.Model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.ansopedia.SignInActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_MIDDLENAME = "middlename";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_ID = "keyid";
    private static final String KEY_ISVERIFIED = "keyisverified";
    private static final String KEY_DISPLAYNAME = "keydisplayname";
    private static final String KEY_DOB = "keydob";
    private static SharedPrefManager mInstance;
    private static Context mCtx ;
    private SharedPrefManager(Context context) {
       mCtx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(UserModel user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUserName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_FIRSTNAME, user.getFirstName());
        editor.putString(KEY_MIDDLENAME, user.getMiddleName());
        editor.putString(KEY_LASTNAME, user.getLastName());
        editor.putString(KEY_DISPLAYNAME, user.getDisplayName());
        editor.putString(KEY_ISVERIFIED, user.getIsVerified());
        editor.putString(KEY_DOB, user.getDob());
        editor.apply();
    }

                    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

                    //this method will give the logged in user
    public UserModel getUser() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return new UserModel(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_DISPLAYNAME, null),
                sharedPreferences.getString(KEY_ISVERIFIED, null),
                sharedPreferences.getString(KEY_FIRSTNAME, null),
                sharedPreferences.getString(KEY_LASTNAME, null),
                sharedPreferences.getString(KEY_MIDDLENAME, null),
                sharedPreferences.getString(KEY_DOB, null)
            );
    }

        //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLs.ROOT_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                                .build();
        logout logout = retrofit.create(SharedPrefManager.logout.class);
        Call<Boolean> call = logout.getlogout(sharedPreferences.getString(KEY_USERNAME, null));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Toast.makeText(mCtx.getApplicationContext(), "Logout Success", Toast.LENGTH_SHORT).show();
                editor.clear();
                editor.apply();
                mCtx.startActivity(new Intent(mCtx, SignInActivity.class));
                ((Activity) mCtx).finish();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                editor.clear();
                editor.apply();
            }
        });

    }
    interface logout{
        @FormUrlEncoded
        @POST("logout.php")
        Call<Boolean>  getlogout(@Field("username") String username);
    }

}
