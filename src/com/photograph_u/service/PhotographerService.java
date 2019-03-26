package com.photograph_u.service;

import com.photograph_u.dao.*;
import com.photograph_u.domain.PageBean;
import com.photograph_u.util.JdbcUtils;

import java.util.List;
import java.util.Map;

public class PhotographerService {
    //摄影师查询个人信息
    public Map<String, Object> lookInfo(int userId) {
        PhotographerDao photographerDao = new PhotographerDao();
        PhotoDao photoDao = new PhotoDao();
        ReviewDao reviewDao = new ReviewDao();
        FollowDao followDao = new FollowDao();
        UserDao userDao = new UserDao();
        Map<String, Object> photographerMap = photographerDao.queryPhotographerByUserId(userId);
        //根据摄影师的用户Id查询摄影师ID
        int photographerId = (int) photographerMap.get("id");
        //查询个人作品
        List<Map<String, Object>> photoMapList = photoDao.queryPhotos(photographerId);
        //查询个人评论
        List<Map<String, Object>> reviewMapList = reviewDao.queryReviews(photographerId);
        for (Map<String, Object> reviewMap : reviewMapList) {//评论人的昵称与头像
            int reviewUserId = (int) reviewMap.get("userId");
            Map<String, Object> nicknameAndHeadImageMap = userDao.queryUserHeadImageAndNickNameById(reviewUserId);
            reviewMap.put("user", nicknameAndHeadImageMap);
        }
        //查询个人关注者
        List<Map<String, Object>> followMapList = followDao.queryFollows(photographerId);
        for (Map<String, Object> followMap : followMapList) {//关注人人的昵称与头像
            int followUserId = (int) followMap.get("userId");
            Map<String, Object> nicknameAndHeadImageMap = userDao.queryUserHeadImageAndNickNameById(followUserId);
            followMap.put("user", nicknameAndHeadImageMap);
        }
        photographerMap.put("photos", photoMapList);
        photographerMap.put("reviews", reviewMapList);
        photographerMap.put("follows", followMapList);
        return photographerMap;
    }

    //修改信息
    public boolean updateInfo(int userId, double price, String serverContent, String introduce) {
        PhotographerDao photographerDao = new PhotographerDao();
        return photographerDao.updatePhotographer(userId, price, serverContent, introduce);
    }

    //添加图片
    public boolean addPhoto(int userId, List<String> images) {
        PhotographerDao photographerDao = new PhotographerDao();
        PhotoDao photoDao = new PhotoDao();
        JdbcUtils.beginTransaction();
        boolean succeed = true;
        int photographerId = photographerDao.queryPhotographerIdByUserId(userId);
        for (String image : images) {
            if (!photoDao.addPhoto(photographerId, image)) {
                succeed = false;
            }
        }
        JdbcUtils.commitTransaction();
        return succeed;
    }

    //删除图片
    public boolean deletePhoto(int userId, int photoId) {
        PhotographerDao photographerDao = new PhotographerDao();
        PhotoDao photoDao = new PhotoDao();
        int photographerId = photographerDao.queryPhotographerIdByUserId(userId);
        return photoDao.deletePhoto(photoId, photographerId);
    }

    //列出所有未处理订单,传入参数是摄影师的userId
    public List<Map<String, Object>> listUnTreatedOrders(int userId) {
        OrderInfoDao orderInfoDao = new OrderInfoDao();
        UserDao userDao = new UserDao();
        int photographerId = orderInfoDao.queryPhotographerId(userId);
        List<Map<String, Object>> orderInfoMapList = orderInfoDao.queryUnTreatedOrders(photographerId);
        for (Map<String, Object> orderInfoMap : orderInfoMapList) {
            int orderUserId = (int) orderInfoMap.get("userId");
            Map<String, Object> nicknameAndHeadImageMap = userDao.queryUserHeadImageAndNickNameById(orderUserId);
            orderInfoMap.put("user", nicknameAndHeadImageMap);
        }
        return orderInfoMapList;
    }

