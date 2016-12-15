package com.example.yangenneng0.myapplication.dao;

import com.example.yangenneng0.myapplication.db.ConnectionFactory;
import com.example.yangenneng0.myapplication.db.ControlDB;
import com.example.yangenneng0.myapplication.model.MoodEntity;
import com.example.yangenneng0.myapplication.utils.APPglobal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: yangenneng
 * DateTime: 2016/12/15 18:49
 * Description:QQ说说数据访问对象
 */
public class MoodDAO {


    //发送消息时，获取当前事件
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date());
    }

    //插入QQ说说
    public boolean insertMood(String mood) throws SQLException {
        boolean flag=true;
        ControlDB controlDB=new ControlDB();
        String sql="insert into tb_mood(content,time,person) values('"+mood+"','"+
               getDate()+"','"+ APPglobal.NAME+"')";
        flag=controlDB.executeSQL(sql);
        return flag;
    }

    //获取所有说说
    public List<MoodEntity> getAllMood() throws SQLException {
        List<MoodEntity> list = new ArrayList<MoodEntity>();
        Connection conn=null;
        Statement st=null;
        ResultSet rs=null;
        conn= ConnectionFactory.getConnection();
        try {
            st=conn.createStatement();
            String sql="select * from tb_mood";
            rs=st.executeQuery(sql);
            while (rs.next()){
                MoodEntity mood=new MoodEntity();
                mood.setContent(rs.getString("content"));//内容
                mood.setTime(rs.getString("time"));      //时间
                mood.setPerson(rs.getString("person"));  //发布者
                list.add(mood);
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }finally {
            conn.close();
            st.close();
            rs.close();
        }
        return list;
    }


}
