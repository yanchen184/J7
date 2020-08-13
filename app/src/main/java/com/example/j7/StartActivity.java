package com.example.j7;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.j7.databinding.ActivityMainBinding;
import com.example.j7.databinding.User;
import com.example.j7.fourBtn.FourCardAdd;
import com.example.j7.fourBtn.FourCardChange;
import com.example.j7.fourBtn.FourCardUpgrade;
import com.example.j7.fourBtn.FourRoleAdd;
import com.example.j7.game.AtkDecide;
import com.example.j7.tools.Tools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.j7.LoginActivity.TSUserId;
import static com.example.j7.LoginActivity.userId;
import static com.example.j7.tools.Name.STATUS_INIT;
import static com.example.j7.tools.Name.STATUS_JOINED;
import static com.example.j7.tools.Name.fsNum;

public class StartActivity extends AppCompatActivity {

    private static final int REQUEST_LOGIN = 100;
    private static final String TAG = StartActivity.class.getSimpleName();
    private boolean logon = false;
    public View line11, line12, line13, line14, line15, line16, line17, line18, line19;
    public View line21, line22, line23, line24, line25, line26, line27, line28, line29;
    public View line31, line32, line33, line34, line35, line36, line37, line38, line39;
    public View line41, line42, line43, line44, line45, line46, line47, line48, line49;
    public View line51, line52, line53, line54, line55, line56, line57, line58, line59;
    public Button buttonAtk1, buttonAtk2, buttonAtk3, buttonAtk4, buttonAtk5, buttonAtk6;
    public TextView HP1, HP2, HP3, HP4, HP5;
    public TextView MP1, MP2, MP3, MP4, MP5;

    TextView role, introduction;

    List<Drawable> drawableList = new ArrayList<Drawable>();//存放圖片
    Button arrowLeftTextView;//左箭頭
    Button arrowRightTextView;//右箭頭

    Tools tools = new Tools();
    private Boolean[] indexGetBoolean;//角色擁有
    private List indexGetInt;//角色擁有
    private String[] indexAll = {"j4", "fs", "player", "b74"};//角色擁有
    private int index = 0;//角色

    DatabaseReference FRoom;
    DatabaseReference FUser;

    ArrayList<Integer> j4HP;
    ArrayList<Integer> j4MP;
    ArrayList<ArrayList<Integer>> j4atkR;

    ArrayList<Integer> fsHP;
    ArrayList<Integer> fsMP;
    ArrayList<ArrayList<Integer>> fsatkR;

    ArrayList<Integer> playerHP;
    ArrayList<Integer> playerMP;
    ArrayList<ArrayList<Integer>> playeratkR;

    ArrayList<Integer> b74HP;
    ArrayList<Integer> b74MP;
    ArrayList<ArrayList<Integer>> b74atkR;

