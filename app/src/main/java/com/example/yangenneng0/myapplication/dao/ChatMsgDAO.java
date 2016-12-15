package com.example.yangenneng0.myapplication.dao;

import com.example.yangenneng0.myapplication.db.ConnectionFactory;
import com.example.yangenneng0.myapplication.db.ControlDB;
import com.example.yangenneng0.myapplication.model.ChatMsgEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: yangenneng
 * DateTime: 2016/12/15 18:50
 * Description:聊天记录数据访问对象
 */
public class ChatMsgDAO {

    //发送消息
    public boolean insertChatMsg(ChatMsgEntity chatMsg) throws SQLException {
        boolean flag=true;
        ControlDB controlDB=new ControlDB();
        String sql="insert into tb_chatmsg(message,date,sendp,receivep) values('"+chatMsg.getMessage()+"','"+
                chatMsg.getDate()+"','"+chatMsg.getSendp()+"','"+chatMsg.getReceivep()+"')";
        flag=controlDB.executeSQL(sql);
        return flag;
    }

    //获得所有两人聊天的消息
    public List<ChatMsgEntity> getAllChatMsgByUser(String sendp,String recievep) throws SQLException {
        List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
        Connection conn=null;
        Statement st=null;
        ResultSet rs=null;
        conn= ConnectionFactory.getConnection();
        try {
            st=conn.createStatement();
            String sql="select * from tb_chatmsg where sendp='"+sendp+"' and receivep='"+recievep+"'";
            rs=st.executeQuery(sql);
            while (rs.next()){
                ChatMsgEntity chatMsg=new ChatMsgEntity();
                chatMsg.setMessage(rs.getString("message"));    //消息内容
                chatMsg.setDate(rs.getString("date"));          //消息时间
                chatMsg.setSendp(rs.getString("sendp"));        //发送者
                chatMsg.setReceivep(rs.getString("receivep"));  //接收者
                list.add(chatMsg);
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
