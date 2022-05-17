package com.capstone.itshere.accountBook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.capstone.itshere.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class statsActivity extends AppCompatActivity {

    PieChart pieChart;
    RecyclerView stats_view;
    private ImageButton back;
    private TextView title;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StatsItem> statsArrayList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseUser User = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //툴바 설정
        back = (ImageButton) findViewById(R.id.tool_sub1_back);
        title = (TextView) findViewById(R.id.tool_sub1_title);
        title.setText("월통계");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //초기화
        stats_view = findViewById(R.id.stats_recyclerView);
        pieChart = findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        stats_view = findViewById(R.id.stats_recyclerView);
        stats_view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        stats_view.setLayoutManager(layoutManager);

        //Piechart
        pieChart.setUsePercentValues(true); //퍼센트 값으로 표시
        pieChart.getDescription().setEnabled(false); //chart및 description표시 안모이게
        pieChart.setExtraOffsets(5,10,5,5);
//        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(false); //도넛 구멍 없애기
//        pieChart.setTransparentCircleRadius(70f);//눌렀을때 투명원 반경 설정(아마도)

        ArrayList<PieEntry> sampledata = new ArrayList<>();
        sampledata.add(new PieEntry(30f,"식비"));
        sampledata.add(new PieEntry(5f,"차량/교통"));
        sampledata.add(new PieEntry(5f,"문화생활"));
        sampledata.add(new PieEntry(10f,"패션/미용"));
        sampledata.add(new PieEntry(20f,"생활용품"));
        sampledata.add(new PieEntry(20f,"경조사/회비"));
        sampledata.add(new PieEntry(10f,"기타"));
        PieDataSet dataSet = new PieDataSet(sampledata, "Sample Data");

        dataSet.setSliceSpace(1.5f); // 구역간 라인표시
//        dataSet.setSelectionShift(5f);//??
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
        pieChart.invalidate();//값이 바뀌었다고? 생성되었다고 알려줌
//        pieChart.animateY(1400, Easing.EaseInOutQuad); //애니메이션 효과

        //recyclerview sample
        statsArrayList = new ArrayList<>();
        statsArrayList.add(new StatsItem(30, "식비", 100000));
        statsArrayList.add(new StatsItem(5, "차량/교통", 100000));
        statsArrayList.add(new StatsItem(5, "문화생활", 100000));
        statsArrayList.add(new StatsItem(10, "패션/미용", 100000));
        statsArrayList.add(new StatsItem(20, "생활용품", 100000));
        statsArrayList.add(new StatsItem(20, "경조사/회비", 100000));
        statsArrayList.add(new StatsItem(10, "기타", 100000));
        adapter = new statsItemAdapter(statsArrayList,getApplicationContext());
        stats_view.setAdapter(adapter);


    }//onCreate--*
}