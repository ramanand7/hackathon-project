package com.example.firenotes.model;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firenotes.R;
import com.example.firenotes.shownotedetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


//public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
//  List<String> titles;
//  List<String> content;
//
//
//    public adapter(List<String> titless,List<String> contents)
//    {
//        titles=titless;
//        content=contents;
//    }
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.noteviewlayout,parent,false);
//
//        return new ViewHolder(view);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        holder.notetitle.setText(titles.get(position));
//        holder.notecontent.setText(content.get(position));
//        final int code= getRandomeColor();
//        holder.mcardview.setCardBackgroundColor(holder.view.getResources().getColor(code,null));
//        holder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "your notes is safe here", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(v.getContext(), shownotedetail.class);
//                intent.putExtra("title",titles.get(position));
//                intent.putExtra("contennt",content.get(position));
//                intent.putExtra("codecolr", code);
//
//                v.getContext().startActivity(intent);
//            }
//        });
//
//    }
//
//    private int getRandomeColor() {
//        List<Integer> colorcode = new ArrayList<>();
//
//        colorcode.add(R.color.blue);
//        colorcode.add(R.color.red);
//        colorcode.add(R.color.skyblue);
//        colorcode.add(R.color.gray);
//        colorcode.add(R.color.greenlight);
//        colorcode.add(R.color.pink);
//        colorcode.add(R.color.lightPurple);
//        colorcode.add(R.color.lightGreen);
//        colorcode.add(R.color.notgreen);
//        colorcode.add(R.color.yellow);
//        Random randomnum= new Random();
//        int number = randomnum.nextInt(colorcode.size());
//
//        return colorcode.get(number);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return titles.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView notetitle,notecontent;
//        CardView mcardview;
//        View view;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            notecontent = itemView.findViewById(R.id.content);
//            notetitle = itemView.findViewById(R.id.titles);
//            mcardview = itemView.findViewById(R.id.noteCard);
//            view = itemView;
//        }
//    }
//}
