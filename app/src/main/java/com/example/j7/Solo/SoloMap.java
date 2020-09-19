package com.example.j7.Solo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.j7.GameActivity;
import com.example.j7.Parameter;
import com.example.j7.Queue;
import com.example.j7.R;
import com.example.j7.StartActivity;
import com.example.j7.Variable;
import com.example.j7.fourBtn.FourRoleAdd;
import com.example.j7.tools.Tools;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.j7.LoginActivity.TSUserId;
import static com.example.j7.LoginActivity.userId;

public class SoloMap extends AppCompatActivity {

    DatabaseReference FUser, FRoom;
    Tools tools = new Tools();
    String roomKey = "SoloGame_" + TSUserId;
    Variable variable = new Variable();
    Parameter parameter = new Parameter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_map);
        FUser = FirebaseDatabase.getInstance().getReference("users").child(TSUserId);
        FRoom = FirebaseDatabase.getInstance().getReference("rooms");

        Intent it = getIntent();
        variable.setIndex(it.getIntExtra("index", 0));

    }


    public void choose(View v) {

        FUser.child("role").child(tools.roleChange(variable.getIndex())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addHPMP(snapshot, "player1");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addHPMP(@NonNull DataSnapshot snapshot, String player) {

        //回血回魔 法師跟騎士有稍微調整
        if (tools.roleChange(variable.getRoleS()).equals("b74")) {
            variable.setUpHPInt(parameter.getUpHPB74());
        } else {
            variable.setUpHPInt(parameter.getUpHPOther());
        }

        if (tools.roleChange(variable.getRoleS()).equals("fs")) {
            variable.setUpMPInt(parameter.getUpMPFs());
        } else {
            variable.setUpMPInt(parameter.getUpMPOther());
        }

        /** 使用的角色 - 初始血量 - 補血魔 - 初始位置 */
        FRoom.child(roomKey).child(player).child("Index").setValue(variable.getIndex());
        FRoom.child(roomKey).child(player).child("HP").setValue(snapshot.child("SHP").getValue());
        FRoom.child(roomKey).child(player).child("MP").setValue(snapshot.child("SMP").getValue());
        FRoom.child(roomKey).child(player).child("game").child("gameAtkR").setValue((ArrayList<ArrayList<Integer>>) snapshot.child("atkR").getValue());
        FRoom.child(roomKey).child(player).child("game").child("gameHP").setValue((ArrayList<Integer>) snapshot.child("HP").getValue());
        FRoom.child(roomKey).child(player).child("game").child("gameMP").setValue((ArrayList<Integer>) snapshot.child("MP").getValue());
        FRoom.child(roomKey).child(player).child("HPUP").setValue(variable.getUpHPInt());
        FRoom.child(roomKey).child(player).child("MPUP").setValue(variable.getUpMPInt());
        FRoom.child(roomKey).child(player).child("X").setValue(0);
        FRoom.child(roomKey).child(player).child("Y").setValue(1);


        /**行動後的位置 - 補血魔 - 攻擊內容 之後都是記錄於此 */
        ArrayList<Integer> yc = new ArrayList<>();
        yc.add(0);
        FRoom.child(roomKey).child(player).child("Next").child("locationXSelf").setValue(0);
        FRoom.child(roomKey).child(player).child("Next").child("locationYSelf").setValue(1);
        FRoom.child(roomKey).child(player).child("Next").child("HPUP").setValue(0);
        FRoom.child(roomKey).child(player).child("Next").child("MPUP").setValue(0);
        FRoom.child(roomKey).child(player).child("Next").child("atkR").setValue(yc);
        FRoom.child(roomKey).child(player).child("Next").child("atkHP").setValue(0);
        FRoom.child(roomKey).child(player).child("Next").child("atkMP").setValue(0);

        FRoom.child(roomKey).child(player).child("unique").setValue(false);
        /**使用 狀態值-獨有技能*/


        FRoom.child(roomKey).child(player).child("name").setValue(userId); // 創房者為player1 同時也是 userId
        FRoom.child(roomKey).child("player2").child("name").setValue("Boss1"); // 創房者為player1 同時也是 userId
        /*****************************/

        /** 使用的角色 - 初始血量 - 補血魔 - 初始位置 */
        FRoom.child(roomKey).child("player2").child("Index").setValue(4);
        FRoom.child(roomKey).child("player2").child("HP").setValue(50);
        FRoom.child(roomKey).child("player2").child("MP").setValue(100);

        int[][] b74Atk = {{4, 5, 6}, {2, 5, 8}, {5, 6}, {1, 3, 4, 6, 7, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}};
        int[] b74HP = {4, 4, 5, 2, 0};
        int[] b74MP = {3, 3, 3, 6, 2};
        ArrayList<ArrayList<Integer>> b74Atkk = new ArrayList<>();
        for (int i = 0; i < b74Atk.length; i++) {
            b74Atkk.add(intToList(b74Atk[i]));
        }


        FRoom.child(roomKey).child("player2").child("game").child("gameAtkR").setValue((ArrayList<ArrayList<Integer>>) b74Atkk);
        FRoom.child(roomKey).child("player2").child("game").child("gameHP").setValue((ArrayList<Integer>) intToList(b74HP));
        FRoom.child(roomKey).child("player2").child("game").child("gameMP").setValue((ArrayList<Integer>) intToList(b74MP));

        FRoom.child(roomKey).child("player2").child("HPUP").setValue(variable.getUpHPInt());
        FRoom.child(roomKey).child("player2").child("MPUP").setValue(variable.getUpMPInt());
        FRoom.child(roomKey).child("player2").child("X").setValue(0);
        FRoom.child(roomKey).child("player2").child("Y").setValue(1);
        /**行動後的位置 - 補血魔 - 攻擊內容 之後都是記錄於此 */
        FRoom.child(roomKey).child("player2").child("Next").child("locationXSelf").setValue(0);
        FRoom.child(roomKey).child("player2").child("Next").child("locationYSelf").setValue(1);
        FRoom.child(roomKey).child("player2").child("Next").child("HPUP").setValue(0);
        FRoom.child(roomKey).child("player2").child("Next").child("MPUP").setValue(0);
        FRoom.child(roomKey).child("player2").child("Next").child("atkR").setValue(yc);
        FRoom.child(roomKey).child("player2").child("Next").child("atkHP").setValue(0);
        FRoom.child(roomKey).child("player2").child("Next").child("atkMP").setValue(0);


        FRoom.child(roomKey).child("player2").child("unique").setValue(false);
        /**使用 狀態值-獨有技能*/

        /*****************************/


        FRoom.child(roomKey).child("fourStatus").child("player11").setValue(false);
        FRoom.child(roomKey).child("fourStatus").child("player12").setValue(0);
        FRoom.child(roomKey).child("fourStatus").child("player21").setValue(false);
        FRoom.child(roomKey).child("fourStatus").child("player22").setValue(0);
//        FRoom.child(roomKey).child("fourStatus").child("match").setValue(0);

        Intent it = new Intent(this, GameActivity.class);
        it.putExtra("player", "player1");
        it.putExtra("index", variable.getIndex());
        it.putExtra("roomKey", roomKey);


//        it.putExtra("finalHP", activity.finalHP);
//        it.putExtra("finalMP", activity.finalMP);
//        Bundle mBundle = new Bundle();
//        mBundle.putSerializable("list", activity.finalAtlR);
//        it.putExtras(mBundle);
        startActivity(it);
    }

    public ArrayList<Integer> intToList(int[] data) {
        ArrayList<Integer> x = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            x.add(data[i]);
        }
        return x;
    }
}