package com.example.j7.fourBtn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.j7.R;
import com.example.j7.tools.Tools;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static com.example.j7.LoginActivity.userId;


public class FourCardAdd extends AppCompatActivity {

    TextView juge, sg2, HP1, MP1, je;
    View atkR, HPMP;
    public View line11, line12, line13, line14, line15, line16, line17, line18, line19;
    int index = 5;
    int index2 = 5;
    Button buttonAtk1, shop;
    public DatabaseReference level, backpack, record;
    Tools tools = new Tools();
    String rocord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_card_add);

        findView();
        level = FirebaseDatabase.getInstance().getReference("users").child(userId).child("level");

        record = FirebaseDatabase.getInstance().getReference("users").child(userId).child("record");

        /**更改 rocord 值*/
        record.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long x = (long) snapshot.getValue();
                rocord = tools.roleChange((int) x);
//                backpack = FirebaseDatabase.getInstance().getReference("users").child(userId).child("role").child(rocord).child("backpack");
                je.setText(tools.roleChangeName(rocord));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        FirebaseDatabase.getInstance().getReference("users").child(userId).addValueEventListener(re);//監聽
        HP1.setText("");
        MP1.setText("");
        juge.setText("判斷技能強度 : ");

        buttonAtk1.setEnabled(false);
        buttonAtk1.setBackgroundColor(Color.parseColor("#00000000"));

        atkDrawINVISIBLE();
    }

    public void findView() {

        shop = findViewById(R.id.shop);

        juge = findViewById(R.id.juge);

        je = findViewById(R.id.je);

        atkR = findViewById(R.id.atkR);

        HPMP = findViewById(R.id.HPMP);

        sg2 = findViewById(R.id.sg2);

        line11 = findViewById(R.id.line11);

        line12 = findViewById(R.id.line12);

        line13 = findViewById(R.id.line13);

        line14 = findViewById(R.id.line14);

        line15 = findViewById(R.id.line15);

        line16 = findViewById(R.id.line16);

        line17 = findViewById(R.id.line17);

        line18 = findViewById(R.id.line18);

        line19 = findViewById(R.id.line19);

        HP1 = findViewById(R.id.HP1);

        MP1 = findViewById(R.id.MP1);

        buttonAtk1 = findViewById(R.id.buttonAtk1);
    }

    public void j4(View v) {
        record.setValue(0);
        je.setText("劍士");
    }

    public void fs(View v) {
        record.setValue(1);
        je.setText("法師");
    }

    public void player(View v) {
        record.setValue(2);
        je.setText("普通人");
    }

    public void b74(View v) {
        record.setValue(3);
        je.setText("騎士");
    }


    private ValueEventListener re = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            /** 有素材才能升等 隨即監聽按鈕是否可按*/
            long i = (long) snapshot.child("level").getValue();
            index = (int) i;

            /**背包有格子才能按*/
            ArrayList<Integer> x = (ArrayList<Integer>) snapshot.child("role").child(rocord).child("backpack").child("HP").getValue();
            System.out.println("rocord : " + rocord);
            int w = x.indexOf((long)0);

            switch (w) {
                case -1:
                    index2 = 0;
                    break;
                default:
                    index2 = 5 - w;
                    break;
            }
            System.out.println("背包格子數量 : " + index2);
            if (index2 <= 0 || index <= 0) {
                shop.setEnabled(false);
            } else {
                shop.setEnabled(true);
            }
            sg2.setText("目前剩下 " + index + " 素材,背包還有 " + index2 + " 格空間");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };


    public void btn_b(View v) {
        finish();
    }

    ArrayList<Integer> HPBackpack;
    ArrayList<Integer> MPBackpack;
    ArrayList<ArrayList<Integer>> atkRBackpack;

    public int randomHP = 0;
    public int randomMP = 0;
    public ArrayList<Integer> randomAtkR = new ArrayList<>();

    public void shop(View v) {
        if (index >= 0) {
            /**初始化*/
            atkDrawINVISIBLE();
            /**計算結果*/
            countSkillIntensity();
            /**計算評價*/
            judgingSkillIntensity();

            FirebaseDatabase.getInstance().getReference("users").child(userId).child("role").child(rocord).child("backpack").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HPBackpack = (ArrayList<Integer>) snapshot.child("HP").getValue();
                    System.out.println("HPBackpack :" + HPBackpack);
                    MPBackpack = (ArrayList<Integer>) snapshot.child("MP").getValue();
                    atkRBackpack = (ArrayList<ArrayList<Integer>>) snapshot.child("atkR").getValue();

                    int indexx = 0;
                    for (int k = 0; k < HPBackpack.size(); k++) {
                        int a = Integer.parseInt(String.valueOf(HPBackpack.get(k)));
                        if (a == 0) {
                            indexx = k;
                            break;
                        }
                    }

                    HPBackpack.set(indexx, randomHP);
                    MPBackpack.set(indexx, randomMP);
                    atkRBackpack.set(indexx, randomAtkR);

                    backpack = FirebaseDatabase.getInstance().getReference("users").child(userId).child("role").child(rocord).child("backpack");
                    backpack.child("HP").setValue(HPBackpack);
                    backpack.child("MP").setValue(MPBackpack);
                    backpack.child("atkR").setValue(atkRBackpack);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }
    }

    public void countSkillIntensity() {
        level.setValue(index - 1);
        index2 = index2 - 1;
        randomHP = (int) (Math.random() * 10) + 1;//產生1-10
        randomMP = (int) (Math.random() * 10) + 1;//產生1-10
        int randomAtkRCount = (int) (Math.random() * 9) + 1;//產生1-9
        randomAtkR = new ArrayList<>();
        for (int j = 0; j < randomAtkRCount; j++) {
            randomAtkR.add((int) (Math.random() * 9));
        }
        /**去重後排序*/
        randomAtkR = new ArrayList<>(new HashSet<>(randomAtkR));
        Collections.sort(randomAtkR);

        /**顯示計算結果*/
        HP1.setText(randomHP + "");
        MP1.setText(randomMP + "");
        atkDraw(randomAtkR);
        juge.setVisibility(View.VISIBLE);
        atkR.setVisibility(View.VISIBLE);
        HPMP.setVisibility(View.VISIBLE);
    }


    public void judgingSkillIntensity() {
        String abcd = null;
        int j = randomHP * (10 - randomMP) * randomAtkR.size();
        if (j <= 15) {//0 - 10%
            abcd = "F";
        } else if (j <= 32) {//10 - 25%
            abcd = "D";
        } else if (j < 75) {//25 - 50 %
            abcd = "C";
        } else if (j < 160) {//50 - 75%
            abcd = "B";
        } else if (j < 270) {//75 - 90%
            abcd = "A";
        } else if (j < 350) {//90 - 95%
            abcd = "S";
        } else if (j < 10000) {//95 - 100%
            abcd = "SSS";
        }
        juge.setText("判斷技能強度 : " + abcd);
    }


    public void atkDraw(ArrayList<Integer> atk) {
        for (int i = 0; i < atk.size(); i++) {
            int yc = Integer.parseInt(String.valueOf(atk.get(i)));
            switch (yc) {
                case 1:
                    line11.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    line12.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    line13.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    line14.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    line15.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    line16.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    line17.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    line18.setVisibility(View.VISIBLE);
                    break;
                case 9:
                    line19.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    public void atkDrawINVISIBLE() {
        line11.setVisibility(View.INVISIBLE);
        line12.setVisibility(View.INVISIBLE);
        line13.setVisibility(View.INVISIBLE);
        line14.setVisibility(View.INVISIBLE);
        line15.setVisibility(View.INVISIBLE);
        line16.setVisibility(View.INVISIBLE);
        line17.setVisibility(View.INVISIBLE);
        line18.setVisibility(View.INVISIBLE);
        line19.setVisibility(View.INVISIBLE);
    }
}