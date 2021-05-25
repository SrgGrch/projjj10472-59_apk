package com.example.myapplication.Fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.App;
import com.example.myapplication.CustomViews.CircleView;
import com.example.myapplication.CustomViews.MainInventoryButton;
import com.example.myapplication.CustomViews.OpenInventoryButton;
import com.example.myapplication.Player;
import com.example.myapplication.R;
import com.example.myapplication.SocketHelpers.InventoryItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ClickableViewAccessibility")
public class GameInterfaceFragment extends Fragment {
    private float topPlayersTime = 4000;

    private List<MainInventoryButton> mainInventoryButtons;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout view = (FrameLayout)inflater.inflate(R.layout.game_interface, container, false);
        view.setId(R.id.game_interface);

        App.fm.findFragmentByTag(App.TAGS.CIRCLE_SCREEN).getView().setOnTouchListener(onCircleTouchListener);

        LinearLayout buttons = view.findViewById(R.id.inventory_buttons);
        mainInventoryButtons = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            MainInventoryButton mButton = new MainInventoryButton(view.getContext());
            mButton.setSlot(i+1);
            mButton.setOnClickListener(mainInventoryButtonClickListener);
            mainInventoryButtons.add(mButton);
            buttons.addView(mButton);
        }
        mainInventoryButtons.get(0).setSelected(true);

        if (App.fm.findFragmentByTag(App.TAGS.INVENTORY_SCREEN) == null) {
            InventoryFragment inventoryFragment = new InventoryFragment();
            App.fm.beginTransaction()
                    .add(view.getId(), inventoryFragment, App.TAGS.INVENTORY_SCREEN)
                    .hide(inventoryFragment)
                    .commit();
        }

        OpenInventoryButton oButton = new OpenInventoryButton(view.getContext());
        oButton.setOnClickListener((View v) -> {
            Fragment curFrag = App.fm.findFragmentByTag(App.TAGS.INVENTORY_SCREEN);
            FragmentTransaction ft = App.fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_from_bottom, R.anim.slide_to_bottom);
            if (curFrag != null) ft.show(curFrag);
            ft.commit();
        });
        buttons.addView(oButton);

        FrameLayout topPlayers = view.findViewById(R.id.top_players);
        topPlayers.setOnClickListener(v -> {
            if (topPlayersTime >= 400){
                topPlayersTime = 20;
            } else {
                topPlayersTime = 4000;
                ObjectAnimator.ofFloat(topPlayers, "translationX", -App.getDP(0)).setDuration(400).start();
            }
        });
        LinearLayout playersList = topPlayers.findViewById(R.id.players_list);

        Runnable run = () -> {
            if (topPlayersTime > 0){
                topPlayersTime -= 20;
                if (topPlayersTime <= 0){
                    ObjectAnimator.ofFloat(topPlayers, "translationX", App.getDP(143)).setDuration(400).start();
                }
            }
        };
        Runnable sortTop = () -> {
            if (!App.mainPlayer.isPlaying) return;
            List<Player> copyPlayers = new ArrayList<>(App.players.values());
            copyPlayers.add(App.mainPlayer);
            Collections.sort(copyPlayers, (o1, o2) -> o2.getExp()[0] - o1.getExp()[0]);
            Collections.sort(copyPlayers, (o1, o2) -> o2.getLevel() - o1.getLevel());

            playersList.removeAllViews();
            for (int i = 0; i < 5 && i < copyPlayers.size(); i++) {
                TextView t = new TextView(playersList.getContext());
                Player p = copyPlayers.get(i);
                t.setTextSize(15);
                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins((int) App.getDP(5), (int) App.getDP(5), (int) App.getDP(5), (int) App.getDP(5));
                t.setLayoutParams(lp);
                t.setTextColor(Color.WHITE);
                if (p.getNickname().equals(App.mainPlayer.getNickname()))
                    t.setTypeface(t.getTypeface(), Typeface.BOLD);
                t.setSingleLine(true);
                t.setText((i + 1) + ". " + p.getNickname() + " [" + p.getLevel() + "]");
                playersList.addView(t);
            }
        };

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                topPlayers.post(run);
            }
        }, 0, 20);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                topPlayers.post(sortTop);
            }
        }, 0, 1000);

        updateInventory();

        return view;
    }

    public void updateInventory() {
        updateInventory(1);
        InventoryFragment inventoryFragment = (InventoryFragment) App.fm.findFragmentByTag(App.TAGS.INVENTORY_SCREEN);
        if (inventoryFragment != null) {
            inventoryFragment.updateInventory();
        }
    }
    public void updateInventory(int ignore) {
        if (mainInventoryButtons != null) {
            for (int i = 0; i < 5; i++) {
                InventoryItem item = App.inventory[i];
                MainInventoryButton mib = mainInventoryButtons.get(i);
                mainInventoryButtons.get(i).setItemInInventory(item);
                if (mib.isSelected()) checkIsBuilding(mib.getItem());
                mib.setItemInInventory(item);
            }
        }
    }

