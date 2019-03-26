package com.photograph_u.service;

import com.photograph_u.dao.*;
import com.photograph_u.domain.*;
import com.photograph_u.util.CommUtils;
import com.photograph_u.util.JdbcUtils;
import com.photograph_u.util.MessageCodeSendUtil;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UserService {
    //发送验证码
    public boolean sendMessageCode(String phone, HttpSession session) {
        boolean succeed;
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        buffer.append(random.nextInt(10));
        buffer.append(random.nextInt(10));
        buffer.append(random.nextInt(10));
        buffer.append(random.nextInt(10));
        buffer.append(random.nextInt(10));
        buffer.append(random.nextInt(10));
        String messageCode = buffer.toString();
        MessageCodeSendUtil.sendMessageCode(phone, messageCode, 30);
        session.setAttribute(phone, messageCode);//手机号—验证码
        succeed = true;
        return succeed;
    }

    //注册
    public boolean register(String phone, String password) {
        UserDao userDao = new UserDao();
        if (userDao.checkUser(phone)) {
            return false;
        }
        return userDao.addUser(phone, password);
    }

    //登录
    public User login(String phone, String password) {
        UserDao userDao = new UserDao();
        User user = userDao.queryUser(phone, password);
        return user;
    }

    //修改密码
    public boolean updatePassword(int userId, String password, String newPassword) {
        UserDao userDao = new UserDao();
        return userDao.updatePassword(userId, password, newPassword);
    }

    //重置密码
    public boolean resetPassword(String phone, String newPassword) {
        UserDao userDao = new UserDao();
        return userDao.resetPassword(phone, newPassword);
    }

    //设置头像
    public boolean setHeadImage(int userId, String headImage) {
        UserDao userDao = new UserDao();
        return userDao.updateHeadImage(userId, headImage);

    }

    //查询头像
    public String queryHeadImage(int userId) {
        UserDao userDao = new UserDao();
        Map<String, Object> headImageMap = userDao.queryHeadImage(userId);
        if (headImageMap != null) {
            return String.valueOf(headImageMap.get("headImage"));
        }
        return null;
    }

    //修改个人信息
    public boolean updateInfo(int userId, User user) {
        user.setId(userId);
        UserDao userDao = new UserDao();
        return userDao.updateInfo(user);
    }

    //查询个人信息
    public User lookInfo(int userId) {
        UserDao userDao = new UserDao();
        User user = userDao.queryUserById(userId);
        return user;
    }

    //查询所有风格
    public List<Style> listAllStyles() {
        StyleDao styleDao = new StyleDao();
        return styleDao.queryAllStyles();
    }


    //发布帖子
    public boolean releaseNote(int userId, int styleId, String content, List<String> imageList) {
        JdbcUtils.beginTransaction();//开启事物
        NoteDao noteDao = new NoteDao();
        ImageDao imageDao = new ImageDao();
        StyleDao styleDao = new StyleDao();
        String datetime = CommUtils.getDateTime();
        boolean succeed = true;
        if (!styleDao.checkStyleById(styleId)) {
            succeed = false;
        }
        if (!noteDao.addNote(userId, styleId, content, datetime)) {//执行了插入操作会产生auto_increment的id
            succeed = false;
        }
        int noteId = noteDao.queryLastInsertId();//查出auto_increment的id
        if (imageList != null) {
            for (String imageName : imageList) {
                if (!imageDao.addImage(noteId, imageName)) {
                    succeed = false;
                }
            }
        }
        JdbcUtils.commitTransaction();//提交事物
        return succeed;
    }

    //查询所有帖子
    public List<Map<String, Object>> listAllNotes(int styleId) {
        NoteDao noteDao = new NoteDao();
        UserDao userDao = new UserDao();
        CommentDao commentDao = new CommentDao();
        AdmireDao admireDao = new AdmireDao();
        ImageDao imageDao = new ImageDao();
        //查询帖子
        List<Map<String, Object>> noteMapList = noteDao.queryAllNotes(styleId);
        for (Map<String, Object> noteMap : noteMapList) {
            int noteId = (int) noteMap.get("id");
            int userId = (int) noteMap.get("userId");
            //查询每个帖子的用户头像与昵称
            Map<String, Object> userMap = userDao.queryUserHeadImageAndNickNameById(userId);
            noteMap.put("user", userMap);
            //查询帖子的图片
            List<Image> imageList = imageDao.queryImagesByNoteId(noteId);
            noteMap.put("images", imageList);
            //查询每个帖子的点赞数量
            int admireCount = admireDao.queryAdmireAmountById(noteId);
            //查询每个帖子的顶级评论数量
            int commentCount = commentDao.queryCommentAmount(noteId);
            noteMap.put("admireCount", admireCount);
            noteMap.put("commentCount", commentCount);
        }
        return noteMapList;
    }

    //分页查询所有帖子
    public PageBean<List<Map<String, Object>>> listAllNotesWithPage(int styleId, int pageSize, int currentPage) {
        NoteDao noteDao = new NoteDao();
        UserDao userDao = new UserDao();
        CommentDao commentDao = new CommentDao();
        AdmireDao admireDao = new AdmireDao();
        ImageDao imageDao = new ImageDao();
        int totalRows = noteDao.countNotes(styleId);
        PageBean<List<Map<String, Object>>> pageBean = new PageBean<>(pageSize, currentPage, totalRows);
        int startIndex = pageBean.getStartIndex();
        List<Map<String, Object>> noteMapList = noteDao.queryAllNotesWithOffset(styleId, pageSize, startIndex);
        for (Map<String, Object> noteMap : noteMapList) {
            int noteId = (int) noteMap.get("id");
            int userId = (int) noteMap.get("userId");
            Map<String, Object> userMap = userDao.queryUserHeadImageAndNickNameById(userId);
            List<Image> imageList = imageDao.queryImagesByNoteId(noteId);
            noteMap.put("user", userMap);
            noteMap.put("images", imageList);
            int admireCount = admireDao.queryAdmireAmountById(noteId);
            int commentCount = commentDao.queryCommentAmount(noteId);
            noteMap.put("admireCount", admireCount);
            noteMap.put("commentCount", commentCount);
        }
        pageBean.setData(noteMapList);
        return pageBean;
    }

    //根据帖子ID查看详情
    public Map<String, Object> findNoteById(int noteId) {
        NoteDao noteDao = new NoteDao();
        UserDao userDao = new UserDao();
        CommentDao commentDao = new CommentDao();
        AdmireDao admireDao = new AdmireDao();
        ImageDao imageDao = new ImageDao();
        Map<String, Object> noteMap = noteDao.queryNoteById(noteId);
        int userId = (int) noteMap.get("userId");
        //查询每个帖子的用户头像与昵称
        Map<String, Object> userMap = userDao.queryUserHeadImageAndNickNameById(userId);
        noteMap.put("user", userMap);
        //查询帖子的图片
        List<Image> imageList = imageDao.queryImagesByNoteId(noteId);
        noteMap.put("images", imageList);
        //查询每个帖子的点赞
        List<Map<String, Object>> admireMapList = admireDao.queryAdmireById(noteId);
        //查询每个帖子的顶级评论
        List<Map<String, Object>> fatherCommentMapList = commentDao.queryAllFatherCommentsByNoteId(noteId);
        for (Map<String, Object> fatherComment : fatherCommentMapList) {
            int commentId = (int) fatherComment.get("id");
            //查询每个父评论的子评论
            List<Map<String, Object>> sonCommentMapList = commentDao.queryAllSonCommentsByNoteId(noteId, commentId);
            fatherComment.put("son_comments", sonCommentMapList);
        }
        noteMap.put("comments", fatherCommentMapList);
        noteMap.put("admires", admireMapList);
        return noteMap;
    }

    //评论帖子
    public boolean commentNote(int userId, int noteId, String content, int fatherId) {
        CommentDao commentDao = new CommentDao();
        return commentDao.addComment(userId, noteId, content, fatherId);
    }

    //删除自己对别人的评论
    public boolean deleteMyComment(int userId, int commentId) {
        CommentDao commentDao = new CommentDao();
        if (!commentDao.checkMyComment(userId, commentId)) {
            return false;
        }
        return commentDao.deleteComment(commentId);
    }

    //删除别人对自己的评论,即检查自己的帖子有没有该条评论
    public boolean deleteOthersComment(int userId, int commentId) {
        CommentDao commentDao = new CommentDao();
        if (!commentDao.checkOthersComment(userId, commentId)) {
            return false;
        }
        return commentDao.deleteComment(commentId);
    }

    //点赞帖子
    public boolean admireNote(int userId, int noteId) {
        AdmireDao admireDao = new AdmireDao();
        if (admireDao.checkAdmire(userId, noteId)) {
            return false;
        }
        return admireDao.addAdmire(userId, noteId);
    }

    //取消对别人帖子的点赞
    public boolean deleteAdmire(int userId, int noteId) {
        AdmireDao admireDao = new AdmireDao();
        return admireDao.deleteAdmire(userId, noteId);
    }

    //根据用户ID查询的帖子
    public List<Map<String, Object>> listMyNotes(int userId) {
        NoteDao noteDao = new NoteDao();
        UserDao userDao = new UserDao();
        CommentDao commentDao = new CommentDao();
        AdmireDao admireDao = new AdmireDao();
        ImageDao imageDao = new ImageDao();
        //查询帖子
        List<Map<String, Object>> noteMapList = noteDao.queryNotesByUserId(userId);
        for (Map<String, Object> noteMap : noteMapList) {
            int noteId = (int) noteMap.get("id");
            //查询每个帖子的用户头像与昵称
            Map<String, Object> userMap = userDao.queryUserHeadImageAndNickNameById(userId);
            noteMap.put("user", userMap);
            //查询帖子的图片
            List<Image> imageList = imageDao.queryImagesByNoteId(noteId);
            noteMap.put("images", imageList);
            //查询每个帖子的点赞数量
            int admireCount = admireDao.queryAdmireAmountById(noteId);
            //查询每个帖子的顶级评论数量
            int commentCount = commentDao.queryCommentAmount(noteId);
            noteMap.put("admireCount", admireCount);
            noteMap.put("commentCount", commentCount);
        }
        return noteMapList;
    }

    //删除我的帖子
    public boolean deleteMyNote(int userId, int noteId) {
        NoteDao noteDao = new NoteDao();
        return noteDao.deleteNote(userId, noteId);
    }

    /**
     * 申请成为摄影师
     */
    public boolean requestToBePhotographer(int userId, String cardNo, String cardImage) {
        VerifyDao verifyDao = new VerifyDao();
        if (verifyDao.checkVerifyByUserId(userId)) {
            return false;
        }
        return verifyDao.addVerify(userId, cardNo, cardImage, CommUtils.getDateTime());
    }

    //查询申请记录
    public List<Map<String, Object>> listVerifyRecord(int userId) {
        VerifyDao verifyDao = new VerifyDao();
        List<Map<String, Object>> verifyMapList = verifyDao.queryVerifys(userId);
        return verifyMapList;
    }

    //检查用户是否摄影师
    public boolean checkIsPhotographer(int userId) {
        PhotographerDao photographerDao = new PhotographerDao();
        return photographerDao.queryExists(userId);
    }

    //根据学校列出摄影师
    public List<Map<String, Object>> listPhotographerBySchool(String school) {
        PhotographerDao photographerDao = new PhotographerDao();
        List<Map<String, Object>> photographerMapList = photographerDao.queryPhotographerBySchool(school);
        return photographerMapList;
    }

    //根据学校分页列出摄影师
    public PageBean<List<Map<String, Object>>> listPhotographerBySchoolWithPage(String school, int pageSize, int currrentPage) {
        PhotographerDao photographerDao = new PhotographerDao();
        int totalRows = photographerDao.countPhotographerBySchool(school);
        PageBean<List<Map<String, Object>>> pageBean = new PageBean<>(pageSize, currrentPage, totalRows);
        int startIndex = pageBean.getStartIndex();
        List<Map<String, Object>> photographerMapList = photographerDao.queryPhotographerBySchoolWithOffset(school, pageSize, startIndex);
        pageBean.setData(photographerMapList);
        return pageBean;
    }


    //查询摄影师信息
    public Map<String, Object> findPhotographerById(int photographerId) {
        PhotographerDao photographerDao = new PhotographerDao();
        PhotoDao photoDao = new PhotoDao();
        ReviewDao reviewDao = new ReviewDao();
        FollowDao followDao = new FollowDao();
        UserDao userDao = new UserDao();
        Map<String, Object> photographerMap = photographerDao.queryPhotographerById(photographerId);
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

    //预约摄影师
    public boolean orderPhotographer(int userId, int photographerId, String address, String other) {
        OrderInfoDao orderInfoDao = new OrderInfoDao();
        PhotographerDao photographerDao = new PhotographerDao();
        if (photographerDao.queryExistsByUserIdAndPhotographerId(userId, photographerId)) {//检测是否预约自己
            return false;
        }
        return orderInfoDao.addOrderInfo(userId, photographerId, CommUtils.getDateTime(), address, other);
    }

    //取消预约尚未处理
    public boolean deleteOrder(int userId, int orderInfoId) {
        OrderInfoDao orderInfoDao = new OrderInfoDao();
        return orderInfoDao.deleteOrderInfo(userId, orderInfoId);
    }

    //查询我的预约订单
    public List<Map<String, Object>> listOrders(int userId) {
        OrderInfoDao orderInfoDao = new OrderInfoDao();
        PhotographerDao photographerDao = new PhotographerDao();
        UserDao userDao = new UserDao();
        List<Map<String, Object>> orderMapList = orderInfoDao.queryOrdersByUserId(userId);
        for (Map<String, Object> orderMap : orderMapList) {
            int photographerId = (int) orderMap.get("photographerId");
            System.out.println(photographerId);
            int photographerUserId = photographerDao.queryUserIdByPhotographerId(photographerId);
            Map<String, Object> nicknameAndHeadImageMap = userDao.queryUserHeadImageAndNickNameById(photographerUserId);
            orderMap.put("photographer", nicknameAndHeadImageMap);
        }
        return orderMapList;
    }

    //分页查询我的预约订单
    public PageBean<List<Map<String, Object>>> listOrdersWithPage(int userId, int pageSize, int currentPage) {
        PhotographerDao photographerDao = new PhotographerDao();
        OrderInfoDao orderInfoDao = new OrderInfoDao();
        UserDao userDao = new UserDao();
        int totalRows = orderInfoDao.countOrders(userId);
        PageBean<List<Map<String, Object>>> pageBean = new PageBean<>(pageSize, currentPage, totalRows);
        int startIndex = pageBean.getStartIndex();
        List<Map<String, Object>> resultMapList = orderInfoDao.queryOrdersByUserIdWithOffset(userId, pageSize, startIndex);
        for (Map<String, Object> orderMap : resultMapList) {
            int photographerId = (int) orderMap.get("photographerId");
            int photographerUserId = photographerDao.queryUserIdByPhotographerId(photographerId);
            Map<String, Object> nicknameAndHeadImageMap = userDao.queryUserHeadImageAndNickNameById(photographerUserId);
            orderMap.put("photographer", nicknameAndHeadImageMap);
        }
        pageBean.setData(resultMapList);
        return pageBean;
    }

    //关注摄影师
    public boolean followPhotographer(int userId, int photographerId) {
        FollowDao followDao = new FollowDao();
        PhotographerDao photographerDao = new PhotographerDao();
        if (followDao.checkFollow(userId, photographerId)) { //检查是否已关注
            return false;
        }
        if (photographerDao.queryExistsByUserIdAndPhotographerId(userId, photographerId)) {//检查是否自己关注自己
            return false;
        }
        return followDao.addFollow(userId, photographerId);
    }

    //取消关注
    public boolean deleteFollow(int userId, int photographerId) {
        FollowDao followDao = new FollowDao();
        if (!followDao.checkFollow(userId, photographerId)) { //检查是否已关注
            return false;
        }
        return followDao.deleteFollow(userId, photographerId);
    }

    //查询关注的摄影师
    public List<Map<String, Object>> listFollows(int userId) {
        FollowDao followDao = new FollowDao();
        PhotographerDao photographerDao = new PhotographerDao();
        UserDao userDao = new UserDao();
        List<Map<String, Object>> followMapList = followDao.queryFollowsByUserId(userId);
        for (Map<String, Object> followMap : followMapList) {
            int photographerId = (int) followMap.get("photographerId");
            int photographerUserId = photographerDao.queryUserIdByPhotographerId(photographerId);
            Map<String, Object> nicknameAndHeadImageMap = userDao.queryUserHeadImageAndNickNameById(photographerUserId);
            followMap.put("info", nicknameAndHeadImageMap);
        }
        return followMapList;
    }

    //评价摄影师
    public boolean reviewPhotographer(int userId, int photographerId, String content, int changeStarValue) {
        JdbcUtils.beginTransaction();
        boolean succeed = true;
        OrderInfoDao orderInfoDao = new OrderInfoDao();
        ReviewDao reviewDao = new ReviewDao();
        PhotographerDao photographerDao = new PhotographerDao();
        if (!orderInfoDao.checkOrderInfoByUserIdAndPhotographerId(userId, photographerId)) {      //检查是否有过交易
            succeed = false;
        }
        if (!reviewDao.addReview(userId, photographerId, content)) {
            succeed = false;
        }
        if (!photographerDao.updateStarValue(photographerId, changeStarValue)) {
            succeed = false;
        }
        JdbcUtils.commitTransaction();
        return succeed;
    }

}
