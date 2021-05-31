package com.example.myapplication.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.App;
import com.example.myapplication.CustomViews.CraftInventoryButton;
import com.example.myapplication.CustomViews.InventoryButton;
import com.example.myapplication.R;
import com.example.myapplication.SocketHelpers.InventoryItem;
import com.example.myapplication.helpers.ItemState;

import java.util.ArrayList;
import java.util.List;

public class CraftsFragment extends Fragment {
    private List<CraftInventoryButton> craftInventoryButtons;
    private String selectedNow;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crafts, container, false);

        craftInventoryButtons = new ArrayList<>();

        updateCrafts(view);

        view.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            view.findViewById(R.id._items).setPadding(0, view.getHeight()/2 - craftInventoryButtons.get(0).getFullSize()/2,
                    0, view.getHeight()/2 - craftInventoryButtons.get(0).getFullSize()/2);
        });

        if (savedInstanceState != null){
            if (savedInstanceState.getBoolean("isHidden", false)){
                App.fm.beginTransaction().hide(this).commit();
            }
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isHidden", isHidden());
    }

    public void updateCrafts(View view){
        List<CraftInventoryButton> cib = new ArrayList<>();
        boolean trying = true;
        while (trying) {
            try {
                for (ItemState is : App.inventoryItemStats.values()) {
                    if (is.crafts.size() == 0) continue;
                    CraftInventoryButton craftInventoryButton = new CraftInventoryButton(view.getContext(), LinearLayout.class);

                    InventoryItem craftInventoryItem = new InventoryItem();
                    craftInventoryItem.setName(is.name);
                    craftInventoryItem.setCount(1);
                    craftInventoryButton.setOnClickListener(onClickListener);
                    craftInventoryButton.setNeedWorkbench(is.needWorkbench);
                    craftInventoryButton.setItemInInventory(craftInventoryItem);

                    boolean canCraft = true;
                    for (int i = 0; i < is.crafts.size(); i++){
                        InventoryItem needForCraft = is.crafts.get(i);
                        if (App.inventoryCountItems(needForCraft.getName()) < needForCraft.getCount()){
                            canCraft = false;
                        }
                    }

                    if (canCraft){
                        cib.add(0, craftInventoryButton);
                    } else {
                        cib.add(craftInventoryButton);
                    }
                    craftInventoryButton.setCanCraft(canCraft);
                }
                trying = false;
            } catch (Exception ignore) {}
            craftInventoryButtons = cib;
        }
        invalidateCrafts(view);
    }

    private void invalidateCrafts(View view){
        view.post(() -> {
            LinearLayout ll = view.findViewById(R.id._items);
            ll.removeAllViews();
            view.findViewById(R.id.craft_items_container).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.make_craft_button).setVisibility(View.INVISIBLE);
            for (int i = 0; i < craftInventoryButtons.size(); i++){
                ll.addView(craftInventoryButtons.get(i));
                if (craftInventoryButtons.get(i).getItem().getName().equals(selectedNow)){
                    craftInventoryButtons.get(i).callOnClick();
                }
            }
        });
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CraftInventoryButton but = (CraftInventoryButton)v;
            ScrollView sv = getView().findViewById(R.id.scroll_view_);
            sv.smoothScrollTo(0, (int)(v.getY() + v.getHeight()/2 - sv.getHeight()/2) - ((LinearLayout.LayoutParams)v.getLayoutParams()).topMargin);
            selectedNow = ((CraftInventoryButton)v).getItem().getName();
            for (int i = 0; i < craftInventoryButtons.size(); i++){
                if (craftInventoryButtons.get(i).isSelected()) craftInventoryButtons.get(i).setSelected(false);
            }

            ((TextView)getView().findViewById(R.id.craft_item_name)).setText(((InventoryButton)v).getItem().getState().visibleName);
            ((TextView)getView().findViewById(R.id.craft_item_description)).setText(((InventoryButton)v).getItem().getState().getFullDescription());

            LinearLayout craftsBy = getView().findViewById(R.id.craft_items);
            craftsBy.removeAllViews();
            if (but.getItem().getState().crafts != null){
                for (int i = 0; i < but.getItem().getState().crafts.size(); i++){
                    InventoryItem _ii = but.getItem().getState().crafts.get(i);
                    CraftInventoryButton _ib = new CraftInventoryButton(getView().getContext(), LinearLayout.class);
                    if (!but.canCraft()){
                        getView().findViewById(R.id.make_craft_button).setEnabled(false);
                        if (App.inventoryCountItems(_ii.getName()) < _ii.getCount()){
                            _ib.setCountOkay(false);
                        }
                    } else {
                        getView().findViewById(R.id.make_craft_button).setEnabled(true);
                    }
                    _ib.setItemInInventory(_ii);
                    craftsBy.addView(_ib);
                }
            }

            getView().findViewById(R.id.craft_items_container).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.make_craft_button).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.make_craft_button).setOnClickListener((view) -> {
                App.api.emit("craft", but.getItem().getName());;
            });
            but.setSelected(true);
        }
    };
}
