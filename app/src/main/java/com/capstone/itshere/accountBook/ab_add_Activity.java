package com.capstone.itshere.accountBook;

import androidx.appcompat.app.AppCompatActivity;

import com.capstone.itshere.R;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ab_add_Activity extends AppCompatActivity {

    private ImageButton back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ab_add);
        /*ps 패키지 추가후 R에서 오류가 나면 R을 import 해야한다
        * import com.example.blahblah.R; */

        Intent intent = getIntent();

        back = (ImageButton) findViewById(R.id.tool_sub1_back);
        title = (TextView) findViewById(R.id.tool_sub1_title);

        title.setText("등록하기");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}