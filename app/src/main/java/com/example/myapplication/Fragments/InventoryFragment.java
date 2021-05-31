package com.example.myapplication.Fragments;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.App;
import com.example.myapplication.CustomViews.DraggingItem;
import com.example.myapplication.CustomViews.InventoryButton;
import com.example.myapplication.R;
import com.example.myapplication.SocketHelpers.InventoryItem;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {
    private int slot = 1;
    private List<InventoryButton> inventoryButtons;
    boolean inventoryCraftOpened = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventory, container, false);
        view.setId(R.id.inventory_view);

        inventoryButtons = new ArrayList<>();

        // Create inventory slots
        for (int i = 0; i < 5; i++){
            InventoryButton inventoryButton = new InventoryButton(view.getContext(), LinearLayout.class);
            createInventoryButton(inventoryButton);

            ( (LinearLayout)view.findViewById(R.id.main_inventory_buttons) ).addView(inventoryButton);
        }
        for (int i = 5; i < 20; i++){
            InventoryButton inventoryButton = new InventoryButton(view.getContext(), GridLayout.class);
            createInventoryButton(inventoryButton);

            ( (GridLayout)view.findViewById(R.id.secondary_inventory_buttons) ).addView(inventoryButton);
        }

        View closeButton = view.findViewById(R.id.close_inventory_button);
        closeButton.setOnClickListener(v -> App.fm
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_from_bottom, R.anim.slide_to_bottom)
                .hide(this)
                .commit());

        FrameLayout inventoryCraftButton = view.findViewById(R.id.inventory_craft_button);
        inventoryCraftButton.setZ(2);
        inventoryCraftButton.setOnClickListener(v -> {
            Fragment craftsFragment = App.fm.findFragmentByTag(App.TAGS.CRAFTS_SCREEN);
            if (craftsFragment != null) {
                if (!inventoryCraftOpened) {
                    App.fm
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_right)
                            .show(craftsFragment)
                            .commit();
                } else {
                    App.fm
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_right)
                            .hide(craftsFragment)
                            .commit();
                }
                inventoryCraftOpened = !inventoryCraftOpened;
            }
        });

        updateInventory();
        inventoryButtons.get(0).setSelected(true);
        inventoryButtons.get(0).updateText(view);

        if (App.fm.findFragmentByTag(App.TAGS.CRAFTS_SCREEN) == null) {
            CraftsFragment craftsFragment = new CraftsFragment();
            App.fm.beginTransaction()
                    .add(view.getId(), craftsFragment, App.TAGS.CRAFTS_SCREEN)
                    .hide(craftsFragment)
                    .commit();
        }

        if (savedInstanceState != null){
            if (savedInstanceState.getBoolean("isHidden", false)){
                App.fm.beginTransaction().hide(this).commit();
            }
        }

        return view;
    }

    public void updateInventory() {
        CraftsFragment craftsFragment = (CraftsFragment) App.fm.findFragmentByTag(App.TAGS.CRAFTS_SCREEN);
        if (craftsFragment != null && getView() != null) {
            craftsFragment.updateCrafts(getView());
        }
        if (inventoryButtons != null) {
            for (int i = 0; i < App.inventory.length; i++) {
                inventoryButtons.get(i).setItemInInventory(App.inventory[i]);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isHidden", isHidden());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createInventoryButton(InventoryButton ib){
        ib.setSlot(slot);
        ib.setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN){
                ib.callOnClick();
            } else if (e.getAction() == MotionEvent.ACTION_MOVE){
                InventoryItem curItem = ib.getItem();

                if (curItem != null){
                    DraggingItem item = new DraggingItem(App.mainView.getContext());
                    item.setBitmap(ib.getBitmap());
                    item.setItem(ib.getItem());
                    item.layout(0, 0, (int)App.getDP(60), (int)App.getDP(60));;
                    item.invalidate();

                    ClipData.Item _item = new ClipData.Item(String.valueOf(ib.getSlot()));
                    ClipData dragData = new ClipData("moveFromSlot", new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, _item);
                    View.DragShadowBuilder shadow = new View.DragShadowBuilder(item);

                    v.startDrag(dragData, shadow, null, 0);
                }
            }
            return true;
        });

        ib.setOnClickListener(v -> {
            for (int i = 0; i < inventoryButtons.size(); i++){
                if (inventoryButtons.get(i).isSelected()) inventoryButtons.get(i).setSelected(false);
            }
            ib.setSelected(true);
            ib.updateText(getView());
        });

        ib.setOnDragListener((View v, DragEvent event) -> {
            switch (event.getAction()){
                case DragEvent.ACTION_DROP:
                    ((InventoryButton) v).setDragEntered(false);

                    int fromSlot = Integer.parseInt(event.getClipData().getItemAt(0).getText().toString());
                    if (fromSlot == ib.getSlot()) return true;

                    InventoryButton from_ib = inventoryButtons.get(fromSlot - 1);

                    InventoryItem fromItem = from_ib.getItem();
                    InventoryItem toItem = ib.getItem();

                    if (toItem == null) {
                        ib.setItemInInventory(fromItem);
                        from_ib.setItemInInventory(null);
                    } else if (!toItem.getName().equals(fromItem.getName())) {
                        ib.setItemInInventory(fromItem);
                        from_ib.setItemInInventory(toItem);
                    } else {
                        if (toItem.getState().maxCount >= toItem.getCount() + fromItem.getCount()) {
                            toItem.setCount(toItem.getCount() + fromItem.getCount());
                            from_ib.setItemInInventory(null);
                        } else {
                            fromItem.setCount(toItem.getCount() + fromItem.getCount() - toItem.getState().maxCount);
                            toItem.setCount(toItem.getState().maxCount);
                            from_ib.invalidate();
                        }
                    }

                    App.inventory[ib.getSlot()-1] = ib.getItem();
                    App.inventory[from_ib.getSlot()-1] = from_ib.getItem();

                    ib.callOnClick();

                    ((GameInterfaceFragment) App.fm.findFragmentByTag(App.TAGS.GAME_INTERFACE_SCREEN)).updateInventory(1);

                    App.api.emit("moveItem", new int[]{from_ib.getSlot(), ib.getSlot()});
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    ((InventoryButton)v).setDragEntered(true);
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    ((InventoryButton)v).setDragEntered(false);
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                case DragEvent.ACTION_DRAG_LOCATION:
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                default:
                    return false;
            }
        });

        inventoryButtons.add(ib);
        slot++;
    }
}
