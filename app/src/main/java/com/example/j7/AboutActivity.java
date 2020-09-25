package com.example.j7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.j7.databinding.ActivityAboutBinding;
import com.example.j7.databinding.ActivitySoloMapBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.j7.LoginActivity.TSUserId;

public class AboutActivity extends AppCompatActivity {

    ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        FirebaseDatabase.getInstance().getReference("pg").child("about").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.textView.setText(snapshot.child("text1").getValue().toString());
                binding.textView1.setText(snapshot.child("text2").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void btn_b(View v) {
        finish();
    }
}