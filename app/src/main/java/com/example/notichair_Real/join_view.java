package com.example.notichair_Real;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class join_view extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore cloudDB;
    String email = "";
    String password = "";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    EditText ed_singupeamil, ed_signuppassword;
    Button bt_newsignup, bt_backmain;
    TextView tv_error_email,tv_error_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_view);

        init();

        // newsignup button Listener
        bt_newsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=ed_singupeamil.getText().toString();
                password=ed_signuppassword.getText().toString();
                if(email.matches(emailPattern))
                {
                    tv_error_email.setText("");         //에러 메세지 제거
                    ed_singupeamil.setBackgroundResource(R.drawable.white_edittext);  //테투리 흰색으로 변경
                }
                else {
                    tv_error_email.setText("이메일 형식으로 입력해주세요.");
                    ed_singupeamil.setBackgroundResource(R.drawable.red_edittext);  // 적색 테두리 적용
                }

                if(password.getBytes().length<6){
                    tv_error_password.setText("6자리 이상 입력해주세요.");
                    ed_signuppassword.setBackgroundResource(R.drawable.red_edittext);  // 적색 테두리 적용


                }
                else{
                    tv_error_password.setText("");         //에러 메세지 제거
                    ed_signuppassword.setBackgroundResource(R.drawable.white_edittext);  //테투리 흰색으로 변경

                }
                if(isValidEmail() && isValidPasswd()) {
                    createUser(email, password);
                }
            }
        });

        // backmain button Listener
        bt_backmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    // 회원가입
    private void createUser(final String email, final String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            Toast.makeText(join_view.this, "Notichair에 오신 것을 환영합니다.", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(), after_login.class);
                            startActivity(intent);
                            UserInfo userinfo = new UserInfo(email, password);
                            cloudDB.collection("users").document(firebaseAuth.getUid()).set(userinfo);
                            finish();
                        } else {
                            // 회원가입 실패
                            Toast.makeText(join_view.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            Toast.makeText(join_view.this,"패스워드가 공백입니다.",Toast.LENGTH_SHORT);
            return false;

        } else {
            return true;
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            Toast.makeText(join_view.this,"이메일이 공백입니다.",Toast.LENGTH_SHORT);
            return false;
        }
        else {
            return true;
        }
    }

    private void init(){
        ed_singupeamil=(EditText)findViewById(R.id.ed_signupemail);
        ed_signuppassword=(EditText)findViewById(R.id.ed_signuppassword);
        bt_newsignup=(Button)findViewById(R.id.bt_newsignup);
        bt_backmain=(Button)findViewById(R.id.bt_backmain);
        tv_error_email = (TextView)findViewById(R.id.tv_error_email);
        tv_error_password=(TextView)findViewById(R.id.tv_error_password);

        firebaseAuth = FirebaseAuth.getInstance();
        cloudDB = FirebaseFirestore.getInstance();
    }
}
