package com.example.j7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText edUserid;
    private EditText edPasswd;

    public static String userId = "0";
    public static String passWd = "0";

    public static String TSUserId = "0";
    public static String TSPassWd = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edUserid = findViewById(R.id.ed_userid);
        edPasswd = findViewById(R.id.ed_passwd);
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

        FirebaseDatabase.getInstance().getReference("users").child(userId).child("users").setValue(userId);
        FirebaseDatabase.getInstance().getReference("users").child(userId).child("password").setValue(passWd);
        FirebaseDatabase.getInstance().getReference("users").child(userId).child("level").setValue(1);


        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pw = snapshot.getValue().toString();
                System.out.println("pw");
                System.out.println(pw);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        Toast.makeText(this, "歡迎 " + TSUserId +" 登入遊戲！", Toast.LENGTH_LONG).show();
    }

}