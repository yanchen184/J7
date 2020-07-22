package com.example.j7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.j7.R;
import com.example.j7.databinding.ActivityMainBinding;
import com.example.j7.databinding.User;
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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.WebSocket;

import static com.example.j7.LoginActivity.TSUserId;
import static com.example.j7.LoginActivity.userId;
import static com.example.j7.tools.Name.STATUS_INIT;

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
    /**
     * 命名用途 只有不同名字的人才可以連線成功　TODO
     */
    String[] text1 = {"預言家", "女巫", "獵人", "騎士", "守衛", "禁言長老",
            "魔術師", "通靈師", "熊", "白癡", "炸彈人", "守墓人", "九尾妖狐"};
    public String userName = text1[(int) (Math.random() * text1.length)] + (int) (Math.random() * 9999 + 1);

    public int locationXSelf = 0;
    public int locationYSelf = 1;
    public int locationXCom = 4;
    public int locationYCom = 1;
    private View lineX0, lineX1, lineX2, lineX3, lineX4, lineY0, lineY1, lineY2, lineY3, lineY4;
    public View atkKJ00, atkKJ10, atkKJ20, atkKJ30, atkKJ40;
    public View atkKJ01, atkKJ11, atkKJ21, atkKJ31, atkKJ41;
    public View atkKJ02, atkKJ12, atkKJ22, atkKJ32, atkKJ42;
    public Button initGame;
    public TextView txt_self_hp, txt_self_mp, txt_com_hp, txt_com_mp;
    public View button, button2, button3, button4, button5, button6;
    public View buttonAtk1, buttonAtk2, buttonAtk3, buttonAtk4, buttonAtk5, buttonAtk6;
    public String ip;

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
    String roomKey;
    String player;
    String otherPlayer;
    DatabaseReference fullRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("---------遊戲開始---------");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_main);

        connectManager.initiateSocketConnection();//連線
        findView(); //findView()
        intent(); //從上一頁告知我 roomKey player otherPlayer

        fullRoom = FirebaseDatabase.getInstance().getReference("rooms").child(roomKey);

        locationX = new View[]{lineX0, lineX1, lineX2, lineX3, lineX4};
        locationY = new View[]{lineY0, lineY1, lineY2};


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
                imageCom.setScaleX(-1);

                //告訴我兩個人的名字
                txt_self_name.setText(snapshot.child(player).child("name").getValue().toString());
                txt_com_name.setText(snapshot.child(otherPlayer).child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        atkRules.atkDraw();
        atkRules.atkDrawHPMP();

        openBtnAtk();

        //繪製圖片
//        imagePlayer.layout(locationX[locationXSelf].getLeft() + 30, locationY[locationYSelf].getTop() - 200, locationX[locationXSelf].getLeft() + 100 + 30, locationY[locationYSelf].getBottom());
//        imageCom.layout(locationX[locationXCom].getLeft() + 30, locationY[locationYCom].getTop() - 200, locationX[locationXCom].getLeft() + 100 + 30, locationY[locationYCom].getBottom());
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

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };




    private ValueEventListener statusListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.getValue() == null)
                return;
            long x = (long) snapshot.getValue();
            if ((int) x == 2) {
                fullRoom.child("fourStatus").setValue(0);
                receiveMessage();
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };






    @Override
    protected void onStart() {
        super.onStart();
        fullRoom.addValueEventListener(upListener);//監聽
        fullRoom.child("fourStatus").addValueEventListener(statusListener);//監聽
    }


    private void intent() {
        Intent it = getIntent();
        roomKey = it.getStringExtra("roomKey");
        player = it.getStringExtra("player");
        switch (player) {
            case "player1":
                otherPlayer = "player2";
                break;
            case "player2":
                otherPlayer = "player1";
                break;
        }
    }

    private void findView() {

        roomEditText = findViewById(R.id.roomEditText);
        btnInitStart = findViewById(R.id.initStart);
        btnInitConnect = findViewById(R.id.initConnect);
        imageCom = findViewById(R.id.image_com);
        imagePlayer = findViewById(R.id.image_player);

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

        visibility();//創建遊戲

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

        line51 = findViewById(R.id.line51);
        line52 = findViewById(R.id.line52);
        line53 = findViewById(R.id.line53);
        line54 = findViewById(R.id.line54);
        line55 = findViewById(R.id.line55);
        line56 = findViewById(R.id.line56);
        line57 = findViewById(R.id.line57);
        line58 = findViewById(R.id.line58);
        line59 = findViewById(R.id.line59);

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


    public void includeMoveInvisible() {
        includeMove.setVisibility(View.INVISIBLE);
        includeAtk.setVisibility(View.VISIBLE);
    }

    public void includeMoveVisible() {
        includeMove.setVisibility(View.VISIBLE);
        includeAtk.setVisibility(View.INVISIBLE);
    }

    public void visibility() {
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
        button.setAlpha(0.2f);
        button2.setAlpha(0.2f);
        button3.setAlpha(0.2f);
        button4.setAlpha(0.2f);
        button5.setAlpha(0.2f);
        button6.setAlpha(0.2f);
    }

    public void lockBtnAtk() {
        buttonAtk1.setEnabled(false);
        buttonAtk2.setEnabled(false);
        buttonAtk3.setEnabled(false);
        buttonAtk4.setEnabled(false);
        buttonAtk5.setEnabled(false);
        buttonAtk5.setEnabled(false);
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

    public void openBtn() {
        button.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        button4.setEnabled(true);
        button5.setEnabled(true);
        button6.setEnabled(true);
        button.setAlpha(1.0f);
        button2.setAlpha(1.0f);
        button3.setAlpha(1.0f);
        button4.setAlpha(1.0f);
        button5.setAlpha(1.0f);
        button6.setAlpha(1.0f);
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
        sendMessageUp("hp", 2);
    }

    public void tool2(View view) {
        sendMessageUp("mp", 4);
    }

    public void controlMPHP(TextView PP, int add) {
        int MpBefore = Integer.parseInt(PP.getText().toString());
        int MpAfter = MpBefore + add;
        PP.setText(MpAfter + "");
        if (MpAfter >= 10) {
            PP.setText(10 + "");
        }
    }

    public void initConnect(View view) {
        connectManager.SERVER_PATH = ip;
        connectManager.initiateSocketConnection();
        initGame1();
        Toast.makeText(this, "重新連線", Toast.LENGTH_SHORT).show();
    }

    public void initStart(View view) {
        initGame1();
        sendMessageRestart();
        Toast.makeText(this, "重新開始", Toast.LENGTH_SHORT).show();
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
        atkRules.MPLimit(atkDecide.MP[0], buttonAtk1);
        atkRules.MPLimit(atkDecide.MP[1], buttonAtk2);
        atkRules.MPLimit(atkDecide.MP[2], buttonAtk3);
        atkRules.MPLimit(atkDecide.MP[3], buttonAtk4);
        atkRules.MPLimit(atkDecide.MP[4], buttonAtk5);
        gameEnd();
        atkRules.initAtkRange();
    }


    public void gameEnd() {
        if (Integer.parseInt(txt_self_hp.getText().toString()) <= 0 || Integer.parseInt(txt_com_hp.getText().toString()) <= 0) {
            initGame.setVisibility(View.VISIBLE);
//            Toast.makeText(this, "遊戲結束", Toast.LENGTH_LONG).show();
            lockBtn();
        }
    }

    public void initGame1() {
        initGame.setVisibility(View.INVISIBLE);
        txt_self_hp.setText("10");
        txt_com_hp.setText("10");
        txt_self_mp.setText("10");
        txt_com_mp.setText("10");
        locationXSelf = 0;
        locationYSelf = 1;
        locationXCom = 4;
        locationYCom = 1;
        includeMoveVisible();
        visibility();//重開新局 消滅地圖火焰

        openBtn();
        openBtnAtk();
        imagePlayer.layout(locationX[locationXSelf].getLeft() + 30, locationY[locationYSelf].getTop() - 200, locationX[locationXSelf].getLeft() + 100 + 30, locationY[locationYSelf].getBottom());
        imageCom.layout(locationX[locationXCom].getLeft() + 30, locationY[locationYCom].getTop() - 200, locationX[locationXCom].getLeft() + 100 + 30, locationY[locationYCom].getBottom());

    }

    /**
     * 重新開始 初始化遊戲
     */
    public void initGame(View v) {
        initGame1();
    }


    public void sendMessageMove() {
//        includeMoveInvisible();
        statusUp();
        Toast.makeText(this, "移動", Toast.LENGTH_SHORT).show();
        fullRoom.child(player).child("locationXSelf").setValue(locationXSelf);
        fullRoom.child(player).child("locationYSelf").setValue(locationYSelf);
//        connectManager.sendMessage(0, locationXSelf, locationYSelf, "move");

    }

    public void sendMessageUp(String pp, int l) {
        includeMoveInvisible();
        statusUp();
        Toast.makeText(this, "回復", Toast.LENGTH_SHORT).show();
        connectManager.sendMessage(l, pp, "up");
    }

    public void sendMessageMoveAtk(int[] atk, int hp, int mp) {
        statusUp();
        Toast.makeText(this, "攻擊", Toast.LENGTH_SHORT).show();
        connectManager.sendMessage(atk, hp, mp, "atk");
    }

    public void sendMessageRestart() {
        Toast.makeText(this, "重新開始", Toast.LENGTH_SHORT).show();
        connectManager.sendMessage("restart");
    }


    public void atk1(View v) {
        lockBtnAtk();
        sendMessageMoveAtk(atkDecide.atk0[0], atkDecide.HP[0], atkDecide.MP[0]);
    }

    public void atk2(View v) {
        lockBtnAtk();
        sendMessageMoveAtk(atkDecide.atk0[1], atkDecide.HP[1], atkDecide.MP[1]);
    }

    public void atk3(View v) {
        lockBtnAtk();
        sendMessageMoveAtk(atkDecide.atk0[2], atkDecide.HP[2], atkDecide.MP[2]);
    }

    public void atk4(View v) {
        lockBtnAtk();
        sendMessageMoveAtk(atkDecide.atk0[3], atkDecide.HP[3], atkDecide.MP[3]);
    }

    public void atk5(View v) {
        lockBtnAtk();
        sendMessageMoveAtk(atkDecide.atk0[4], atkDecide.HP[4], atkDecide.MP[4]);
    }

    public void atk6(View v) {
        lockBtnAtk();
        sendMessageMoveAtk(null, 0, 0);
    }


    private void receiveMessage() {

        visibility();//收到後 地板火焰先取消

        for (int i = 0; i < 2; i++) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("NUM", i);
            msg.setData(bundle);
            msg.what = 0;
            handler.sendMessageDelayed(msg, 1000 * (i + 1));
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
                            long xl = (long) snapshot.child("locationXSelf").getValue();
                            long yl = (long) snapshot.child("locationYSelf").getValue();
                            fullRoom.child(player).child("X").setValue((int) xl);
                            fullRoom.child(player).child("Y").setValue((int) yl);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case 1:
                    fullRoom.child(otherPlayer).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long xl = (long) snapshot.child("locationXSelf").getValue();
                            long yl = (long) snapshot.child("locationYSelf").getValue();
                            fullRoom.child(otherPlayer).child("X").setValue((int) xl);
                            fullRoom.child(otherPlayer).child("Y").setValue((int) yl);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    System.out.println("歸零");
                    fullRoom.child("fourStatus").setValue(0);
                    break;
                case 2:
                    fullRoom.child("fourStatus").setValue(0);
                    break;
                case 3:
                    fullRoom.child("fourStatus").setValue(0);
                    break;
            }
        }
    }


}