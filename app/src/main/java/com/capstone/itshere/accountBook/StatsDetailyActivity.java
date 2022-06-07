package com.capstone.itshere.accountBook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.itshere.Fragment1;
import com.capstone.itshere.R;
import com.capstone.itshere.account.FirebaseID;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class StatsDetailyActivity extends AppCompatActivity {

    private static String TAG = "StatsDetailyActiviy";
    ImageButton back;
    TextView title;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseUser User = mAuth.getCurrentUser();
    private String categoryname;
    private String document_email;
    private ArrayList<DailyNote> arrayList;
    private TextView tv_noitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_detaily);

        //intent
        categoryname = getIntent().getStringExtra("category");

        //Initialize
        recyclerView = findViewById(R.id.rview_statsdetaily);
        recyclerView.setHasFixedSize(true);//리사이클러뷰 기존성능강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        tv_noitem = findViewById(R.id.tv_noitem);
        arrayList = new ArrayList<>();

        //툴바 설정
        back = (ImageButton) findViewById(R.id.tool_sub1_back);
        title = (TextView) findViewById(R.id.tool_sub1_title);
        title.setText(categoryname + " 내역");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadCategoryData();
    }

    private void loadCategoryData() {
        try{
            document_email = User.getEmail();
            //db에서 값 가져오기 > arraylist에 담기 > adpater에 저장 > 리사이클러 뷰에 뿌리기
            db.collection(FirebaseID.noteboard).document(document_email).collection(FirebaseID.noteitem)
                    .orderBy(FirebaseID.notedate, Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value != null){
                                arrayList.clear();
                                for(DocumentSnapshot snap : value.getDocuments()){
                                    Map<String, Object> shot = snap.getData();
                                    String category = String.valueOf(shot.get(FirebaseID.category));
                                    String bigcate = String.valueOf(shot.get(FirebaseID.bigcate));
                                    if (category.equals(categoryname) && bigcate.equals("지출")){
                                        String date = Fragment1.timestampToString(String.valueOf(shot.get(FirebaseID.notedate)));
                                        String note = String.valueOf(shot.get(FirebaseID.note));
                                        int amount = Integer.parseInt(String.valueOf(shot.get(FirebaseID.amount)));
                                        String docId = String.valueOf(shot.get(FirebaseID.documentId));

                                        DailyNote item = new DailyNote(bigcate, date, category, note, amount, docId);
                                        arrayList.add(item);
                                    }
                                }
                                Log.e("갯수", arrayList.size() + "개");
                                if(arrayList.size() == 0){
                                    tv_noitem.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                    Log.e("ㅇㅇㅇㅇ", "개");
                                }else{
                                    adapter = new DailyNoteAdapter(arrayList, getApplicationContext());
                                    recyclerView.setAdapter(adapter);
                                    tv_noitem.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                }

                            }
                        }
                    });
        }catch (NullPointerException e){
            Log.e(TAG , String.valueOf(e));
            Toast.makeText(this, "error",Toast.LENGTH_SHORT).show();
        }
    }
}