package com.example.chiwaya.schbar;

/**
 * Created by Chiwaya on 4/8/2018.
 */

public class PostItem {

    public String title;
    public String description;
    public String imageUri;
    public String user;
    public String display_photo;

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

    public String getImageUri() { return imageUri; }

    public void setImageUri(String imageUri) { this.imageUri = imageUri; }

    public String getUser() { return user; }

    public String getDisplay_photo() {
        return display_photo;
    }

    public void setDisplay_photo(String display_photo) {
        this.display_photo = display_photo;
    }
}
