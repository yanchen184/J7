package com.example.j7.game;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.j7.GameActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ConnectManager extends AppCompatActivity {

    private Context context;
    private WebSocket webSocket;
    private GameActivity activity;
    private TextView txtCom;
    private TextView txtWinLose;
    public static String SERVER_PATH = "http://925836ab87fa.ngrok.io";

    /**
     * 用來避免改變tgBtn狀態時不知道是收到還是發送的狀況
     */
    public boolean isReceiving = false;

    //TODO
//    private SkillUtil util = new SkillUtil();

    public ConnectManager(Context context) {
        this.context = context;
        activity = (GameActivity) context;
    }


    public void initiateSocketConnection() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        System.out.println("request : " + request);
        if (request != null) {
            webSocket = client.newWebSocket(request, new SocketListener());
        }
    }

    private class SocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "連線成功 Socket Connection Successful",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            super.onMessage(webSocket, text);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(text);
                        /**調整位移跟攻擊*/
                        JSONArray jsonArray = new JSONArray("[" + text + "]");
                        JSONArray jsonArrayOrder = new JSONArray();

                        /**改變位置*/
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            String kind = jsonObject.getString("kind");//kind
                            if (!kind.equals("atk")) {
                                jsonArrayOrder.put(jsonObject);
                            }
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            String kind = jsonObject.getString("kind");//kind
                            if (kind.equals("atk")) {
                                jsonArrayOrder.put(jsonObject);
                            }
                        }

                        receiveMessage(jsonArrayOrder);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            System.err.println("throwable:" + t);
            System.err.println("response:" + response);
        }
    }


    /**
     * bind with GameActivity
     *
     * @param
     */
    private void receiveMessage(JSONArray jsonArray) {
//        isReceiving = true;
        activity.fireVisible();//收到
//        int totalTime = 4;
//        new Handler().postDelayed(() -> {
//            rm(0, 1, jsonArray);
//        }, 1500);
//        new Handler().postDelayed(() -> {
//            rm(1, 2, jsonArray);
//        }, 3000);
//        new Handler().postDelayed(() -> {
//            rm(2, 3, jsonArray);
//        }, 4500);
//        new Handler().postDelayed(() -> {
//            rm(3, 4, jsonArray);
//        }, 6000);

//        new CountDownTimer(totalTime * 1000, 1000) {
//            public void onTick(long millisUntilFinished) {
//                int second = Math.round(millisUntilFinished / 1000);
//                    int startTime = totalTime - second;
//                    rm(startTime, startTime + 1, jsonArray);
//                    System.out.println("倒數計時 : " + Math.round(millisUntilFinished / 1000));
//            }
//            public void onFinish() {
//            }
//        }.start();

        try {
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);//如果收到的第一筆資料 -> restart
            String kind = jsonObject.getString("kind");
            if (kind == "restart") {
//                activity.initGame1();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("JSON_STRING", jsonArray.toString());
            bundle.putInt("NUM", i);
            msg.setData(bundle);
            msg.what = JSON_STRING;
            handler.sendMessageDelayed(msg, 1000 * (i + 1));
        }
    }

    private static final int JSON_STRING = 1;
    private MyHandler handler = new MyHandler();
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case JSON_STRING:
                    String x = msg.getData().getString("JSON_STRING");
                    int start = msg.getData().getInt("NUM");
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(x);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rm(start, start + 1, jsonArray);//最終執行
                    break;
                default:
                    break;
            }
        }
    }


    public void rm(int start, int end, JSONArray jsonArray) {
        try {
            for (int i = start; i < end; i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String sender = jsonObject.getString("USER");//被操作人是誰
                System.out.println("執行 : " + i);
                switch (jsonObject.getString("kind")) {
                    case "move":
                        int x = jsonObject.getInt("x");
                        int y = jsonObject.getInt("y");
                        System.out.println("是不是自己要移動 : " + sender.equals(activity.userName));
                        if (sender.equals(activity.userName)) {
                            activity.moveRules.moveJudgmentSelf(x, y);
                        } else {
                            activity.moveRules.moveJudgmentCom(x, y);
                        }
                        break;
                    case "up":
                        int l = jsonObject.getInt("l");
                        String pp = jsonObject.getString("pp");
                        System.out.println("是不是自己要回復 " + pp + " : " + sender.equals(activity.userName));
                        if (sender.equals(activity.userName)) {
//                            activity.upRules.upJudgmentSelf(l, pp);
                        } else {
//                            activity.upRules.upJudgmentCom(l, pp);
                        }
                        break;
                }
            }
            for (int i = start; i < end; i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String sender = jsonObject.getString("USER");//被操作人是誰
                switch (jsonObject.getString("kind")) {
                    case "atk":
                        System.out.println("執行 : " + i);
                        System.out.println("是不是自己要攻擊 : " + sender.equals(activity.userName));
                        String atk = jsonObject.getString("atk");
                        atk = atk.substring(1, atk.length() - 1);
                        atk = atk.replace(" ", "");
                        String c[] = atk.split(",");
                        int[] atkx = new int[c.length];
                        if (!c[0].equals("ul")) {
                            for (int j = 0; j < c.length; j++) {
                                System.out.print(c[j] + " ");
                                System.out.println("攻擊位置");
                                atkx[j] = Integer.parseInt(c[j]);
                            }
                        } else {
                            atkx[0] = 0;
                        }

                        int hp = jsonObject.getInt("hp");
                        int mp = jsonObject.getInt("mp");

                        if (sender.equals(activity.userName)) {
//                            activity.atkRules.atkJudgmentSelf(atkx, hp, mp);
                        } else {
//                            activity.atkRules.atkJudgmentCom(atkx, hp, mp);
                        }
                        break;
                }
            }
            activity.moveVisible();
            if (jsonArray.length() == end) {
                activity.init();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * bind with GameActivity
     *
     * @param num
     * @param kind
     */
    public void sendMessage(int[] num, int hp, int mp, String kind) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("kind", kind);
            jsonObject.put("USER", activity.userName);
            switch (kind) {
                case "atk":
                    jsonObject.put("atk", Arrays.toString(num));
                    jsonObject.put("hp", hp);
                    jsonObject.put("mp", mp);
                    webSocket.send(jsonObject.toString());
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(int step, int x, int y, String kind) {
//        if (!isReceiving) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("kind", kind);
            jsonObject.put("USER", activity.userName);
            switch (kind) {
                case "move":
//                    jsonObject.put("step", step);
                    jsonObject.put("x", x);
                    jsonObject.put("y", y);
                    webSocket.send(jsonObject.toString());
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        }
    }


    public void sendMessage(int l, String pp, String kind) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("kind", kind);
            jsonObject.put("USER", activity.userName);
            switch (kind) {
                case "up":
                    jsonObject.put("l", l);
                    jsonObject.put("pp", pp);
                    webSocket.send(jsonObject.toString());
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * bind with GameActivity
     */
    public void sendMessage(String kind) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("kind", kind);
            jsonObject.put("USER", activity.userName);
            switch (kind) {
                case "restart":
                    webSocket.send(jsonObject.toString());
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * bind with GameActivity
     */
    public void cancelConnect() {
//        View layout_pair = findViewById(R.id.layout_pair);
//        View layout_pair1 = findViewById(R.id.layout_pair1);
//        layout_pair.setVisibility(View.INVISIBLE);
//        layout_pair1.setVisibility(View.INVISIBLE);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return activity.findViewById(id);
    }
}
