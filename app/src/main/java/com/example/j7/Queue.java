package com.example.j7;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.j7.R;
import com.example.j7.StartActivity;
import com.example.j7.tools.Tools;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.j7.LoginActivity.userId;
import static com.example.j7.tools.Name.STATUS_INIT;

public class Queue {
    private StartActivity activity;
    private Context context;

    public Queue(Context context) {
        this.activity = (StartActivity) context;
        this.context = context;
    }

    Tools tools = new Tools();

//    public void checkRepeat() {
//        /**
//         * 1. 給出 roomKey 值 ->用tools.random4Number()算
//         * 2. 告訴玩家 玩家是player1 還是 player2
//         * 3.
//         * */
//
//        activity.roomKey = tools.random4Number();
//        System.out.println("checkRepeat :" + activity.roomKey);
//
//        activity.FRoom.child(activity.roomKey).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                System.out.println(snapshot.getValue());
//                if (snapshot.getValue() == null) {
//
//
//                    /**
//                     * 這邊是創造房間
//                     * 1.增加房間player1名稱
//                     * 2.狀態設為 0
//                     * 3.告訴前端四位數密碼
//                     * 4.監聽狀態
//                     * 5.增加房間自己的血量跟魔量
//                     * */
//
//                    activity.player = "player1"; //創建者是player1
//                    activity.FRoom.child(activity.roomKey).child("player1").child("name").setValue(userId); // 創房者為player1 同時也是 userId
//                    activity.FRoom.child(activity.roomKey).child("status").setValue(STATUS_INIT);//狀態為0
//
////                    activity.binding.
//
//                    TextView create1234 = findViewById(R.id.create1234);
//                    create1234.setText(roomKey);
//
//                    FRoom
//                            .child(roomKey)
//                            .child("status")
//                            .addValueEventListener(statusListener);
//
//                    FUser.child("role").addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            addHPMP(snapshot, "player1");
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                        }
//                    });
//
//                } else {
//                    System.out.println("運氣不錯喔  :" + roomKey);
//                    checkRepeat();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


    public void friendsCreate() {
//        activity.binding.layoutPair2
//        checkRepeat(); //roomKey
//        Button HTTPCreate = findViewById(R.id.HTTPCreate);
//        HTTPCreate.setEnabled(false);
//        Button HTTPJoin = findViewById(R.id.HTTPJoin);
//        HTTPJoin.setEnabled(false);
//        EditText roomEditText = findViewById(R.id.roomEditText);
//        roomEditText.setEnabled(false);
    }


}

