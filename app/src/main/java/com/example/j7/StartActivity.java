package com.example.j7;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.j7.databinding.ActivityMainBinding;
import com.example.j7.databinding.User;
import com.example.j7.game.AtkDecide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

import static com.example.j7.LoginActivity.TSUserId;

public class StartActivity extends AppCompatActivity {

    private static final int REQUEST_LOGIN = 100;
    private static final String TAG = StartActivity.class.getSimpleName();
    private boolean logon = false;
    public AtkDecide atkDecide = new AtkDecide();
    public View line11, line12, line13, line14, line15, line16, line17, line18, line19;
    public View line21, line22, line23, line24, line25, line26, line27, line28, line29;
    public View line31, line32, line33, line34, line35, line36, line37, line38, line39;
    public View line41, line42, line43, line44, line45, line46, line47, line48, line49;
    public View line51, line52, line53, line54, line55, line56, line57, line58, line59;
    public View buttonAtk1, buttonAtk2, buttonAtk3, buttonAtk4, buttonAtk5, buttonAtk6;
    public TextView HP1, HP2, HP3, HP4, HP5;
    public TextView MP1, MP2, MP3, MP4, MP5;

    TextView role;
    List<Drawable> drawableList = new ArrayList<Drawable>();//存放圖片
    Button arrowLeftTextView;//左箭頭
    Button arrowRightTextView;//右箭頭
    private int index = 0;//角色

    
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
    protected void onStart() {
        super.onStart();

        //資料綁定
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        final User user = new User();
        binding.setUser(user);
        //reading data from firebase database
        FirebaseDatabase.getInstance().getReference("users").child(TSUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        System.out.println("123 :" + dataSnapshot.getValue().toString() );
//                        user.setName(dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue() != null) {
                            /**等級*/
                            user.setLevel(dataSnapshot.child("level").getValue().toString());
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
        atkDrawHPMP();
        atkDraw();


        final ImageSwitcher imageSwitcher = findViewById(R.id.hand);
        arrowLeftTextView = findViewById(R.id.rightBtn);
        arrowRightTextView = findViewById(R.id.leftBtn);
        role = findViewById(R.id.role);

        //工廠
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(StartActivity.this);
            }
        });

        imageSwitcher.setImageDrawable(drawableList.get(0));//初始化顯示第一張圖片
        role.setText("index : " + index);
        arrowRightTextView.setOnClickListener(new View.OnClickListener() {//左箭頭單擊事件
            @Override
            public void onClick(View view) {
                index = index - 1;
                if (index < 0)//如果到達了圖片的開始，則直接顯示最後一張圖片
                {
                    index = drawableList.size() - 1;
                }
                imageSwitcher.setImageDrawable(drawableList.get(index));
                role.setText("index : " + index);
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
                imageSwitcher.setImageDrawable(drawableList.get(index));
                role.setText("index : " + index);
            }
        });


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


    public void atkDrawHPMP() {
        HP1.setText(atkDecide.HP[0] + "");
        HP2.setText(atkDecide.HP[1] + "");
        HP3.setText(atkDecide.HP[2] + "");
        HP4.setText(atkDecide.HP[3] + "");
        HP5.setText(atkDecide.HP[4] + "");
        MP1.setText(atkDecide.MP[0] + "");
        MP2.setText(atkDecide.MP[1] + "");
        MP3.setText(atkDecide.MP[2] + "");
        MP4.setText(atkDecide.MP[3] + "");
        MP5.setText(atkDecide.MP[4] + "");

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
        Intent intent = new Intent(this, GameMainActivity.class);
        startActivity(intent);

    }


    public void atkDraw() {
        for (int i = 0; i < atkDecide.atk0[0].length; i++) {
            switch (atkDecide.atk0[0][i]) {
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

        for (int i = 0; i < atkDecide.atk0[1].length; i++) {
            switch (atkDecide.atk0[1][i]) {
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
        for (int i = 0; i < atkDecide.atk0[2].length; i++) {
            switch (atkDecide.atk0[2][i]) {
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
        for (int i = 0; i < atkDecide.atk0[3].length; i++) {
            switch (atkDecide.atk0[3][i]) {
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
        for (int i = 0; i < atkDecide.atk0[4].length; i++) {
            switch (atkDecide.atk0[4][i]) {
                case 1:
                    line51.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    line52.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    line53.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    line54.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    line55.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    line56.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    line57.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    line58.setVisibility(View.VISIBLE);
                    break;
                case 9:
                    line59.setVisibility(View.VISIBLE);
                    break;
            }
        }
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

        line51 = findViewById(R.id.line51);
        line52 = findViewById(R.id.line52);
        line53 = findViewById(R.id.line53);
        line54 = findViewById(R.id.line54);
        line55 = findViewById(R.id.line55);
        line56 = findViewById(R.id.line56);
        line57 = findViewById(R.id.line57);
        line58 = findViewById(R.id.line58);
        line59 = findViewById(R.id.line59);

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}