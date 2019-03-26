package com.photograph_u.dao;

import com.photograph_u.domain.User;

import java.util.Map;

public class UserDao extends BaseDao {
    //检查用户是否存在
    public boolean checkUser(String phone) {
        String sql = "select count(*) as count from user where phone=? and is_deleted=0";
        long count = (long) queryToMap(sql, new Object[]{phone}).get("count");//注意这里为long
        return count == 1;
    }

    //添加用户
    public boolean addUser(String phone, String password) {
        String sql = "insert into user(phone,password) values(?,?)";
        int count = update(sql, new Object[]{phone, password});
        return count == 1;
    }

    //查询用户
    public User queryUser(String phone, String password) {
        String sql = "select id,nickname,head_image,sex,birthday,phone,school from user where phone=? and password=? and is_deleted=0";
        User user = queryToBean(sql, new Object[]{phone, password}, User.class);
        return user;
    }

    //修改密码字段
    public boolean updatePassword(int userId, String password, String newPassword) {
        String sql = "update user set password=? where password=? and id=? and is_deleted=0";
        int count = update(sql, new Object[]{newPassword, password, userId});
        return count == 1;
    }

    //重置密码
    public boolean resetPassword(String phone, String newPassword) {
        String sql = "update user set password=? where phone=? and is_deleted=0";
        int count = update(sql, new Object[]{newPassword, phone});
        return count == 1;
    }

    //查询用户
    public User queryUserById(int userId) {
        String sql = "select id,nickname,head_image,sex,birthday,phone,school from user where id=? and is_deleted=0";
        User user = queryToBean(sql, new Object[]{userId}, User.class);
        return user;
    }

    //修改个人信息
    public boolean updateInfo(User user) {
        String sql = "update user set nickname=?,sex=?,birthday=?,school=? where id=? and is_deleted=0";
        Object[] params = {user.getNickname(), user.getSex(), user.getBirthday(), user.getSchool(), user.getId()};
        int count = update(sql, params);
        return count == 1;
    }

    //修改头像
    public boolean updateHeadImage(int userId, String headImage) {
        String sql = "update user set head_image=? where id=? and is_deleted=0";
        int count = update(sql, new Object[]{headImage, userId});
        return count == 1;
    }

    //查询用户头像
    public Map<String, Object> queryHeadImage(int userId) {
        String sql = "select head_image from user where id=? and is_deleted=0";
        return queryToMap(sql, new Object[]{userId});
    }

    //查询用户头像和昵称
    public Map<String, Object> queryUserHeadImageAndNickNameById(int userId) {
        String sql = "select nickname,head_image from user where id=? and is_deleted=0";
        Map<String, Object> resultMap = queryToMap(sql, new Object[]{userId});
        return resultMap;
    }

}
