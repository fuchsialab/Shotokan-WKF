package com.fuchsia.shotokanwkf;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.FullScreenContentCallback;

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


                if (Admob.mInterstitialAd != null) {

                    Admob.mInterstitialAd.show(basicKarate.getInstance());

                    Admob.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                        @Override
                        public void onAdDismissedFullScreenContent() {

                            Intent intent =new Intent(activity, videoPlayer.class);
                            intent.putExtra("nam", basicmodel.getURL());
                            activity.startActivity(intent);

                            Admob.mInterstitialAd = null;
                            Admob.loadInter(basicKarate.getInstance());

                        }
                    });

                }
                else{

                    Intent intent =new Intent(activity, videoPlayer.class);
                    intent.putExtra("nam", basicmodel.getURL());
                    activity.startActivity(intent);

                    Admob.mInterstitialAd = null;
                    Admob.loadInter(basicKarate.getInstance());
                }

            }
        });

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AppCompatActivity activity= (AppCompatActivity) view.getContext();


                if (Admob.mInterstitialAd != null) {

                    Admob.mInterstitialAd.show(basicKarate.getInstance());

                    Admob.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                        @Override
                        public void onAdDismissedFullScreenContent() {

                            Intent intent= new Intent(activity, wkfShotokan.class);
                            intent.putExtra("katapic",basicmodel.getPic());
                            activity.startActivity(intent);

                            Admob.mInterstitialAd = null;
                            Admob.loadInter(basicKarate.getInstance());

                        }
                    });

                }
                else{

                    Intent intent= new Intent(activity, wkfShotokan.class);
                    intent.putExtra("katapic",basicmodel.getPic());
                    activity.startActivity(intent);

                    Admob.mInterstitialAd = null;
                    Admob.loadInter(basicKarate.getInstance());
                }


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
        ImageView vid, pic;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);

            vid =itemView.findViewById(R.id.katavid);
            pic= itemView.findViewById(R.id.katapic);
            textView= itemView.findViewById ( R.id.kataname );


        }
    }


}