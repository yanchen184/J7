package com.example.j7.game;


public class AtkKind {

    private static int[] atk0 = {4, 5, 6};
    private static int[] atk1 = {1, 3, 5, 7, 9};
    private static int[] atk2 = {1, 2, 3, 5};
    private static int[] atk3 = {1, 2, 3, 5};

//    public static int[][][] atk = new int[][][]{pointJB(atk0), pointJB(atk1), pointJB(atk2) ,  pointJB(atk3)};
    public static int[] atkHP = new int[]{5, 6};
    public static int[] atkMP = new int[]{5, 6};




    public static int getAtkHP(int x) {
        return atkHP[x];
    }

    public static int getAtkMP(int x) {
        return atkMP[x];
    }


}


