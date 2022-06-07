package com.capstone.itshere.accountBook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.capstone.itshere.R;
import com.capstone.itshere.account.FirebaseID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class DailyDetailActivity extends AppCompatActivity {

    private ImageButton back;
    private TextView title;
    private TextView dd_inex,dd_date,dd_account,dd_category,dd_amount,dd_note,dd_memo;
    private Button btn_modify,btn_delete;
    private String idNum;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseUser User = mAuth.getCurrentUser();
    private String document_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_detail);

        //intent
        idNum = getIntent().getStringExtra("idNum");
        Log.e("in ddactivity", idNum);

        //툴바 설정
        back = (ImageButton) findViewById(R.id.tool_sub1_back);
        title = (TextView) findViewById(R.id.tool_sub1_title);
        title.setText("상세내용");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //객체 초기화
        dd_inex = findViewById(R.id.dd_inex);
        dd_date = findViewById(R.id.dd_date);
        dd_account = findViewById(R.id.dd_account);
        dd_category = findViewById(R.id.dd_category);
        dd_amount = findViewById(R.id.dd_amount);
        dd_note = findViewById(R.id.dd_note);
        dd_memo = findViewById(R.id.dd_memo);
        btn_modify = findViewById(R.id.btn_modify);
        btn_delete = findViewById(R.id.btn_delete);

        //수정버튼
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //

            }
        });

        //삭제버튼
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DailyDetailActivity.this);
                alertDialogBuilder.setTitle("가계부 내역 삭제");
                alertDialogBuilder
                        .setMessage("가계부 내역을 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("삭제",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //삭제 기능 실행
                                        db.collection(FirebaseID.noteboard).document(document_email).collection(FirebaseID.noteitem)
                                                .document(idNum).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Daily Note delete", "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("Daily Note delete", "Error deleting document", e);
                                                    }
                                                });
                                        //액티비티 종료
                                        DailyDetailActivity.this.finish();
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();
                // 다이얼로그 보여주기
                alertDialog.show();
            }
        });
    }//--onCreate--*

    @Override
    protected void onStart() {
        super.onStart();

        document_email = User.getEmail();
        //db에서 값 가져오기 > arraylist에 담기 > adpater에 저장 > 리사이클러 뷰에 뿌리기
        db.collection(FirebaseID.noteboard).document(document_email).collection(FirebaseID.noteitem)
                .document(idNum).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()){
                                Map<String, Object> shot = doc.getData();
                                String date = timestampToString(String.valueOf(shot.get(FirebaseID.notedate)));
                                String category = String.valueOf(shot.get(FirebaseID.category));
                                String note = String.valueOf(shot.get(FirebaseID.note));
                                String amount = String.valueOf(shot.get(FirebaseID.amount));
                                String account = String.valueOf(shot.get(FirebaseID.account));
                                String bigcate = String.valueOf(shot.get(FirebaseID.bigcate));
                                String memo = String.valueOf(shot.get(FirebaseID.memo));

                                dd_inex.setText(bigcate);
                                dd_date.setText(date);
                                dd_account.setText(account);
                                dd_category.setText(category);
                                dd_amount.setText(amount);
                                dd_note.setText(note);
                                dd_memo.setText(memo);
                            }//--if doc exits-*
                        }//--if task-*
                    }
                });


    }//--OnStart--**

    private String timestampToString(String stamp){
        stamp = stamp.replace("Timestamp(seconds=", "").replace(" nanoseconds=", "").replace(")", "");
        String[] array = stamp.split(",");
        String ds = array[0];
        long timestamp = Long.parseLong(ds);
        Date date = new java.util.Date(timestamp*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

}