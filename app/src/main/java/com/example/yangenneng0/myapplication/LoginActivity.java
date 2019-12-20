package com.example.yangenneng0.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.example.yangenneng0.myapplication.dao.PersonDAO;
import com.example.yangenneng0.myapplication.utils.APPglobal;
import com.example.yangenneng0.myapplication.viewUI.RegistActivity;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity {

    private static boolean isExit=false;//判断是否直接退出程序

    private AutoCompleteTextView mEmailView;  //用户名
    private EditText mPasswordView;           //密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);   //用户名控件
        mPasswordView = (EditText) findViewById(R.id.password);         //密码控件

        //setOnEditorActionListener这个方法，并不是在我们点击EditText的时候触发，
        // 也不是在我们对EditText进行编辑时触发，而是在我们编辑完之后点击软键盘上的回车键才会触发。﻿﻿
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if ( id == R.id.login || id == EditorInfo.IME_NULL ) {
                    //attemptLogin();//调用函数检查登陆信息是否合法
                    login();
                    return true;
                }
                return false;
            }
        });

        //登录按钮
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();//调用函数检查登陆信息是否合法
                login();
            }
        });

        //注册按钮
        Button registButton= (Button) findViewById(R.id.register);
        registButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(LoginActivity.this,RegistActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit=false;
        }
    };

    static String generateJwt() {
        Date var0 = new Date();
        Long var1 = Long.valueOf(var0.getTime());
        Date var2 = new Date(var1.longValue() + 200000L);
        HashMap var3 = new HashMap();
        var3.put("typ", "JWT");
        var3.put("alg", "HS256");
        String var4 = "";

        try {
            var4 = Jwts.builder().
                    setHeader(var3).
                    setIssuer(sAuthOctoKey).    //请求的发起者；
                    setIssuedAt(var0).          //请求的发起时间；
                    setExpiration(var2).        //expTime是过期时间，当前时间+200秒；
                    signWith(SignatureAlgorithm.HS256, Base64.encode(sAuthSecret.getBytes("UTF-8")))   //两个参数，一个是加密算法，一个秘钥；SECRET_KEY是加密算法对应的密钥，这里使用额是HS256加密算法；
                    .claim("key","vaule")               //该方法是在JWT中加入值为vaule 的 key 自定义字段；
            compact();
        } catch (Exception var6) {
            Log.e("HttpRequest", "octo generate error...", var6);
        }

        return var4;
    }


    private void login() {
        Log.e("login:", "=====hello====");
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();

        String name = mEmailView.getText().toString();
        String passwd = mPasswordView.getText().toString();

        HttpUrl httpurl = new HttpUrl.Builder()
                .scheme("http")
                .host("10.130.47.187")
                .port(1234)
                .addPathSegment("login")
                .addQueryParameter("name", name)
                .addQueryParameter("passwd", passwd)
                .build();


        //创建一个Request
        final Request request = new Request.Builder()
                .url(httpurl)
                .addHeader("Authorization", "Bearer " + generateJwt())
                .build();

        //new call
        Log.e("login:", request.toString());
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Request request, IOException e)
            {
                Log.e("login:", "======login fail=====");
            }

            @Override
            public void onResponse(final Response response) throws IOException
            {
                //String htmlStr =  response.body().string();
                Log.e("login:", "======login =====");
            }
        });
    }

    //退出确认
    //@Override
    //public boolean onKeyDown(int keyCode, KeyEvent event) {
    //    if(keyCode==KeyEvent.KEYCODE_BACK){
    //        if(!isExit){
    //            isExit=true;
    //            Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
    //            handler.sendEmptyMessageDelayed(0,2000);
    //        }
    //    }else {
    //        finish();
    //        System.exit(0);
    //    }
    //    return super.onKeyDown(keyCode, event);
    //}

    /**
     * 输入信息的检查
     */
    private void attemptLogin() {

        // 初始化错误信息为null
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // 获取输入信息.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;//是否是非法信息
        View focusView = null;

        // 检查密码是否有效
        if ( !TextUtils.isEmpty(password) && !isPasswordValid(password) ) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //  检查邮箱
        if ( TextUtils.isEmpty(email) ) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if ( cancel ) {//非法信息
            focusView.requestFocus();//标签用于指定屏幕内的焦点View。
        } else {//合法信息
           //登陆跳转逻辑
            PersonDAO personDAO=new PersonDAO();
            boolean sussess=personDAO.chechLogin(email,password);
            if(sussess){  //信息合法
                APPglobal.NAME=PersonDAO.findNameByUsername(email);//保存用户登录信息到全局变量中
                APPglobal.USERNAME=email;
                Intent intent=new Intent();
                intent.setClass(LoginActivity.this,MainActivity.class);
                LoginActivity.this.startActivity(intent);
            }
            else {
                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 密码是否合法：至少需要4位
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }

}

