package com.example.chiwaya.schbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
     public void setOnClickListener(Context context, SCHBarViewHolder viewHolder, PostItem postItem)
     {
         Intent intent = new Intent(context,Details.class);
         ActivityOptionsCompat appOptions1 = ActivityOptionsCompat
                 .makeSceneTransitionAnimation((Activity) context, viewHolder.Image,
                         ViewCompat.getTransitionName(viewHolder.Image));
         intent.putExtra("User", postItem.getUser());
         intent.putExtra("Image",postItem.getImageUri());
         intent.putExtra("Title", postItem.getTitle());
         intent.putExtra("Description", postItem.getDescription());
         context.startActivity(intent,appOptions1.toBundle());
     }
}
