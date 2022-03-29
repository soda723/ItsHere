package com.capstone.itshere;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.itshere.accountBook.ab_add_Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Fragment1 extends Fragment {

    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_1, container, false);

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
    }
}