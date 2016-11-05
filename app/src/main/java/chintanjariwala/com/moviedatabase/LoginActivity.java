package chintanjariwala.com.moviedatabase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int SIGNUP = 0;

    EditText etEmail = null, etPassword = null;
    Button btnlogin = null;
    TextView tvsignUp = null;

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
        btnlogin.setEnabled(false);

        final ProgressDialog pg = new ProgressDialog(LoginActivity.this);
        pg.setIndeterminate(true);
        pg.setMessage("Authenticating");
        pg.show();

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoginSuccess();
                pg.dismiss();
            }
        },3000);
    }

    private void onLoginSuccess() {
        btnlogin.setEnabled(true);
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
            etEmail.setError("Enter a valid email address");
            goodToGo = false;
        }else{
            etEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etPassword.setError("Password must be between 4 to 10 characters");
            goodToGo = false;
        }else{
            etPassword.setError(null);
        }

        return goodToGo;
    }

    private void init() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        tvsignUp = (TextView) findViewById(R.id.tvSignup);

    }
}
