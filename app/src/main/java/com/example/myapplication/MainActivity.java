package com.example.myapplication;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragments.CircleFragment;
import com.example.myapplication.Fragments.EntryFragment;
import com.example.myapplication.Fragments.GameFragment;
import com.example.myapplication.Fragments.LoginFragment;
import com.example.myapplication.Fragments.MenuFragment;
import com.example.myapplication.Fragments.RegistrationFragment;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* =================> WORK WITH UI */
        // Hide navigation bar
        hideUI();
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> hideUI());

        // Fix font size (if changed in settings)
        Configuration override = new Configuration(getResources().getConfiguration());
        override.fontScale = 1.0f;
        getResources().updateConfiguration(override, getResources().getDisplayMetrics());

        tryToFixAnimationDurationScale();
        /* WORK WITH UI <=================*/


        App.PACKAGE_NAME = getPackageName();

        // Get local saved data
        if (App.prefs == null){
            App.prefs = getSharedPreferences("player", MODE_PRIVATE);
        }

        // Create fragment container
        FrameLayout mainView = new FrameLayout(this);
        mainView.setId(R.id.main_view);
        App.mainView = mainView;

        // Create main fragments
        FragmentManager fm = getSupportFragmentManager();
        App.fm = fm;

        FragmentTransaction ft = fm.beginTransaction();
        if (fm.findFragmentByTag(App.TAGS.GAME_SCREEN) == null){
            Fragment gameFragment = new GameFragment();
            ft.add(App.mainView.getId(), gameFragment, App.TAGS.GAME_SCREEN);
        }
        if (fm.findFragmentByTag(App.TAGS.CIRCLE_SCREEN) == null){
            Fragment circleFragment = new CircleFragment();
            ft.add(App.mainView.getId(), circleFragment, App.TAGS.CIRCLE_SCREEN).hide(circleFragment);
        }
        if (fm.findFragmentByTag(App.TAGS.MENU_SCREEN) == null){
            Fragment mf = new MenuFragment();
            ft.add(App.mainView.getId(), mf, App.TAGS.MENU_SCREEN).hide(mf);
        }
        try {
            ft.commit();
        } catch (Exception e){
            ft.commitAllowingStateLoss();
        }

        setContentView(mainView);
    }


    public void loginAsGuest(View view) {
        App.api.emit("loginAsGuest", new LoginData(
                ((EditText)findViewById(R.id.nickname)).getText().toString()
        ));
    }

    public void open_reg(View view) {
        Fragment curFrag = App.fm.findFragmentByTag(App.TAGS.ENTRY_SCREEN);
        FragmentTransaction ft = App.fm.beginTransaction();
        if (curFrag != null) ft.hide(curFrag);
        ft.add(App.mainView.getId(), new RegistrationFragment(), App.TAGS.REGISTRATION_SCREEN).commit();
    }

    public void open_login(View view) {
        Fragment curFrag = App.fm.findFragmentByTag(App.TAGS.ENTRY_SCREEN);
        FragmentTransaction ft = App.fm.beginTransaction();
        if (curFrag != null) ft.hide(curFrag);
        ft.add(App.mainView.getId(), new LoginFragment(), App.TAGS.REGISTRATION_SCREEN).commit();
    }

    public void login(View view){
        App.api.emit("login", new LoginData(
                ((EditText)findViewById(R.id.login_nickname_input)).getText().toString(),
                ((EditText)findViewById(R.id.login_password_input)).getText().toString()
        ));
    }
    public void registration(View view) {
        App.api.emit("reg", new LoginData(
                ((EditText)findViewById(R.id.registration_nickname_input)).getText().toString(),
                ((EditText)findViewById(R.id.registration_password_input)).getText().toString()
        ));
    }

    public void join_room(View view){
        App.showLoading("Вход в игру");
        Fragment curFrag = App.fm.findFragmentByTag(App.TAGS.MENU_SCREEN);
        if (curFrag != null){
            App.fm.beginTransaction().setCustomAnimations(R.anim.slide_from_bottom, R.anim.slide_to_bottom).hide(curFrag).commit();
        }
        App.api.emit("joinRoom", null);
    }

    public void logout (View view){
        App.prefs.edit().remove("token").apply();
        Fragment curFrag = App.fm.findFragmentByTag(App.TAGS.MENU_SCREEN);
        FragmentTransaction ft = App.fm.beginTransaction();
        if (curFrag != null) ft.remove(curFrag);
        ft.add(App.mainView.getId(), new EntryFragment(), App.TAGS.ENTRY_SCREEN).commit();
        App.api.emit("logout", null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tryToFixAnimationDurationScale();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    private static class LoginData {
        private final String nickname, password;
        public LoginData (String nickname, String password){
            this.nickname = nickname;
            this.password = password;
        }
        public LoginData (String nickname){
            this.nickname = nickname;
            this.password = null;
        }
    }

    private void hideUI(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void tryToFixAnimationDurationScale () {
        try {
            ValueAnimator.class.getMethod("setDurationScale", float.class).invoke(null, 1f);
        } catch (Exception ignore) {}
    }

    public void preventClick(View v){}

    public void profile (View v){
        App.showMessage("В настоящее время данный раздел в разработке");
    }

    public void leaders_table(View v){
        App.showMessage("В настоящее время данный раздел в разработке");
    }
}

