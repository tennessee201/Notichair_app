package com.example.notichair_Real;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class after_login extends AppCompatActivity {

    private BottomNavigationView mBottomNV;
    private FirebaseAuth firebaseAuth;

    @Override public void onBackPressed() { //super.onBackPressed();
         }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_login); //UI 생성

        // 유저가 로그인하지 않은 상태라면 로그인 화면 실행
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        else{
            FirebaseUser user = firebaseAuth.getCurrentUser();
        }

        mBottomNV = findViewById(R.id.nav_view);  //약간 이해 안됨
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                BottomNavigate(menuItem.getItemId());

                return false;
            }
        });
        mBottomNV.setSelectedItemId(R.id.menu_watch);
    }

    private void BottomNavigate(int id){

        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager(); // getSupportFragmentManager()를 호출하여 FragmentManager를 가져옵니다.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();  //Fragment의 추가, 제거, 변경 등의 작업을 할 수 있습니다.

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if(currentFragment != null){
            fragmentTransaction.hide(currentFragment);
        }


        // 왜 기본 fragment 설정이 안되는것 ?  menu_home 단을 메인으로 기본설정하고싶은디 ㅠ
        // ++ 첫번쨰 프라그먼트의 text가 안없어짐 ㅡㅡ
        Fragment fragment = fragmentManager.findFragmentByTag(tag);


        if( fragment == null) {

            fragment = new main_fragment();
             if (id == R.id.menu_watch)
            {
                fragment = new watch_fragment();
            }
            if (id == R.id.menu_setting)
            {
                fragment = new setting_fragment();
            }
            if (id == R.id.menu_home)
            {
                fragment = new main_fragment();
            }
            if (id == R.id.menu_mypage)
            {
                fragment = new mypage_fragment();
            }
             if (id == R.id.menu_sevendays)
            {
                fragment = new sevendays_fragment();
            }

            fragmentTransaction.add(R.id.content_layout, fragment, tag);
        }else{
            fragmentTransaction.show(fragment);

        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();
    }
}




