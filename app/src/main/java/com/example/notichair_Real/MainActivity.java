package com.example.notichair_Real;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

    private String email = "";
    private String password = "";
    private EditText ed_eamil, ed_password;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button imageButton = (Button) findViewById(R.id.join_button); //회원가입
        Button image2Button = (Button) findViewById(R.id.login_button); //로그인
        ed_eamil = (EditText) findViewById(R.id.id); // email text view
        ed_password = (EditText) findViewById(R.id.pass); // pass text view

        firebaseAuth = FirebaseAuth.getInstance(); // firebase

        // 회원가입 버튼리스너
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { //회원가입 버튼 클릭 시, join_view 클래스로 전환   Intent가 액티비티간에 인수와 리턴값을 전달하는 역할
                Intent intent = new Intent(getApplicationContext(), join_view.class);  //첫번째 인수 : 액티비티 클래스를 구현하는 컨텍스트(context), 보통 this를 사용한다.
                                                                                       //두번쨰 인수 : 호출할 액티비티의 클래스
                startActivity(intent);  //첫번째 인수(intent) : 누구를 호출하여 무슨 작업을 시킬 것인가에 대한 정보가 담겨있다.
            }
        });

        //  로그인 버튼리스너
        image2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = ed_eamil.getText().toString();
                password = ed_password.getText().toString();
                if (isValidEmail() && isValidPasswd()) {
                    loginUser(email, password);
                }
            }
        });

        getAppKeyHash();
    }

    class SessionCallback implements ISessionCallback {
        @Override

        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    Intent intent = new Intent(getApplicationContext(), after_login.class);

                    intent.putExtra("profile", result.getProfileImagePath()); //카카오 프로필 사진 액티비티 이동//


                    intent.putExtra("name", result.getNickname()); //카카오 이름 액티비티 이동//



                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    // 로그인
    private void loginUser(String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(MainActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // 로그인 실패
                            Toast.makeText(MainActivity.this, "이메일/비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            Toast.makeText(MainActivity.this,"패스워드가 공백입니다.",Toast.LENGTH_SHORT);
            return false;

        } else {
            return true;
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            Toast.makeText(MainActivity.this,"이메일이 공백입니다.",Toast.LENGTH_SHORT);
            return false;
        }
        else {
            return true;
        }
    }
}