package com.example.j7.fourBtn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.j7.R;
import com.example.j7.databinding.ActivityFourCardAddBinding;
import com.example.j7.databinding.ActivityMainBinding;
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
    int power;//素材量
    int maxBackpack = 7;//背包上限值
    Button buttonAtk1, shop;
    public DatabaseReference level, backpack, record;
    Tools tools = new Tools();
    String rocord;

    public ActivityFourCardAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_card_add);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_four_card_add);

        findView();
        level = FirebaseDatabase.getInstance().getReference("users").child(userId).child("level");
        record = FirebaseDatabase.getInstance().getReference("users").child(userId).child("record");

        binding.j4.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.player.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.j4.setTextColor(Color.parseColor("#faf3f2"));
        binding.fs.setTextColor(Color.parseColor("#faf3f2"));
        binding.player.setTextColor(Color.parseColor("#faf3f2"));
        binding.b74.setTextColor(Color.parseColor("#faf3f2"));

        /**更改 rocord 值*/
        record.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long x = (long) snapshot.getValue();
                rocord = tools.roleChange((int) x);
                je.setText(tools.roleChangeName(rocord));
                switch (rocord) {
                    case "j4":
                        binding.j4.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.j4.setTextColor(Color.parseColor("#2b0501"));
                        break;
                    case "fs":
                        binding.fs.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.fs.setTextColor(Color.parseColor("#2b0501"));
                        break;
                    case "player":
                        binding.player.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.player.setTextColor(Color.parseColor("#2b0501"));
                        break;
                    case "b74":
                        binding.b74.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.b74.setTextColor(Color.parseColor("#2b0501"));
                        break;
                }
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
        binding.j4.setBackgroundColor(Color.parseColor("#00000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.player.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.j4.setTextColor(Color.parseColor("#2b0501"));
        binding.fs.setTextColor(Color.parseColor("#faf3f2"));
        binding.player.setTextColor(Color.parseColor("#faf3f2"));
        binding.b74.setTextColor(Color.parseColor("#faf3f2"));
        record.setValue(0);
        je.setText("劍士");
    }

    public void fs(View v) {
        binding.j4.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#00000000"));
        binding.player.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.j4.setTextColor(Color.parseColor("#faf3f2"));
        binding.fs.setTextColor(Color.parseColor("#2b0501"));
        binding.player.setTextColor(Color.parseColor("#faf3f2"));
        binding.b74.setTextColor(Color.parseColor("#faf3f2"));
        record.setValue(1);
        je.setText("法師");
    }

    public void player(View v) {
        binding.j4.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.player.setBackgroundColor(Color.parseColor("#00000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.j4.setTextColor(Color.parseColor("#faf3f2"));
        binding.fs.setTextColor(Color.parseColor("#faf3f2"));
        binding.player.setTextColor(Color.parseColor("#2b0501"));
        binding.b74.setTextColor(Color.parseColor("#faf3f2"));
        record.setValue(2);
        je.setText("普通人");
    }

    public void b74(View v) {
        binding.j4.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.player.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#00000000"));
        binding.j4.setTextColor(Color.parseColor("#faf3f2"));
        binding.fs.setTextColor(Color.parseColor("#faf3f2"));
        binding.player.setTextColor(Color.parseColor("#faf3f2"));
        binding.b74.setTextColor(Color.parseColor("#2b0501"));
        record.setValue(3);
        je.setText("騎士");
    }


    private ValueEventListener re = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            /** 有素材才能升等 隨即監聽按鈕是否可按*/
            long i = (long) snapshot.child("level").getValue();
            power = (int) i;

            /**背包有格子才能按*/
            ArrayList<Integer> x = (ArrayList<Integer>) snapshot.child("role").child(rocord).child("backpack").child("HP").getValue();

            int c = x == null ? 0 : x.size();
            System.out.println("剩餘背包格子數量 : " + (maxBackpack - c));

            if (power <= 0 || (maxBackpack - c) <= 0) {
                shop.setEnabled(false);
            } else {
                shop.setEnabled(true);
            }

            sg2.setText("目前剩下 " + power + " 素材,背包還有 " + (maxBackpack - c) + " / " + maxBackpack + " 格空間");
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

    public int getRandomHP() {
        return randomHP;
    }

    public void setRandomHP(int randomHP) {
        this.randomHP = randomHP;
    }

    public int getRandomMP() {
        return randomMP;
    }

    public void setRandomMP(int randomMP) {
        this.randomMP = randomMP;
    }

    public ArrayList<Integer> getRandomAtkR() {
        return randomAtkR;
    }

    public void setRandomAtkR(ArrayList<Integer> randomAtkR) {
        this.randomAtkR = randomAtkR;
    }

    public ArrayList<Integer> randomAtkR = new ArrayList<>();

    public void shop(View v) {
        if (maxBackpack >= 0) {
            /**初始化*/
            atkDrawINVISIBLE();
            /**計算結果*/
            countSkillIntensity();
            /**計算評價*/
            juge.setText("判斷技能強度 : " + judgingSkillIntensity(randomHP, randomMP, randomAtkR));
            /**消耗一素材*/
            level.setValue(power - 1);

            FirebaseDatabase.getInstance().getReference("users").child(userId).child("role").child(rocord).child("backpack").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HPBackpack = (ArrayList<Integer>) snapshot.child("HP").getValue();
                    MPBackpack = (ArrayList<Integer>) snapshot.child("MP").getValue();
                    atkRBackpack = (ArrayList<ArrayList<Integer>>) snapshot.child("atkR").getValue();
                    System.out.println("HPBackpack :" + HPBackpack);
                    int c = HPBackpack == null ? 0 : HPBackpack.size();


                    if (c == 0) {
                        HPBackpack = new ArrayList<>();
                        HPBackpack.add(randomHP);
                        MPBackpack = new ArrayList<>();
                        MPBackpack.add(randomMP);

                        atkRBackpack = new ArrayList<>();
                        ArrayList<Integer> yc = new ArrayList<>();
                        for (int i = 0; i < randomAtkR.size(); i++) {
                            yc.add(randomAtkR.get(i));
                        }
                        atkRBackpack.add(yc);

                    } else {
                        HPBackpack.add(getRandomHP());
                        MPBackpack.add(getRandomMP());
                        atkRBackpack.add(getRandomAtkR());
                        Log.d("TAG", "onDataChange: " + HPBackpack);
                        Log.d("TAG", "onDataChange: " + MPBackpack);
                        Log.d("TAG", "onDataChange: " + atkRBackpack);

//                        HPBackpack.set(c, getRandomHP());
//                        MPBackpack.set(c, getRandomMP());
//                        atkRBackpack.set(c, getRandomAtkR());
                    }

//                    Log.d("TAG", "onDataChange: " + getRandomHP());
//                    Log.d("TAG", "onDataChange: " + HPBackpack);
//                    Log.d("TAG", "onDataChange: " + MPBackpack);
//                    Log.d("TAG", "onDataChange: " + atkRBackpack);
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

    int randomHPCount;
    int randomAtkRCount;

    public void atkRAndHp(int max, int low) {
        do {
            randomHPCount = (int) (Math.random() * 20) + 1;//產生1-10
            randomAtkRCount = (int) (Math.random() * 9) + 1;//產生1-9
        } while (randomHPCount * randomAtkRCount > low && max > randomHPCount * randomAtkRCount);
    }

    public void countSkillIntensity() {

        float p = (float) Math.random();
        float[] yc1 = {0.1f, 0.1f, 0.2f, 0.3f, 0.2f, 0.05f, 0.048f, 0.0012f, 0.0008f};
        float[] yc = {0f,0f,0f,0f,0f,0f,0f,0f,0f};
        for (int i = 0; i < yc1.length; i++) {
            for (int j = 0; j <= i; j++) {
                yc[i] = yc[i] + yc1[j];
                yc[i] = ((float)Math.round(yc[i]*10000))/ 10000;
            }
//            System.out.println(yc[i]);
        }

        Log.d("p", String.valueOf(p));

        if (p > yc[0]) {//F
            atkRAndHp(10, 0);
            setRandomMP((int) (Math.random() * 7) + 4);//產生1-10
        } else if (p > yc[1]) {//E
            atkRAndHp(20, 10);
            setRandomMP((int) (Math.random() * 6) + 5);//產生1-10
        } else if (p > yc[2]) {//D
            atkRAndHp(20, 10);
            setRandomMP((int) (Math.random() * 6) + 5);//產生1-10
        } else if (p > yc[3]) {//C
            atkRAndHp(30, 20);
            setRandomMP((int) (Math.random() * 5) + 6);//產生1-10
        } else if (p > yc[4]) {//B
            atkRAndHp(40, 30);
            setRandomMP((int) (Math.random() * 4) + 7);//產生1-10
        } else if (p > yc[5]) {//A
            atkRAndHp(50, 40);
            setRandomMP((int) (Math.random() * 4) + 7);//產生1-10
        } else if (p > yc[6]) {//S
            atkRAndHp(60, 50);
            setRandomMP((int) (Math.random() * 3) + 8);//產生1-10
        } else if (p > yc[7]) {//SS
            atkRAndHp(90, 60);
            setRandomMP((int) (Math.random() * 2) + 9);//產生1-10
        } else if (p > yc[8]) {//SSS
            atkRAndHp(2000, 90);
            setRandomMP((int) (Math.random() * 5) + 6);//產生1-10
        }

        setRandomHP(randomHPCount);

        /**生成攻擊格子*/
        int[] arr = new int[randomAtkRCount];
        for (int i = 0; i < randomAtkRCount; i++) {
            arr[i] = (int) (Math.random() * 9) + 1;
            for (int j = 0; j < i; j++) {
                if (arr[j] == arr[i]) {
                    i--;
                    break;
                }
            }
        }
        randomAtkR = new ArrayList<>();
        for (int k = 0; k < arr.length; k++) {
            randomAtkR.add(arr[k]);
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


    public String judgingSkillIntensity(int randomHP, int randomMP, ArrayList<Integer> randomAtkR) {
        String abcd = null;
        int j = randomHP * randomAtkR.size();
        if (j < 10) {//0 - 10%
            abcd = "F";
        } else if (j < 20) {//10 - 25%
            abcd = "D";
        } else if (j < 30) {//25 - 50 %
            abcd = "C";
        } else if (j < 40) {//50 - 75%
            abcd = "B";
        } else if (j < 50) {//75 - 90%
            abcd = "A";
        } else if (j < 60) {//90 - 95%
            abcd = "S";
        } else if (j < 90) {//95 - 100%
            abcd = "SSS";
        } else {//95 - 100%
            abcd = "SSS";
        }

        return abcd;
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