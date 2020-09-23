package com.example.j7.Solo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.j7.GameActivity;
import com.example.j7.Parameter;
import com.example.j7.Queue;
import com.example.j7.R;
import com.example.j7.StartActivity;
import com.example.j7.Variable;
import com.example.j7.databinding.ActivitySoloMapBinding;
import com.example.j7.fourBtn.FourRoleAdd;
import com.example.j7.game.Boss1;
import com.example.j7.tools.Tools;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.j7.LoginActivity.TSUserId;
import static com.example.j7.LoginActivity.userId;

public class SoloMap extends AppCompatActivity {

    DatabaseReference FUser, FRoom;
    Tools tools = new Tools();
    String roomKey = "SoloGame_" + TSUserId;
    Variable variable = new Variable();
    Parameter parameter = new Parameter();
    Parameter boss1 = new Parameter();

    ActivitySoloMapBinding binding;
    List<Drawable> drawableList = new ArrayList<Drawable>();//存放圖片
    String[] level = {"簡單", "一般", "困難", "煉獄"};
    final int[] levelNum = {1};
    final int[] levelString = {1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_map);
        FUser = FirebaseDatabase.getInstance().getReference("users").child(TSUserId);
        FRoom = FirebaseDatabase.getInstance().getReference("rooms");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_solo_map);

        Intent it = getIntent();
        variable.setIndex(it.getIntExtra("index", 0));


        ArrayAdapter<String> lunchList = new ArrayAdapter<>(SoloMap.this,
                android.R.layout.simple_spinner_dropdown_item,
                level);


        binding.spinner.setAdapter(lunchList);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                levelString[0] = position;
                switch (position) {
                    case 0:
                    case 1:
                        levelNum[0] = position + 1;
                        break;
                    case 2:
                        levelNum[0] = position + 2;
                        break;
                    case 3:
                        levelNum[0] = position + 7;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        drawableList.add(getResources().getDrawable(R.drawable.bosss));//圖片04
        drawableList.add(getResources().getDrawable(R.drawable.boss1));//圖片01
        drawableList.add(getResources().getDrawable(R.drawable.bosstortoise));//圖片02
        drawableList.add(getResources().getDrawable(R.drawable.bosklpng));//圖片03
        drawableList.add(getResources().getDrawable(R.drawable.bossd));//圖片04


        binding.award.setOnTouchListener(new Button.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
//                    binding.award.setBackgroundResource(R.drawable.black_background);
                    binding.includeA.imageView.setVisibility(View.VISIBLE);
                    binding.includeA.textView.setVisibility(View.VISIBLE);
                    binding.award.setTextColor(Color.BLACK);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
//                    binding.award.setBackgroundResource(R.drawable.white_background);
                    binding.includeA.imageView.setVisibility(View.INVISIBLE);
                    binding.includeA.textView.setVisibility(View.INVISIBLE);
                    binding.award.setTextColor(Color.WHITE);
                }
                return false;
            }
        });

    }

    int sg = 0;

    public void words() {
        switch (index % drawableList.size()) {
            case 0:
                sg = 1;
                binding.includeA.textView.setText("Boss : 勇往直前的戰士\n選擇:" + level[levelString[0]] + "\n獎勵內容 :素材 " + sg * levelNum[0] + " 個");
                break;
            case 1:
                sg = 3;
                binding.includeA.textView.setText("Boss : 謹慎的樹人\n選擇:" + level[levelString[0]] + "\n獎勵內容 :素材 " + sg * levelNum[0] + " 個");
                break;
            case 2:
                sg = 5;
                binding.includeA.textView.setText("Boss : 愛咬人的玄武\n選擇:" + level[levelString[0]] + "\n獎勵內容 :素材 " + sg * levelNum[0] + " 個");
                break;
            case 3:
                sg = 20;
                binding.includeA.textView.setText("Boss : 苦痛的骷顱頭\n選擇:" + level[levelString[0]] + "\n獎勵內容 :素材 " + sg * levelNum[0] + "  個");
                break;
            case 4:
                sg = 50;
                binding.includeA.textView.setText("Boss : 古代巨龍\n選擇:" + level[levelString[0]] + "\n獎勵內容 :素材 " + sg * levelNum[0] + " 個");
                break;
        }

    }

    public void award(View v) {
    }

    int index = 1;

    public void turn() {
        if (index >= drawableList.size()) {
            index = 0;
        }
        if (index < 0) {
            index = drawableList.size() - 1;
        }
    }

    public int turn2(int x) {
        int i = index + x;
        if (i >= drawableList.size()) {
            i = 0;
        }
        if (i < 0) {
            i = drawableList.size() - 1;
        }
        return i;
    }

    public void br(View v) {
        index++;
        turn3();
    }

    public void bl(View v) {
        index--;
        turn3();
    }

    private void turn3() {
        turn();
        binding.middle.setImageDrawable(drawableList.get(index));
        binding.right.setImageDrawable(drawableList.get(turn2(1)));
        binding.left.setImageDrawable(drawableList.get(turn2(-1)));
        words();
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
        int indexP = 0;
        ArrayList<ArrayList<Integer>> bossAtk;
        switch (index) {
            case 0:
                indexP = 5;
                FRoom.child(roomKey).child("player2").child("HP").setValue(parameter.HP0 * levelNum[0]);
                FRoom.child(roomKey).child("player2").child("MP").setValue(parameter.MP0 * levelNum[0]);
                bossAtk = new ArrayList<>();
                for (int i = 0; i < parameter.Boss0Atk.length; i++) {
                    bossAtk.add(intToList(parameter.Boss0Atk[i]));
                }
                FRoom.child(roomKey).child("player2").child("game").child("gameAtkR").setValue((ArrayList<ArrayList<Integer>>) bossAtk);
                FRoom.child(roomKey).child("player2").child("game").child("gameHP").setValue((ArrayList<Integer>) intToList(parameter.Boss0HP));
                FRoom.child(roomKey).child("player2").child("game").child("gameMP").setValue((ArrayList<Integer>) intToList(parameter.Boss0MP));
                FRoom.child(roomKey).child("player2").child("HPUP").setValue(parameter.HPUPBoss0);
                FRoom.child(roomKey).child("player2").child("MPUP").setValue(parameter.MPUPBoss0);
                break;
            case 1:
                indexP = 4;
                FRoom.child(roomKey).child("player2").child("HP").setValue(parameter.HP1 * levelNum[0]);
                FRoom.child(roomKey).child("player2").child("MP").setValue(parameter.MP1 * levelNum[0]);
                bossAtk = new ArrayList<>();
                for (int i = 0; i < parameter.Boss1Atk.length; i++) {
                    bossAtk.add(intToList(parameter.Boss1Atk[i]));
                }
                FRoom.child(roomKey).child("player2").child("game").child("gameAtkR").setValue((ArrayList<ArrayList<Integer>>) bossAtk);
                FRoom.child(roomKey).child("player2").child("game").child("gameHP").setValue((ArrayList<Integer>) intToList(parameter.Boss1HP));
                FRoom.child(roomKey).child("player2").child("game").child("gameMP").setValue((ArrayList<Integer>) intToList(parameter.Boss1MP));
                FRoom.child(roomKey).child("player2").child("HPUP").setValue(parameter.HPUPBoss1);
                FRoom.child(roomKey).child("player2").child("MPUP").setValue(parameter.MPUPBoss1);
                break;
            case 2:
                indexP = 6;
                FRoom.child(roomKey).child("player2").child("HP").setValue(parameter.HP2 * levelNum[0]);
                FRoom.child(roomKey).child("player2").child("MP").setValue(parameter.MP2 * levelNum[0]);
                bossAtk = new ArrayList<>();
                for (int i = 0; i < parameter.Boss2Atk.length; i++) {
                    bossAtk.add(intToList(parameter.Boss2Atk[i]));
                }
                FRoom.child(roomKey).child("player2").child("game").child("gameAtkR").setValue((ArrayList<ArrayList<Integer>>) bossAtk);
                FRoom.child(roomKey).child("player2").child("game").child("gameHP").setValue((ArrayList<Integer>) intToList(parameter.Boss2HP));
                FRoom.child(roomKey).child("player2").child("game").child("gameMP").setValue((ArrayList<Integer>) intToList(parameter.Boss2MP));
                FRoom.child(roomKey).child("player2").child("HPUP").setValue(parameter.HPUPBoss2);
                FRoom.child(roomKey).child("player2").child("MPUP").setValue(parameter.MPUPBoss2);
                break;
            case 3:
                indexP = 7;
                FRoom.child(roomKey).child("player2").child("HP").setValue(parameter.HP3 * levelNum[0]);
                FRoom.child(roomKey).child("player2").child("MP").setValue(parameter.MP3 * levelNum[0]);
                bossAtk = new ArrayList<>();
                for (int i = 0; i < parameter.Boss3Atk.length; i++) {
                    bossAtk.add(intToList(parameter.Boss3Atk[i]));
                }
                FRoom.child(roomKey).child("player2").child("game").child("gameAtkR").setValue((ArrayList<ArrayList<Integer>>) bossAtk);
                FRoom.child(roomKey).child("player2").child("game").child("gameHP").setValue((ArrayList<Integer>) intToList(parameter.Boss3HP));
                FRoom.child(roomKey).child("player2").child("game").child("gameMP").setValue((ArrayList<Integer>) intToList(parameter.Boss3MP));
                FRoom.child(roomKey).child("player2").child("HPUP").setValue(parameter.HPUPBoss3);
                FRoom.child(roomKey).child("player2").child("MPUP").setValue(parameter.MPUPBoss3);
                break;
            case 4:
                indexP = 8;
                FRoom.child(roomKey).child("player2").child("HP").setValue(parameter.HP4 * levelNum[0]);
                FRoom.child(roomKey).child("player2").child("MP").setValue(parameter.MP4 * levelNum[0]);
                bossAtk = new ArrayList<>();
                for (int i = 0; i < parameter.Boss4Atk.length; i++) {
                    bossAtk.add(intToList(parameter.Boss4Atk[i]));
                }
                FRoom.child(roomKey).child("player2").child("game").child("gameAtkR").setValue((ArrayList<ArrayList<Integer>>) bossAtk);
                FRoom.child(roomKey).child("player2").child("game").child("gameHP").setValue((ArrayList<Integer>) intToList(parameter.Boss4HP));
                FRoom.child(roomKey).child("player2").child("game").child("gameMP").setValue((ArrayList<Integer>) intToList(parameter.Boss4MP));
                FRoom.child(roomKey).child("player2").child("HPUP").setValue(parameter.HPUPBoss4);
                FRoom.child(roomKey).child("player2").child("MPUP").setValue(parameter.MPUPBoss4);
                break;
        }

        FRoom.child(roomKey).child("player2").child("Index").setValue(indexP);


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