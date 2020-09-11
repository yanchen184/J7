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
