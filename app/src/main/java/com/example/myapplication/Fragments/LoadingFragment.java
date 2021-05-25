package com.example.myapplication.Fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.App;
import com.example.myapplication.R;

public class LoadingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout view = new LinearLayout(inflater.getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int)App.getDP(200), FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        view.setLayoutParams(lp);

        view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.loading_background));
        view.setOrientation(LinearLayout.VERTICAL);
        view.setGravity(Gravity.CENTER_HORIZONTAL);

        ProgressBar loadingCircle = new ProgressBar(view.getContext());
        LinearLayout.LayoutParams circle_lp = new LinearLayout.LayoutParams((int)App.getDP(50), (int)App.getDP(50));
        circle_lp.setMargins(0, (int)App.getDP(10), 0, (int)App.getDP(10));
        loadingCircle.setLayoutParams(circle_lp);
        loadingCircle.setIndeterminate(true);
        loadingCircle.setIndeterminateDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.loading_progress));

        view.addView(loadingCircle);

        if (getArguments().getString("message") != null){
            TextView messageView = new TextView(view.getContext());
            LinearLayout.LayoutParams message_lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            message_lp.setMargins(0, 0, 0, (int)App.getDP(10));
            messageView.setGravity(Gravity.CENTER_HORIZONTAL);
            messageView.setText(getArguments().getString("message"));
            messageView.setTextSize(App.getDP(8));
            messageView.setTextColor(Color.WHITE);
            messageView.setLayoutParams(message_lp);
            view.addView(messageView);
        }

        view.setAlpha(0.9f);
        view.setZ(10000);

        return view;
    }

    public static LoadingFragment newInstance(String message) {
        Bundle args = new Bundle();
        args.putString("message", message);
        LoadingFragment f = new LoadingFragment();
        f.setArguments(args);
        return f;
    }
}
