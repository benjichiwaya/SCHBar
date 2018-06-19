package com.example.chiwaya.schbar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Chiwaya on 6/18/2018.
 */

public class RecyclerViewHolder extends RecyclerView.Adapter<RecyclerViewHolder.ViewHolder> {

    private static final String TAG = "RecyclerViewHolder";

    private ArrayList<String> postImages = new ArrayList<>();
    private ArrayList<String> postTitle = new ArrayList<>();
    private ArrayList<String> postdescrptn = new ArrayList<>();
    private ArrayList<String> postUser = new ArrayList<>();

    private Context postcontext;

    public RecyclerViewHolder(ArrayList<String> postImages, ArrayList<String> postTitle, ArrayList<String> postdescrptn
                                    , ArrayList<String> postUser, Context context) {
        this.postImages = postImages;
        this.postTitle = postTitle;
        this.postdescrptn = postdescrptn;
        this.postUser = postUser;
        this.postcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //INflating layout from the adapter into the new context
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: called");

        //loading all images
        Glide.with(postcontext)
                .asBitmap()
                .load(postImages.get(position)).into(holder.image);

        //Loading all text information
        holder.title.setText(postTitle.get(position));
        holder.descrptn.setText(postdescrptn.get(position));
        holder.user.setText(postUser.get(position));


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*
            * **************************************
            *                                      *
            *                                      *
            *        This code is crutial          *
            *                                      *
            *                                      *
            * **************************************
            *
            * It defines what happens when the item (card view and all of it's contents) are clicked
            * This is where you add the code for the new page.
            * */
            }
        });

    }

    @Override
    public int getItemCount() {
        return postTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        CardView cardView;
        TextView title, user, descrptn;
        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.final_image);
            cardView = itemView.findViewById(R.id.cardView_parent);
            title = itemView.findViewById(R.id.post_descript);
            descrptn = itemView.findViewById(R.id.post_notes);
            user = itemView.findViewById(R.id.card_user);
        }
    }
}
