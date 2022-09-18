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

public class PengeluaranActivity extends AppCompatActivity {

    EditText tglPengeluaranEdt, nominalPengeluaranEdt, keteranganPengeluaranEdt;
    ImageButton calendarPengeluaran;
    Button kembaliPengeluaran, simpanPengeluaran;
    final Calendar myCalendarPengeluaran = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener datePengeluaran = null;
    DatabaseHelper databaseHelper = null;
    DetailCashFlow dcf;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran);
//        Objects.requireNonNull(getSupportActionBar()).setSubtitle("Tambah Pengeluaran");
        if(getIntent().hasExtra("User")){
            user = getIntent().getParcelableExtra("User");
        }
        initViews();
        initObjects();
    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        datePengeluaran = (view, year, month, dayOfMonth) -> {
            myCalendarPengeluaran.set(Calendar.YEAR, year);
            myCalendarPengeluaran.set(Calendar.MONTH, month);
            myCalendarPengeluaran.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        calendarPengeluaran.setOnClickListener(v -> {
            new DatePickerDialog(PengeluaranActivity.this, datePengeluaran, myCalendarPengeluaran.get(Calendar.YEAR),myCalendarPengeluaran.get(Calendar.MONTH),myCalendarPengeluaran.get(Calendar.DAY_OF_MONTH)).show();
        });
        kembaliPengeluaran.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        });
        simpanPengeluaran.setOnClickListener(v ->{
            dcf = new DetailCashFlow();
            dcf.setNominal(Integer.parseInt(nominalPengeluaranEdt.getText().toString()));
            dcf.setTanggal(String.valueOf(myCalendarPengeluaran.get(Calendar.DAY_OF_MONTH)));
            dcf.setKeterangan(keteranganPengeluaranEdt.getText().toString());
            dcf.setBulan(String.valueOf(myCalendarPengeluaran.get(Calendar.MONTH)+1));
            dcf.setTahun(String.valueOf(myCalendarPengeluaran.get(Calendar.YEAR)));
            dcf.setTipe("pengeluaran");
            databaseHelper.addCashFlow(dcf);
            Toast.makeText(getApplicationContext(),"Pengeluaran berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),PengeluaranActivity.class));
            finish();
        });
    }

    private void updateLabel() {
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        tglPengeluaranEdt.setText(dateFormat.format(myCalendarPengeluaran.getTime()));
    }

    private void initViews(){
        tglPengeluaranEdt = findViewById(R.id.pengeluaran_datepicker);
        calendarPengeluaran = findViewById(R.id.icon_pengeluaran_kalender);
        kembaliPengeluaran = findViewById(R.id.kembali_pengeluaran_button);
        keteranganPengeluaranEdt = findViewById(R.id.pengeluaran_keterangan);
        nominalPengeluaranEdt = findViewById(R.id.pengeluaran_nominal);
        simpanPengeluaran = findViewById(R.id.simpan_pengeluaran_button);
    }
}