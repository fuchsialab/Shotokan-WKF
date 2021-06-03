package com.fuchsia.shotokanwkf;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;



public class kumiteAdapter extends FirebaseRecyclerAdapter<kumitemodel,kumiteAdapter.myviewholder> {

    public kumiteAdapter(FirebaseRecyclerOptions<kumitemodel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, final int position, @NonNull final kumitemodel kumitemodel)
    {
        holder.textView.setText(kumitemodel.getName());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AppCompatActivity activity= (AppCompatActivity) view.getContext();
                    Intent intent =new Intent(activity, videoPlayer.class);
                    intent.putExtra("nam", kumitemodel.getURL());
                    activity.startActivity(intent);
                    kumiteActivity.showInterstitial();

            }

        });


    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.kumrow,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {

        CardView cardView;
        TextView textView;

        public myviewholder(@NonNull View itemView)
        {
            super(itemView);


            textView= itemView.findViewById ( R.id.kataname );
            cardView= itemView.findViewById ( R.id.cardview );


        }
    }


}