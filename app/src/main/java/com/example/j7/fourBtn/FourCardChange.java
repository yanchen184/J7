package com.example.j7.fourBtn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.example.j7.R;
import com.example.j7.StartActivity;
import com.example.j7.databinding.ActivityFourCardChangeBinding;
import com.example.j7.databinding.User;
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
import static com.example.j7.tools.Name.CHANGE_CARD;

public class FourCardChange extends AppCompatActivity {

    public RAdapter rAdapter;
    public List<AtkCard> atkCardList = new ArrayList<>();
    public RecyclerView recyclerView;
    public ActivityFourCardChangeBinding binding;

    public int role;
    public DatabaseReference level, backpack, record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_card_change);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_four_card_change);


//        backpack = FirebaseDatabase.getInstance().getReference("users").child(userId).child("role");
        record = FirebaseDatabase.getInstance().getReference("users").child(userId).child("record");

        record.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                role = Integer.parseInt(String.valueOf(snapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        for (int i = 0; i < fsHP.length; i++) {
//            AtkCard atkCard = new AtkCard(fsHP[i], fsMP[i], fsAtk[i]);
//            atkCardList.add(atkCard);
//        }

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 6));//橫向

        backpackRoleChange("j4");
        binding.j4.setBackgroundColor(Color.parseColor("#00000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.player.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.j4.setTextColor(Color.parseColor("#2b0501"));
        binding.fs.setTextColor(Color.parseColor("#faf3f2"));
        binding.player.setTextColor(Color.parseColor("#faf3f2"));
        binding.b74.setTextColor(Color.parseColor("#faf3f2"));

        /***/
        FUser = FirebaseDatabase.getInstance().getReference("users").child(TSUserId);

        FUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /***/

        binding.includeAtk1.includeAtk1.buttonAtk1.setEnabled(true);
        binding.includeAtk1.includeAtk2.buttonAtk2.setEnabled(true);
        binding.includeAtk1.includeAtk3.buttonAtk3.setEnabled(true);
        binding.includeAtk1.includeAtk4.buttonAtk4.setEnabled(true);
        binding.includeAtk1.includeAtk5.buttonAtk5.setEnabled(false);
        binding.includeAtk1.includeAtk6.buttonAtk6.setEnabled(false);

        FUser.addValueEventListener(downListener);//監聽


    }


    private ValueEventListener downListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            setRole();
            backpackRoleChange(tools.roleChange(index));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    public DatabaseReference FUser;
    public int index = 0;//角色
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

    private void backpackRoleChange(String role) {

        backpack = FirebaseDatabase.getInstance().getReference("users").child(userId).child("role").child(role).child("backpack");
        backpack.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Integer> HPBackpack;
                ArrayList<Integer> MPBackpack;
                ArrayList<ArrayList<Integer>> atkRBackpack;
                HPBackpack = (ArrayList<Integer>) snapshot.child("HP").getValue();
                MPBackpack = (ArrayList<Integer>) snapshot.child("MP").getValue();
                atkRBackpack = (ArrayList<ArrayList<Integer>>) snapshot.child("atkR").getValue();

                atkCardList = new ArrayList<>();
                int yc = HPBackpack == null ? 0 : HPBackpack.size();
                for (int i = 0; i < yc; i++) {
                    int[] atkRBackpackInt = new int[atkRBackpack.get(i).size()];
                    for (int j = 0; j < atkRBackpack.get(i).size(); j++) {
                        atkRBackpackInt[j] = Integer.parseInt(String.valueOf(atkRBackpack.get(i).get(j)));
                    }
                    AtkCard atkCard = new AtkCard(Integer.parseInt(String.valueOf(HPBackpack.get(i))), Integer.parseInt(String.valueOf(MPBackpack.get(i))), atkRBackpackInt);
                    atkCardList.add(atkCard);
                }
                rAdapter = new RAdapter(atkCardList);
                recyclerView.setAdapter(rAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void j4Add(View v) {
        backpackRoleChange("j4");
        record.setValue(0);
        binding.j4.setBackgroundColor(Color.parseColor("#00000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.player.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.j4.setTextColor(Color.parseColor("#2b0501"));
        binding.fs.setTextColor(Color.parseColor("#faf3f2"));
        binding.player.setTextColor(Color.parseColor("#faf3f2"));
        binding.b74.setTextColor(Color.parseColor("#faf3f2"));
//        je.setText("劍士");

    }


    public void fsAdd(View v) {
        backpackRoleChange("fs");
        record.setValue(1);
        binding.j4.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#00000000"));
        binding.player.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.j4.setTextColor(Color.parseColor("#faf3f2"));
        binding.fs.setTextColor(Color.parseColor("#2b0501"));
        binding.player.setTextColor(Color.parseColor("#faf3f2"));
        binding.b74.setTextColor(Color.parseColor("#faf3f2"));
//        je.setText("法師");
    }

    public void playerAdd(View v) {
        backpackRoleChange("player");
        record.setValue(2);
        binding.j4.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.player.setBackgroundColor(Color.parseColor("#00000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.j4.setTextColor(Color.parseColor("#faf3f2"));
        binding.fs.setTextColor(Color.parseColor("#faf3f2"));
        binding.player.setTextColor(Color.parseColor("#2b0501"));
        binding.b74.setTextColor(Color.parseColor("#faf3f2"));
//        je.setText("普通人");
    }

    public void b74Add(View v) {
        backpackRoleChange("b74");
        record.setValue(3);
        binding.j4.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.player.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#00000000"));
        binding.j4.setTextColor(Color.parseColor("#faf3f2"));
        binding.fs.setTextColor(Color.parseColor("#faf3f2"));
        binding.player.setTextColor(Color.parseColor("#faf3f2"));
        binding.b74.setTextColor(Color.parseColor("#2b0501"));
//        je.setText("騎士");
    }


    public void btn_b(View v) {
        finish();
    }

    FourCardAdd fourCardAdd = new FourCardAdd();

    public class RAdapter extends RecyclerView.Adapter<RAdapter.ContactHolder> {
        List<AtkCard> atkCards;

        public RAdapter(List<AtkCard> atkCards) {
            this.atkCards = atkCards;
        }

        @NonNull
        @Override
        public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.card_change_main, parent, false);
            return new ContactHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
            AtkCard atkCard = atkCards.get(position);
            holder.hpText.setText(atkCard.hp + "");
            holder.mpText.setText(atkCard.mp + "");
            ArrayList<Integer> yc = new ArrayList<>();
            for (int i = 0; i < atkCard.atkR.length; i++) {
                yc.add(atkCard.atkR[i]);
            }
            holder.pg.setText("評價 : " + fourCardAdd.judgingSkillIntensity(atkCard.hp, atkCard.mp, yc));
            atkDraw(atkCard.atkR, holder.line11, holder.line12, holder.line13, holder.line14, holder.line15, holder.line16, holder.line17, holder.line18, holder.line19);
        }

        @Override
        public int getItemCount() {
            return atkCards.size();
        }


        public class ContactHolder extends RecyclerView.ViewHolder {
            TextView hpText;
            TextView mpText;
            TextView pg;
            View line11, line12, line13, line14, line15, line16, line17, line18, line19;

            public ContactHolder(@NonNull View itemView) {
                super(itemView);
                hpText = itemView.findViewById(R.id.HP);
                mpText = itemView.findViewById(R.id.MP);
                line11 = itemView.findViewById(R.id.line11);
                line12 = itemView.findViewById(R.id.line12);
                line13 = itemView.findViewById(R.id.line13);
                line14 = itemView.findViewById(R.id.line14);
                line15 = itemView.findViewById(R.id.line15);
                line16 = itemView.findViewById(R.id.line16);
                line17 = itemView.findViewById(R.id.line17);
                line18 = itemView.findViewById(R.id.line18);
                line19 = itemView.findViewById(R.id.line19);
                pg = itemView.findViewById(R.id.pg);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        view.setAlpha(0.4f);

                        if (lastClick != null && lastClick != view) {
                            lastClick.setAlpha(1.0f);
                            clickInt = getAdapterPosition();
//                            Toast.makeText(view.getContext(),
//                                    "選擇 :  " + clickInt, Toast.LENGTH_SHORT).show();
                        }

                        if (lastClick != null && lastClick == view) {
                            clickInt = CHANGE_CARD;
                            lastClick.setAlpha(1.0f);
//                            Toast.makeText(view.getContext(),
//                                    "取消選擇 :  " + clickInt, Toast.LENGTH_SHORT).show();
                        }

                        if (lastClick == null) {
                            clickInt = getAdapterPosition();
//                            Toast.makeText(view.getContext(),
//                                    "選擇 :  " + clickInt, Toast.LENGTH_SHORT).show();
                        }

                        Log.d("TAG", "onClick: "+recordButton);
                        if(recordButton != 0){
                            changeCard(recordButton-1,getAdapterPosition());

                        }

                        lastClick = view;


//                        backpackRoleChange();
                    }
                });
            }
        }
    }

    View lastClick;
    int clickInt = CHANGE_CARD;

    public void changeCard(final int btn, final int bp) {

        if (bp == CHANGE_CARD) {
            return;
        }
        FUser.child("role").child(tools.roleChange(index)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Integer> x1 = (ArrayList<Integer>) snapshot.child("HP").getValue();
                ArrayList<Integer> x2 = (ArrayList<Integer>) snapshot.child("MP").getValue();
                ArrayList<ArrayList<Integer>> x3 = (ArrayList<ArrayList<Integer>>) snapshot.child("atkR").getValue();

                /**保留按鈕的數據*/
                int z1 = Integer.parseInt(String.valueOf(x1.get(btn)));
                int z2 = Integer.parseInt(String.valueOf(x2.get(btn)));
                ArrayList<Integer> z3 = new ArrayList<>();
                for (int i = 0; i < x3.get(btn).size(); i++) {
                    z3.add(x3.get(btn).get(i));
                }

//                System.out.println("按鈕的數據 : " + x1.get(bp));
                ArrayList<Integer> y1 = (ArrayList<Integer>) snapshot.child("backpack").child("HP").getValue();
                ArrayList<Integer> y2 = (ArrayList<Integer>) snapshot.child("backpack").child("MP").getValue();
                ArrayList<ArrayList<Integer>> y3 = (ArrayList<ArrayList<Integer>>) snapshot.child("backpack").child("atkR").getValue();

//                System.out.println("背包的數據 : " + y1.get(bp));
                /**把背包的數據寫進按鈕*/
                x1.set(btn, y1.get(bp));
                x2.set(btn, y2.get(bp));
                x3.set(btn, y3.get(bp));
//                System.out.println("按鈕交換過後的數據 : " + x1.get(bp));

                /**把按鈕寫進背包*/
                y1.set(bp, z1);
                y2.set(bp, z2);
                y3.set(bp, z3);
//                System.out.println("背包交換過後的數據 : " + y1.get(bp));
//
//                System.out.println("按鈕交換過後的數據 : " + x1);
//                System.out.println("背包交換過後的數據 : " + y1);
                FUser.child("role").child(tools.roleChange(index)).child("HP").setValue(x1);
                FUser.child("role").child(tools.roleChange(index)).child("MP").setValue(x2);
                FUser.child("role").child(tools.roleChange(index)).child("atkR").setValue(x3);

                FUser.child("role").child(tools.roleChange(index)).child("backpack").child("HP").setValue(y1);
                FUser.child("role").child(tools.roleChange(index)).child("backpack").child("MP").setValue(y2);
                FUser.child("role").child(tools.roleChange(index)).child("backpack").child("atkR").setValue(y3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        clickInt = CHANGE_CARD;
        recordButton = 0;
    }

    public void atkDraw(int[] atk, View line11, View line12, View line13, View line14, View line15, View line16, View line17, View line18, View line19) {
        for (int i = 0; i < atk.length; i++) {
            int yc = Integer.parseInt(String.valueOf(atk[i]));
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

    public void atkBR() {
        binding.includeAtk1.includeAtk1.buttonAtk1.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeAtk1.includeAtk2.buttonAtk2.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeAtk1.includeAtk3.buttonAtk3.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeAtk1.includeAtk4.buttonAtk4.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeAtk1.includeAtk5.buttonAtk5.setBackgroundColor(Color.parseColor("#00000000"));
    }

    public int recordButton = 0;

    public void atk1(View v) {
        atkBR();
        if (recordButton == 1) {
            recordButton = 0;
        } else {
            binding.includeAtk1.includeAtk1.buttonAtk1.setBackgroundColor(Color.parseColor("#e0000000"));
            recordButton = 1;
        }

        changeCard(0, clickInt);
    }

    public void atk2(View v) {
        atkBR();
        if (recordButton == 2) {
            recordButton = 0;
        } else {
            binding.includeAtk1.includeAtk2.buttonAtk2.setBackgroundColor(Color.parseColor("#e0000000"));
            recordButton = 2;
        }
        changeCard(1, clickInt);
    }

    public void atk3(View v) {
        atkBR();
        if (recordButton == 3) {
            recordButton = 0;
        } else {
            binding.includeAtk1.includeAtk3.buttonAtk3.setBackgroundColor(Color.parseColor("#e0000000"));
            recordButton = 3;
        }
        changeCard(2, clickInt);
    }

    public void atk4(View v) {
        atkBR();
        if (recordButton == 4) {
            recordButton = 0;
        } else {
            binding.includeAtk1.includeAtk4.buttonAtk4.setBackgroundColor(Color.parseColor("#e0000000"));
            recordButton = 4;
        }
        changeCard(3, clickInt);
    }

    public void atk5(View v) {
        atkBR();
        if (recordButton == 5) {
            recordButton = 0;
        } else {
            binding.includeAtk1.includeAtk5.buttonAtk5.setBackgroundColor(Color.parseColor("#e0000000"));
            recordButton = 5;
        }
        changeCard(4, clickInt);
    }


    StartActivity startActivity = new StartActivity();
    Tools tools = new Tools();

    public void setRole() {

        atkBR();
//        binding.includeAtk1.includeAtk6.buttonAtk6.setEnabled(true);
//        binding.includeAtk1.includeAtk6.buttonAtk6.setBackgroundColor(Color.parseColor("#00000000"));
        /**
         * 1. 更改圖片
         * 2. 更改角色名稱
         * 3. 更改技能組
         */
        startActivity.atkDrawINVISIBLE(binding.includeAtk1.includeAtk1.line11, binding.includeAtk1.includeAtk1.line12, binding.includeAtk1.includeAtk1.line13, binding.includeAtk1.includeAtk1.line14, binding.includeAtk1.includeAtk1.line15, binding.includeAtk1.includeAtk1.line16, binding.includeAtk1.includeAtk1.line17, binding.includeAtk1.includeAtk1.line18, binding.includeAtk1.includeAtk1.line19
                , binding.includeAtk1.includeAtk2.line21, binding.includeAtk1.includeAtk2.line22, binding.includeAtk1.includeAtk2.line23, binding.includeAtk1.includeAtk2.line24, binding.includeAtk1.includeAtk2.line25, binding.includeAtk1.includeAtk2.line26, binding.includeAtk1.includeAtk2.line27, binding.includeAtk1.includeAtk2.line28, binding.includeAtk1.includeAtk2.line29
                , binding.includeAtk1.includeAtk3.line31, binding.includeAtk1.includeAtk3.line32, binding.includeAtk1.includeAtk3.line33, binding.includeAtk1.includeAtk3.line34, binding.includeAtk1.includeAtk3.line35, binding.includeAtk1.includeAtk3.line36, binding.includeAtk1.includeAtk3.line37, binding.includeAtk1.includeAtk3.line38, binding.includeAtk1.includeAtk3.line39
                , binding.includeAtk1.includeAtk4.line41, binding.includeAtk1.includeAtk4.line42, binding.includeAtk1.includeAtk4.line43, binding.includeAtk1.includeAtk4.line44, binding.includeAtk1.includeAtk4.line45, binding.includeAtk1.includeAtk4.line46, binding.includeAtk1.includeAtk4.line47, binding.includeAtk1.includeAtk4.line48, binding.includeAtk1.includeAtk4.line49);
        switch (tools.roleChange(index)) {
            case "j4":
                binding.includeAtk1.includeAtk5.buttonAtk5.setText("續力");
                atkDrawHPMP(j4HP, j4MP);
                startActivity.atkDraw(j4atkR, binding.includeAtk1.includeAtk1.line11, binding.includeAtk1.includeAtk1.line12, binding.includeAtk1.includeAtk1.line13, binding.includeAtk1.includeAtk1.line14, binding.includeAtk1.includeAtk1.line15, binding.includeAtk1.includeAtk1.line16, binding.includeAtk1.includeAtk1.line17, binding.includeAtk1.includeAtk1.line18, binding.includeAtk1.includeAtk1.line19
                        , binding.includeAtk1.includeAtk2.line21, binding.includeAtk1.includeAtk2.line22, binding.includeAtk1.includeAtk2.line23, binding.includeAtk1.includeAtk2.line24, binding.includeAtk1.includeAtk2.line25, binding.includeAtk1.includeAtk2.line26, binding.includeAtk1.includeAtk2.line27, binding.includeAtk1.includeAtk2.line28, binding.includeAtk1.includeAtk2.line29
                        , binding.includeAtk1.includeAtk3.line31, binding.includeAtk1.includeAtk3.line32, binding.includeAtk1.includeAtk3.line33, binding.includeAtk1.includeAtk3.line34, binding.includeAtk1.includeAtk3.line35, binding.includeAtk1.includeAtk3.line36, binding.includeAtk1.includeAtk3.line37, binding.includeAtk1.includeAtk3.line38, binding.includeAtk1.includeAtk3.line39
                        , binding.includeAtk1.includeAtk4.line41, binding.includeAtk1.includeAtk4.line42, binding.includeAtk1.includeAtk4.line43, binding.includeAtk1.includeAtk4.line44, binding.includeAtk1.includeAtk4.line45, binding.includeAtk1.includeAtk4.line46, binding.includeAtk1.includeAtk4.line47, binding.includeAtk1.includeAtk4.line48, binding.includeAtk1.includeAtk4.line49);
                break;
            case "fs":
                binding.includeAtk1.includeAtk5.buttonAtk5.setText("末日");
                atkDrawHPMP(fsHP, fsMP);
                startActivity.atkDraw(fsatkR, binding.includeAtk1.includeAtk1.line11, binding.includeAtk1.includeAtk1.line12, binding.includeAtk1.includeAtk1.line13, binding.includeAtk1.includeAtk1.line14, binding.includeAtk1.includeAtk1.line15, binding.includeAtk1.includeAtk1.line16, binding.includeAtk1.includeAtk1.line17, binding.includeAtk1.includeAtk1.line18, binding.includeAtk1.includeAtk1.line19
                        , binding.includeAtk1.includeAtk2.line21, binding.includeAtk1.includeAtk2.line22, binding.includeAtk1.includeAtk2.line23, binding.includeAtk1.includeAtk2.line24, binding.includeAtk1.includeAtk2.line25, binding.includeAtk1.includeAtk2.line26, binding.includeAtk1.includeAtk2.line27, binding.includeAtk1.includeAtk2.line28, binding.includeAtk1.includeAtk2.line29
                        , binding.includeAtk1.includeAtk3.line31, binding.includeAtk1.includeAtk3.line32, binding.includeAtk1.includeAtk3.line33, binding.includeAtk1.includeAtk3.line34, binding.includeAtk1.includeAtk3.line35, binding.includeAtk1.includeAtk3.line36, binding.includeAtk1.includeAtk3.line37, binding.includeAtk1.includeAtk3.line38, binding.includeAtk1.includeAtk3.line39
                        , binding.includeAtk1.includeAtk4.line41, binding.includeAtk1.includeAtk4.line42, binding.includeAtk1.includeAtk4.line43, binding.includeAtk1.includeAtk4.line44, binding.includeAtk1.includeAtk4.line45, binding.includeAtk1.includeAtk4.line46, binding.includeAtk1.includeAtk4.line47, binding.includeAtk1.includeAtk4.line48, binding.includeAtk1.includeAtk4.line49);
                break;
            case "player":
                binding.includeAtk1.includeAtk5.buttonAtk5.setText("尚未開發");
                atkDrawHPMP(playerHP, playerMP);
                startActivity.atkDraw(playeratkR, binding.includeAtk1.includeAtk1.line11, binding.includeAtk1.includeAtk1.line12, binding.includeAtk1.includeAtk1.line13, binding.includeAtk1.includeAtk1.line14, binding.includeAtk1.includeAtk1.line15, binding.includeAtk1.includeAtk1.line16, binding.includeAtk1.includeAtk1.line17, binding.includeAtk1.includeAtk1.line18, binding.includeAtk1.includeAtk1.line19
                        , binding.includeAtk1.includeAtk2.line21, binding.includeAtk1.includeAtk2.line22, binding.includeAtk1.includeAtk2.line23, binding.includeAtk1.includeAtk2.line24, binding.includeAtk1.includeAtk2.line25, binding.includeAtk1.includeAtk2.line26, binding.includeAtk1.includeAtk2.line27, binding.includeAtk1.includeAtk2.line28, binding.includeAtk1.includeAtk2.line29
                        , binding.includeAtk1.includeAtk3.line31, binding.includeAtk1.includeAtk3.line32, binding.includeAtk1.includeAtk3.line33, binding.includeAtk1.includeAtk3.line34, binding.includeAtk1.includeAtk3.line35, binding.includeAtk1.includeAtk3.line36, binding.includeAtk1.includeAtk3.line37, binding.includeAtk1.includeAtk3.line38, binding.includeAtk1.includeAtk3.line39
                        , binding.includeAtk1.includeAtk4.line41, binding.includeAtk1.includeAtk4.line42, binding.includeAtk1.includeAtk4.line43, binding.includeAtk1.includeAtk4.line44, binding.includeAtk1.includeAtk4.line45, binding.includeAtk1.includeAtk4.line46, binding.includeAtk1.includeAtk4.line47, binding.includeAtk1.includeAtk4.line48, binding.includeAtk1.includeAtk4.line49);
                break;
            case "b74":
                binding.includeAtk1.includeAtk5.buttonAtk5.setText("隔檔");
                atkDrawHPMP(b74HP, b74MP);
                startActivity.atkDraw(b74atkR, binding.includeAtk1.includeAtk1.line11, binding.includeAtk1.includeAtk1.line12, binding.includeAtk1.includeAtk1.line13, binding.includeAtk1.includeAtk1.line14, binding.includeAtk1.includeAtk1.line15, binding.includeAtk1.includeAtk1.line16, binding.includeAtk1.includeAtk1.line17, binding.includeAtk1.includeAtk1.line18, binding.includeAtk1.includeAtk1.line19
                        , binding.includeAtk1.includeAtk2.line21, binding.includeAtk1.includeAtk2.line22, binding.includeAtk1.includeAtk2.line23, binding.includeAtk1.includeAtk2.line24, binding.includeAtk1.includeAtk2.line25, binding.includeAtk1.includeAtk2.line26, binding.includeAtk1.includeAtk2.line27, binding.includeAtk1.includeAtk2.line28, binding.includeAtk1.includeAtk2.line29
                        , binding.includeAtk1.includeAtk3.line31, binding.includeAtk1.includeAtk3.line32, binding.includeAtk1.includeAtk3.line33, binding.includeAtk1.includeAtk3.line34, binding.includeAtk1.includeAtk3.line35, binding.includeAtk1.includeAtk3.line36, binding.includeAtk1.includeAtk3.line37, binding.includeAtk1.includeAtk3.line38, binding.includeAtk1.includeAtk3.line39
                        , binding.includeAtk1.includeAtk4.line41, binding.includeAtk1.includeAtk4.line42, binding.includeAtk1.includeAtk4.line43, binding.includeAtk1.includeAtk4.line44, binding.includeAtk1.includeAtk4.line45, binding.includeAtk1.includeAtk4.line46, binding.includeAtk1.includeAtk4.line47, binding.includeAtk1.includeAtk4.line48, binding.includeAtk1.includeAtk4.line49);
                break;
        }
    }

    public void atkDrawHPMP(ArrayList<Integer> HP, ArrayList<Integer> MP) {
        binding.includeAtk1.HP1.setText(HP.get(0) + "");
        binding.includeAtk1.HP2.setText(HP.get(1) + "");
        binding.includeAtk1.HP3.setText(HP.get(2) + "");
        binding.includeAtk1.HP4.setText(HP.get(3) + "");
        binding.includeAtk1.HP5.setText(HP.get(4) + "");
        binding.includeAtk1.MP1.setText(MP.get(0) + "");
        binding.includeAtk1.MP2.setText(MP.get(1) + "");
        binding.includeAtk1.MP3.setText(MP.get(2) + "");
        binding.includeAtk1.MP4.setText(MP.get(3) + "");
        binding.includeAtk1.MP5.setText(MP.get(4) + "");
    }
}