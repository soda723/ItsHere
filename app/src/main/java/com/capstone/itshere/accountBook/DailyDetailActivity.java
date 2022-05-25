package com.capstone.itshere.accountBook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.capstone.itshere.R;

public class DailyDetailActivity extends AppCompatActivity {

    private ImageButton back;
    private TextView title;
    private TextView dd_inex,dd_date,dd_account,dd_category,dd_amount,dd_note,dd_memo;
    private Button btn_modify,btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_detail);

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
                //
            }
        });
    }//--onCreate--*

    @Override
    protected void onStart() {
        super.onStart();
    }
}