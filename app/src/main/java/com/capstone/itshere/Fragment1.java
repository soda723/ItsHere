package com.capstone.itshere;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.capstone.itshere.account.FirebaseID;
import com.capstone.itshere.accountBook.DailyNote;
import com.capstone.itshere.accountBook.DailyNoteAdapter;
import com.capstone.itshere.accountBook.ab_add_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

public class Fragment1 extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DailyNote> arrayList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static String document_email;
    private TextView tv_hint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_1, container, false);

        //Initialize
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);//리사이클러뷰 기존성능강화
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // Dailynote 객체를 담을 어레이 리스트
        tv_hint = view.findViewById(R.id.tv_hint);

        //등록버튼 설정
        fab = (FloatingActionButton) view.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ab_add_Activity.class);
                startActivity(intent);
            }
        });
        return view;
    }//OnCreateView

    @Override
    public void onStart(){
        Log.e("here0", document_email+"happy");
        //사용자ID 가져오기
        if(mAuth.getCurrentUser() != null){
            Log.e("herea", document_email+"happy");
            db.collection(FirebaseID.user).document(mAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult() != null){
                                Log.e("hereb", document_email+"happy");
                                document_email = (String) task.getResult().getData()
                                        .get(FirebaseID.email);
                                Log.e("here1", document_email+"happy");

                            }
                        }});
        }
        super.onStart();
        try{

            //db값 가져오기
            Log.e("here2", document_email+"happy");
            db.collection(FirebaseID.noteboard).document("test1@example.com").collection(FirebaseID.noteitem)
                    .orderBy(FirebaseID.notedate, Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value != null){
                                arrayList.clear();
                                for(DocumentSnapshot snap : value.getDocuments()){
                                    Map<String, Object> shot = snap.getData();
                                    String date = timestampToString(String.valueOf(shot.get(FirebaseID.notedate)));
                                    String category = String.valueOf(shot.get(FirebaseID.category));
                                    String note = String.valueOf(shot.get(FirebaseID.note));
                                    Integer amount = Integer.parseInt((String) shot.get(FirebaseID.amount));

                                    DailyNote item = new DailyNote(date, category, note, amount);
                                    arrayList.add(item);
                                }
                                Log.e("abc", String.valueOf(arrayList.size()));
                                adapter = new DailyNoteAdapter(arrayList, getContext());
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    });
            tv_hint.setVisibility(View.GONE);

        }catch (NullPointerException e){
            Log.e("fragment1 db error", String.valueOf(e));
            tv_hint.setVisibility(View.VISIBLE);
        }

    }

    public String timestampToString(String stamp){
        stamp = stamp.replace("Timestamp(seconds=", "").replace(" nanoseconds=", "").replace(")", "");
        String[] array = stamp.split(",");
        String ds = array[0] ;
        return ds;
    }

}