package com.example.aplikasikeuangan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplikasikeuangan.helpers.DatabaseHelper;
import com.example.aplikasikeuangan.helpers.User;
import com.google.android.material.snackbar.Snackbar;

public class PengaturanActivity extends AppCompatActivity {

    EditText passwordLama;
    EditText passwordBaru;
    ConstraintLayout constraintLayoutPengaturan;
    Button simpanPengaturanBtn, kembaliPengaturanBtn;

    User user;

    DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);
//        Objects.requireNonNull(getSupportActionBar()).setSubtitle("Tambah Pengaturan");
        if(getIntent().hasExtra("User")){
            user = getIntent().getParcelableExtra("User");
        }
        initViews();
        initObjects();
    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        simpanPengaturanBtn.setOnClickListener(v->{
            if(validate(passwordLama.getText().toString(), passwordBaru.getText().toString())){
                boolean checkOldPass = databaseHelper.checkOldPassword(passwordLama.getText().toString());
                if(checkOldPass){
                    user = databaseHelper.updatePassword(user, passwordBaru.getText().toString());
                    Toast.makeText(getApplicationContext(),"Password berhasil diupdate!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), PengaturanActivity.class);
                    i.putExtra("User", user);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(),"Inputan password saat ini salah!", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Semua inputan tidak sesuai!", Toast.LENGTH_LONG).show();
            }
        });
        kembaliPengaturanBtn.setOnClickListener(v->{
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            i.putExtra("User", user);
            startActivity(i);
            finish();
        });
    }

    private void initViews() {
        passwordLama = findViewById(R.id.password_saat_ini_edt);
        passwordBaru = findViewById(R.id.password_baru_edt);
        simpanPengaturanBtn = findViewById(R.id.simpan_pengaturan_button);
        kembaliPengaturanBtn = findViewById(R.id.kembali_pengaturan_button);
        constraintLayoutPengaturan = findViewById(R.id.pengaturan_constraint);
        constraintLayoutPengaturan = findViewById(R.id.pengaturan_constraint);
    }

    private boolean validate(String oldPass, String newPass) {
        boolean valid;
        if (oldPass.isEmpty()) {
            valid = false;
            Snackbar.make(constraintLayoutPengaturan, "Password saat ini tidak boleh kosong!", Snackbar.LENGTH_LONG).show();
        } else {
            valid = true;
        }
        if (newPass.isEmpty()) {
            valid = false;
            Snackbar.make(constraintLayoutPengaturan, "Password baru tidak boleh kosong!", Snackbar.LENGTH_LONG).show();
        } else {
            valid = true;
        }
        return valid;
    }
}