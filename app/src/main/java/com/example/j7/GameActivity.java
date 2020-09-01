package com.example.j7;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.j7.databinding.GameActivityMainBinding;
import com.example.j7.game.AtkDecide;
import com.example.j7.game.AtkRules;
import com.example.j7.game.ConnectManager;
import com.example.j7.game.MoveRules;
import com.example.j7.game.UpRules;
import com.example.j7.tools.Tools;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.sql.SQLOutput;
import java.util.ArrayList;

import static com.example.j7.LoginActivity.TSUserId;
import static com.example.j7.LoginActivity.userId;

public class GameActivity extends AppCompatActivity {
    /**0626讓冠宇看懂的連線版本 */
    /**
     * 0627 :
     * 1.增加按鈕可以重新連線 initConnect
     * 0704 :
     * 1.分類
     */
    private TextView txt_self_name, txt_com_name;
    private Button btnInitStart, btnInitConnect;
    public ImageView imagePlayer, imageCom;
    private EditText roomEditText;
    public int step;
    ConnectManager connectManager = new ConnectManager(this);
    public MoveRules moveRules = new MoveRules(this);
    public AtkRules atkRules = new AtkRules(this);
    public UpRules upRules = new UpRules(this);
    public AtkDecide atkDecide = new AtkDecide();
    Tools tools = new Tools();
    public StartActivity startActivity = new StartActivity();


    public int locationXSelf = 0;
    public int locationYSelf = 1;
    public int locationXCom = 4;
    public int locationYCom = 1;
    private View lineX0, lineX1, lineX2, lineX3, lineX4, lineY0, lineY1, lineY2, lineY3, lineY4;
    public View atkKJ00, atkKJ10, atkKJ20, atkKJ30, atkKJ40;
    public View atkKJ01, atkKJ11, atkKJ21, atkKJ31, atkKJ41;
    public View atkKJ02, atkKJ12, atkKJ22, atkKJ32, atkKJ42;
    public Button initGame;
    public TextView txt_self_hp, txt_self_mp, txt_com_hp, txt_com_mp, upHP, upMP;
    public View button, button2, button3, button4, button5, button6;
    public Button buttonAtk1, buttonAtk2, buttonAtk3, buttonAtk4, buttonAtk5, buttonAtk6;
    public Button buttonTools1, buttonTools2;

    public View[] locationX;
    public View[] locationY;
    public View includeAtk, includeMove;
    public View includeAtk1, includeAtk2, includeAtk3, includeAtk4, includeAtk5, includeAtk6;
    public View line11, line12, line13, line14, line15, line16, line17, line18, line19;
    public View line21, line22, line23, line24, line25, line26, line27, line28, line29;
    public View line31, line32, line33, line34, line35, line36, line37, line38, line39;
    public View line41, line42, line43, line44, line45, line46, line47, line48, line49;
    public View line51, line52, line53, line54, line55, line56, line57, line58, line59;
    public TextView HP1, HP2, HP3, HP4, HP5, HP6;
    public TextView MP1, MP2, MP3, MP4, MP5, MP6;
    public String roomKey;
    String player;
    String otherPlayer;

    String playerName;
    String otherPlayerName;
    int indexE;
    public String userName = "";
    public DatabaseReference fullRoom;
    int upHPInt;
    int upMPInt;
    ArrayList<Integer> finalHP = null;
    ArrayList<Integer> finalMP = null;
    ArrayList<ArrayList<Integer>> finalAtlR = null;

