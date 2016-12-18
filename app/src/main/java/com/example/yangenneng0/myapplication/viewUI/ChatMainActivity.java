package com.example.yangenneng0.myapplication.viewUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.example.yangenneng0.myapplication.MainActivity;
import com.example.yangenneng0.myapplication.R;
import com.example.yangenneng0.myapplication.adapter.ChatMsgViewAdapter;
import com.example.yangenneng0.myapplication.model.ChatMsgEntity;
import com.example.yangenneng0.myapplication.utils.APPglobal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: yangenneng
 * DateTime: 2016/12/11 17:11
 * Description:
 */
public class ChatMainActivity extends Activity implements View.OnClickListener {

    private Button mBtnSend;            // 发送btn
    private Button mBtnBack;            // 返回btn
    private EditText mEditTextContent;  // 内容框
    private ListView mListView;         // 聊天记录列表
    private ChatMsgViewAdapter mAdapter;// 聊天记录视图的Adapter
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 聊天记录对象数组


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatmain);

        initView();// 初始化view
        initData();// 初始化数据
        mListView.setSelection(mAdapter.getCount() - 1);

        TextView textView= (TextView) findViewById(R.id.chatname);
        Bundle bundle = this.getIntent().getExtras();
        textView.setText(bundle.getString("name")); //解析传递过来的参数

    }

    /**
     * 初始化view
     * 找出页面的控件
     */
    public void initView() {
        mListView= (ListView) findViewById(R.id.listview);
        mBtnSend=(Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mBtnBack = (Button) findViewById(R.id.chatGoHome);
        mBtnBack.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }


    //消息数组
    private String[] msgArray = new String[] { "您好，我是自动回复机器人，输入关键字即可和我聊天，比如:【hello】 【你好】 【再见】 【...】" };
    //时间数组
    private String[] dataArray = new String[] { getDate()};

    /**
     * 模拟加载消息历史，实际开发可以从数据库中读出
     */
    public void initData() {
        for (int i = 0; i < msgArray.length; i++) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(dataArray[i]);
            if (i % 2 == 0) {

                Bundle bundle = this.getIntent().getExtras();//解析传递过来的参数
                String name = bundle.getString("name");

                entity.setName(name);   //设置对方姓名
                entity.setMsgType(true); // 收到的消息
            } else {
                entity.setName(APPglobal.NAME);   //设置自己姓名
                entity.setMsgType(false);// 发送的消息
            }
            entity.setMessage(msgArray[i]);//消息内容
            mDataArrays.add(entity);
        }

        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:// 发送按钮点击事件
                send();
                break;
            case R.id.chatGoHome:// 返回按钮点击事件
                Intent intent = new Intent();
                intent.setClass(ChatMainActivity.this, MainActivity.class);
                ChatMainActivity.this.startActivity(intent);
                break;
        }
    }


    /**
     * 发送消息
     */
    private void send() {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {

            //-----------发送者-------------
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setName(APPglobal.NAME);      //设置发送消息消息者姓名
            entity.setDate(getDate());  //设置格式化的发送时间
            entity.setMessage(contString); //设置发送内容
            entity.setMsgType(false);      //设置消息类型，true 接受的 false发送的
            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变

           //-----------自动回复-------------
            Bundle bundle = this.getIntent().getExtras();//解析传递过来的参数
            String name = bundle.getString("name");
            ChatMsgEntity chatMsgEntity=new ChatMsgEntity();
            chatMsgEntity.setName(name);
            chatMsgEntity.setDate(getDate());  //设置格式化的发送时间
            chatMsgEntity.setMessage(getRecive(contString)); //设置发送内容
            chatMsgEntity.setMsgType(true);      //设置消息类型，true 接受的 false发送的
            mDataArrays.add(chatMsgEntity);
            mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变

            mEditTextContent.setText("");// 清空编辑框数据
            mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项

        }
    }


    /**
     * 发送消息时，获取当前事件
     * @return 当前时间
     */
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(new Date());
    }

    /**
     * 根据输入的关键字自动回复
     * @param key
     * @return
     */
    private String getRecive(String key){
        HashMap<String,String> ReciveValues=new HashMap<>();
        //初始化自动回复关键字
        ReciveValues.put("你好","你好~~~");
        ReciveValues.put("hello","hello");
        ReciveValues.put("再见"," 好的，再见啦");
        ReciveValues.put("bye"," ^_^ bye bye.");
        ReciveValues.put("...","为什么无语啊？");
        ReciveValues.put("。。。","额，为什么无语啊？");
        ReciveValues.put("？","怎么了，有问题吗？");
        ReciveValues.put("?","怎么了，有问题吗？");

        if(ReciveValues.containsKey(key.toLowerCase())){//查找是否存在
            return ReciveValues.get(key);
        }else {
            return " ^_^ 抱歉，我还听不懂您说的，请等待我升级下一个版本....";
        }

    }

}
