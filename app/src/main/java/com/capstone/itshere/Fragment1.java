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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capstone.itshere.account.FirebaseID;
import com.capstone.itshere.accountBook.DailyNote;
import com.capstone.itshere.accountBook.DailyNoteAdapter;
import com.capstone.itshere.accountBook.ab_add_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private static String TAG = "프레그먼트1";
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DailyNote> arrayList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseUser User = mAuth.getCurrentUser();
    public static String document_email;
    private TextView tv_hint, tv_hint2;
    private LinearLayout ly_total;

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
        tv_hint2 = view.findViewById(R.id.tv_hint2);
        ly_total = view.findViewById(R.id.ly_total);

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
        super.onStart();
//        //사용자ID 가져오기
//        if(mAuth.getCurrentUser(). != null){
//            Log.e(TAG +"_b", document_email+":happy");
//            document_email = mAuth.getID
//            db.collection(FirebaseID.user).document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    Log.e(TAG +"_c", document_email+":happy");
//                    if(task.getResult() != null){
//                        Log.e(TAG +"_d", document_email+":happy");
//                        document_email = (String) task.getResult().getData()
//                                .get(FirebaseID.email);
//                        Log.e(TAG +"_e", document_email+":happy");
//
//                    }
//                }
//            });
//        }//--*사용자ID가져오기 끝
        //db값 가져오기
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
                                    String date = timestampToString(String.valueOf(shot.get(FirebaseID.notedate)));
                                    String category = String.valueOf(shot.get(FirebaseID.category));
                                    String note = String.valueOf(shot.get(FirebaseID.note));
                                    Integer amount = Integer.parseInt((String) shot.get(FirebaseID.amount));

                                    DailyNote item = new DailyNote(date, category, note, amount);
                                    arrayList.add(item);
                                }
                                if(arrayList.size() == 0){
                                    tv_hint2.setVisibility(View.VISIBLE);
                                }else{
                                    tv_hint2.setVisibility(View.GONE);
                                }
                                adapter = new DailyNoteAdapter(arrayList, getContext());
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    });
            //--*db >...>뿌리기 끝
            tv_hint.setVisibility(View.GONE);
            ly_total.setVisibility(View.VISIBLE);
        }catch (NullPointerException e){
            //로그인한 유저가 없을때
            Log.e(TAG + " db error", String.valueOf(e));
            tv_hint.setVisibility(View.VISIBLE);
            ly_total.setVisibility(View.GONE);
            tv_hint2.setVisibility(View.GONE);
        }
    }//onStart--*

    public String timestampToString(String stamp){
        stamp = stamp.replace("Timestamp(seconds=", "").replace(" nanoseconds=", "").replace(")", "");
        String[] array = stamp.split(",");
        String ds = array[0];
        return ds;
    }

}