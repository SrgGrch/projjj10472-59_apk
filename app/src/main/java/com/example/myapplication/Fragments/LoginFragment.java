package com.example.myapplication.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.App;
import com.example.myapplication.R;

public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);
        view.findViewById(R.id.button_back).setOnClickListener((v) -> {
            Fragment curFrag = App.fm.findFragmentByTag(App.TAGS.ENTRY_SCREEN);
            FragmentTransaction ft = App.fm.beginTransaction().remove(this);
            if (curFrag != null) ft.show(curFrag);
            ft.commit();
        });

        return view;
    }
}