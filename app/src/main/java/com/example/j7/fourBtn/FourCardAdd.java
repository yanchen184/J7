package com.example.j7.fourBtn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.j7.R;

public class FourCardAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_card_add);
    }
    public void btn_b(View v){
        finish();
    }
}