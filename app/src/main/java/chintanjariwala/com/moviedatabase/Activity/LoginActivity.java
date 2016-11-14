package chintanjariwala.com.moviedatabase.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import chintanjariwala.com.moviedatabase.R;
import chintanjariwala.com.moviedatabase.Users;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int SIGNUP = 0;

    TextInputLayout emailWrapper,passwordWrapper;
    EditText etEmail = null, etPassword = null;
    Button btnlogin = null;
    TextView tvsignUp = null;
    LinearLayout parent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClicked();
            }
        });

        tvsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupClicked();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGNUP){
            if(resultCode == RESULT_OK){
                Snackbar.make(parent,"Login Successful",Snackbar.LENGTH_LONG).show();
                List<Users> all_users = Users.listAll(Users.class);

                for(Users user : all_users){
                    Log.d(TAG, user.name + " " + user.email + " " + user.passwrold);
                }
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    private void signupClicked() {
        Intent i = new Intent(this,SignupActivity.class);
        startActivityForResult(i,SIGNUP);
    }

    private void loginClicked() {
        Log.d(TAG,"Inside Login clicked");
        
        if(!validate()){
            onLoginFailed();
            return;
        }
        Log.d(TAG,"Validation Passed");
        btnlogin.setEnabled(false);

        boolean userIsThere = false;

        final ProgressDialog pg = new ProgressDialog(LoginActivity.this);
        pg.setIndeterminate(true);
        pg.setMessage("Authenticating");
        pg.show();

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String name = null;
        List<Users> users = Users.find(Users.class, "email = ? and passwrold = ?",email,password);
        if(users != null){
            for(Users user : users){
                    userIsThere = true;
                    Log.d(TAG,user.name + " " + user.email + " " + user.passwrold);
                    name = user.name;
            }
        }

        SharedPreferences sharedPreferences  = getSharedPreferences(String.valueOf(R.string.myPrefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",name);
        editor.putString("email",email);
        editor.commit();

        final boolean finalUserIsThere = userIsThere;
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(finalUserIsThere){
                    onLoginSuccess();
                }else{
                    Snackbar.make(parent, "Wrong Username or password", Snackbar.LENGTH_LONG).show();
                }

                pg.dismiss();
            }
        },3000);
    }

    private void onLoginSuccess() {
        btnlogin.setEnabled(true);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void onLoginFailed() {
        Toast.makeText(this,"Login Failed",Toast.LENGTH_SHORT);
        btnlogin.setEnabled(true);
    }

    private boolean validate() {
        boolean goodToGo = true;

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailWrapper.setErrorEnabled(true);
            etEmail.setError("Enter a valid email address");
            goodToGo = false;
        }else{
            emailWrapper.setErrorEnabled(false);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordWrapper.setErrorEnabled(true);
            etPassword.setError("Password must be between 4 to 10 characters");
            goodToGo = false;
        }else{
            passwordWrapper.setErrorEnabled(false);
        }

        return goodToGo;
    }

    private void init() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        tvsignUp = (TextView) findViewById(R.id.tvSignup);
        emailWrapper = (TextInputLayout) findViewById(R.id.etEmailWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.etPasswordWrapper);
        parent = (LinearLayout) findViewById(R.id.containerLayout);
    }
}
