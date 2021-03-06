package com.capstone.itshere.accountBook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.capstone.itshere.Fragment1;
import com.capstone.itshere.R;
import com.capstone.itshere.account.FirebaseID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ab_add_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String email;

    private ImageButton back;
    private TextView title;

    private RadioGroup radio;
    private RadioButton radioValue;
    EditText ab_add_date, ab_add_amount, ab_add_note, ab_add_memo;
    Spinner spinner_account, spinner_category;
    private Button btn_save;
    private String MONTH;

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
        /*ps ????????? ????????? R?????? ????????? ?????? R??? import ????????????
        * import com.example.blahblah.R; */

        //?????? ??????
        back = (ImageButton) findViewById(R.id.tool_sub1_back);
        title = (TextView) findViewById(R.id.tool_sub1_title);
        title.setText("????????????");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //?????? ?????????
        radio = findViewById(R.id.ab_add_radio);
        ab_add_date = findViewById(R.id.ab_add_date);
        spinner_account = findViewById(R.id.ab_add_account);
        spinner_category = findViewById(R.id.ab_add_category);
        ab_add_amount = findViewById(R.id.ab_add_amount);
        ab_add_note = findViewById(R.id.ab_add_note);
        ab_add_memo = findViewById(R.id.ab_add_memo);
        btn_save = findViewById(R.id.ab_add_save);

        //????????? ??????
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == R.id.ab_add_radio_income){
                    Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_SHORT).show();
                }else if(checkedId == R.id.ab_add_radio_expense){
                    Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_SHORT).show();
                }
                radioValue = findViewById(radio.getCheckedRadioButtonId());
            }
        });
        if (radioValue == null){
            radio.check(R.id.ab_add_radio_expense);
        }




        //datepicker??????
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

        //?????? spinner
        String[] acc_items = {"??????", "??????", "??????"};
        ArrayAdapter<String> acc_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, acc_items);
        acc_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_account.setAdapter(acc_adapter);

        //?????? spinner
        String[] cate_items = {"??????", "??????/??????", "????????????", "??????/??????", "????????????", "?????????/??????", "??????"};
        ArrayAdapter<String> cate_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cate_items);
        cate_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(cate_adapter);

        //?????????ID ????????????
        if(mAuth.getCurrentUser() != null){
            db.collection(FirebaseID.user).document(mAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult() != null){
                                email = (String) task.getResult().getData()
                                        .get(FirebaseID.email);
                            }
                        }
                    });
        }

        //add?????? ??????
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() != null){
                    String noteId = db.collection(FirebaseID.note).document().getId(); //?????? id??? ??????
                    Map<String,Object> data = new HashMap<>(); //HashMap??? ?????? ????????? (??????)-(???)??????
                    MONTH = getYearMonth(ab_add_date.getText().toString()); // ????????? ???????????? ??????-??? ??? ??????
                    data.put(FirebaseID.documentId, noteId);
                    try{
                        data.put(FirebaseID.bigcate, radioValue.getText().toString());
                    }catch(NullPointerException e){
                        data.put(FirebaseID.bigcate, "??????"); // ??????/??????????????? ???????????? ????????? ????????? ????????? ??????
                    }
                    data.put(FirebaseID.notedate, StringToTimeStamp(ab_add_date.getText().toString()));
                    data.put(FirebaseID.account, spinner_account.getSelectedItem().toString());
                    data.put(FirebaseID.category, spinner_category.getSelectedItem().toString());
                    data.put(FirebaseID.amount, Integer.parseInt(String.valueOf(ab_add_amount.getText())));
                    data.put(FirebaseID.note, ab_add_note.getText().toString());
                    data.put(FirebaseID.memo, ab_add_memo.getText().toString());
                    //hashMap???????????? firestore??? ??????
                    db.collection(FirebaseID.noteboard).document(email)
                            .collection(MONTH).document(noteId)
                            .set(data, SetOptions.merge());
                    finish(); //???????????? ???????????? ??????
                }else{
                    //????????? ?????? ?????? ???????????? ?????????????????? ???????????? ????????? ???????????? ??????
                    Toast.makeText(ab_add_Activity.this, "?????????????????????.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }//onCreate

    private String getYearMonth(String stringdate) {
        String[] temp = stringdate.split("-");
        return temp[0] + "-" + temp[1];
    }

    private void updateLabel(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        ab_add_date.setText(sdf.format(myCalendar.getTime()));

    }
    
    public static Timestamp StringToTimeStamp(String datestring){
        String newdate = datestring + " 00:00:00";
        Timestamp timestamp = Timestamp.valueOf(newdate);
        return timestamp;
    }
}