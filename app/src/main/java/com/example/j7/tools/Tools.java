package com.example.j7.tools;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.j7.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.example.j7.LoginActivity.TSUserId;

public class Tools {

    public String random4Number() {
        int n = (int) (Math.random() * 9998 + 1);
//        int n = (int) (Math.random() * 3 + 1);
        String nn = "";
        if (Integer.toString(n).length() < 4) {
            for (int i = 0; i < (4 - Integer.toString(n).length()); i++) {
                nn = nn + "0";
            }
        }
        nn = nn + Integer.toString(n);
        return nn;
    }

    public String roleChange(int index) {
        String je = null;
        switch (index) {
            case 0:
                je = "j4";
                break;
            case 1:
                je = "fs";
                break;
            case 2:
                je = "player";
                break;
            case 3:
                je = "b74";
                break;
            case 4:
                je = "boss1";
                break;
        }
        return je;
    }


    public String roleChangeName(String index) {
        String je = null;
        switch (index) {
            case "j4":
                je = "劍士";
                break;
            case "fs":
                je = "法師";
                break;
            case "player":
                je = "普通人";
                break;
            case "b74":
                je = "騎士";
                break;
        }
        return je;
    }


    public int roleChangePicture(int index) {
        int je = 0;
        switch (index) {
            case 0:
                je = R.drawable.j4;
                break;
            case 1:
                je = R.drawable.fs;
                break;
            case 2:
                je = R.drawable.player;
                break;
            case 3:
                je = R.drawable.b74;
                break;
            case 4:
                je = R.drawable.boss1;
                break;
        }
        return je;
    }

    public int fBGetInt(DatabaseReference who) {
        final int[] x = {0};
        who.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {

                } else {
                    long index = (long) snapshot.getValue();
                    x[0] = (int) index;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return x[0];
    }

    public String fBGetString(DatabaseReference who) {
        final String[] x = {""};
        who.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {

                } else {
                    x[0] = snapshot.getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return x[0];
    }

}
