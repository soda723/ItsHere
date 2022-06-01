package com.capstone.itshere.accountBook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.capstone.itshere.R;
import com.capstone.itshere.account.FirebaseID;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

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
    private int v_total=-1;
    private int[] arrayValue = {0,0,0,0,0,0,0};
    private float[] percentageV = {0,0,0,0,0,0,0};

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

    }//onCreate--*

    @Override
    protected void onStart() {
        super.onStart();
        String document_email = User.getEmail();
        //db에서 값 가져오기 > arraylist에 담기 > adpater에 저장 > 리사이클러 뷰에 뿌리기
        db.collection(FirebaseID.noteboard).document(document_email).collection(FirebaseID.noteitem)
                .orderBy(FirebaseID.notedate, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null){

                            for(DocumentSnapshot snap : value.getDocuments()){
                                Map<String, Object> shot = snap.getData();
                                String bigcate = String.valueOf(shot.get(FirebaseID.bigcate));
                                if(bigcate.equals("지출")){
                                    String category = String.valueOf(shot.get(FirebaseID.category));
                                    int amount = Integer.parseInt(shot.get(FirebaseID.amount).toString());
                                    v_total += amount;

                                    if (category.equals(CategoryId.food)){
                                        arrayValue[0] += amount;
                                    }else if(category.equals(CategoryId.traffic)){
                                        arrayValue[1] += amount;
                                    }else if(category.equals(CategoryId.culture)){
                                        arrayValue[2] += amount;
                                    }else if(category.equals(CategoryId.beauty)){
                                        arrayValue[3] += amount;
                                    }else if(category.equals(CategoryId.household)){
                                        arrayValue[4] += amount;
                                    }else if(category.equals(CategoryId.congratuations)){
                                        arrayValue[5] += amount;
                                    }else { //etc
                                        arrayValue[6] += amount;
                                    }
                                }
                            }
                            if (v_total > 0){
                                v_total +=1;
                            }
                            Log.e("arrayValues", arrayValue[0]+""+arrayValue[1]+""+arrayValue[2]+""+arrayValue[3]+""+arrayValue[4]+""+arrayValue[5]+""+arrayValue[6]+"");
                            Log.e("v_total", v_total +"");

                            for(int i = 0; i <percentageV.length; i++){
                                percentageV[i] = Math.round((1.0 * arrayValue[i] / v_total * 100*10)/10.0);
                            }
                            Log.e("percentageV", percentageV[0]+""+percentageV[1]+""+percentageV[2]+""+percentageV[3]+""+percentageV[4]+""+percentageV[5]+""+percentageV[6]+"");

                            statsArrayList = new ArrayList<>();

                            statsArrayList.add(new StatsItem(percentageV[0], CategoryId.food, arrayValue[0]));
                            statsArrayList.add(new StatsItem(percentageV[1], CategoryId.traffic, arrayValue[1]));
                            statsArrayList.add(new StatsItem(percentageV[2], CategoryId.culture, arrayValue[2]));
                            statsArrayList.add(new StatsItem(percentageV[3], CategoryId.beauty, arrayValue[3]));
                            statsArrayList.add(new StatsItem(percentageV[4], CategoryId.household, arrayValue[4]));
                            statsArrayList.add(new StatsItem(percentageV[5], CategoryId.congratuations, arrayValue[5]));
                            statsArrayList.add(new StatsItem(percentageV[6], CategoryId.etc, arrayValue[6]));
                            adapter = new statsItemAdapter(statsArrayList,getApplicationContext());
                            stats_view.setAdapter(adapter);

                            //내림차순 정렬
                            Comparator<StatsItem> percentageDesc = new Comparator<StatsItem>() {
                                @Override
                                public int compare(StatsItem item1, StatsItem item2) {
                                    return (item2.getAmount() - item1.getAmount());
                                }
                            } ;

                            Collections.sort(statsArrayList, percentageDesc) ;
                            adapter.notifyDataSetChanged() ;

                            makePiechart();

                        }
                    }
                });



    }//--OnStart--*

    protected void makePiechart(){
        //Piechart
        pieChart.setUsePercentValues(true); //퍼센트 값으로 표시
        pieChart.getDescription().setEnabled(false); //chart및 description표시 안모이게
        pieChart.setExtraOffsets(5,10,5,5);
//        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(false); //도넛 구멍 없애기
//        pieChart.setTransparentCircleRadius(70f);//눌렀을때 투명원 반경 설정(아마도)

        ArrayList<PieEntry> sampledata = new ArrayList<>();
        String[] valuesId = {CategoryId.food,CategoryId.traffic,CategoryId.culture,
                CategoryId.beauty, CategoryId.household,CategoryId.congratuations,
                CategoryId.etc};
        for(int i = 0; i<percentageV.length; i++){

            if (percentageV[i] > 0.0){
                sampledata.add(new PieEntry(percentageV[i],valuesId[i]));
            }
        }
//        sampledata.add(new PieEntry(percentageV[0],CategoryId.food));
//        sampledata.add(new PieEntry(percentageV[1],CategoryId.traffic));
//        sampledata.add(new PieEntry(percentageV[2],CategoryId.culture));
//        sampledata.add(new PieEntry(percentageV[3],CategoryId.beauty));
//        sampledata.add(new PieEntry(percentageV[4],CategoryId.household));
//        sampledata.add(new PieEntry(percentageV[5], CategoryId.congratuations));
//        sampledata.add(new PieEntry(percentageV[6],CategoryId.etc));
        PieDataSet dataSet = new PieDataSet(sampledata, "Sample Data");

        dataSet.setSliceSpace(1.5f); // 구역간 라인표시
//        dataSet.setSelectionShift(5f);//??
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(12f);
        data.setValueTextColor(ContextCompat.getColor(getApplicationContext(),R.color.myMuk));

        pieChart.setData(data);
        pieChart.invalidate();//값이 바뀌었다고? 생성되었다고 알려줌
//        pieChart.animateY(1400, Easing.EaseInOutQuad); //애니메이션 효과

    }

}