package com.capstone.itshere.accountBook;

import androidx.appcompat.app.AppCompatActivity;

import com.capstone.itshere.R;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ab_add_Activity extends AppCompatActivity {

    private ImageButton back;
    private TextView title;

    private RadioGroup radio;
    EditText ab_add_date, ab_add_amount, ab_add_note, ab_add_memo;
    Spinner spinner_account, spinner_category;
    private Button btn_save;

    private Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ab_add);
        /*ps 패키지 추가후 R에서 오류가 나면 R을 import 해야한다
        * import com.example.blahblah.R; */

        //툴바 설정
        back = (ImageButton) findViewById(R.id.tool_sub1_back);
        title = (TextView) findViewById(R.id.tool_sub1_title);
        title.setText("등록하기");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //객체 초기화
        radio = findViewById(R.id.ab_add_radio);
        ab_add_date = findViewById(R.id.ab_add_date);
        spinner_account = findViewById(R.id.ab_add_account);
        spinner_category = findViewById(R.id.ab_add_category);
        ab_add_amount = findViewById(R.id.ab_add_amount);
        ab_add_note = findViewById(R.id.ab_add_note);
        ab_add_memo = findViewById(R.id.ab_add_memo);
        btn_save = findViewById(R.id.ab_add_save);

        //라디오 설정
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == R.id.ab_add_radio_income){
                    Toast.makeText(getApplicationContext(), "수입", Toast.LENGTH_SHORT).show();
                }else if(checkedId == R.id.ab_add_radio_expense){
                    Toast.makeText(getApplicationContext(), "지출", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //datepicker설정
        ab_add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ab_add_Activity.this,
                        myDatePicker,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        //자산 spinner
        String[] acc_items = {"현금", "카드", "은행"};
        ArrayAdapter<String> acc_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, acc_items);
        acc_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_account.setAdapter(acc_adapter);

        //분류 spinner
        String[] cate_items = {"식비", "차량/교통", "문화생활", "패션/미용", "생활용품", "경조사/회비", "기타"};
        ArrayAdapter<String> cate_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cate_items);
        cate_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(cate_adapter);


    }//onCreate
    private void updateLabel(){
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        ab_add_date.setText(sdf.format(myCalendar.getTime()));

    }
}