//    private final Animation slideToRight = AnimationUtils.loadAnimation(App.mainView.getContext(), R.anim.slide_to_right);
//    private void hideView(View v){
//        if (v.getVisibility() != View.GONE) {
//            v.setVisibility(View.GONE);
//            v.startAnimation(slideToRight);
//        }
//    }
//    private final Animation slideFromRight = AnimationUtils.loadAnimation(App.mainView.getContext(), R.anim.slide_from_right);
//    private void showView(View v){
//        if (v.getVisibility() != View.VISIBLE) {
//            v.setVisibility(View.VISIBLE);
//            v.startAnimation(slideFromRight);
//        }
//    }

    private final View.OnClickListener mainInventoryButtonClickListener = (v) -> {
        MainInventoryButton mButton = (MainInventoryButton)v;
        mainInventoryButtons.get(App.mainPlayer.getActiveSlot()-1).setSelected(false);
        mButton.setSelected(true);
        checkIsBuilding(mButton.getItem());
        App.mainPlayer.setActiveSlot(mButton.getSlot());
        App.api.emit("setActiveSlot", mButton.getSlot());
    };

    private void checkIsBuilding(InventoryItem item){
        App.buildModeEnabled = item != null && item.getState().type.equals("building");
        if (App.buildModeEnabled){
            App.fm.beginTransaction().show(App.fm.findFragmentByTag(App.TAGS.CIRCLE_SCREEN)).commit();
        } else {
            App.fm.beginTransaction().hide(App.fm.findFragmentByTag(App.TAGS.CIRCLE_SCREEN)).commit();
        }
    }


    private final View.OnTouchListener onCircleTouchListener = (v, event) -> {
        if (Math.pow(v.getWidth()/2f-event.getX(), 2) + Math.pow(v.getHeight()/2f-event.getY(), 2) > Math.pow(App.q*380, 2)){
            App.fm.findFragmentByTag(App.TAGS.GAME_SCREEN).getView().onTouchEvent(event);
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                double mapX = App.mainPlayer.getX() * App.q - (v.getWidth()/2f - event.getX());
                double mapY = App.mainPlayer.getY() * App.q - (v.getHeight()/2f - event.getY());
                if (mapX >= 0 && mapY >= 0){
                    int x = (int) (mapX / App.q / App.size);
                    int y = (int) (mapY / App.q / App.size);

                    if (App.buildings != null && App.buildings[x][y] == null){
                        App.api.emit("build", new BuildObject(
                                mainInventoryButtons.get(App.mainPlayer.getActiveSlot()-1).getItem().getName(),
                                x, y
                        ));
                    }
                }
            } else {
                App.fm.findFragmentByTag(App.TAGS.GAME_SCREEN).getView().onTouchEvent(event);
            }
        }
        return true;
    };

    private static class BuildObject {
        public String name;
        public int x, y;
        public BuildObject(String name, int x, int y){
            this.name = name;
            this.x = x;
            this.y = y;
        }
    }
}
