package com.example.j7;

public class Parameter {
    int upHPB74 = 3;
    int upHPOther = 2;
    int upMPFs = 5;
    int upMPOther = 3;

    /**
     * 攻擊範圍
     */
    int[][] atkRangeSelf;
    int[][] atkRangeCom;
    int[][] atkRangeDD;

    /**
     * 角色位置
     */
    int locationXS;
    int locationYS;
    int locationXC;
    int locationYC;


    int atkNumS;
    int atkNumC;

    /*****      Boss 0      *****/
    public int[][] Boss0Atk = {{4, 5, 6}, {2, 5, 8}, {5, 6}, {1, 3, 4, 6, 7, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}, {0}};
    public int[] Boss0HP = {4, 4, 5, 2, 0, 0};
    public int[] Boss0MP = {3, 3, 3, 6, 2, 0};
    public int HPUPBoss0 = 3;
    public int MPUPBoss0 = 5;
    public int HP0 = 20;
    public int MP0 = 20;
    /*****      Boss 1      *****/
    public int[][] Boss1Atk = {{4, 5, 6}, {2, 5, 8}, {5, 6}, {1, 3, 4, 6, 7, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}, {0}};
    public int[] Boss1HP = {4, 4, 5, 2, 0, 0};
    public int[] Boss1MP = {3, 3, 3, 6, 2, 0};
    public int HPUPBoss1 = 3;
    public int MPUPBoss1 = 5;
    public int HP1 = 20;
    public int MP1 = 20;

    /*****      Boss 2      *****/
    public int[][] Boss2Atk = {{4, 5, 6}, {2, 5, 8}, {5, 6}, {1, 3, 4, 6, 7, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}, {0}};
    public int[] Boss2HP = {4, 4, 5, 2, 0, 0};
    public int[] Boss2MP = {3, 3, 3, 6, 2, 0};
    public int HPUPBoss2 = 3;
    public int MPUPBoss2 = 5;
    public int HP2 = 50;
    public int MP2 = 50;

    /*****      Boss 3      *****/
    public int[][] Boss3Atk = {{4, 5, 6}, {2, 5, 8}, {5, 6}, {1, 3, 4, 6, 7, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}, {0}};
    public int[] Boss3HP = {4, 4, 5, 2, 0, 0};
    public int[] Boss3MP = {3, 3, 3, 6, 2, 0};
    public int HPUPBoss3 = 3;
    public int MPUPBoss3 = 5;
    public int HP3 = 100;
    public int MP3 = 100;

    /*****      Boss 4      *****/
    public int[][] Boss4Atk = {{1, 2, 3, 4, 5, 6, 7, 8, 9}, {1, 2, 3, 4, 6, 7, 8, 9}, {2, 4, 5, 6, 8}, {1, 2, 3, 4, 7, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}, {0}};
    public int[] Boss4HP = {7, 9, 11, 10, 0, 0};
    public int[] Boss4MP = {10, 10, 10, 10, 10, 0};
    public int HPUPBoss4 = 10;
    public int MPUPBoss4 = 10;
    public int HP4 = 200;
    public int MP4 = 200;


    public int getUpHPB74() {
        return upHPB74;
    }

    public void setUpHPB74(int upHPB74) {
        this.upHPB74 = upHPB74;
    }

    public int getUpHPOther() {
        return upHPOther;
    }

    public void setUpHPOther(int upHPOther) {
        this.upHPOther = upHPOther;
    }

    public int getUpMPFs() {
        return upMPFs;
    }

    public void setUpMPFs(int upMPFs) {
        this.upMPFs = upMPFs;
    }

    public int getUpMPOther() {
        return upMPOther;
    }

    public void setUpMPOther(int upMPOther) {
        this.upMPOther = upMPOther;
    }

    public int getAtkNumS() {
        return atkNumS;
    }

    public void setAtkNumS(int atkNumS) {
        this.atkNumS = atkNumS;
    }

    public int getAtkNumC() {
        return atkNumC;
    }

    public void setAtkNumC(int atkNumC) {
        this.atkNumC = atkNumC;
    }

    public int[][] getAtkRangeSelf() {
        return atkRangeSelf;
    }

    public void setAtkRangeSelf(int[][] atkRangeSelf) {
        this.atkRangeSelf = atkRangeSelf;
    }

    public int[][] getAtkRangeCom() {
        return atkRangeCom;
    }

    public void setAtkRangeCom(int[][] atkRangeCom) {
        this.atkRangeCom = atkRangeCom;
    }

    public int[][] getAtkRangeDD() {
        return atkRangeDD;
    }

    public void setAtkRangeDD(int[][] atkRangeDD) {
        this.atkRangeDD = atkRangeDD;
    }

    public int getLocationXS() {
        return locationXS;
    }

    public void setLocationXS(int locationXS) {
        this.locationXS = locationXS;
    }

    public int getLocationYS() {
        return locationYS;
    }

    public void setLocationYS(int locationYS) {
        this.locationYS = locationYS;
    }

    public int getLocationXC() {
        return locationXC;
    }

    public void setLocationXC(int locationXC) {
        this.locationXC = locationXC;
    }

    public int getLocationYC() {
        return locationYC;
    }

    public void setLocationYC(int locationYC) {
        this.locationYC = locationYC;
    }


}
