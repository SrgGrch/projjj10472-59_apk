package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Pair;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragments.EntryFragment;
import com.example.myapplication.Fragments.GameInterfaceFragment;
import com.example.myapplication.Fragments.LoadingFragment;
import com.example.myapplication.SocketHelpers.Building;
import com.example.myapplication.SocketHelpers.DamagedData;
import com.example.myapplication.SocketHelpers.ErrorData;
import com.example.myapplication.SocketHelpers.InventoryItem;
import com.example.myapplication.SocketHelpers.LocationStates;
import com.example.myapplication.SocketHelpers.LoginData;
import com.example.myapplication.SocketHelpers.MapObject;
import com.example.myapplication.SocketHelpers.MapObjectSocketData;
import com.example.myapplication.SocketHelpers.NewStatesData;
import com.example.myapplication.SocketHelpers.PlayerSocketData;
import com.example.myapplication.SocketHelpers.SocketData;
import com.example.myapplication.SocketHelpers.UpdateBuilding;
import com.example.myapplication.helpers.ItemState;
import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class API {
    private final String TAG = "WebSocket";
    private final MySocket socket;
    private final List<Pair<String, Object>> queue = new ArrayList<>();

    public API(){
        socket = new MySocket(App.wsHostName);
        socket.start();
    }

    public void emit(String method, Object otherData){
        if (socket != null && socket.isConnected()){
            socket.emit(method, otherData);
        } else {
            queue.add(new Pair<>(method, otherData));
        }
    }

    class MySocket extends Thread {
        private final Gson gson = new Gson();
        private WebSocket ws;
        private final String serverAddress;

        public MySocket (String serverAddress){
            this.serverAddress = serverAddress;
        }

        public void run(){
            try {
                LoadingFragment _f = App.showLoading("Подключение к серверу");

                ws = new WebSocketFactory().createSocket(serverAddress);
                ws.addListener(new WebSocketAdapter() {
                    private LoadingFragment f = _f;
                    @Override
                    public void onTextMessage(WebSocket websocket, String text) {
                        DataFromJson msg = gson.fromJson(text, DataFromJson.class);
                        String method = msg.getMethod();

//                        if (!msg.getMethod().equals("updateState")){
//                            Log.d(TAG + "message", text);
//                        }

                        if ("login".equals(method)) {
                            LoginData data = msg.getData().getLoginData();

                            if (!data.isGuest()) App.prefs
                                    .edit()
                                    .putString("token", data.getToken())
                                    .apply();
                            App.mainPlayer.setNickname(data.getNickname());
                            App.mainPlayer.setId(data.getId());

                            Fragment loginScreen = App.fm.findFragmentByTag(App.TAGS.LOGIN_SCREEN);
                            Fragment registrationScreen = App.fm.findFragmentByTag(App.TAGS.REGISTRATION_SCREEN);
                            Fragment entryScreen = App.fm.findFragmentByTag(App.TAGS.ENTRY_SCREEN);
                            Fragment menuScreen = App.fm.findFragmentByTag(App.TAGS.MENU_SCREEN);

                            FragmentTransaction ft = App.fm.beginTransaction();
                            ft.setCustomAnimations(R.anim.slide_from_bottom, R.anim.slide_to_bottom);

                            if (loginScreen != null) ft.remove(loginScreen);
                            if (registrationScreen != null) ft.remove(registrationScreen);
                            if (entryScreen != null) ft.remove(entryScreen);
                            if (menuScreen != null) ft.show(menuScreen).commit();
                        } else if ("error".equals(method)) {
                            ErrorData data = msg.getData().getErrorData();

                            if (data.isWrongToken()) {
                                App.fm
                                        .beginTransaction()
                                        .add(App.mainView.getId(), new EntryFragment(), App.TAGS.ENTRY_SCREEN)
                                        .commit();
                                App.prefs.edit().remove("token").apply();
                            }
                            App.showMessage(data.getMessage());
                        } else if ("updateStates".equals(method)) {
                            NewStatesData data = msg.getData().getNewStatesData();
                            if (data.getObjects() != null) {
                                for (MapObjectSocketData o : data.getObjects()) {
                                    if (!App.objects.containsKey(o.getId())) {
                                        App.objects.put(o.getId(), new MapObject(o.getId(), o.getX(), o.getY(), o.getRadius(), o.getType()));
                                    }
                                }
                            }

                            if (data.getPlayers() != null) {
                                for (PlayerSocketData p : data.getPlayers()) {
                                    if (p.getId() == App.mainPlayer.getId()) {
                                        App.mainPlayer.update(p);
                                    } else {
                                        if (!App.players.containsKey(p.getId())) {
                                            App.players.put(p.getId(), new Player(p));
                                        } else {
                                            App.players.get(p.getId()).update(p);
                                        }
                                    }
                                }
                            }

                            if (data.getUseWeapon() != null) {
                                if (data.getUseWeapon().user_id == App.mainPlayer.getId()) {
                                    App.mainPlayer.useWeapon(
                                            data.getUseWeapon().useItemTime,
                                            data.getUseWeapon().degrees,
                                            data.getUseWeapon().length
                                    );
                                } else {
                                    App.players.get(data.getUseWeapon().user_id).useWeapon(
                                            data.getUseWeapon().useItemTime,
                                            data.getUseWeapon().degrees,
                                            data.getUseWeapon().length
                                    );
                                }
                            }
                        } else if ("playerLeave".equals(method)) {
                            PlayerSocketData data = msg.getData().getPlayer();

                            App.players.remove(data.getId());
                        } else if ("updateLocation".equals(method)) {
                            LocationStates data = msg.getData().getLocationStates();

                            App.objects.clear();
                            for (MapObjectSocketData o : data.getObjects()) {
                                App.objects.put(o.getId(), new MapObject(o.getId(), o.getX(), o.getY(), o.getRadius(), o.getType()));
                            }

                            App.players.clear();
                            for (PlayerSocketData p : data.getPlayers()) {
                                if (p.getId() == App.mainPlayer.getId()) {
                                    App.mainPlayer.update(p);
                                } else {
                                    App.players.put(p.getId(), new Player(p));
                                }
                            }

                            App.width = data.getWidth();
                            App.height = data.getHeight();
                            App.size = data.getSize();

                            App.buildings = data.getBuildings();
                            Building.updateBuildingsToDraw();

                            initGame();
                        } else if ("updateInventory".equals(method)) {
                            App.inventory = msg.getData().getInventory();
                            App.updateInventory();
                        } else if ("updateBuilding".equals(method)) {
                            UpdateBuilding data = msg.getData().getUpdateBuilding();
                            if (App.buildings != null){
                                App.buildings[data.getX()][data.getY()] = data.getBuilding();
                            }
                            Building.updateBuildingsToDraw();
                        } else if ("damaged".equals(method)){
                            DamagedData damaged = msg.getData().getDamaged();
                            if (App.mainPlayer.getId() == damaged.id){
                                App.mainPlayer.damaged(damaged.damage);
                            } else {
                                Player p = App.players.get(damaged.id);
                                if (p != null){
                                    p.damaged(damaged.damage);
                                }
                            }
                        } else if ("updateItems".equals(method)) {
                            ItemState[] data = msg.getData().getItemStates();

                            for (ItemState itemState : data) {
                                App.inventoryItemStats.put(itemState.name, itemState);

                                int resId = App.mainView.getResources().getIdentifier(
                                        itemState.name,
                                        "drawable",
                                        App.PACKAGE_NAME
                                );

                                App.Images.addImage(itemState.name, App.getBitmap(resId));

                                if (itemState.type.equals("building")) {
                                    Bitmap b = App.getScaledBitmap(resId, (int) (App.q * 80), (int) (App.q * 80));
                                    Building.addBitmap(itemState.name, b);
                                    if (itemState.name.endsWith("_wall")) {
//                                        Building.addBitmap(
//                                                itemState.name + "_helper",
//                                                App.getScaledBitmap(
//                                                        App.mainView.getResources().getIdentifier(
//                                                                itemState.name + "_helper",
//                                                                "drawable",
//                                                                App.PACKAGE_NAME),
//                                                        (int) (App.q * 80), (int) (App.q * 80))
//                                        );
                                        String[] v = new String[]{"_l", "_b", "_r", "_t"};
                                        int rot = 90;
                                        for (int i = 0; i < 4; i++){
                                            Matrix m = new Matrix();
                                            m.setRotate(rot, b.getWidth()*App.q/2f, b.getHeight()*App.q/2f);
                                            Bitmap helper = App.getScaledBitmap(
                                                    App.mainView.getResources().getIdentifier(
                                                            itemState.name + "_helper",
                                                            "drawable",
                                                            App.PACKAGE_NAME),
                                                    (int) (App.q * 80), (int) (App.q * 80));
                                            Bitmap bitmap = Bitmap.createBitmap(helper, 0, 0, helper.getWidth(), helper.getHeight(), m, true);
                                            rot += 90;
                                            Building.addBitmap(itemState.name + "_helper" + v[i], bitmap);
                                        }
                                    }
                                }
                            }
                        } else if ("died".equals(method)){
                            Fragment menuScreen = App.fm.findFragmentByTag(App.TAGS.MENU_SCREEN);
                            if (menuScreen != null) App.fm.beginTransaction().show(menuScreen).commit();
                            App.showMessage("Вы были убиты!");
                            ( (Game) App.fm.findFragmentByTag(App.TAGS.GAME_SCREEN).getView() ).endGame();
                        }
                    }

                    @Override
                    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                        super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
                        if (App.mainPlayer.isPlaying){
                            ((Game)App.fm.findFragmentByTag(App.TAGS.GAME_SCREEN).getView()).endGame();
                        }
                        f = App.showLoading("Подключение к серверу");
                        boolean trying = true;
                        while (trying) {
                            try {
                                ws = ws.recreate().connect();
                                trying = false;
                            } catch (Exception e) {
                                sleep(200);
                            }
                        }
                    }

                    @Override
                    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                        super.onConnected(websocket, headers);
                        Log.d(TAG, "onConnect");
                        App.hideLoading(f);

                        for (int i = 0; i < queue.size(); i++){
                            emit(queue.get(i).first, queue.get(i).second);
                        }
                    }

                    @Override
                    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
                        super.onConnectError(websocket, exception);
                        Log.d(TAG, "onConnectError: " + exception.toString());
                    }

                    @Override
                    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
                        super.onError(websocket, cause);
                        Log.d(TAG, "onError: " + cause.toString());
                    }

                    @Override
                    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
                        super.onUnexpectedError(websocket, cause);
                        Log.d(TAG, "onUnexpectedError: " + cause.toString());
                    }

                    private void initGame(){
                        App.fm
                                .beginTransaction()
                                .setCustomAnimations(R.animator.animation_fade_in_fast, R.animator.animation_in_without_effect)
                                .add(App.mainView.getId(), new GameInterfaceFragment(), App.TAGS.GAME_INTERFACE_SCREEN)
                                .commit();
                        App.hideLoading();
                        App.mainPlayer.isPlaying = true;
                        App.interfaceReady = true;

//                            new Timer().scheduleAtFixedRate(new TimerTask() {
//                                private int i = 0;
//                                private final float will_q = App.mainView.getHeight() / 800f;
//                                private float curr_q = will_q / 3f;
//                                private final float tick = (will_q - curr_q) / 100f;
//                                @Override
//                                public void run() {
//                                    curr_q += tick;
//                                    App.q = curr_q;
//                                    i++;
//                                    if (i >= 100){
//                                        App.q = will_q;
//                                        cancel();
//                                    }
//                                }
//                            }, 0, 10);
                    }
                });
                boolean trying = true;
                while (trying) {
                    try {
                        ws = ws.connect();
                        trying = false;
                    } catch (Exception e) {
                        ws = ws.recreate();
                    }
                }
            } catch (IOException ignore) {}
        }
        public void emit(String method, Object otherData){
            if (ws != null && ws.isOpen()){
                ws.sendText(gson.toJson(new DataToSend(method, otherData)));
            }
        }
        public boolean isConnected(){
            return this.ws != null && this.ws.isOpen();
        }
    }

    class DataToSend {
        private final String method;
        private final Object data;

        public DataToSend(String method, Object data){
            this.method = method;
            this.data = data;
        }
    }

    class DataFromJson {
        private String method;
        private SocketData data;

        public String getMethod() {
            return method;
        }

        public SocketData getData() {
            return data;
        }
    }
}