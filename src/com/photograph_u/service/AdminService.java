package com.photograph_u.service;

import com.photograph_u.dao.*;
import com.photograph_u.domain.PageBean;
import com.photograph_u.domain.Style;
import com.photograph_u.domain.VerifyDao;
import com.photograph_u.util.CommUtils;
import com.photograph_u.util.JdbcUtils;

import java.util.List;
import java.util.Map;

public class AdminService {
    public int login(String account, String password) {
        AdminDao adminDao = new AdminDao();
        int adminId = adminDao.queryAdminId(account, password);
        return adminId;
    }

    //列出所有未审核信息
    public List<Map<String, Object>> listUnTreatedVerifies() {
        VerifyDao verifyDao = new VerifyDao();
        UserDao userDao = new UserDao();
        List<Map<String, Object>> verifyMapList = verifyDao.queryUnTreatedVerifies();
        for (Map<String, Object> verifyMap : verifyMapList) {
            int userId = (int) verifyMap.get("userId");
            Map<String, Object> userMap = userDao.queryUserHeadImageAndNickNameById(userId);
            verifyMap.put("user", userMap);
        }
        return verifyMapList;
    }

    //分页列出所有未审核信息
    public PageBean<List<Map<String, Object>>> listUnTreatedVerifiesWithPage(int pageSize, int currentPage) {
        VerifyDao verifyDao = new VerifyDao();
        UserDao userDao = new UserDao();
        int totalRows = verifyDao.countVerifiesByState(0);//未审核的总行数
        PageBean<List<Map<String, Object>>> pageBean = new PageBean<>(pageSize, currentPage, totalRows);
        int startIndex = pageBean.getStartIndex();
        List<Map<String, Object>> verifyMapList = verifyDao.queryUnTreatedVerifiesWithOffset(pageSize, startIndex);
        for (Map<String, Object> verifyMap : verifyMapList) {
            int userId = (int) verifyMap.get("userId");
            Map<String, Object> userMap = userDao.queryUserHeadImageAndNickNameById(userId);
            verifyMap.put("user", userMap);
        }
        pageBean.setData(verifyMapList);
        return pageBean;
    }

    //列出所有已审核信息
    public List<Map<String, Object>> listTreatedVerifies() {
        VerifyDao verifyDao = new VerifyDao();
        UserDao userDao = new UserDao();
        List<Map<String, Object>> verifyMapList = verifyDao.queryTreatedVerifies();
        for (Map<String, Object> verifyMap : verifyMapList) {
            int userId = (int) verifyMap.get("userId");
            Map<String, Object> userMap = userDao.queryUserHeadImageAndNickNameById(userId);
            verifyMap.put("user", userMap);
        }
        return verifyMapList;
    }

    //分页列出所有已审核信息
    public PageBean<List<Map<String, Object>>> listTreatedVerifiesWithPage(int pageSize, int currentPage) {
        VerifyDao verifyDao = new VerifyDao();
        UserDao userDao = new UserDao();
        int totalRows = verifyDao.countVerifiesByState(1) + verifyDao.countVerifiesByState(2);
        PageBean<List<Map<String, Object>>> pageBean = new PageBean<>(pageSize, currentPage, totalRows);
        int startIndex = pageBean.getStartIndex();
        List<Map<String, Object>> verifyMapList = verifyDao.queryTreatedVerifiesWithOffset(pageSize, startIndex);
        for (Map<String, Object> verifyMap : verifyMapList) {
            int userId = (int) verifyMap.get("userId");
            Map<String, Object> userMap = userDao.queryUserHeadImageAndNickNameById(userId);
            verifyMap.put("user", userMap);
        }
        pageBean.setData(verifyMapList);
        return pageBean;
    }

    //审核用户成为摄影师通过
    public boolean verifyPhotographerQualified(int verifyId) {
        boolean succeed = true;
        PhotographerDao photographerDao = new PhotographerDao();
        VerifyDao verifyDao = new VerifyDao();
        if (verifyDao.checkVerifyById(verifyId)) {
            return false;
        }
        Map<String, Object> verifyMap = verifyDao.queryVerifyById(verifyId);
        int cardId = (int) verifyMap.get("userId");
        String cardNo = (String) verifyMap.get("cardNo");
        String cardImage = (String) verifyMap.get("cardImage");
        JdbcUtils.beginTransaction();
        if (!photographerDao.addPhotographer(cardId, cardNo, cardImage)) {
            succeed = false;
        }
        if (!verifyDao.changeVerifyState(verifyId, 1, CommUtils.getDateTime())) {
            succeed = false;
        }
        JdbcUtils.commitTransaction();
        return succeed;
    }

    //审核用户成为摄影师未通过
    public boolean verifyPhotographerUnQualified(int verifyId) {
        VerifyDao verifyDao = new VerifyDao();
        if (verifyDao.checkVerifyById(verifyId)) {
            return false;
        }
        return verifyDao.changeVerifyState(verifyId, 2, CommUtils.getDateTime());
    }

    //查询所有风格
    public List<Style> listAllStyles() {
        StyleDao styleDao = new StyleDao();
        return styleDao.queryAllStyles();
    }

    //添加风格
    public boolean addStyle(String name, String image) {
        StyleDao styleDao = new StyleDao();
        return styleDao.addStyle(name, image);
    }

    //删除风格
    public boolean deleteStyle(int styleId) {
        StyleDao styleDao = new StyleDao();
        return styleDao.deleteStyle(styleId);
    }
}
