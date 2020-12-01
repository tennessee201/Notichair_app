package com.example.notichair_Real;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class mypage_fragment extends Fragment {

    private View view;
    private Context context;

    public static mypage_fragment newInstance() {
        mypage_fragment mypage = new mypage_fragment();
        return mypage;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_mypage_fragment,container,false);


        context = container.getContext();

        return view;
    }






    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button logoutButton = (Button) getView().findViewById(R.id.logout_btn);
        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {   //로그아웃 버튼을 누를 경우
                Intent intent = new Intent(getActivity(), MainActivity.class); //메인화면으로 돌아감
                startActivity(intent);
            }
        });

        Button change_btn = (Button) getView().findViewById(R.id.change_btn);
        change_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {   //수정하기 버튼을 누를 경우


                Toast.makeText(context,"수정되었습니다!",Toast.LENGTH_LONG).show();
            }
        });


    }


}