    //分页列出未处理订单
    public PageBean<List<Map<String, Object>>> listUnTreatedOrdersWithPage(int userId, int pageSize, int currentPage) {
        OrderInfoDao orderInfoDao = new OrderInfoDao();
        UserDao userDao = new UserDao();
        int photographerId = orderInfoDao.queryPhotographerId(userId);
        int totalRows = orderInfoDao.countOrders(photographerId, 0);
        PageBean<List<Map<String, Object>>> pageBean = new PageBean<>(pageSize, currentPage, totalRows);
        int startIndex = pageBean.getStartIndex();
        List<Map<String, Object>> orderInfoMapList = orderInfoDao.queryUnTreatedOrdersWithOffset(photographerId, pageSize, startIndex);
        for (Map<String, Object> orderInfoMap : orderInfoMapList) {
            int orderUserId = (int) orderInfoMap.get("userId");
            Map<String, Object> nicknameAndHeadImageMap = userDao.queryUserHeadImageAndNickNameById(orderUserId);
            orderInfoMap.put("user", nicknameAndHeadImageMap);
        }
        pageBean.setData(orderInfoMapList);
        return pageBean;
    }

    //列出所有已处理订单
    public List<Map<String, Object>> listTreatedOrders(int userId) {
        OrderInfoDao orderInfoDao = new OrderInfoDao();
        UserDao userDao = new UserDao();
        int photographerId = orderInfoDao.queryPhotographerId(userId);
        List<Map<String, Object>> orderInfoMapList = orderInfoDao.queryTreatedOrders(photographerId);
        for (Map<String, Object> orderInfoMap : orderInfoMapList) {
            int orderUserId = (int) orderInfoMap.get("userId");
            Map<String, Object> nicknameAndHeadImageMap = userDao.queryUserHeadImageAndNickNameById(orderUserId);
            orderInfoMap.put("user", nicknameAndHeadImageMap);
        }
        return orderInfoMapList;
    }

    //分页列出所有已处理订单
    public PageBean<List<Map<String, Object>>> listTreatedOrdersWithPage(int userId, int pageSize, int currentPage) {
        OrderInfoDao orderInfoDao = new OrderInfoDao();
        UserDao userDao = new UserDao();
        int photographerId = orderInfoDao.queryPhotographerId(userId);
        int totalRows = orderInfoDao.countOrders(photographerId, 1) + orderInfoDao.countOrders(photographerId, 2);
        PageBean<List<Map<String, Object>>> pageBean = new PageBean<>(pageSize, currentPage, totalRows);
        int startIndex = pageBean.getStartIndex();
        List<Map<String, Object>> orderInfoMapList = orderInfoDao.queryTreatedOrdersWithOffset(photographerId, pageSize, startIndex);
        for (Map<String, Object> orderInfoMap : orderInfoMapList) {
            int orderUserId = (int) orderInfoMap.get("userId");
            Map<String, Object> nicknameAndHeadImageMap = userDao.queryUserHeadImageAndNickNameById(orderUserId);
            orderInfoMap.put("user", nicknameAndHeadImageMap);
        }
        pageBean.setData(orderInfoMapList);
        return pageBean;
    }

    //接单
    public boolean receiveOrder(int userId, int orderInfoId) {
        OrderInfoDao orderInfoDao = new OrderInfoDao();
        int photographerId = orderInfoDao.queryPhotographerId(userId);
        if (!orderInfoDao.checkOrderInfo(photographerId, orderInfoId)) {
            return false;
        }
        return orderInfoDao.changeOrderState(orderInfoId, 1);
    }

    //拒单
    public boolean refuseOrder(int userId, int orderInfoId) {
        OrderInfoDao orderInfoDao = new OrderInfoDao();
        int photographerId = orderInfoDao.queryPhotographerId(userId);
        if (!orderInfoDao.checkOrderInfo(photographerId, orderInfoId)) {
            return false;
        }
        return orderInfoDao.changeOrderState(orderInfoId, 2);
    }

}
