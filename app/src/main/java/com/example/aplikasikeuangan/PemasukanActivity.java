package com.example.aplikasikeuangan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.aplikasikeuangan.helpers.DatabaseHelper;
import com.example.aplikasikeuangan.helpers.DetailCashFlow;
import com.example.aplikasikeuangan.helpers.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PemasukanActivity extends AppCompatActivity {

    final Calendar myCalendarPemasukan = Calendar.getInstance();
    EditText tglPemasukanEdt, nominalPemasukanEdt, keteranganPemasukanEdt;
    ImageButton calendarPemasukan;
    Button simpanpemasukan, kembaliPemasukan;
    DatePickerDialog.OnDateSetListener datePemasukan = null;
    DatabaseHelper databaseHelper = null;
    DetailCashFlow dcf;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemasukan);
        if(getIntent().hasExtra("User")){
            user = getIntent().getParcelableExtra("User");
        }
//        Objects.requireNonNull(getSupportActionBar()).setSubtitle("Tambah Pemasukan");
        initViews();
        initObjects();

    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        datePemasukan = (view, year, month, dayOfMonth) -> {
            myCalendarPemasukan.set(Calendar.YEAR, year);
            myCalendarPemasukan.set(Calendar.MONTH, month);
            myCalendarPemasukan.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        calendarPemasukan.setOnClickListener(v -> {
            new DatePickerDialog(PemasukanActivity.this, datePemasukan, myCalendarPemasukan.get(Calendar.YEAR),myCalendarPemasukan.get(Calendar.MONTH),myCalendarPemasukan.get(Calendar.DAY_OF_MONTH)).show();
        });
        kembaliPemasukan.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        });
        simpanpemasukan.setOnClickListener(v ->{
            dcf = new DetailCashFlow();
            dcf.setNominal(Integer.parseInt(nominalPemasukanEdt.getText().toString()));
            dcf.setKeterangan(keteranganPemasukanEdt.getText().toString());
            dcf.setTanggal(String.valueOf(myCalendarPemasukan.get(Calendar.DAY_OF_MONTH)));
            dcf.setBulan(String.valueOf(myCalendarPemasukan.get(Calendar.MONTH)+1));
            dcf.setTahun(String.valueOf(myCalendarPemasukan.get(Calendar.YEAR)));
            dcf.setTipe("pemasukan");
            databaseHelper.addCashFlow(dcf);
            Toast.makeText(getApplicationContext(),"Pemasukan berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),PemasukanActivity.class));
            finish();
        });
    }

    private void updateLabel() {
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        tglPemasukanEdt.setText(dateFormat.format(myCalendarPemasukan.getTime()));
    }

    private void initViews(){
        tglPemasukanEdt = findViewById(R.id.pemasukan_datepicker);
        calendarPemasukan = findViewById(R.id.icon_pemasukan_kalender);
        nominalPemasukanEdt = findViewById(R.id.pemasukan_nominal);
        keteranganPemasukanEdt = findViewById(R.id.pemasukan_keterangan);
        kembaliPemasukan = findViewById(R.id.kembali_pemasukan_button);
        simpanpemasukan = findViewById(R.id.simpan_pemasukan_button);
    }
}