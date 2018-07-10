package com.example.chiwaya.schbar;

/**
 * Created by Chiwaya on 4/8/2018.
 */

public class PostItem {

    public String title;
    public String description;
    public String image;
    public String user;

    public PostItem(){
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public PostItem(String User , String Image, String Title, String description)
    {
        this.user = User;
        this.image = Image;
        this.title = Title;
        this.description = description;
    }


}
