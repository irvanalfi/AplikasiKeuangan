package com.example.aplikasikeuangan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aplikasikeuangan.helpers.DatabaseHelper;
import com.example.aplikasikeuangan.helpers.User;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    ConstraintLayout constraintLayoutLogin;
    DatabaseHelper databaseHelper;
    TextView directToRegister;
    EditText usernameEdt;
    EditText passwordEdt;
    Button loginBtn;
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        initViews();
        loginBtn.setOnClickListener(v-> {
            if (usernameEdt.getText() != null && passwordEdt.getText() != null) {
                String userName = usernameEdt.getText().toString();
                String passWord = passwordEdt.getText().toString();
                if (validate(userName, passWord)) {
                    user = new User();
                    user.setUsername(userName);
                    user.setPassword(passWord);
                    User currentUser = databaseHelper.checkUser(user);
                    if (currentUser != null) {
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        i.putExtra("User", currentUser);
                        startActivity(i);
                    } else {
                        Snackbar.make(constraintLayoutLogin, "Anda belum terdaftar, silahkan melakukan registrasi!", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(constraintLayoutLogin, "Username / Password Anda salah!", Snackbar.LENGTH_LONG).show();
                }
            }else{
                Snackbar.make(constraintLayoutLogin, "Silahkan cek inputan Anda!", Snackbar.LENGTH_LONG).show();
            }
        });
        directToRegister.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        });
    }

    private boolean validate(String username, String password) {

        boolean valid;
        if (username.isEmpty()) {
            valid = false;
            Snackbar.make(constraintLayoutLogin, "Username tidak boleh kosong!", Snackbar.LENGTH_LONG).show();
        } else {
            if(username.contains(" ")){
                valid = false;
                Snackbar.make(constraintLayoutLogin, "Username tidak boleh memiliki spasi!", Snackbar.LENGTH_LONG).show();
            }else{
                valid = true;
            }
        }
        if (password.isEmpty()) {
            valid = false;
            Snackbar.make(constraintLayoutLogin, "Password tidak boleh kosong!", Snackbar.LENGTH_LONG).show();
        } else {
            valid = true;
        }
        return valid;

    }

    private void initViews(){
        usernameEdt = findViewById(R.id.username_login_edt);
        passwordEdt = findViewById(R.id.password_login_edt);
        directToRegister = findViewById(R.id.direct_to_register_tv);
        loginBtn = findViewById(R.id.login_btn);
        constraintLayoutLogin= findViewById(R.id.login_constraint);
    }
}