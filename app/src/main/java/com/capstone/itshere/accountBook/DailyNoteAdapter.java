package com.capstone.itshere.accountBook;

import android.content.Context;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.itshere.R;

import java.util.ArrayList;

public class DailyNoteAdapter extends RecyclerView.Adapter<DailyNoteAdapter.DailyNoteHolder> {

    private ArrayList<DailyNote> arrayList;
    private Context context;

    public DailyNoteAdapter(ArrayList<DailyNote> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public DailyNoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dailynote_item, parent,false);
        DailyNoteHolder holder = new DailyNoteHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyNoteHolder holder, int position) {
        holder.tv_dn_date.setText(arrayList.get(position).getDate());
        holder.tv_dn_category.setText(arrayList.get(position).getCategory());
        holder.tv_dn_note.setText(arrayList.get(position).getNote());
        holder.tv_dn_amount.setText(arrayList.get(position).getAmount());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class DailyNoteHolder extends RecyclerView.ViewHolder {
        TextView tv_dn_date;
        TextView tv_dn_category;
        TextView tv_dn_note;
        TextView tv_dn_amount;
        public DailyNoteHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_dn_date = itemView.findViewById(R.id.tv_dn_date);
            this.tv_dn_category = itemView.findViewById(R.id.tv_dn_category);
            this.tv_dn_note = itemView.findViewById(R.id.tv_dn_note);
            this.tv_dn_amount = itemView.findViewById(R.id.tv_dn_amount);

        }
    }
}
