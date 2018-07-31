package com.example.chiwaya.schbar;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

public class SCHBarViewHolder extends RecyclerView.ViewHolder {
    private static String TAG = SCHBarViewHolder.class.getClass().getName();
    View view;
    TextView Description;
    TextView Title;
    ImageView Image;
    TextView User;

    public SCHBarViewHolder(View itemView)
    {
        super(itemView);
        view = itemView.getRootView();
    }

    public void setView( String user, String image, String title, String descrption)
    {
        User = view.findViewById(R.id.card_user);
        Image = view.findViewById(R.id.final_image);
        Title = view.findViewById(R.id.post_descript);
        Description = view.findViewById(R.id.post_notes);
        Log.d(TAG,"||||||| this is where the "+image+"is. Hope it shows");
        User.setText(user);
        Picasso.get().load(image).into(Image);
        Title.setText(title);
        Description.setText(descrption);
    }
}
