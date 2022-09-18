package com.example.aplikasikeuangan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aplikasikeuangan.helpers.DatabaseHelper;
import com.example.aplikasikeuangan.helpers.DetailCashFlow;
import com.example.aplikasikeuangan.helpers.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    TextView pemasukanBulanIniTotal;
    TextView pengeluaranBulanIniTotal;
    ImageView detailIcon;
    ImageView pemasukanIcon;
    ImageView pengaturanIcon;
    ImageView pengeluaranIcon;
    DatabaseHelper databaseHelper = null;
    User user;

    private  static final String TAG= "HomeActivity";
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//      chart
        mChart = (LineChart) findViewById(R.id.lineChart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        ArrayList<Entry> Value = new ArrayList<>();
        Value.add(new Entry(11,30000));
        Value.add(new Entry(12,40000));
        Value.add(new Entry(13,40000));
        Value.add(new Entry(14,60000));
        Value.add(new Entry(15,10000));
        Value.add(new Entry(16,50000));
        Value.add(new Entry(17,70000));
        LineDataSet set1 = new LineDataSet(Value, "Pengeluaran");
        set1.setColor(Color.BLUE);
        set1.setLineWidth(2f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.BLUE);

        ArrayList<Entry> Value2 = new ArrayList<>();
        Value2.add(new Entry(11,100000));
        Value2.add(new Entry(12,400000));
        Value2.add(new Entry(13,700000));
        Value2.add(new Entry(14,80000));
        Value2.add(new Entry(15,80000));
        Value2.add(new Entry(16,500000));
        Value2.add(new Entry(17,300000));
        LineDataSet set2 = new LineDataSet(Value2, "Pemasukan");
        set1.setColor(Color.RED);
        set1.setLineWidth(2f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.RED);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        LineData data = new LineData(dataSets);
        mChart.setData(data);
//      end chart

        DateFormat dateFormat = new SimpleDateFormat("MM", Locale.US);
        Date date = new Date();
        String currentMonth = dateFormat.format(date);
        Log.d("Current month", currentMonth);
        if(getIntent().hasExtra("User")){
            user = getIntent().getParcelableExtra("User");
        }
        initViews();
        initObjects(currentMonth);

    }

    private void initViews(){
        pengaturanIcon = findViewById(R.id.pengaturan_icon);
        pemasukanIcon = findViewById(R.id.pemasukan_icon);
        pengeluaranIcon = findViewById(R.id.pengeluaran_icon);
        detailIcon = findViewById(R.id.detail_cash_flow_icon);
        pemasukanBulanIniTotal = findViewById(R.id.total_pemasukan_bulan_ini);
        pengeluaranBulanIniTotal = findViewById(R.id.total_pengeluaran_bulan_ini);
    }

    private void initObjects(String currentMonth){
        databaseHelper = new DatabaseHelper(getApplicationContext());
        pengaturanIcon.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), PengaturanActivity.class);
            i.putExtra("User",user);
            startActivity(i);
        });
        pemasukanIcon.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), PemasukanActivity.class);
            i.putExtra("User",user);
            startActivity(i);
        });
        pengeluaranIcon.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), PengeluaranActivity.class);
            i.putExtra("User",user);
            startActivity(i);
        });
        detailIcon.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), DetailCashFlowActivity.class);
            i.putExtra("User",user);
            startActivity(i);
        });
        if(databaseHelper.getNominalPemasukanByBulan(currentMonth) != null){
            int pemasukan = 0;
            List<DetailCashFlow> list = databaseHelper.getNominalPemasukanByBulan(currentMonth);
            for (int i = 0; i < list.size(); i++) {
                pemasukan += list.get(i).getNominal();
            }
            pemasukanBulanIniTotal.setText("Rp. "+ pemasukan);
            Log.d("Pemasukan", ""+pemasukan);
        }else{
            pemasukanBulanIniTotal.setText("Rp. 0");
        }
        if(databaseHelper.getNominalPengeluaranByBulan(currentMonth) != null) {
            int pengeluaran = 0;
            List<DetailCashFlow> list = databaseHelper.getNominalPengeluaranByBulan(currentMonth);
            for (int i = 0; i < list.size(); i++) {
                pengeluaran += list.get(i).getNominal();
            }
            pengeluaranBulanIniTotal.setText("Rp. "+pengeluaran);
            Log.d("Pengeluaran", ""+pengeluaran);
        }else{
            pengeluaranBulanIniTotal.setText("Rp. 0");
        }

    }
}