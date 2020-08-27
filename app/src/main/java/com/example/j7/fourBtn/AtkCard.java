package com.example.j7.fourBtn;

public class AtkCard {

    int hp;
    int mp;
    int[] atkR ;

    public AtkCard(int hp, int mp, int[] atkR) {
        this.hp = hp;
        this.mp = mp;
        this.atkR = atkR;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int[] getAtkR() {
        return atkR;
    }

    public void setAtkR(int[] atkR) {
        this.atkR = atkR;
    }


}
