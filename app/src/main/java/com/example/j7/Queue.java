package com.example.j7;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.j7.R;
import com.example.j7.Solo.SoloMap;
import com.example.j7.StartActivity;
import com.example.j7.tools.Tools;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.j7.LoginActivity.TSUserId;
import static com.example.j7.LoginActivity.userId;
import static com.example.j7.tools.Name.STATUS_INIT;
import static com.example.j7.tools.Name.STATUS_JOINED;

public class Queue {
    private StartActivity activity;
    private SoloMap activitySolo;
    private Context context;
    public DatabaseReference FRoom = FirebaseDatabase.getInstance().getReference("rooms");
    public DatabaseReference WRoom = FirebaseDatabase.getInstance().getReference("waitRoom");
    String TAG = "Queue";

    public Queue(Context context) {
        this.activity = (StartActivity) context;
        this.context = context;
    }

    public Queue(Context context, int x) {
        this.activitySolo = (SoloMap) context;
        this.context = context;
    }

    public static Queue getQueue(Context context) {
        return new Queue(context);
    }

    Tools tools = new Tools();

    public void friendsCreate() {
        checkRepeat();
        activity.binding.layoutPair.HTTPCreate.setEnabled(false);
        activity.binding.layoutPair.HTTPJoin.setEnabled(false);
        activity.binding.layoutPair.roomEditText.setEnabled(false);
    }