    public GameActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("---------遊戲開始---------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.game_activity_main);

        findView(); //findView()
        intent(); //從上一頁告知我 roomKey player otherPlayer

        fullRoom = FirebaseDatabase.getInstance().getReference("rooms").child(roomKey);//FirebaseDatabase
        locationX = new View[]{lineX0, lineX1, lineX2, lineX3, lineX4};
        locationY = new View[]{lineY0, lineY1, lineY2};


        upText();

        fullRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //告訴我兩個人選的角色
                long x1 = (long) snapshot.child(player).child("Index").getValue();
                long x2 = (long) snapshot.child(otherPlayer).child("Index").getValue();
                //告訴我兩個人選的角色的圖片
                int role1 = tools.roleChangePicture((int) x1);
                int role2 = tools.roleChangePicture((int) x2);
                //換圖
                imagePlayer.setImageResource(role1);
                imageCom.setImageResource(role2);
                //翻轉高雄
                imageCom.setScaleX(-1);
                //告訴我兩個人的名字
                txt_self_name.setText(snapshot.child(player).child("name").getValue().toString());
                txt_com_name.setText(snapshot.child(otherPlayer).child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        binding.includeAtkE.includeAtk1.buttonAtk1.setEnabled(false);
        binding.includeAtkE.includeAtk2.buttonAtk2.setEnabled(false);
        binding.includeAtkE.includeAtk3.buttonAtk3.setEnabled(false);
        binding.includeAtkE.includeAtk4.buttonAtk4.setEnabled(false);
        binding.includeAtkE.includeAtk5.buttonAtk5.setEnabled(false);
        binding.includeAtkE.includeAtk6.buttonAtk6.setEnabled(false);

        //繪製圖片
//        imagePlayer.layout(locationX[locationXSelf].getLeft() + 30, locationY[locationYSelf].getTop() - 200, locationX[locationXSelf].getLeft() + 100 + 30, locationY[locationYSelf].getBottom());
//        imageCom.layout(locationX[locationXCom].getLeft() + 30, locationY[locationYCom].getTop() - 200, locationX[locationXCom].getLeft() + 100 + 30, locationY[locationYCom].getBottom());
    }


    public void MPUse() {
        fullRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long MP1 = (long) snapshot.child("player1").child("MP").getValue();
                long MPUse1 = (long) snapshot.child("player1").child("Next").child("atkMP").getValue();

                long MP2 = (long) snapshot.child("player2").child("MP").getValue();
                long MPUse2 = (long) snapshot.child("player2").child("Next").child("atkMP").getValue();

//                System.out.println("player1" + " 魔力 " + MP1 + " - " + MPUse1 + " = " + ((int) MP1 - (int) MPUse1));
//                System.out.println("player2" + " 魔力 " + MP2 + " - " + MPUse2 + " = " + ((int) MP2 - (int) MPUse2));
                fullRoom.child("player1").child("MP").setValue((int) MP1 - (int) MPUse1);
                fullRoom.child("player2").child("MP").setValue((int) MP2 - (int) MPUse2);
                fullRoom.child("player1").child("Next").child("atkMP").setValue(0);
                fullRoom.child("player2").child("Next").child("atkMP").setValue(0);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void upText() {
        upHP = findViewById(R.id.upHP);
        upMP = findViewById(R.id.upMP);
        /**回血回魔的量*/

        fullRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long index = (long) snapshot.child(player).child("Index").getValue();
                if (tools.roleChange((int) index).equals("b74")) { // b74
                    upHP.setText("4");
                    upHPInt = 4;
                } else {
                    upHP.setText("2");
                    upHPInt = 2;
                }

                if (tools.roleChange((int) index).equals("fs")) {
                    upMP.setText("6");
                    upMPInt = 6;
                } else {
                    upMP.setText("4");
                    upMPInt = 4;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private ValueEventListener upListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.getValue() == null)
                return;
            txt_self_hp.setText(snapshot.child(player).child("HP").getValue().toString());
            txt_self_mp.setText(snapshot.child(player).child("MP").getValue().toString());
            txt_com_hp.setText(snapshot.child(otherPlayer).child("HP").getValue().toString());
            txt_com_mp.setText(snapshot.child(otherPlayer).child("MP").getValue().toString());
            long x1 = (long) snapshot.child(player).child("X").getValue();
            long y1 = (long) snapshot.child(player).child("Y").getValue();
            long x2 = (long) snapshot.child(otherPlayer).child("X").getValue();
            long y2 = (long) snapshot.child(otherPlayer).child("Y").getValue();
            moveRules.moveJudgmentSelf((int) x1, (int) y1);
            moveRules.moveJudgmentCom((int) x2, (int) y2);


            int selfMP = Integer.parseInt(String.valueOf(snapshot.child(player).child("MP").getValue()));
            int selfHP = Integer.parseInt(String.valueOf(snapshot.child(player).child("HP").getValue()));
            int comHP = Integer.parseInt(String.valueOf(snapshot.child(otherPlayer).child("HP").getValue()));
            MPLimit(selfMP, Integer.parseInt(String.valueOf(finalMP.get(0))), buttonAtk1);
            MPLimit(selfMP, Integer.parseInt(String.valueOf(finalMP.get(1))), buttonAtk2);
            MPLimit(selfMP, Integer.parseInt(String.valueOf(finalMP.get(2))), buttonAtk3);
            MPLimit(selfMP, Integer.parseInt(String.valueOf(finalMP.get(3))), buttonAtk4);
            MPLimit(selfMP, Integer.parseInt(String.valueOf(finalMP.get(4))), buttonAtk5);
            gameEnd(selfHP, comHP);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };

    public void MPLimit(int selfMP, int atkKindNum, View buttonX) {
        if (selfMP < atkKindNum) {
            buttonX.setBackgroundColor(Color.parseColor("#e0000000"));
            buttonX.setEnabled(false);
        } else {
            buttonX.setBackgroundColor(Color.parseColor("#00000000"));
            buttonX.setEnabled(true);
        }
    }


    private ValueEventListener statusListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.getValue() == null)
                return;
            long x = (long) snapshot.getValue();
            if ((int) x == turn) {
                fullRoom.child("fourStatus").setValue(0);
                receiveMessage();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };

    public void gameEnd(int selfHP, int comHP) {
        if (selfHP <= 0) {
            initGame.setVisibility(View.VISIBLE);
            initGame.setText("對手太強");
            lockBtn();
        }
        if (comHP <= 0) {
            initGame.setVisibility(View.VISIBLE);
            initGame.setText("獲得勝利！");
            DatabaseReference level =  FirebaseDatabase.getInstance().getReference("users").child(TSUserId).child("level");
            level.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    DatabaseReference level =  FirebaseDatabase.getInstance().getReference("users").child(TSUserId).child("level");
                    level.setValue((int)snapshot.getValue() + 5);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            lockBtn();
        }
        if (selfHP <= 0 && comHP <= 0) {
            initGame.setVisibility(View.VISIBLE);
            initGame.setText("平手");
            lockBtn();
        }
        if (selfHP > 0 && comHP > 0) {
            initGame.setVisibility(View.INVISIBLE);
//            openBtn();
        }
    }


    public void gameEnd() {
        if (Integer.parseInt(txt_self_hp.getText().toString()) <= 0 || Integer.parseInt(txt_com_hp.getText().toString()) <= 0) {
            initGame.setVisibility(View.VISIBLE);
//            Toast.makeText(this, "遊戲結束", Toast.LENGTH_LONG).show();
            lockBtn();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        buttonAtk6.setText("站著");
        openBtnAtk();

        fullRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long x = (long) snapshot.child(player).child("Index").getValue();
                int index = Integer.parseInt(String.valueOf(x));

                switch (tools.roleChange(index)) {
                    case "j4":
                        buttonAtk5.setText("續力");
                        break;
                    case "fs":
                        buttonAtk5.setText("末日");
                        break;
                    case "player":
                        buttonAtk5.setText("尚未開發");
                        break;
                    case "b74":
                        buttonAtk5.setText("隔檔");
                        break;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fullRoom.addValueEventListener(upListener);//監聽
        fullRoom.child("fourStatus").addValueEventListener(statusListener);//監聽
    }

    int index;

    private void intent() {
        Intent it = getIntent();
//        finalHP = new ArrayList<>(it.getIntegerArrayListExtra("finalHP"));
//        finalMP = new ArrayList<>(it.getIntegerArrayListExtra("finalMP"));
        roomKey = it.getStringExtra("roomKey");
        index = it.getIntExtra("index", 0);


        FirebaseDatabase.getInstance().getReference("users").child(userId).child("role").child(tools.roleChange(index)).child("atkR").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("finalAtlR", String.valueOf((ArrayList<ArrayList<Integer>>) snapshot.getValue()));
                finalAtlR = (ArrayList<ArrayList<Integer>>) snapshot.getValue();
                startActivity.atkDraw((ArrayList<ArrayList<Integer>>) snapshot.getValue(), binding.includeAtk.includeAtk1.line11, binding.includeAtk.includeAtk1.line12, binding.includeAtk.includeAtk1.line13, binding.includeAtk.includeAtk1.line14, binding.includeAtk.includeAtk1.line15, binding.includeAtk.includeAtk1.line16, binding.includeAtk.includeAtk1.line17, binding.includeAtk.includeAtk1.line18, binding.includeAtk.includeAtk1.line19
                        , binding.includeAtk.includeAtk2.line21, binding.includeAtk.includeAtk2.line22, binding.includeAtk.includeAtk2.line23, binding.includeAtk.includeAtk2.line24, binding.includeAtk.includeAtk2.line25, binding.includeAtk.includeAtk2.line26, binding.includeAtk.includeAtk2.line27, binding.includeAtk.includeAtk2.line28, binding.includeAtk.includeAtk2.line29
                        , binding.includeAtk.includeAtk3.line31, binding.includeAtk.includeAtk3.line32, binding.includeAtk.includeAtk3.line33, binding.includeAtk.includeAtk3.line34, binding.includeAtk.includeAtk3.line35, binding.includeAtk.includeAtk3.line36, binding.includeAtk.includeAtk3.line37, binding.includeAtk.includeAtk3.line38, binding.includeAtk.includeAtk3.line39
                        , binding.includeAtk.includeAtk4.line41, binding.includeAtk.includeAtk4.line42, binding.includeAtk.includeAtk4.line43, binding.includeAtk.includeAtk4.line44, binding.includeAtk.includeAtk4.line45, binding.includeAtk.includeAtk4.line46, binding.includeAtk.includeAtk4.line47, binding.includeAtk.includeAtk4.line48, binding.includeAtk.includeAtk4.line49);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        FirebaseDatabase.getInstance().getReference("users").child(userId).child("role").child(tools.roleChange(index)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                finalHP = (ArrayList<Integer>)snapshot.child("HP").getValue();
                finalMP = (ArrayList<Integer>)snapshot.child("MP").getValue();

                startActivity.atkDrawHPMP((ArrayList<Integer>)snapshot.child("HP").getValue(),(ArrayList<Integer>)snapshot.child("MP").getValue()
                        ,binding.includeAtk.HP1,binding.includeAtk.HP2,binding.includeAtk.HP3,binding.includeAtk.HP4,binding.includeAtk.HP5
                        ,binding.includeAtk.MP1,binding.includeAtk.MP2,binding.includeAtk.MP3,binding.includeAtk.MP4,binding.includeAtk.MP5);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        TextView roomNum = findViewById(R.id.roomNum);
        roomNum.setText(roomKey);

        player = it.getStringExtra("player");
        switch (player) {
            case "player1":
                otherPlayer = "player2";
                break;
            case "player2":
                otherPlayer = "player1";
                break;
        }

        FirebaseDatabase.getInstance().getReference("rooms").child(roomKey).child(otherPlayer).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                otherPlayerName = (String) snapshot.child("name").getValue();
                indexE = Integer.parseInt(String.valueOf(snapshot.child("Index").getValue()));
                Log.d("TAG", "敵人名稱為 : "  +otherPlayerName);
                Log.d("TAG", "敵人角色為 : " + tools.roleChange(indexE));

                FirebaseDatabase.getInstance().getReference("users").child(otherPlayerName).child("role").child(tools.roleChange(indexE)).child("atkR").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("TAG", String.valueOf((ArrayList<ArrayList<Integer>>) snapshot.getValue()));
                        binding.includeAtkE.includeAtk1.buttonAtk1.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.includeAtkE.includeAtk2.buttonAtk2.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.includeAtkE.includeAtk3.buttonAtk3.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.includeAtkE.includeAtk4.buttonAtk4.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.includeAtkE.includeAtk5.buttonAtk5.setBackgroundColor(Color.parseColor("#00000000"));
                        startActivity.atkDraw((ArrayList<ArrayList<Integer>>) snapshot.getValue(), binding.includeAtkE.includeAtk1.line11, binding.includeAtkE.includeAtk1.line12, binding.includeAtkE.includeAtk1.line13, binding.includeAtkE.includeAtk1.line14, binding.includeAtkE.includeAtk1.line15, binding.includeAtkE.includeAtk1.line16, binding.includeAtkE.includeAtk1.line17, binding.includeAtkE.includeAtk1.line18, binding.includeAtkE.includeAtk1.line19
                                , binding.includeAtkE.includeAtk2.line21, binding.includeAtkE.includeAtk2.line22, binding.includeAtkE.includeAtk2.line23, binding.includeAtkE.includeAtk2.line24, binding.includeAtkE.includeAtk2.line25, binding.includeAtkE.includeAtk2.line26, binding.includeAtkE.includeAtk2.line27, binding.includeAtkE.includeAtk2.line28, binding.includeAtkE.includeAtk2.line29
                                , binding.includeAtkE.includeAtk3.line31, binding.includeAtkE.includeAtk3.line32, binding.includeAtkE.includeAtk3.line33, binding.includeAtkE.includeAtk3.line34, binding.includeAtkE.includeAtk3.line35, binding.includeAtkE.includeAtk3.line36, binding.includeAtkE.includeAtk3.line37, binding.includeAtkE.includeAtk3.line38, binding.includeAtkE.includeAtk3.line39
                                , binding.includeAtkE.includeAtk4.line41, binding.includeAtkE.includeAtk4.line42, binding.includeAtkE.includeAtk4.line43, binding.includeAtkE.includeAtk4.line44, binding.includeAtkE.includeAtk4.line45, binding.includeAtkE.includeAtk4.line46, binding.includeAtkE.includeAtk4.line47, binding.includeAtkE.includeAtk4.line48, binding.includeAtkE.includeAtk4.line49);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                FirebaseDatabase.getInstance().getReference("users").child(otherPlayerName).child("role").child(tools.roleChange(indexE)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("TAG", String.valueOf(snapshot.child("HP").getValue()));
                        startActivity.atkDrawHPMP((ArrayList<Integer>)snapshot.child("HP").getValue(),(ArrayList<Integer>)snapshot.child("MP").getValue()
                                ,binding.includeAtkE.HP1,binding.includeAtkE.HP2,binding.includeAtkE.HP3,binding.includeAtkE.HP4,binding.includeAtkE.HP5
                                ,binding.includeAtkE.MP1,binding.includeAtkE.MP2,binding.includeAtkE.MP3,binding.includeAtkE.MP4,binding.includeAtkE.MP5);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    private void findView() {

        roomEditText = findViewById(R.id.roomEditText);
        imageCom = findViewById(R.id.image_com);
        imagePlayer = findViewById(R.id.image_player);


        buttonTools1 = findViewById(R.id.buttonTools1);
        buttonTools2 = findViewById(R.id.buttonTools2);


        lineX0 = findViewById(R.id.lineX0);
        lineX1 = findViewById(R.id.lineX1);
        lineX2 = findViewById(R.id.lineX2);
        lineX3 = findViewById(R.id.lineX3);
        lineX4 = findViewById(R.id.lineX4);
        lineY0 = findViewById(R.id.lineY0);
        lineY1 = findViewById(R.id.lineY1);
        lineY2 = findViewById(R.id.lineY2);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        buttonAtk1 = findViewById(R.id.buttonAtk1);
        buttonAtk2 = findViewById(R.id.buttonAtk2);
        buttonAtk3 = findViewById(R.id.buttonAtk3);
        buttonAtk4 = findViewById(R.id.buttonAtk4);
        buttonAtk5 = findViewById(R.id.buttonAtk5);
        buttonAtk6 = findViewById(R.id.buttonAtk6);

        txt_self_name = findViewById(R.id.txt_self_name);
        txt_com_name = findViewById(R.id.txt_com_name);
        txt_self_hp = findViewById(R.id.txt_self_hp);
        txt_self_mp = findViewById(R.id.txt_self_mp);
        txt_com_hp = findViewById(R.id.txt_com_hp);
        txt_com_mp = findViewById(R.id.txt_com_mp);
        atkKJ00 = findViewById(R.id.atkKJ00);
        atkKJ10 = findViewById(R.id.atkKJ10);
        atkKJ20 = findViewById(R.id.atkKJ20);
        atkKJ30 = findViewById(R.id.atkKJ30);
        atkKJ40 = findViewById(R.id.atkKJ40);
        atkKJ01 = findViewById(R.id.atkKJ01);
        atkKJ11 = findViewById(R.id.atkKJ11);
        atkKJ21 = findViewById(R.id.atkKJ21);
        atkKJ31 = findViewById(R.id.atkKJ31);
        atkKJ41 = findViewById(R.id.atkKJ41);
        atkKJ02 = findViewById(R.id.atkKJ02);
        atkKJ12 = findViewById(R.id.atkKJ12);
        atkKJ22 = findViewById(R.id.atkKJ22);
        atkKJ32 = findViewById(R.id.atkKJ32);
        atkKJ42 = findViewById(R.id.atkKJ42);
        initGame = findViewById(R.id.initGame);
        initGame.setVisibility(View.INVISIBLE);

        fireVisibile();//創建遊戲

        includeMove = findViewById(R.id.includeMove);
        includeAtk = findViewById(R.id.includeAtk);
        includeAtk.setVisibility(View.INVISIBLE);


        includeAtk1 = findViewById(R.id.includeAtk1);
        includeAtk2 = findViewById(R.id.includeAtk2);
        includeAtk3 = findViewById(R.id.includeAtk3);
        includeAtk4 = findViewById(R.id.includeAtk4);
        includeAtk5 = findViewById(R.id.includeAtk5);
        includeAtk6 = findViewById(R.id.includeAtk6);

        line11 = findViewById(R.id.line11);
        line12 = findViewById(R.id.line12);
        line13 = findViewById(R.id.line13);
        line14 = findViewById(R.id.line14);
        line15 = findViewById(R.id.line15);
        line16 = findViewById(R.id.line16);
        line17 = findViewById(R.id.line17);
        line18 = findViewById(R.id.line18);
        line19 = findViewById(R.id.line19);

        line21 = findViewById(R.id.line21);
        line22 = findViewById(R.id.line22);
        line23 = findViewById(R.id.line23);
        line24 = findViewById(R.id.line24);
        line25 = findViewById(R.id.line25);
        line26 = findViewById(R.id.line26);
        line27 = findViewById(R.id.line27);
        line28 = findViewById(R.id.line28);
        line29 = findViewById(R.id.line29);

        line31 = findViewById(R.id.line31);
        line32 = findViewById(R.id.line32);
        line33 = findViewById(R.id.line33);
        line34 = findViewById(R.id.line34);
        line35 = findViewById(R.id.line35);
        line36 = findViewById(R.id.line36);
        line37 = findViewById(R.id.line37);
        line38 = findViewById(R.id.line38);
        line39 = findViewById(R.id.line39);

        line41 = findViewById(R.id.line41);
        line42 = findViewById(R.id.line42);
        line43 = findViewById(R.id.line43);
        line44 = findViewById(R.id.line44);
        line45 = findViewById(R.id.line45);
        line46 = findViewById(R.id.line46);
        line47 = findViewById(R.id.line47);
        line48 = findViewById(R.id.line48);
        line49 = findViewById(R.id.line49);

//        line51 = findViewById(R.id.line51);
//        line52 = findViewById(R.id.line52);
//        line53 = findViewById(R.id.line53);
//        line54 = findViewById(R.id.line54);
//        line55 = findViewById(R.id.line55);
//        line56 = findViewById(R.id.line56);
//        line57 = findViewById(R.id.line57);
//        line58 = findViewById(R.id.line58);
//        line59 = findViewById(R.id.line59);

        HP1 = findViewById(R.id.HP1);
        HP2 = findViewById(R.id.HP2);
        HP3 = findViewById(R.id.HP3);
        HP4 = findViewById(R.id.HP4);
        HP5 = findViewById(R.id.HP5);
        HP6 = findViewById(R.id.HP6);
        MP1 = findViewById(R.id.MP1);
        MP2 = findViewById(R.id.MP2);
        MP3 = findViewById(R.id.MP3);
        MP4 = findViewById(R.id.MP4);
        MP5 = findViewById(R.id.MP5);
        MP6 = findViewById(R.id.MP6);

    }


    public void atkVisible() {
        includeMove.setVisibility(View.INVISIBLE);
        includeAtk.setVisibility(View.VISIBLE);
    }

    public void moveVisible() {
        includeMove.setVisibility(View.VISIBLE);
        includeAtk.setVisibility(View.INVISIBLE);
    }

    public void fireVisibile() {
        atkKJ00.setVisibility(View.INVISIBLE);
        atkKJ10.setVisibility(View.INVISIBLE);
        atkKJ20.setVisibility(View.INVISIBLE);
        atkKJ30.setVisibility(View.INVISIBLE);
        atkKJ40.setVisibility(View.INVISIBLE);
        atkKJ01.setVisibility(View.INVISIBLE);
        atkKJ11.setVisibility(View.INVISIBLE);
        atkKJ21.setVisibility(View.INVISIBLE);
        atkKJ31.setVisibility(View.INVISIBLE);
        atkKJ41.setVisibility(View.INVISIBLE);
        atkKJ02.setVisibility(View.INVISIBLE);
        atkKJ12.setVisibility(View.INVISIBLE);
        atkKJ22.setVisibility(View.INVISIBLE);
        atkKJ32.setVisibility(View.INVISIBLE);
        atkKJ42.setVisibility(View.INVISIBLE);

//        lineX0.setVisibility(View.INVISIBLE);
//        lineX1.setVisibility(View.INVISIBLE);
//        lineX2.setVisibility(View.INVISIBLE);
//        lineX3.setVisibility(View.INVISIBLE);
//        lineX4.setVisibility(View.INVISIBLE);
        lineY0.setVisibility(View.INVISIBLE);
//        lineY1.setVisibility(View.INVISIBLE);
//        lineY2.setVisibility(View.INVISIBLE);


    }

    public void lockBtn() {
        button.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        button4.setEnabled(false);
        button5.setEnabled(false);
        button6.setEnabled(false);
        buttonTools1.setEnabled(false);
        buttonTools2.setEnabled(false);
        button.setBackgroundColor(Color.parseColor("#e0000000"));
        button2.setBackgroundColor(Color.parseColor("#e0000000"));
        button3.setBackgroundColor(Color.parseColor("#e0000000"));
        button4.setBackgroundColor(Color.parseColor("#e0000000"));
        button5.setBackgroundColor(Color.parseColor("#e0000000"));
        button6.setBackgroundColor(Color.parseColor("#e0000000"));
//        button.setAlpha(0.2f);
//        button2.setAlpha(0.2f);
//        button3.setAlpha(0.2f);
//        button4.setAlpha(0.2f);
//        button5.setAlpha(0.2f);
//        button6.setAlpha(0.2f);
    }

    public void openBtn() {
        button.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        button4.setEnabled(true);
        button5.setEnabled(true);
        button6.setEnabled(true);
        buttonTools1.setEnabled(true);
        buttonTools2.setEnabled(true);
        button.setBackgroundColor(Color.parseColor("#00000000"));
        button2.setBackgroundColor(Color.parseColor("#00000000"));
        button3.setBackgroundColor(Color.parseColor("#00000000"));
        button4.setBackgroundColor(Color.parseColor("#00000000"));
        button5.setBackgroundColor(Color.parseColor("#00000000"));
        button6.setBackgroundColor(Color.parseColor("#00000000"));
    }

    public void lockBtnAtk() {
        buttonAtk1.setEnabled(false);
        buttonAtk2.setEnabled(false);
        buttonAtk3.setEnabled(false);
        buttonAtk4.setEnabled(false);
        buttonAtk5.setEnabled(false);
        buttonAtk5.setEnabled(false);
        buttonAtk6.setEnabled(false);
        buttonAtk1.setBackgroundColor(Color.parseColor("#e0000000"));
        buttonAtk2.setBackgroundColor(Color.parseColor("#e0000000"));
        buttonAtk3.setBackgroundColor(Color.parseColor("#e0000000"));
        buttonAtk4.setBackgroundColor(Color.parseColor("#e0000000"));
        buttonAtk5.setBackgroundColor(Color.parseColor("#e0000000"));
        buttonAtk6.setBackgroundColor(Color.parseColor("#e0000000"));

    }

    public void openBtnAtk() {
        buttonAtk1.setEnabled(true);
        buttonAtk2.setEnabled(true);
        buttonAtk3.setEnabled(true);
        buttonAtk4.setEnabled(true);
        buttonAtk5.setEnabled(true);
        buttonAtk6.setEnabled(true);
        buttonAtk1.setBackgroundColor(Color.parseColor("#00000000"));
        buttonAtk2.setBackgroundColor(Color.parseColor("#00000000"));
        buttonAtk3.setBackgroundColor(Color.parseColor("#00000000"));
        buttonAtk4.setBackgroundColor(Color.parseColor("#00000000"));
        buttonAtk5.setBackgroundColor(Color.parseColor("#00000000"));
        buttonAtk6.setBackgroundColor(Color.parseColor("#00000000"));
    }


    public void moveRight(View view) {
        moveRules.moveCommon(1, 0);
    }

    public void moveLeft(View view) {
        moveRules.moveCommon(-1, 0);
    }

    public void moveTop(View view) {
        moveRules.moveCommon(0, 1);
    }

    public void moveDown(View view) {
        moveRules.moveCommon(0, -1);
    }

    public void tool1(View view) {
        sendMessageUp("hp", upHPInt);
    }

    public void tool2(View view) {
        sendMessageUp("mp", upMPInt);
    }

    public void controlMPHP(TextView PP, int add) {
        int MpBefore = Integer.parseInt(PP.getText().toString());
        int MpAfter = MpBefore + add;
        PP.setText(MpAfter + "");
        if (MpAfter >= 10) {
            PP.setText(10 + "");
        }
    }


    public void statusUp() {
        fullRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long x = (long) snapshot.child("fourStatus").getValue();
                fullRoom.child("fourStatus").setValue((int) x + 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * 回合結束觸發
     * 1.增加雙發完家的MP以及HP
     * 2.判斷攻擊技能是否可以使用
     * 3.判斷遊戲是否結束
     * 4.把攻擊範圍初始化
     */
    public void init() {
        openBtn();
        openBtnAtk();
        gameEnd();
        atkRules.initAtkRange();
    }


//    public void initGame1() {
//        initGame.setVisibility(View.INVISIBLE);
//        txt_self_hp.setText("10");
//        txt_com_hp.setText("10");
//        txt_self_mp.setText("10");
//        txt_com_mp.setText("10");
//        locationXSelf = 0;
//        locationYSelf = 1;
//        locationXCom = 4;
//        locationYCom = 1;
//        moveVisible();
//        fireVisibile();//重開新局 消滅地圖火焰
//
//        openBtn();
//        openBtnAtk();
//        imagePlayer.layout(locationX[locationXSelf].getLeft() + 30, locationY[locationYSelf].getTop() - 200, locationX[locationXSelf].getLeft() + 100 + 30, locationY[locationYSelf].getBottom());
//        imageCom.layout(locationX[locationXCom].getLeft() + 30, locationY[locationYCom].getTop() - 200, locationX[locationXCom].getLeft() + 100 + 30, locationY[locationYCom].getBottom());
//
//    }

    /**
     * 重新開始 初始化遊戲
     */
    public void initGame(View v) {
//        initGame1();
        Intent it = new Intent(this, StartActivity.class);
        startActivity(it);
    }


    public void sendMessageMove() {
        atkVisible();
        lockBtn();
        statusUp();
        Toast.makeText(this, "移動", Toast.LENGTH_SHORT).show();
        fullRoom.child(player).child("Next").child("locationXSelf").setValue(locationXSelf);
        fullRoom.child(player).child("Next").child("locationYSelf").setValue(locationYSelf);
//        connectManager.sendMessage(0, locationXSelf, locationYSelf, "move");

    }

    public void sendMessageUp(String HPMP, int up) {
        atkVisible();
        statusUp();
        Toast.makeText(this, "回復", Toast.LENGTH_SHORT).show();
        switch (HPMP) {
            case "hp":
                fullRoom.child(player).child("Next").child("HPUP").setValue(up);
                break;
            case "mp":
                fullRoom.child(player).child("Next").child("MPUP").setValue(up);
                break;
        }
    }


    public void sendMessageMoveAtk(ArrayList<Integer> atk, int hp, int mp) {
        statusUp();
        fullRoom.child(player).child("Next").child("atkR").setValue(atk);
        fullRoom.child(player).child("Next").child("atkHP").setValue(hp);
        fullRoom.child(player).child("Next").child("atkMP").setValue(mp);
    }


    public void atk1(View v) {
        lockBtnAtk();
        moveVisible();
        lockBtn();
        int a = Integer.parseInt(String.valueOf(finalHP.get(0)));
        int b = Integer.parseInt(String.valueOf(finalMP.get(0)));
        sendMessageMoveAtk(finalAtlR.get(0), a, b);
    }

    public void atk2(View v) {
        lockBtnAtk();
        moveVisible();
        lockBtn();
        int a = Integer.parseInt(String.valueOf(finalHP.get(1)));
        int b = Integer.parseInt(String.valueOf(finalMP.get(1)));
        sendMessageMoveAtk(finalAtlR.get(1), a, b);
    }

    public void atk3(View v) {
        lockBtnAtk();
        moveVisible();
        lockBtn();
        int a = Integer.parseInt(String.valueOf(finalHP.get(2)));
        int b = Integer.parseInt(String.valueOf(finalMP.get(2)));
        sendMessageMoveAtk(finalAtlR.get(2), a, b);
    }

    public void atk4(View v) {
        lockBtnAtk();
        moveVisible();
        lockBtn();
        int a = Integer.parseInt(String.valueOf(finalHP.get(3)));
        int b = Integer.parseInt(String.valueOf(finalMP.get(3)));
        sendMessageMoveAtk(finalAtlR.get(3), a, b);
    }

    public void atk5(View v) {//變成特殊技能
        lockBtnAtk();
        moveVisible();
        lockBtn();
        fullRoom.child(player).child("unique").setValue(true);

        ArrayList<Integer> x = new ArrayList<>();
        x.add(-1);
        if (tools.roleChange(index).equals("fs")) {
            ArrayList<Integer> y = new ArrayList<>();
            for (int i = 1; i <= 9; i++) {
                y.add(i);
            }
            fullRoom.child(player).child("unique").setValue(false);
            sendMessageMoveAtk(y, 2, 5);
        } else {
            sendMessageMoveAtk(x, 0, 0);
        }

    }


    public void atk6(View v) {
        lockBtnAtk();
        moveVisible();
        lockBtn();
        ArrayList<Integer> x = new ArrayList<>();
        x.add(0);
        sendMessageMoveAtk(x, 0, 0);
    }

    int turn = 4;

    private void receiveMessage() {
        fireVisibile();//收到後 地板火焰先取消

        for (int i = 0; i < turn; i++) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("NUM", i);
            msg.setData(bundle);
            msg.what = 0;
            switch (i) {
                case 0:
                    handler.sendMessageDelayed(msg, 1000);
                    break;
                case 1:
                    handler.sendMessageDelayed(msg, 1050);
                    break;
                case 2:
                    handler.sendMessageDelayed(msg, 2000);
                    break;
                case 3:
                    handler.sendMessageDelayed(msg, 2050);
                    break;
            }

        }
    }


    private MyHandler handler = new MyHandler();

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int start = msg.getData().getInt("NUM");
            System.out.println("開始動作 : " + start);
            switch (start) {
                case 0:
                    fullRoom.child(player).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long xl = (long) snapshot.child("Next").child("locationXSelf").getValue();
                            long yl = (long) snapshot.child("Next").child("locationYSelf").getValue();
                            fullRoom.child(player).child("X").setValue((int) xl);
                            fullRoom.child(player).child("Y").setValue((int) yl);

                            long HPUP = (long) snapshot.child("Next").child("HPUP").getValue();
                            long MPUP = (long) snapshot.child("Next").child("MPUP").getValue();
                            long HP = (long) snapshot.child("HP").getValue();
                            long MP = (long) snapshot.child("MP").getValue();
                            if ((int) HPUP > 0) {
                                fullRoom.child(player).child("HP").setValue((int) HPUP + (int) HP);
                            }
                            if ((int) MPUP > 0) {
                                fullRoom.child(player).child("MP").setValue((int) MPUP + (int) MP);
                            }
                            fullRoom.child(player).child("Next").child("HPUP").setValue(0);
                            fullRoom.child(player).child("Next").child("MPUP").setValue(0);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case 2:
                    fullRoom.child(player).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            /**假如player自己是b74 則隔檔本次傷害*/
                            long index = (long) snapshot.child("Index").getValue();
                            Boolean aBoolean = (Boolean) snapshot.child("unique").getValue();

                            long j4MP = (long) snapshot.child("Next").child("atkMP").getValue();

                            if (tools.roleChange((int) index).equals("b74") && aBoolean) {
                                fullRoom.child(otherPlayer).child("Next").child("atkHP").setValue(0);
                                fullRoom.child(player).child("unique").setValue(false);
                            }


                            if (tools.roleChange((int) index).equals("j4") && aBoolean && ((int) j4MP != 0)) {
                                long j4atk = (long) snapshot.child("Next").child("atkHP").getValue();
                                fullRoom.child(player).child("Next").child("atkHP").setValue((int) j4atk * 2);
                                fullRoom.child(player).child("unique").setValue(false);
                            }


                            long HP = (long) snapshot.child("Next").child("atkHP").getValue();
                            long MP = (long) snapshot.child("Next").child("atkMP").getValue();
                            ArrayList<Integer> atkR = (ArrayList<Integer>) snapshot.child("Next").child("atkR").getValue();

                            atkRules.atkJudgmentSelf(atkR, (int) HP, (int) MP, player);


//                            MPUse(player, (int) MP);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    break;
                case 3:
                    fullRoom.child(otherPlayer).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long HP = (long) snapshot.child("Next").child("atkHP").getValue();
                            long MP = (long) snapshot.child("Next").child("atkMP").getValue();
                            ArrayList<Integer> atkR = (ArrayList<Integer>) snapshot.child("Next").child("atkR").getValue();
                            atkRules.atkJudgmentCom(atkR, (int) HP, (int) MP);
                            settlement();
                            moveVisible();
                            init();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    System.out.println("歸零");
                    break;
            }
        }
    }

//    public boolean fsUnique = false;
//
//    public void fsUnique() {
//        fullRoom.child(player).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Boolean aBoolean = (Boolean) snapshot.child("unique").getValue();
//                long Index = (long) snapshot.child("Index").getValue();
//                if (aBoolean && tools.roleChange((int)Index).equals("fs")) {
//                    fsUnique = true;
//                    fullRoom.child(player).child("unique").setValue(false);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    public void settlement() {
        MPUse();
    }


}