package chintanjariwala.com.moviedatabase;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText etEmail = null, etPassword = null,etname = null;
    Button btnSignup = null;
    TextView tvlogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void signUp() {
        if(!validate()){
            onSignupFailed();
            return;
        }

        btnSignup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String name = etname.getText().toString();

        //Signup logic

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);

    }

    private void onSignupSuccess() {
        btnSignup.setEnabled(true);
        setResult(RESULT_OK,null);
        finish();
    }

    private void onSignupFailed() {
        btnSignup.setEnabled(true);
        Toast.makeText(this,"Signup failed",Toast.LENGTH_SHORT);

    }

    private boolean validate() {
        boolean goodToGo = true;
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String name = etname.getText().toString();

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

        if (name.isEmpty() || name.length() < 2) {
            etname.setError("Name must have atleast 2 characters");
            goodToGo = false;
        }else{
            etname.setError(null);
        }

        return goodToGo;
    }

    private void init() {

        etEmail = (EditText) findViewById(R.id.etEmailSignup);
        etPassword = (EditText) findViewById(R.id.etPasswordSignUp);
        btnSignup = (Button) findViewById(R.id.btnSignUp);
        tvlogin = (TextView) findViewById(R.id.tvLogin);
        etname = (EditText) findViewById(R.id.etName);

    }
}
