package com.example.chiwaya.schbar;

/**
 * Created by Chiwaya on 4/8/2018.
 */

public class PostItem {

    public String title, desc, image, user;

    public PostItem(){
    }

    public PostItem(String title, String desc, String image, String user)
    {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.user = user;

    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public  String getUser(){ return user;}


    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(String user){ this.user = user;}
}
