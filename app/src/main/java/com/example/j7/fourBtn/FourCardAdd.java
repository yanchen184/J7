package com.example.j7.fourBtn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    int maxBackpack = 12;//背包上限值
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

        binding.ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                /**如果素材量比較多就沒有問題*/
                if (!String.valueOf(editable).equals("")) {
                    if (power >= Integer.parseInt(String.valueOf(editable))) {
                        use = Integer.parseInt(String.valueOf(editable));
                    } else {
                        use = power;
                    }
                    binding.shop.setText("花費 " + use + " 素材");
                    common();
                }
            }
        });
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
            level.setValue(power - use);

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
//                        Log.d("ADDCARD", "onDataChange: " + HPBackpack);
//                        Log.d("ADDCARD", "onDataChange: " + MPBackpack);
//                        Log.d("ADDCARD", "onDataChange: " + atkRBackpack);
                        HPBackpack.add(getRandomHP());
                        MPBackpack.add(getRandomMP());
                        Log.d("AddCard", String.valueOf(atkRBackpack));
                        Log.d("AddCard", String.valueOf(getRandomAtkR()));

                        atkRBackpack.add(getRandomAtkR());


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

    public void atkRAndHp(int m, int max) {
        do {
            randomHPCount = (int) (Math.random() * max) + 1;//產生1-20
            randomAtkRCount = (int) (Math.random() * 9) + 1;//產生1-9
        } while (randomHPCount * randomAtkRCount < mm[m - 1] || mm[m] < randomHPCount * randomAtkRCount);

    }

    int[] mm = {0, 10, 12, 16, 20, 25, 30, 50, 60, 100};

    float[] yc1 = {0.1f, 0.1f, 0.2f, 0.3f, 0.2f, 0.05f, 0.048f, 0.0012f, 0.0008f};
    String detail;

    public void useUp() {
        if (use < 5) {
            yc1 = new float[]{0.1f, 0.1f, 0.2f, 0.3f, 0.2f, 0.05f, 0.048f, 0.0012f, 0.0008f};
        } else if (use < 10) {
            yc1 = new float[]{0f, 0.2f, 0.2f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f, 0};
        } else if (use < 20) {
            yc1 = new float[]{0f, 0.1f, 0.1f, 0.2f, 0.2f, 0.2f, 0.1f, 0.05f, 0.05f};
        } else if (use < 40) {
            yc1 = new float[]{0f, 0f, 0f, 0f, 0.4f, 0.2f, 0.2f, 0.2f, 0f};
        } else if (use < 80) {
            yc1 = new float[]{0f, 0f, 0f, 0f, 0f, 0.4f, 0.2f, 0.2f, 0.2f};
        } else {
            yc1 = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0.1f, 0.2f, 0.7f};
        }
        detail();
    }

    public void detail() {
        detail = "抽中機率 : \nF : " + (float) Math.round(yc1[0] * 10000) / 100 + "% " +
                "\nE : " + (float) Math.round(yc1[1] * 10000) / 100 + "% " +
                "\nD : " + (float) Math.round(yc1[2] * 10000) / 100 + "% " +
                "\nC : " + (float) Math.round(yc1[3] * 10000) / 100 + "% " +
                "\nB : " + (float) Math.round(yc1[4] * 10000) / 100 + "% " +
                "\nA :" + (float) Math.round(yc1[5] * 10000) / 100 + "% " +
                "\nS : " + (float) Math.round(yc1[6] * 10000) / 100 + "% " +
                "\nSS : " + (float) Math.round(yc1[7] * 10000) / 100 + "% " +
                "\nSSS : " + (float) Math.round(yc1[8] * 10000) / 100 + "%";
        binding.includeA.textView.setText(detail);
    }

    public void countSkillIntensity() {

        float p = (float) Math.random();
        /**機率*/


        float total = 0;
        for (int i = 0; i < yc1.length; i++) {
            total = yc1[i] + total;
        }
        Log.d("total", String.valueOf(total));

        float[] yc = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
        for (int i = 0; i < yc1.length; i++) {
            for (int j = 0; j <= i; j++) {

                yc[i] = yc[i] + yc1[j];
                yc[i] = ((float) Math.round(yc[i] * 10000)) / 10000;
            }
//            System.out.println(yc[i]);
        }


        Log.d("p", String.valueOf(p));

        if (p > yc[8]) {//SSS
            atkRAndHp(9, 20);
            setRandomMP((int) (Math.random() * 5) + 6);//產生1-10
            Log.d("卡片等級", "SSS");
        } else if (p > yc[7]) {//SS
            atkRAndHp(8, 18);
            setRandomMP((int) (Math.random() * 2) + 9);//產生1-10
            Log.d("卡片等級", "SS");
        } else if (p > yc[6]) {//S
            atkRAndHp(7, 14);
            setRandomMP((int) (Math.random() * 3) + 8);//產生1-10
            Log.d("卡片等級", "S");
        } else if (p > yc[5]) {//A
            atkRAndHp(6, 12);
            setRandomMP((int) (Math.random() * 5) + 6);//產生1-10
            Log.d("卡片等級", "A");
        } else if (p > yc[4]) {//B
            atkRAndHp(5, 10);
            setRandomMP((int) (Math.random() * 8) + 3);//產生1-10
            Log.d("卡片等級", "B");
        } else if (p > yc[3]) {//C
            atkRAndHp(4, 8);
            setRandomMP((int) (Math.random() * 9) + 2);//產生1-10
            Log.d("卡片等級", "C");
        } else if (p > yc[2]) {//D
            atkRAndHp(3, 7);
            setRandomMP((int) (Math.random() * 8) + 3);//產生1-10
            Log.d("卡片等級", "D");
        } else if (p > yc[1]) {//E
            atkRAndHp(2, 6);
            setRandomMP((int) (Math.random() * 9) + 2);//產生1-10
            Log.d("卡片等級", "E");
        } else if (p > yc[0]) {//F
            atkRAndHp(1, 7);
            setRandomMP((int) (Math.random() * 5) + 6);//產生1-10
            Log.d("卡片等級", "F");
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
        Log.d("AddCard", String.valueOf(randomAtkR));
        setRandomAtkR(randomAtkR);

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

//    int[] mm = {0, 10, 12, 16, 20, 25, 30, 50, 60, 100};

    public String judgingSkillIntensity(int randomHP, int randomMP, ArrayList<Integer> randomAtkR) {
        String abcd = null;
        int j = randomHP * randomAtkR.size();
        if (j <= mm[1]) {//0 - 10%
            abcd = "F";
        } else if (j <= mm[2]) {//10 - 25%
            abcd = "E";
        } else if (j <= mm[3]) {//10 - 25%
            abcd = "D";
        } else if (j <= mm[4]) {//25 - 50 %
            abcd = "C";
        } else if (j <= mm[5]) {//50 - 75%
            abcd = "B";
        } else if (j <= mm[6]) {//75 - 90%
            abcd = "A";
        } else if (j <= mm[7]) {//90 - 95%
            abcd = "S";
        } else if (j <= mm[8]) {//95 - 100%
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

    int use = 1;

    public void common() {
        binding.buttonDown.setEnabled(true);
        binding.buttonUp.setEnabled(true);
        if (use == 1) {
            binding.buttonDown.setEnabled(false);
        }
        if (use == power) {
            binding.buttonUp.setEnabled(false);
        }
    }

    //    int[] useJ = {1,5,10,20,50,100}
    public void up(View v) {
        use++;
        common();
        useUp();
        binding.shop.setText("花費 " + use + " 素材");
    }

    public void down(View v) {
        use--;
        common();
        useUp();
        binding.shop.setText("花費 " + use + " 素材");
    }
}