    ArrayList<Integer> finalHP = null;
    ArrayList<Integer> finalMP = null;
    ArrayList<ArrayList<Integer>> finalAtlR = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
        if (!logon) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }

        drawableList.add(getResources().getDrawable(R.drawable.j4));//圖片01
        drawableList.add(getResources().getDrawable(R.drawable.fs));//圖片02
        drawableList.add(getResources().getDrawable(R.drawable.player));//圖片03
        drawableList.add(getResources().getDrawable(R.drawable.b74));//圖片04


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                logon = true;

            } else {
                finish();
            }
        }
    }

    public StartActivity() {
    }

    public String roomKey;
    public ActivityMainBinding binding;

    @Override
    protected void onRestart() {
        super.onRestart();

        //資料綁定
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        final User user = new User();
        binding.setUser(user);
        //reading data from firebase database

        FUser = FirebaseDatabase.getInstance().getReference("users").child(TSUserId);
        FRoom = FirebaseDatabase.getInstance().getReference("rooms");


        FUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    /**等級*/
                    user.setLevel(dataSnapshot.child("level").getValue().toString());
                    Boolean roleJ4Have = (Boolean) dataSnapshot.child("role").child("j4").child("have").getValue();
                    Boolean roleFsHave = (Boolean) dataSnapshot.child("role").child("j4").child("have").getValue();
                    Boolean rolePlayerHave = (Boolean) dataSnapshot.child("role").child("j4").child("have").getValue();
                    Boolean roleB74Have = (Boolean) dataSnapshot.child("role").child("j4").child("have").getValue();
                    indexGetBoolean = new Boolean[]{roleJ4Have, roleFsHave, rolePlayerHave, roleB74Have};

                    index = Integer.parseInt(String.valueOf(dataSnapshot.child("record").getValue()));

                    j4HP = (ArrayList<Integer>) dataSnapshot.child("role").child("j4").child("HP").getValue();
                    j4MP = (ArrayList<Integer>) dataSnapshot.child("role").child("j4").child("MP").getValue();
                    j4atkR = (ArrayList<ArrayList<Integer>>) dataSnapshot.child("role").child("j4").child("atkR").getValue();

                    fsHP = (ArrayList<Integer>) dataSnapshot.child("role").child("fs").child("HP").getValue();
                    fsMP = (ArrayList<Integer>) dataSnapshot.child("role").child("fs").child("MP").getValue();
                    fsatkR = (ArrayList<ArrayList<Integer>>) dataSnapshot.child("role").child("fs").child("atkR").getValue();

                    playerHP = (ArrayList<Integer>) dataSnapshot.child("role").child("player").child("HP").getValue();
                    playerMP = (ArrayList<Integer>) dataSnapshot.child("role").child("player").child("MP").getValue();
                    playeratkR = (ArrayList<ArrayList<Integer>>) dataSnapshot.child("role").child("player").child("atkR").getValue();

                    b74HP = (ArrayList<Integer>) dataSnapshot.child("role").child("b74").child("HP").getValue();
                    b74MP = (ArrayList<Integer>) dataSnapshot.child("role").child("b74").child("MP").getValue();
                    b74atkR = (ArrayList<ArrayList<Integer>>) dataSnapshot.child("role").child("b74").child("atkR").getValue();


                    setRole();
                    dataSnapshot.child("role").child("j4").child("atkR").getValue();
                    for (int i = 0; i < indexGetBoolean.length; i++) {
                        if (indexGetBoolean[i]) {
//                                    indexGetInt.add(i);
                        }
                    }
                } else {
                    user.setLevel("00");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        /**名字*/
        user.setName(TSUserId);

//        fullRoom.addValueEventListener(upListener);//監聽

        /**密碼四碼後才開放使用加入遊戲*/
        Button HTTPJoin = findViewById(R.id.HTTPJoin);
        HTTPJoin.setEnabled(false);
        EditText roomEditText = findViewById(R.id.roomEditText);
        roomEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                EditText roomEditText = findViewById(R.id.roomEditText);
                if (roomEditText.getText().toString().length() == 4) {
                    Button HTTPJoin = findViewById(R.id.HTTPJoin);
                    HTTPJoin.setEnabled(true);
                }
                Button HTTPCreate = findViewById(R.id.HTTPCreate);
                if (roomEditText.getText().toString().length() > 0) {
                    HTTPCreate.setEnabled(false);
                } else {
                    HTTPCreate.setEnabled(true);
                }
            }
        });


        /**用於重新登入TODO*/
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "點此登出", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findId();
        openBtnAtk();

        final ImageSwitcher imageSwitcher = findViewById(R.id.hand);


        arrowLeftTextView = findViewById(R.id.rightBtn);
        arrowRightTextView = findViewById(R.id.leftBtn);
        role = findViewById(R.id.role);
        introduction = findViewById(R.id.introduction);
        //工廠
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(StartActivity.this);
            }
        });
        arrowRightTextView.setOnClickListener(new View.OnClickListener() {//左箭頭單擊事件
            @Override
            public void onClick(View view) {
                index = index - 1;
                if (index < 0)//如果到達了圖片的開始，則直接顯示最後一張圖片
                {
                    index = drawableList.size() - 1;
                }
                FUser.child("record").setValue(index);
            }
        });

        arrowLeftTextView.setOnClickListener(new View.OnClickListener() {//右箭頭單擊事件
            @Override
            public void onClick(View view) {
                index = index + 1;
                if (index >= drawableList.size())//如果點選到達圖片的末尾，則回到第一張圖片
                {
                    index = 0;
                }
                FUser.child("record").setValue(index);
            }
        });
        cancelConnect();//顯示的部分
        FUser.addValueEventListener(downListener);//監聽
    }


    private ValueEventListener downListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            setRole();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    public void setRole() {
        /**
         * 1. 更改圖片
         * 2. 更改角色名稱
         * 3. 更改技能組
         */
        atkDrawINVISIBLE();
        final ImageSwitcher imageSwitcher = findViewById(R.id.hand);
        imageSwitcher.setImageDrawable(drawableList.get(index));
        switch (tools.roleChange(index)) {
            case "j4":
                buttonAtk5.setText("續力");
                role.setText("劍士");
                atkDrawHPMP(j4HP, j4MP);
                atkDraw(j4atkR);
                introduction.setText("攻擊範圍較小,傷害極高,回血較快 \n 特殊技能 : 續力..下次攻擊傷害為雙倍");
                break;
            case "fs":
                buttonAtk5.setText("末日");
                role.setText("法師");
                atkDrawHPMP(fsHP, fsMP);
                atkDraw(fsatkR);
                introduction.setText("攻擊範圍較大,傷害較低,回魔為一般人1.5倍 \n 特殊技能 : 末日..九宮格傷害");
                break;
            case "player":
                buttonAtk5.setText("尚未開發");
                role.setText("普通人");
                atkDrawHPMP(playerHP, playerMP);
                atkDraw(playeratkR);
                introduction.setText("就是普通人 \n 特殊技能 : 還沒想到");
                break;
            case "b74":
                buttonAtk5.setText("隔檔");
                role.setText("騎士");
                atkDrawHPMP(b74HP, b74MP);
                atkDraw(b74atkR);
                introduction.setText("擁有雙倍的厚實血量,回血為一般人1.5倍 \n 特殊技能 : 隔檔...可以隔檔攻擊");
                break;
        }

    }

    public void roleAdd(View v) {
        Intent it = new Intent(this, FourRoleAdd.class);
        startActivity(it);
    }

    public void cardAdd(View v) {
        Intent it = new Intent(this, FourCardAdd.class);
        startActivity(it);
    }

    public void cardUpgrade(View v) {
        Intent it = new Intent(this, FourCardUpgrade.class);
        startActivity(it);
    }

    public void cardChange(View v) {
        Intent it = new Intent(this, FourCardChange.class);
        startActivity(it);
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


    public void atkDrawHPMP(ArrayList<Integer> HP, ArrayList<Integer> MP) {
        finalHP = HP;
        finalMP = MP;
        HP1.setText(HP.get(0) + "");
        HP2.setText(HP.get(1) + "");
        HP3.setText(HP.get(2) + "");
        HP4.setText(HP.get(3) + "");
        HP5.setText(HP.get(4) + "");
        MP1.setText(MP.get(0) + "");
        MP2.setText(MP.get(1) + "");
        MP3.setText(MP.get(2) + "");
        MP4.setText(MP.get(3) + "");
        MP5.setText(MP.get(4) + "");
    }

    public void atk1(View v) {
    }

    public void atk2(View v) {

    }

    public void atk3(View v) {

    }

    public void atk4(View v) {

    }

    public void atk5(View v) {

    }

    public void atk6(View v) {
        showConnect();
        Button cancelC = findViewById(R.id.cancelC);
        cancelC.setVisibility(View.VISIBLE);

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
        line21.setVisibility(View.INVISIBLE);
        line22.setVisibility(View.INVISIBLE);
        line23.setVisibility(View.INVISIBLE);
        line24.setVisibility(View.INVISIBLE);
        line25.setVisibility(View.INVISIBLE);
        line26.setVisibility(View.INVISIBLE);
        line27.setVisibility(View.INVISIBLE);
        line28.setVisibility(View.INVISIBLE);
        line29.setVisibility(View.INVISIBLE);
        line31.setVisibility(View.INVISIBLE);
        line32.setVisibility(View.INVISIBLE);
        line33.setVisibility(View.INVISIBLE);
        line34.setVisibility(View.INVISIBLE);
        line35.setVisibility(View.INVISIBLE);
        line36.setVisibility(View.INVISIBLE);
        line37.setVisibility(View.INVISIBLE);
        line38.setVisibility(View.INVISIBLE);
        line39.setVisibility(View.INVISIBLE);
        line41.setVisibility(View.INVISIBLE);
        line42.setVisibility(View.INVISIBLE);
        line43.setVisibility(View.INVISIBLE);
        line44.setVisibility(View.INVISIBLE);
        line45.setVisibility(View.INVISIBLE);
        line46.setVisibility(View.INVISIBLE);
        line47.setVisibility(View.INVISIBLE);
        line48.setVisibility(View.INVISIBLE);
        line49.setVisibility(View.INVISIBLE);
//        line51.setVisibility(View.INVISIBLE);
//        line52.setVisibility(View.INVISIBLE);
//        line53.setVisibility(View.INVISIBLE);
//        line54.setVisibility(View.INVISIBLE);
//        line55.setVisibility(View.INVISIBLE);
//        line56.setVisibility(View.INVISIBLE);
//        line57.setVisibility(View.INVISIBLE);
//        line58.setVisibility(View.INVISIBLE);
//        line59.setVisibility(View.INVISIBLE);


    }


    public void atkDraw(ArrayList<ArrayList<Integer>> atk) {
        finalAtlR = atk;
        for (int i = 0; i < atk.get(0).size(); i++) {
            int yc = Integer.parseInt(String.valueOf(atk.get(0).get(i)));
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
        for (int i = 0; i < atk.get(1).size(); i++) {
            int yc = Integer.parseInt(String.valueOf(atk.get(1).get(i)));
            switch (yc) {
                case 1:
                    line21.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    line22.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    line23.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    line24.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    line25.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    line26.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    line27.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    line28.setVisibility(View.VISIBLE);
                    break;
                case 9:
                    line29.setVisibility(View.VISIBLE);
                    break;
            }
        }
        for (int i = 0; i < atk.get(2).size(); i++) {
            int yc = Integer.parseInt(String.valueOf(atk.get(2).get(i)));
            switch (yc) {
                case 1:
                    line31.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    line32.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    line33.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    line34.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    line35.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    line36.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    line37.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    line38.setVisibility(View.VISIBLE);
                    break;
                case 9:
                    line39.setVisibility(View.VISIBLE);
                    break;
            }
        }
        for (int i = 0; i < atk.get(3).size(); i++) {
            int yc = Integer.parseInt(String.valueOf(atk.get(3).get(i)));
            switch (yc) {
                case 1:
                    line41.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    line42.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    line43.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    line44.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    line45.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    line46.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    line47.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    line48.setVisibility(View.VISIBLE);
                    break;
                case 9:
                    line49.setVisibility(View.VISIBLE);
                    break;
            }
        }
//        for (int i = 0; i < atk.get(4).size(); i++) {
//            int yc = Integer.parseInt(String.valueOf(atk.get(4).get(i)));
//            switch (yc) {
//                case 1:
//                    line51.setVisibility(View.VISIBLE);
//                    break;
//                case 2:
//                    line52.setVisibility(View.VISIBLE);
//                    break;
//                case 3:
//                    line53.setVisibility(View.VISIBLE);
//                    break;
//                case 4:
//                    line54.setVisibility(View.VISIBLE);
//                    break;
//                case 5:
//                    line55.setVisibility(View.VISIBLE);
//                    break;
//                case 6:
//                    line56.setVisibility(View.VISIBLE);
//                    break;
//                case 7:
//                    line57.setVisibility(View.VISIBLE);
//                    break;
//                case 8:
//                    line58.setVisibility(View.VISIBLE);
//                    break;
//                case 9:
//                    line59.setVisibility(View.VISIBLE);
//                    break;
//            }
//        }
    }

    public void findId() {

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

        buttonAtk1 = findViewById(R.id.buttonAtk1);
        buttonAtk2 = findViewById(R.id.buttonAtk2);
        buttonAtk3 = findViewById(R.id.buttonAtk3);
        buttonAtk4 = findViewById(R.id.buttonAtk4);
        buttonAtk5 = findViewById(R.id.buttonAtk5);
        buttonAtk6 = findViewById(R.id.buttonAtk6);

        HP1 = findViewById(R.id.HP1);
        HP2 = findViewById(R.id.HP2);
        HP3 = findViewById(R.id.HP3);
        HP4 = findViewById(R.id.HP4);
        HP5 = findViewById(R.id.HP5);
        MP1 = findViewById(R.id.MP1);
        MP2 = findViewById(R.id.MP2);
        MP3 = findViewById(R.id.MP3);
        MP4 = findViewById(R.id.MP4);
        MP5 = findViewById(R.id.MP5);

        EditText roomEditText = findViewById(R.id.roomEditText);
        roomEditText.setHint("----");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void cancelConnect() {
        View layout_pair = findViewById(R.id.layout_pair);
        View layout_pair_bk = findViewById(R.id.layout_pair_bk);
        View layout_pair2 = findViewById(R.id.layout_pair2);
        layout_pair.setVisibility(View.INVISIBLE);
        layout_pair_bk.setVisibility(View.INVISIBLE);
        layout_pair2.setVisibility(View.INVISIBLE);
    }

    public void showConnect() {
        View layout_pair = findViewById(R.id.layout_pair);
        View layout_pair_bk = findViewById(R.id.layout_pair_bk);
        View layout_pair2 = findViewById(R.id.layout_pair2);
        layout_pair.setVisibility(View.INVISIBLE);
        layout_pair_bk.setVisibility(View.VISIBLE);
        layout_pair2.setVisibility(View.VISIBLE);
    }

    public void onFriend(View v) {
        View layout_pair = findViewById(R.id.layout_pair);
        View layout_pair2 = findViewById(R.id.layout_pair2);
        layout_pair.setVisibility(View.VISIBLE);
        layout_pair2.setVisibility(View.INVISIBLE);
    }

    public void onRandom(View v) {
        Toast.makeText(this, "不要逼ㄛ 我還沒寫", Toast.LENGTH_LONG).show();
    }

    String player;



    public void onHTTPCreate(View v) {
//        checkRepeat(); //roomKey
        Button HTTPCreate = findViewById(R.id.HTTPCreate);
        HTTPCreate.setEnabled(false);
        Button HTTPJoin = findViewById(R.id.HTTPJoin);
        HTTPJoin.setEnabled(false);
        EditText roomEditText = findViewById(R.id.roomEditText);
        roomEditText.setEnabled(false);
    }

    public void onHTTPJoin(View v) {

        EditText roomEditText = findViewById(R.id.roomEditText);
        String roomNum = roomEditText.getText().toString();
        roomKey = roomNum;
        Toast.makeText(this, "檢查是否有此房號....",
                Toast.LENGTH_SHORT).show();

        FRoom.child(roomNum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    Toast.makeText(StartActivity.this, "查無此房間....", Toast.LENGTH_SHORT).show();
                } else {

                    /**
                     * 1.增加房間player2名稱
                     * 2.狀態設為 1
                     * 4.監聽狀態
                     * 5.增加房間自己的血量跟魔量
                     * */

                    FRoom.child(roomKey).child("player2").child("name").setValue(TSUserId);
                    player = "player2";
                    FUser.child("role").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            addHPMP(snapshot, "player2");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    FRoom.child(roomKey).child("status").setValue(1);
                    FRoom
                            .child(roomKey)
                            .child("status")
                            .addValueEventListener(statusListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void cancelC(View v) {
        Button cancelC = findViewById(R.id.cancelC);
        cancelC.setVisibility(View.INVISIBLE);
        cancelConnect();
        Button HTTPCreate = findViewById(R.id.HTTPCreate);
        HTTPCreate.setEnabled(true);
        TextView create1234 = findViewById(R.id.create1234);
        create1234.setText("----");
//        Button HTTPJoin = findViewById(R.id.HTTPJoin);
//        HTTPJoin.setEnabled(true);
        EditText roomEditText = findViewById(R.id.roomEditText);
        roomEditText.setEnabled(true);
        roomEditText.setText("");
    }


    private ValueEventListener statusListener = new ValueEventListener() {
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
                    Intent it = new Intent(StartActivity.this, GameActivity.class);
                    it.putExtra("player", player);
                    it.putExtra("index", index);
                    it.putExtra("roomKey", roomKey);
                    it.putExtra("finalHP", finalHP);
                    it.putExtra("finalMP", finalMP);

                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("list", finalAtlR);
                    it.putExtras(mBundle);


//                    it.putExtra("finalAtlR",finalAtlR);

                    startActivity(it);
                    break;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    private void addHPMP(@NonNull DataSnapshot snapshot, String player) {

        FRoom.child(roomKey).child(player).child("Index").setValue(index);
        FRoom.child(roomKey).child(player).child("HP").setValue(snapshot.child(tools.roleChange(index)).child("SHP").getValue());
        FRoom.child(roomKey).child(player).child("MP").setValue(snapshot.child(tools.roleChange(index)).child("SMP").getValue());
        FRoom.child(roomKey).child(player).child("HPUP").setValue(0);
        FRoom.child(roomKey).child(player).child("MPUP").setValue(0);
        FRoom.child(roomKey).child(player).child("X").setValue(0);
        FRoom.child(roomKey).child(player).child("Y").setValue(1);
        FRoom.child(roomKey).child(player).child("Next").child("locationXSelf").setValue(0);
        FRoom.child(roomKey).child(player).child("Next").child("locationYSelf").setValue(1);
        FRoom.child(roomKey).child(player).child("Next").child("HPUP").setValue(0);
        FRoom.child(roomKey).child(player).child("Next").child("MPUP").setValue(0);
        FRoom.child(roomKey).child(player).child("Next").child("atkR").setValue(0);
        FRoom.child(roomKey).child(player).child("Next").child("atkHP").setValue(0);
        FRoom.child(roomKey).child(player).child("Next").child("atkMP").setValue(0);
        FRoom.child(roomKey).child("fourStatus").setValue(0);
        FRoom.child(roomKey).child(player).child("unique").setValue(false);
    }


}