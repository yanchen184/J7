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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.TextView;

import com.example.j7.R;
import com.example.j7.StartActivity;
import com.example.j7.databinding.ActivityFourCardChangeBinding;
import com.example.j7.tools.Tools;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.j7.LoginActivity.userId;

public class FourCardChange extends AppCompatActivity {


    public int[][] fsAtk = {{4, 5, 6}, {1, 2, 3, 5, 7, 8, 9}, {1, 3, 5, 7, 9}, {1, 3, 4, 6, 7, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}, {4, 5, 6}, {1, 2, 3, 5, 7, 8, 9}, {1, 3, 5, 7, 9}, {1, 3, 4, 6, 7, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}};
    public int[] fsHP = {4, 2, 5, 2, 5, 4, 2, 5, 2, 5};
    public int[] fsMP = {4, 5, 3, 6, 8, 4, 2, 5, 2, 5};
    public RAdapter rAdapter;
    public List<AtkCard> atkCardList = new ArrayList<>();
    public RecyclerView recyclerView;
    //    public Model model;
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
//        rAdapter = new RAdapter(atkCardList);
//        recyclerView.setAdapter(rAdapter);
        backpackRoleChange("j4");
        binding.j4.setBackgroundColor(Color.parseColor("#00000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.player.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#e0000000"));
    }


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
//        je.setText("劍士");
        binding.j4.setBackgroundColor(Color.parseColor("#00000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.player.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#e0000000"));

    }


    public void fsAdd(View v) {
        backpackRoleChange("fs");
        record.setValue(1);
        binding.j4.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#00000000"));
        binding.player.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#e0000000"));
//        je.setText("法師");
    }

    public void playerAdd(View v) {
        backpackRoleChange("player");
        record.setValue(2);
        binding.j4.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.player.setBackgroundColor(Color.parseColor("#00000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#e0000000"));
//        je.setText("普通人");
    }

    public void b74Add(View v) {
        backpackRoleChange("b74");
        record.setValue(3);
        binding.j4.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.fs.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.player.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.b74.setBackgroundColor(Color.parseColor("#00000000"));
//        je.setText("騎士");
    }


    public void btn_b(View v) {
        finish();
    }

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
            atkDraw(atkCard.atkR, holder.line11, holder.line12, holder.line13, holder.line14, holder.line15, holder.line16, holder.line17, holder.line18, holder.line19);
        }

        @Override
        public int getItemCount() {
            return atkCards.size();
        }


        public class ContactHolder extends RecyclerView.ViewHolder {
            TextView hpText;
            TextView mpText;
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
            }
        }
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

    StartActivity startActivity = new StartActivity();
    Tools tools = new Tools();

//    public void setRole() {
//        /**
//         * 1. 更改圖片
//         * 2. 更改角色名稱
//         * 3. 更改技能組
//         */
//        startActivity.atkDrawINVISIBLE();
//
//
//        switch (tools.roleChange(role)) {
//            case "j4":
//                buttonAtk5.setText("續力");
//                atkDrawHPMP(j4HP, j4MP);
//                atkDraw(j4atkR);
//                break;
//            case "fs":
//                buttonAtk5.setText("末日");
//                atkDrawHPMP(fsHP, fsMP);
//                atkDraw(fsatkR);
//                break;
//            case "player":
//                buttonAtk5.setText("尚未開發");
//                atkDrawHPMP(playerHP, playerMP);
//                atkDraw(playeratkR);
//                break;
//            case "b74":
//                buttonAtk5.setText("隔檔");
//                atkDrawHPMP(b74HP, b74MP);
//                atkDraw(b74atkR);
//                break;
//        }
//
//    }


}