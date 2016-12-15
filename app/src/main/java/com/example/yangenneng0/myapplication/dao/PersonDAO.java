package com.example.yangenneng0.myapplication.dao;

import com.example.yangenneng0.myapplication.db.ConnectionFactory;
import com.example.yangenneng0.myapplication.db.ControlDB;
import com.example.yangenneng0.myapplication.model.Person;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: yangenneng
 * DateTime: 2016/12/15 18:42
 * Description:联系人数据访问对象
 */
public class PersonDAO {

    //插入联系人
    public boolean insertPerson(Person person) throws SQLException {
        boolean flag=true;
        ControlDB controlDB=new ControlDB();
        String sql="insert into tb_person(username,password,name) values('"+person.getUsername()+"','"+
                person.getPassword()+"','"+person.getName()+"')";
        flag=controlDB.executeSQL(sql);
        return flag;
    }

    //判断用户名密码是否正确
    public boolean checkPerson(String username,String password) throws SQLException {
        boolean flag=true;
        ControlDB controlDB=new ControlDB();
        String sql="select * from tb_person where username='"+username+"' and password='"+password+"'";
        flag=controlDB.executeSQL(sql);
        return flag;
    }

    //获取所有联系人
    public List<Person> getAllPerson() throws SQLException {
        List<Person> list = new ArrayList<Person>();
        Connection conn=null;
        Statement st=null;
        ResultSet rs=null;
        conn= ConnectionFactory.getConnection();
        try {
            st=conn.createStatement();
            String sql="select * from tb_person";
            rs=st.executeQuery(sql);
            while (rs.next()){
                Person user=new Person();
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                list.add(user);
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


    //根据用户名获得单个联系人信息
    public Person findPersonByUserName(String username){
        Connection conn=null;
        Statement st=null;
        ResultSet rs=null;

        Person user=new Person();
        String sql="select * from tb_person where  username='"+username+"'";
        try {
            st=conn.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next()) {     //有用户 则返回
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return user;
    }

    //根据姓名获得单个联系人信息
    public Person findPersonByName(String name){
        Connection conn=null;
        Statement st=null;
        ResultSet rs=null;

        Person user=new Person();
        String sql="select * from tb_person where  name='"+name+"'";
        try {
            st=conn.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next()) {     //有用户 则返回
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return user;
    }


}
