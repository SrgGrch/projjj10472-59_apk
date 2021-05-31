package com.example.myapplication;

import com.example.myapplication.CustomViews.AttackButton;
import com.example.myapplication.CustomViews.CustomProgressBar;
import com.example.myapplication.SocketHelpers.PlayerSocketData;
import com.example.myapplication.SocketHelpers.SocketData;

public class MainPlayer extends Player {
    public boolean isPlaying = false;
    private int activeSlot = 1;

    public MainPlayer() {
        super();
    }

    public void setActiveSlot(int activeSlot) {
        this.activeSlot = activeSlot;
    }

    public int getActiveSlot() {
        return activeSlot;
    }

    @Override
    public void useWeapon(int t, float d, int l) {
        AttackButton ab = App.mainView.findViewById(R.id.button_sword);
        ab.setTime(t);
        super.useWeapon(t, d, l);
    }

    @Override
    public void update(PlayerSocketData p) {
        super.update(p);
//        ((CustomProgressBar)App.mainView.findViewById(R.id.level_progress_bar)).update(
//                100*p.getExp()[0]/p.getExp()[1],
//                "Уровень " + p.getLevel() + " (" + p.getExp()[0] + "/" + p.getExp()[1] + ")"
//        );
        CustomProgressBar cpb = (CustomProgressBar)App.mainView.findViewById(R.id.level_progress_bar);
        if (cpb != null) {
            cpb.update(
                    100 * p.getExp()[0] / (float) p.getExp()[1],
                    p.getLevel() + ""
            );
        }
    }
}
