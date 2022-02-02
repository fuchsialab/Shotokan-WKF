package com.fuchsia.shotokanwkf;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


import java.util.Map;

public class Kumitelongadapter extends FirebaseRecyclerAdapter<Kumitelongmodel, Kumitelongadapter.myviewholder> {


    public Kumitelongadapter(FirebaseRecyclerOptions<Kumitelongmodel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final Kumitelongmodel model) {


        holder.textView.setText(model.getName());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(activity, videoPlayer.class);
                intent.putExtra("nam", model.getURL());
                activity.startActivity(intent);

                KumiteLong a = KumiteLong.getInstance();
                a.showRewarrdedAds();
                a.rewardAds();

            }

        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newkumite, parent, false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textView;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.kataname);
            cardView = itemView.findViewById(R.id.cardview);


        }
    }
}