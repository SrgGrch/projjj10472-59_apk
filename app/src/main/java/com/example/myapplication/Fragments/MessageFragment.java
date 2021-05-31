package com.example.myapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.App;
import com.example.myapplication.CustomViews.CustomButton;
import com.example.myapplication.R;

public class MessageFragment extends Fragment {
    private String message = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message, container, false);
        if (savedInstanceState != null) {
            ((TextView) view.findViewById(R.id.message_text)).setText(savedInstanceState.getString("message"));
        } else {
            ((TextView) view.findViewById(R.id.message_text)).setText(message);
        }
        (view.findViewById(R.id.button_close_message)).setOnClickListener(this::close);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("message", message);
    }

    public void setMessage(String msg){
        message = msg;
    }

    private void close (View view){
        App.fm
                .beginTransaction()
                .remove(this)
                .commit();
    }
}
