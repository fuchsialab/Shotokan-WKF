package com.fuchsia.shotokanwkf;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;



public class basicAdapter extends FirebaseRecyclerAdapter<basicmodel,basicAdapter.myviewholder> {

    public basicAdapter(FirebaseRecyclerOptions<basicmodel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, final int position, @NonNull final basicmodel basicmodel)
    {
        holder.textView.setText(basicmodel.getName());


        holder.vid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AppCompatActivity activity= (AppCompatActivity) view.getContext();
                Intent intent =new Intent(activity, videoPlayer.class);
                intent.putExtra("nam", basicmodel.getURL());
                activity.startActivity(intent);
                basicKarate.showInterstitial();


            }
        });

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AppCompatActivity activity= (AppCompatActivity) view.getContext();
                Intent intent= new Intent(activity, wkfShotokan.class);
                intent.putExtra("katapic",basicmodel.getPic());
                activity.startActivity(intent);
                basicKarate.showInterstitial();


            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {

        TextView textView;
        ImageButton vid, pic;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);

            vid =itemView.findViewById(R.id.katavid);
            pic= itemView.findViewById(R.id.katapic);
            textView= itemView.findViewById ( R.id.kataname );


        }
    }


}