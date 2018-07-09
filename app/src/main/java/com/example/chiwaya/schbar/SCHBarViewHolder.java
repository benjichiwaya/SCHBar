package com.example.chiwaya.schbar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class SCHBarViewHolder extends RecyclerView.ViewHolder {
    View view;

    public SCHBarViewHolder(View itemView)
    {
        super(itemView);
        view = itemView;
    }

    public void setView(Context context, String user, String image, String title, String descrption)
    {
        TextView User = view.findViewById(R.id.card_user);
        ImageView Image = view.findViewById(R.id.final_image);
        TextView Title = view.findViewById(R.id.post_descript);
        TextView Description = view.findViewById(R.id.post_notes);

        User.setText(user);
        Picasso.get().load(image).into(Image);
        Title.setText(title);
        Description.setText(descrption);
    }
}
