package com.example.aplikasikeuangan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aplikasikeuangan.helpers.DatabaseHelper;
import com.example.aplikasikeuangan.helpers.User;
import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameEdt;
    EditText passwordEdt;
    EditText fullnameEdt;
    EditText nimEdt;
    Button registerBtn;
    TextView directToLogin;
    ConstraintLayout constraintLayoutRegister;
    DatabaseHelper databaseHelper = null;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initObjects();
        registerBtn.setOnClickListener(v->{
            if(fullnameEdt.getText() != null && usernameEdt.getText() != null && passwordEdt.getText() != null) {
                if (validate(fullnameEdt.getText().toString(), usernameEdt.getText().toString(), passwordEdt.getText().toString())) {
                    user = new User();
                    user.setFullname(fullnameEdt.getText().toString());
                    user.setUsername(usernameEdt.getText().toString());
                    user.setPassword(passwordEdt.getText().toString());
                    if (databaseHelper.checkUser(user) == null) {
                        databaseHelper.addUser(user);
                        Snackbar.make(constraintLayoutRegister, "Register berhasil. Silahkan Login! ", Snackbar.LENGTH_LONG).show();
                        new Handler().postDelayed(() -> {
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }, Snackbar.LENGTH_SHORT);
                    } else {
                        Snackbar.make(constraintLayoutRegister, "User dengan username:" + user.getUsername() + " sudah ada", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(constraintLayoutRegister, "Input dengan benar!", Snackbar.LENGTH_LONG).show();
                }
            }else{
                Snackbar.make(constraintLayoutRegister, "Input dengan benar!", Snackbar.LENGTH_LONG).show();
            }
        });
        directToLogin.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }

    private void initViews(){
        usernameEdt = findViewById(R.id.username_register_edt);
        passwordEdt = findViewById(R.id.password_register_edt);
        fullnameEdt = findViewById(R.id.fullname_register_edt);
        directToLogin = findViewById(R.id.direct_to_login_tv);
        registerBtn = findViewById(R.id.register_btn);
        constraintLayoutRegister = findViewById(R.id.register_constraint);
    }

    private void initObjects(){
        databaseHelper = new DatabaseHelper(getApplicationContext());
    }

    private boolean validate(String fullname, String username, String password){
        boolean valid;
        if(username.isEmpty()){
            valid = false;
            Snackbar.make(constraintLayoutRegister, "Username tidak boleh kosong!", Snackbar.LENGTH_LONG).show();
        }else {
            if(username.contains(" ")){
                valid = false;
                Snackbar.make(constraintLayoutRegister, "Masukkan tidak boleh memiliki spasi!", Snackbar.LENGTH_LONG).show();
            }else{
                valid = true;
            }
        }
        if(fullname.isEmpty()){
            valid = false;
            Snackbar.make(constraintLayoutRegister, "Nama lengkap tidak boleh kosong!", Snackbar.LENGTH_LONG).show();
        }else{
            valid = true;
        }
        if(password.isEmpty()){
            valid = false;
            Snackbar.make(constraintLayoutRegister, "Password tidak boleh kosong!", Snackbar.LENGTH_LONG).show();
        }else{
            valid = true;
        }
        return valid;
    }

}
