package com.example.j7;

import java.util.ArrayList;

public class Variable {

    int index; // 自己使用的角色
    int indexE;//對手使用的角色
    String playerName;//自己的名字
    String otherPlayerName;//對手的名字

    String roomKey; // 房間號碼

    ArrayList<Integer> finalHP;//自己所有招式所有傷害
    ArrayList<Integer> finalMP;//自己所有招式所有魔力
    ArrayList<ArrayList<Integer>> finalAtlR;//自己所有招式所有攻擊範圍
    String player; //自己是player幾? player1 or player2
    String otherPlayer; //對手是player幾? player1 or player2


    Boolean unique = false; //自己是否開啟了獨有技能


    int roleS; //自己使用的角色
    int roleC; //對手使用的角色

    int upHPInt; // 回血量
    int upMPInt; // 回魔量

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getUpHPInt() {
        return upHPInt;
    }

    public void setUpHPInt(int upHPInt) {
        this.upHPInt = upHPInt;
    }

    public int getUpMPInt() {
        return upMPInt;
    }

    public void setUpMPInt(int upMPInt) {
        this.upMPInt = upMPInt;
    }



    public int getRoleS() {
        return roleS;
    }

    public void setRoleS(int roleS) {
        this.roleS = roleS;
    }

    public int getRoleC() {
        return roleC;
    }

    public void setRoleC(int roleC) {
        this.roleC = roleC;
    }

    public String getOtherPlayerName() {
        return otherPlayerName;
    }

    public void setOtherPlayerName(String otherPlayerName) {
        this.otherPlayerName = otherPlayerName;
    }

    public String getOtherPlayer() {
        return otherPlayer;
    }

    public void setOtherPlayer(String otherPlayer) {
        this.otherPlayer = otherPlayer;
    }


    public ArrayList<Integer> getFinalHP() {
        return finalHP;
    }

    public void setFinalHP(ArrayList<Integer> finalHP) {
        this.finalHP = finalHP;
    }

    public ArrayList<Integer> getFinalMP() {
        return finalMP;
    }

    public void setFinalMP(ArrayList<Integer> finalMP) {
        this.finalMP = finalMP;
    }

    public ArrayList<ArrayList<Integer>> getFinalAtlR() {
        return finalAtlR;
    }

    public void setFinalAtlR(ArrayList<ArrayList<Integer>> finalAtlR) {
        this.finalAtlR = finalAtlR;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }

    public int getIndexE() {
        return indexE;
    }

    public void setIndexE(int indexE) {
        this.indexE = indexE;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
