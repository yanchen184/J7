package com.example.j7.game;

public enum playerMoraList {
    石頭((byte) 0, "石頭"),
    剪刀((byte) 1, "剪刀"),
    布((byte) 2, "布"),
    還沒出((byte) 3, "還沒出");

    private int id;
    private String choose;

    playerMoraList(byte id, String choose) {
        this.id = id ;
        this.choose = choose ;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return choose;
    }

    public static playerMoraList enumOfId(int id) {
        for (playerMoraList pf : playerMoraList.values()) {
            if (pf.getId() == id) {
                return pf;
            }
        }
        return null;
    }
}