    public void friendsJoin() {
        Toast.makeText(context, "檢查是否有此房號....", Toast.LENGTH_SHORT).show();
        String roomNum = activity.binding.layoutPair.roomEditText.getText().toString(); //使用者打的roomNum
        activity.roomKey = roomNum;
        FRoom.child(roomNum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    Toast.makeText(context, "查無此房間....", Toast.LENGTH_SHORT).show();
                } else {
                    roomJoinFB("player2", STATUS_JOINED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void randomJoin() {
        /**
         * 1. 給出 roomKey 值 ->用tools.random4Number()算
         * 2. 告訴玩家 玩家是player1 還是 player2
         * */
        activity.roomKey = tools.random4Number();
        System.out.println("房間號 :" + activity.roomKey);

        /**先看看有沒有人在等待*/
        WRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    Log.d(TAG, "創建房間");
                    randomCreate();
                } else {
                    Log.d(TAG, "加入房間");
                    randomJ();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void randomJ() {
        /**有人在等的話就加入他的房間*/
        WRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {

                    WRoom.child(snapshot.getValue().toString().substring(1, 5)).setValue(STATUS_JOINED);
                    activity.roomKey = snapshot.getValue().toString().substring(1, 5);
                    System.out.println("加入房號");
                    roomJoinFB("player2", 1);
                    WRoom.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void randomCreate() {
        /**沒有人等待的話 創建的房號有沒有跟別人重複*/
        FRoom.child(activity.roomKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                if (snapshot.getValue() == null) {
                    /**房號沒有重複時 : */
                    WRoom.child(activity.roomKey).setValue(STATUS_INIT);
                    System.out.println("創建房號");
                    roomJoinFB("player1", 0);

                    /**************************************/
                } else {
                    System.out.println("運氣不錯喔  :" + activity.roomKey);
                    randomJoin();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void checkRepeat() {
        /**
         * 1. 給出 roomKey 值 ->用tools.random4Number()算
         * 2. 告訴玩家 玩家是player1 還是 player2
         * */
        activity.roomKey = tools.random4Number();
        System.out.println("checkRepeat :" + activity.roomKey);

        FRoom.child(activity.roomKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                if (snapshot.getValue() == null) {
                    /**3.告訴前端四位數密碼*/
                    activity.binding.layoutPair.create1234.setText(activity.roomKey);//顯示房間名稱
                    roomJoinFB("player1", STATUS_INIT);
                } else {
                    System.out.println("運氣不錯喔  :" + activity.roomKey);
                    checkRepeat();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void roomJoinFB(String player12, int status) {
        /**
         * 房間五要素
         * 1.在這房間中你是player1 or player2
         * 2.狀態設定 : 0是創建房間 1是開始遊戲
         * 3.紀錄玩家名字(顯示用)
         * 4.在房間增加自己的血量跟魔量讓大家都能看到(顯示用)
         * 5.監聽狀態  是1就開始
         * */

        //1
        activity.player = player12; //創建者是player1
        //2
        FRoom.child(activity.roomKey).child("status").setValue(status);//狀態為status
        //3
        FRoom.child(activity.roomKey).child(player12).child("name").setValue(userId); // 創房者為player1 同時也是 userId
        // 4
        activity.FUser.child("role").child(tools.roleChange(activity.index)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.child("SHP").getValue();
                snapshot.child("SMP").getValue();
                FRoom.child(activity.roomKey).child(activity.player).child("HP").setValue(snapshot.child("SHP").getValue());
                FRoom.child(activity.roomKey).child(activity.player).child("MP").setValue(snapshot.child("SMP").getValue());
                FRoom.child(activity.roomKey).child(activity.player).child("game").child("gameAtkR").setValue((ArrayList<ArrayList<Integer>>) snapshot.child("atkR").getValue());
                FRoom.child(activity.roomKey).child(activity.player).child("game").child("gameHP").setValue((ArrayList<Integer>) snapshot.child("HP").getValue());
                FRoom.child(activity.roomKey).child(activity.player).child("game").child("gameMP").setValue((ArrayList<Integer>) snapshot.child("MP").getValue());
                /** 使用的角色 - 初始血量 - 補血魔 - 初始位置 */
                FRoom.child(activity.roomKey).child(activity.player).child("Index").setValue(activity.index);
                FRoom.child(activity.roomKey).child(activity.player).child("HPUP").setValue(0);
                FRoom.child(activity.roomKey).child(activity.player).child("MPUP").setValue(0);
                FRoom.child(activity.roomKey).child(activity.player).child("X").setValue(0);
                FRoom.child(activity.roomKey).child(activity.player).child("Y").setValue(1);
                /**行動後的位置 - 補血魔 - 攻擊內容 之後都是記錄於此 */
                ArrayList<Integer> yc = new ArrayList<>();
                yc.add(0);
                FRoom.child(activity.roomKey).child(activity.player).child("Next").child("locationXSelf").setValue(0);
                FRoom.child(activity.roomKey).child(activity.player).child("Next").child("locationYSelf").setValue(1);
                FRoom.child(activity.roomKey).child(activity.player).child("Next").child("HPUP").setValue(0);
                FRoom.child(activity.roomKey).child(activity.player).child("Next").child("MPUP").setValue(0);
                FRoom.child(activity.roomKey).child(activity.player).child("Next").child("atkR").setValue(yc);
                FRoom.child(activity.roomKey).child(activity.player).child("Next").child("atkHP").setValue(0);
                FRoom.child(activity.roomKey).child(activity.player).child("Next").child("atkMP").setValue(0);
                /**使用 狀態值-獨有技能*/
                FRoom.child(activity.roomKey).child("fourStatus").child("player11").setValue(false);
                FRoom.child(activity.roomKey).child("fourStatus").child("player12").setValue(0);
                FRoom.child(activity.roomKey).child("fourStatus").child("player21").setValue(false);
                FRoom.child(activity.roomKey).child("fourStatus").child("player22").setValue(0);
                FRoom.child(activity.roomKey).child(activity.player).child("unique").setValue(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //5
        FRoom
                .child(activity.roomKey)
                .child("status")
                .addValueEventListener(statusListener);
    }


    public void addMPHPIntoRoom(final String player) {
        activity.FUser.child("role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                addHPMP(snapshot, player);

                /** 使用的角色 - 初始血量 - 補血魔 - 初始位置 */
                FRoom.child(activity.roomKey).child(player).child("Index").setValue(activity.index);
                FRoom.child(activity.roomKey).child(player).child("HP").setValue(snapshot.child(tools.roleChange(activity.index)).child("SHP").getValue());
                FRoom.child(activity.roomKey).child(player).child("MP").setValue(snapshot.child(tools.roleChange(activity.index)).child("SMP").getValue());
                FRoom.child(activity.roomKey).child(player).child("HPUP").setValue(0);
                FRoom.child(activity.roomKey).child(player).child("MPUP").setValue(0);
                FRoom.child(activity.roomKey).child(player).child("X").setValue(0);
                FRoom.child(activity.roomKey).child(player).child("Y").setValue(1);
                /**行動後的位置 - 補血魔 - 攻擊內容 之後都是記錄於此 */
                FRoom.child(activity.roomKey).child(player).child("Next").child("locationXSelf").setValue(0);
                FRoom.child(activity.roomKey).child(player).child("Next").child("locationYSelf").setValue(1);
                FRoom.child(activity.roomKey).child(player).child("Next").child("HPUP").setValue(0);
                FRoom.child(activity.roomKey).child(player).child("Next").child("MPUP").setValue(0);
                ArrayList<Integer> yc = new ArrayList<>();
                yc.add(0);
                FRoom.child(activity.roomKey).child(player).child("Next").child("atkR").setValue(yc);
                FRoom.child(activity.roomKey).child(player).child("Next").child("atkHP").setValue(0);
                FRoom.child(activity.roomKey).child(player).child("Next").child("atkMP").setValue(0);


                /**填入自己的招式**/
                switch (player) {
                    case "player1":
                        FirebaseDatabase.getInstance().getReference("users").child(userId).child("role").child(tools.roleChange(activity.index)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                FRoom.child(activity.roomKey).child("player1").child("game").child("gameAtkR").setValue((ArrayList<ArrayList<Integer>>) snapshot.child("atkR").getValue());
                                FRoom.child(activity.roomKey).child("player1").child("game").child("gameHP").setValue((ArrayList<Integer>) snapshot.child("HP").getValue());
                                FRoom.child(activity.roomKey).child("player1").child("game").child("gameMP").setValue((ArrayList<Integer>) snapshot.child("MP").getValue());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        break;
                    case "player2":
                        FirebaseDatabase.getInstance().getReference("users").child(userId).child("role").child(tools.roleChange(activity.index)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                FRoom.child(activity.roomKey).child("player2").child("game").child("gameAtkR").setValue((ArrayList<ArrayList<Integer>>) snapshot.child("atkR").getValue());
                                FRoom.child(activity.roomKey).child("player2").child("game").child("gameHP").setValue((ArrayList<Integer>) snapshot.child("HP").getValue());
                                FRoom.child(activity.roomKey).child("player2").child("game").child("gameMP").setValue((ArrayList<Integer>) snapshot.child("MP").getValue());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        break;
                    /**填入完畢**/
                }
                /**使用 狀態值-獨有技能*/
                FRoom.child(activity.roomKey).child("fourStatus").child("player11").setValue(false);
                FRoom.child(activity.roomKey).child("fourStatus").child("player12").setValue(0);
                FRoom.child(activity.roomKey).child("fourStatus").child("player21").setValue(false);
                FRoom.child(activity.roomKey).child("fourStatus").child("player22").setValue(0);
                FRoom.child(activity.roomKey).child(player).child("unique").setValue(false);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public ValueEventListener statusListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.getValue() == null)
                return;
            long status = (long) snapshot.getValue();
            switch ((int) status) {
                case 0:
                    System.out.println("等待對手進入遊戲室");
                    break;
                case 1:
                    System.out.println("對手加入了！");
                    Intent it = new Intent(context, GameActivity.class);
                    it.putExtra("player", activity.player);
                    it.putExtra("index", activity.index);
                    it.putExtra("roomKey", activity.roomKey);
                    it.putExtra("finalHP", activity.finalHP);
                    it.putExtra("finalMP", activity.finalMP);

                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("list", activity.finalAtlR);
                    it.putExtras(mBundle);
                    activity.startActivity(it);
                    break;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    public void addHPMP(@NonNull DataSnapshot snapshot, String player) {


    }
}

