package com.example.yangenneng0.myapplication.viewUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.example.yangenneng0.myapplication.R;
import com.example.yangenneng0.myapplication.dao.MoodDAO;
import com.example.yangenneng0.myapplication.model.MoodEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: yangenneng
 * DateTime: 2016/12/13 17:55
 * Description:发表说说
 */
public class QQmoodActivity  extends AppCompatActivity {

    AutoCompleteTextView contentTV;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqmood);

        contentTV= (AutoCompleteTextView) findViewById(R.id.qqmood);

        button= (Button) findViewById(R.id.sendqqmod);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MoodDAO moodDAO=new MoodDAO();
                MoodEntity moodEntity=new MoodEntity(contentTV.getText().toString(),getDate());
                if(moodDAO.insert(moodEntity))
                {
                    Toast.makeText(QQmoodActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(QQmoodActivity.this,QzoneActivity.class);
                    QQmoodActivity.this.startActivity(intent);
                }
                Toast.makeText(QQmoodActivity.this, "发布失败", Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * 发送消息时，获取当前事件
     * @return 当前时间
     */
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date());
    }

}
