package com.example.j7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.j7.game.AtkDecide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.j7.tools.Name.fsNum;
import static com.example.j7.tools.Name.j4Num;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText edUserid;
    private EditText edPasswd;

    public static String userId = "0";
    public static String passWd = "0";

    public static String TSUserId = "0";
    public static String TSPassWd = "0";

    public AtkDecide atkDecide = new AtkDecide();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edUserid = findViewById(R.id.ed_userid);
        edPasswd = findViewById(R.id.ed_passwd);


        String[] text1 = {"預言家", "女巫", "獵人", "騎士", "守衛", "禁言長老",
                "魔術師", "通靈師", "熊", "白癡", "炸彈人", "守墓人", "九尾妖狐"};
        String userName = text1[(int) (Math.random() * text1.length)] + (int) (Math.random() * 9999 + 1);
        int userPassword = (int) (Math.random() * 9999 + 1);

        edUserid.setText(getSharedPreferences("users", MODE_PRIVATE)
                .getString("usersName", userName));
        edPasswd.setText(getSharedPreferences("users", MODE_PRIVATE)
                .getString("usersPassword", userPassword + ""));


        loginHot();
    }


    public LoginActivity() {
    }

    public void loginHot() {
        userId = edUserid.getText().toString();
        passWd = edPasswd.getText().toString();
        inputTS(userId, passWd);
        FirebaseDatabase.getInstance().getReference("users").child(userId).child("password")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                        } else {
                            String pw = dataSnapshot.getValue().toString();
                            if (pw.equals(passWd)) {
                                setResult(RESULT_OK);
                                successDialog();
                                finish();
                            } else {

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }


    public void login(View view) {
        userId = edUserid.getText().toString();
        passWd = edPasswd.getText().toString();
        inputTS(userId, passWd);
        FirebaseDatabase.getInstance().getReference("users").child(userId).child("password")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            failDialog();
                        } else {
                            String pw = dataSnapshot.getValue().toString();
                            if (pw.equals(passWd)) {
                                setResult(RESULT_OK);
                                successDialog();
                                finish();
                            } else {
                                failDialog();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }


    public void sign(View view) {
        userId = edUserid.getText().toString();
        passWd = edPasswd.getText().toString();
        inputTS(userId, passWd);

//        String[] items = {"newItem", "newItem", "newItem"};
//        rootRef.child("role").child("fs").child("atkR").setValue(Arrays.asList(items));

        FirebaseDatabase.getInstance().getReference("users").child(edUserid.getText().toString()).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    newSign();
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("註冊結果")
                            .setMessage("註冊成功")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    successDialog();
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            })
                            .show();
                } else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("註冊結果")
                            .setMessage("帳號已擁有")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void newSign() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        /**新的資料
         * 1.帳號 users
         * 2.密碼 password
         * 3.等級 level
         * 4.擁有的角色 role
         * 5.擁有的角色 role
         * 5.擁有的角色的技能組
         * 6.擁有的角色的技能組atk
         * 7.擁有的角色的技能組MP
         * 8.
         * */
        ArrayList<ArrayList<Integer>> fs = new ArrayList<>();
        for (int i = 0; i < atkDecide.fsAtk.length; i++) {
            fs.add(intToList(atkDecide.fsAtk[i]));
        }
        ArrayList<ArrayList<Integer>> j4 = new ArrayList<>();
        for (int i = 0; i < atkDecide.j4Atk.length; i++) {
            j4.add(intToList(atkDecide.j4Atk[i]));
        }
        ArrayList<ArrayList<Integer>> player = new ArrayList<>();
        for (int i = 0; i < atkDecide.playerAtk.length; i++) {
            player.add(intToList(atkDecide.playerAtk[i]));
        }
        ArrayList<ArrayList<Integer>> b74 = new ArrayList<>();
        for (int i = 0; i < atkDecide.b74Atk.length; i++) {
            b74.add(intToList(atkDecide.b74Atk[i]));
        }
        ArrayList<ArrayList<Integer>> atkRBackpack = new ArrayList<>();
        for (int i = 0; i < atkDecide.AtkBackpack.length; i++) {
            atkRBackpack.add(intToList(atkDecide.AtkBackpack[i]));
        }

        rootRef.child("users").setValue(userId);
        rootRef.child("password").setValue(passWd);
        rootRef.child("level").setValue(10);
        rootRef.child("record").setValue(j4Num);
        /** fs */
        rootRef.child("role").child("fs").child("have").setValue(true);
        rootRef.child("role").child("fs").child("SHP").setValue(10);
        rootRef.child("role").child("fs").child("SMP").setValue(15);
        rootRef.child("role").child("fs").child("atkR").setValue(fs);
        rootRef.child("role").child("fs").child("HP").setValue(intToList(atkDecide.fsHP));
        rootRef.child("role").child("fs").child("MP").setValue(intToList(atkDecide.fsMP));
        rootRef.child("role").child("fs").child("backpack").child("HP").setValue(intToList(atkDecide.HPBackpack));
        rootRef.child("role").child("fs").child("backpack").child("MP").setValue(intToList(atkDecide.HPBackpack));
        rootRef.child("role").child("fs").child("backpack").child("atkR").setValue(atkRBackpack);


        /** j4 */
        rootRef.child("role").child("j4").child("have").setValue(true);
        rootRef.child("role").child("j4").child("SHP").setValue(10);
        rootRef.child("role").child("j4").child("SMP").setValue(10);
        rootRef.child("role").child("j4").child("atkR").setValue(j4);
        rootRef.child("role").child("j4").child("HP").setValue(intToList(atkDecide.j4HP));
        rootRef.child("role").child("j4").child("MP").setValue(intToList(atkDecide.j4MP));
        rootRef.child("role").child("j4").child("backpack").child("HP").setValue(intToList(atkDecide.HPBackpack));
        rootRef.child("role").child("j4").child("backpack").child("MP").setValue(intToList(atkDecide.HPBackpack));
        rootRef.child("role").child("j4").child("backpack").child("atkR").setValue(atkRBackpack);

        /** player */
        rootRef.child("role").child("player").child("have").setValue(true);
        rootRef.child("role").child("player").child("SHP").setValue(10);
        rootRef.child("role").child("player").child("SMP").setValue(10);
        rootRef.child("role").child("player").child("atkR").setValue(player);
        rootRef.child("role").child("player").child("HP").setValue(intToList(atkDecide.playerHP));
        rootRef.child("role").child("player").child("MP").setValue(intToList(atkDecide.playerMP));
        rootRef.child("role").child("player").child("backpack").child("HP").setValue(intToList(atkDecide.HPBackpack));
        rootRef.child("role").child("player").child("backpack").child("MP").setValue(intToList(atkDecide.HPBackpack));
        rootRef.child("role").child("player").child("backpack").child("atkR").setValue(atkRBackpack);

        /** b74 */
        rootRef.child("role").child("b74").child("have").setValue(false);
        rootRef.child("role").child("b74").child("SHP").setValue(20);
        rootRef.child("role").child("b74").child("SMP").setValue(10);
        rootRef.child("role").child("b74").child("atkR").setValue(b74);
        rootRef.child("role").child("b74").child("HP").setValue(intToList(atkDecide.b74HP));
        rootRef.child("role").child("b74").child("MP").setValue(intToList(atkDecide.b74MP));
        rootRef.child("role").child("b74").child("backpack").child("HP").setValue(intToList(atkDecide.HPBackpack));
        rootRef.child("role").child("b74").child("backpack").child("MP").setValue(intToList(atkDecide.HPBackpack));
        rootRef.child("role").child("b74").child("backpack").child("atkR").setValue(atkRBackpack);


    }


    public ArrayList<Integer> intToList(int[] data) {
        ArrayList<Integer> x = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            x.add(data[i]);
        }
        return x;
    }


    public void inputTS(String user, String password) {
        getSharedPreferences("users", MODE_PRIVATE)
                .edit()
                .putString("usersName", user)
                .putString("usersPassword", password)
                .commit();
        TSUserId = getSharedPreferences("users", MODE_PRIVATE)
                .getString("usersName", "errorName");
        TSPassWd = getSharedPreferences("users", MODE_PRIVATE)
                .getString("usersName", "errorPassword");


    }

    private void failDialog() {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("登入失敗")
                .setMessage("帳號或密碼錯誤")
                .setPositiveButton("OK", null)
                .show();
    }

    private void successDialog() {
        Toast.makeText(this, "歡迎 " + TSUserId + " 登入遊戲！", Toast.LENGTH_LONG).show();
    }

}