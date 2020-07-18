package com.example.j7.databinding;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;

import com.example.j7.BR;

import android.widget.ImageView;



public class User extends BaseObservable {

    private String name;
    private String address;
    private String email;
    private String image;


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;

        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getImage() {
        return image;

    }

    @Bindable
    public void setImage(String image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }


    @BindingAdapter({"android:image"})
    public static void loadImage(ImageView imageView, String image) {
//        Glide.with(imageView)
//                .load(image)
//                .into(imageView);
    }
}