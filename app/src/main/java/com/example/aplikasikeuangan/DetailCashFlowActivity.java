package com.example.aplikasikeuangan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.aplikasikeuangan.adapter.DetailCashFlowAdapter;
import com.example.aplikasikeuangan.helpers.DatabaseHelper;
import com.example.aplikasikeuangan.helpers.DetailCashFlow;
import com.example.aplikasikeuangan.helpers.User;

import java.util.ArrayList;
import java.util.List;

public class DetailCashFlowActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewCashFlow;
    private List<DetailCashFlow> detailCashFlowList;
    private DetailCashFlowAdapter detailCashFlowAdapter;
    User user;
    Button kembaliCashFlowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cash_flow);
//        Objects.requireNonNull(getSupportActionBar()).setSubtitle("Detail Cash Flow");
        try{
            if(getIntent().hasExtra("User")){
                user = getIntent().getParcelableExtra("User");
            }
            initViews();
            initObjects();
            detailCashFlowList.clear();
            detailCashFlowList.addAll(databaseHelper.getAllCashFlow());
            detailCashFlowAdapter.notifyDataSetChanged();
            kembaliCashFlowBtn.setOnClickListener(v -> finish());
        }catch(Exception e){
            Log.d(" Cash Flow activity", e.toString());
        }
    }

    private void initObjects() {
        detailCashFlowList = new ArrayList<DetailCashFlow>();
        detailCashFlowAdapter = new DetailCashFlowAdapter(detailCashFlowList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewCashFlow.setLayoutManager(mLayoutManager);
        recyclerViewCashFlow.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCashFlow.setHasFixedSize(true);
        recyclerViewCashFlow.setAdapter(detailCashFlowAdapter);
        databaseHelper = new DatabaseHelper(getApplicationContext());

    }

    private void initViews() {
        kembaliCashFlowBtn = findViewById(R.id.kembali_detail_cash_flow_button);
        recyclerViewCashFlow = findViewById(R.id.detail_cash_flow_recycler_view);
    }
}