package com.photograph_u.action;

import com.photograph_u.consts.FileUriConsts;
import com.photograph_u.domain.MyResponse;
import com.photograph_u.domain.PageBean;
import com.photograph_u.domain.Style;
import com.photograph_u.domain.User;
import com.photograph_u.service.UserService;
import com.photograph_u.util.CommUtils;
import com.photograph_u.util.FileUploadUtil;
import com.photograph_u.util.ResponseUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/user/*")
public class UserAction extends BaseAction {

    //获取验证码
    public void requestMessageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        String phone = request.getParameter("phone");
        if (service.sendMessageCode(phone, request.getSession())) {
            myResponse.setCode(0);
            myResponse.setMessage("验证码获取成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("验证码获取失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //注册
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String messageCode = request.getParameter("message_code");
        String originMessageCode = (String) request.getSession().getAttribute(phone);
        if (!messageCode.equals(originMessageCode)) {
            myResponse.setCode(1);
            myResponse.setMessage("验证码错误");
        } else {
            if (service.register(phone, password)) {
                myResponse.setCode(0);
                myResponse.setMessage("注册成功");
            } else {
                myResponse.setCode(1);
                myResponse.setMessage("用户已存在");
            }
        }
        ResponseUtil.sendResponse(response, myResponse);
    }


    //登录
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<User> myResponse = new MyResponse<>();
        UserService service = new UserService();
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        User user = service.login(phone, password);
        if (user == null) {
            myResponse.setCode(1);
            myResponse.setMessage("账号或密码错误");
        } else {
            request.getSession().setMaxInactiveInterval(1000 * 60 * 60 * 24 * 30);
            //记录登录Session
            request.getSession().setAttribute("user_id", user.getId());
            //如果为摄影师，记录摄影师Session
            if (service.checkIsPhotographer(user.getId())) {
                request.getSession().setAttribute("photographer_user_id", user.getId());
            }
            myResponse.setCode(0);
            myResponse.setMessage("登录成功");
            myResponse.setData(user);
        }
        ResponseUtil.sendResponseWithDate(response, myResponse);
    }

    //修改密码
    public void updatePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        String password = request.getParameter("password");
        String newPassword = request.getParameter("new_password");
        if (service.updatePassword(userId, password, newPassword)) {
            myResponse.setCode(0);
            myResponse.setMessage("密码修改成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("原密码输入错误");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //密码重置
    public void resetPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        String phone = request.getParameter("phone");
        String newPassword = request.getParameter("new_password");
        String messageCode = request.getParameter("message_code");
        String originMessageCode = (String) request.getSession().getAttribute(phone);
        if (!messageCode.equals(originMessageCode)) {
            myResponse.setCode(1);
            myResponse.setMessage("验证码错误");
        } else {
            if (service.resetPassword(phone, newPassword)) {
                myResponse.setCode(0);
                myResponse.setMessage("重置成功");
            } else {
                myResponse.setCode(1);
                myResponse.setMessage("重置失败");
            }
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //修改个人信息
    public void updateInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        Map<String, String[]> userInfoMap = request.getParameterMap();
        int userId = (int) request.getSession().getAttribute("user_id");
        User user = CommUtils.convertMapToBean(userInfoMap, User.class);
        if (service.updateInfo(userId, user)) {
            myResponse.setCode(0);
            myResponse.setMessage("修改成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("修改失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //设置头像
    public void setHeadImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        int userId = (int) request.getSession().getAttribute("user_id");
        FileUploadUtil.upload(request, FileUriConsts.HEAD_IMAGE_URI, 0, 1);
        Map parameterMap = (Map) request.getAttribute("parameterMap");
        List fileList = (List) parameterMap.get("files");
        UserService service = new UserService();
        if (service.setHeadImage(userId, (String) fileList.get(0))) {
            myResponse.setCode(0);
            myResponse.setMessage("头像设置成功");
            myResponse.setData(service.queryHeadImage(userId));
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("头像设置失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //查询个人信息
    public void lookInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<User> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        User user = service.lookInfo(userId);
        if (user != null) {
            myResponse.setCode(0);
            myResponse.setMessage("查询成功");
            myResponse.setData(user);
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("查询失败");
        }
        ResponseUtil.sendResponseWithDate(response, myResponse);
    }

    //列出所有风格
    public void listAllStyles(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<List<Style>> myResponse = new MyResponse<>();
        UserService service = new UserService();
        List<Style> styleList = service.listAllStyles();
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(styleList);
        ResponseUtil.sendResponse(response, myResponse);
    }

    //发布帖子
    public void releaseNote(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        FileUploadUtil.upload(request, FileUriConsts.NOTE_IMAGE_URI, 2, 5);
        Map parameterMap = (Map) request.getAttribute("parameterMap");
        List imageList = (List) parameterMap.get("files");
        int styleId = Integer.parseInt((String) parameterMap.get("style_id"));
        String content = (String) parameterMap.get("content");
        if (service.releaseNote(userId, styleId, content, imageList)) {
            myResponse.setCode(0);
            myResponse.setMessage("发布成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("发布失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    /**
     * 查询所有帖子
     */
    public void listAllNotes(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<List<Map<String, Object>>> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int styleId = Integer.parseInt(request.getParameter("style_id"));
        List<Map<String, Object>> notes = service.listAllNotes(styleId);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(notes);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //分页查询所有帖子
    public void listAllNotesWithPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<PageBean<List<Map<String, Object>>>> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int styleId = Integer.parseInt(request.getParameter("style_id"));
        int pageSize = Integer.parseInt(request.getParameter("page_size"));
        int currentPage = Integer.parseInt(request.getParameter("current_page"));
        PageBean<List<Map<String, Object>>> pageBean = service.listAllNotesWithPage(styleId, pageSize, currentPage);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(pageBean);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //评论帖子
    public void commentNote(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        int noteId = Integer.parseInt(request.getParameter("note_id"));
        String content = request.getParameter("content");
        int fatherId = Integer.parseInt(request.getParameter("father_id"));//顶级评论,父评论id为0
        if (service.commentNote(userId, noteId, content, fatherId)) {
            myResponse.setCode(0);
            myResponse.setMessage("评论成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("评论失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //根据ID查看帖子详情
    public void findNoteById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<Map<String, Object>> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int noteId = Integer.parseInt(request.getParameter("note_id"));
        Map<String, Object> noteMap = service.findNoteById(noteId);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(noteMap);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //删除自己对别人帖子的评论
    public void deleteMyComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int commentId = Integer.parseInt(request.getParameter("comment_id"));
        int userId = (int) request.getSession().getAttribute("user_id");
        if (service.deleteMyComment(userId, commentId)) {
            myResponse.setCode(0);
            myResponse.setMessage("删除成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("删除失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }


    //删除别人对自己帖子的评论
    public void deleteOthersComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int commentId = Integer.parseInt(request.getParameter("comment_id"));
        int userId = (int) request.getSession().getAttribute("user_id");
        if (service.deleteOthersComment(userId, commentId)) {
            myResponse.setCode(0);
            myResponse.setMessage("删除成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("删除失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //点赞帖子
    public void admireNote(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        int noteId = Integer.parseInt(request.getParameter("note_id"));
        if (service.admireNote(userId, noteId)) {
            myResponse.setCode(0);
            myResponse.setMessage("点赞成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("点赞失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //取消点赞
    public void deleteAdmire(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        int noteId = Integer.parseInt(request.getParameter("note_id"));
        if (service.deleteAdmire(userId, noteId)) {
            myResponse.setCode(0);
            myResponse.setMessage("取消点赞成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("操作失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //列出我发布的帖子
    public void listMyNotes(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<List<Map<String, Object>>> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        List<Map<String, Object>> notes = service.listMyNotes(userId);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(notes);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //删除我发布的帖子
    public void deleteMyNote(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        int noteId = Integer.parseInt(request.getParameter("note_id"));
        if (service.deleteMyNote(userId, noteId)) {
            myResponse.setCode(0);
            myResponse.setMessage("删除成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("删除失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //查看他人的个人信息
    public void lookOthersInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<User> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = Integer.parseInt(request.getParameter("user_id"));
        User user = service.lookInfo(userId);
        if (user != null) {
            myResponse.setCode(0);
            myResponse.setMessage("查询他人信息成功");
            myResponse.setData(user);
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("查询失败");
        }
        ResponseUtil.sendResponseWithDate(response, myResponse);
    }

    //查询自己是否摄影师
    public void queryMyIdentity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        if (service.checkIsPhotographer(userId)) {
            myResponse.setCode(0);
            myResponse.setMessage("摄影师");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("普通用户");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //查询他人是否摄影师
    public void queryOthersIdentity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = Integer.parseInt(request.getParameter("user_id"));
        if (service.checkIsPhotographer(userId)) {
            myResponse.setCode(0);
            myResponse.setMessage("该用户为摄影师");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("该用户为普通用户");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    /**
     * 申请成为摄影师
     */
    public void requestToBePhotographer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<User> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        FileUploadUtil.upload(request, FileUriConsts.VERIFY_IMAGE_URI, 1, 1);
        Map parameterMap = (Map) request.getAttribute("parameterMap");
        List imageList = (List) parameterMap.get("files");
        String cardNo = String.valueOf(parameterMap.get("card_no"));
        String cardImage = String.valueOf(imageList.get(0));
        if (service.requestToBePhotographer(userId, cardNo, cardImage)) {
            myResponse.setCode(0);
            myResponse.setMessage("提交审核成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("提交审核失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //查询自己的申请记录
    public void listVerifyRecord(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<List<Map<String, Object>>> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        List<Map<String, Object>> verifyMapList = service.listVerifyRecord(userId);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(verifyMapList);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //列出学校所有摄影师
    public void listPhotographersBySchool(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<List<Map<String, Object>>> myResponse = new MyResponse<>();
        UserService service = new UserService();
        String school = request.getParameter("school");
        List<Map<String, Object>> photographerMapList = service.listPhotographerBySchool(school);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(photographerMapList);
        ResponseUtil.sendResponse(response, myResponse);
    }

    //分页列出学校所有摄影师
    public void listPhotographersBySchoolWithPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<PageBean<List<Map<String, Object>>>> myResponse = new MyResponse<>();
        UserService service = new UserService();
        String school = request.getParameter("school");
        int pageSize = Integer.parseInt(request.getParameter("page_size"));
        int currentPage = Integer.parseInt(request.getParameter("current_page"));
        PageBean<List<Map<String, Object>>> pageBean = service.listPhotographerBySchoolWithPage(school, pageSize, currentPage);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(pageBean);
        ResponseUtil.sendResponse(response, myResponse);

    }

    //查看摄影师信息
    public void findPhotographerById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int photographerId = Integer.parseInt(request.getParameter("photographer_id"));
        MyResponse<Map<String, Object>> myResponse = new MyResponse<>();
        UserService service = new UserService();
        Map<String, Object> resultMap = service.findPhotographerById(photographerId);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(resultMap);
        ResponseUtil.sendResponse(response, myResponse);
    }

    //预约摄影师
    public void orderPhotographer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        int photographerId = Integer.parseInt(request.getParameter("photographer_id"));
        String address = request.getParameter("address");
        String other = request.getParameter("other");
        if (service.orderPhotographer(userId, photographerId, address, other)) {
            myResponse.setCode(0);
            myResponse.setMessage("预约成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("预约失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //取消尚未接单的预约
    public void cancelOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        int orderInfoId = Integer.parseInt(request.getParameter("orderinfo_id"));
        if (service.deleteOrder(userId, orderInfoId)) {
            myResponse.setCode(0);
            myResponse.setMessage("取消成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("取消失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //列出我的预约订单
    public void listOrders(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<List<Map<String, Object>>> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        List<Map<String, Object>> orderMapList = service.listOrders(userId);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(orderMapList);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //分页列出我的预约订单
    public void listOrdersWithPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<PageBean<List<Map<String, Object>>>> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        int pageSize = Integer.parseInt(request.getParameter("page_size"));
        int currentPage = Integer.parseInt(request.getParameter("current_page"));
        PageBean<List<Map<String, Object>>> pageBean = service.listOrdersWithPage(userId, pageSize, currentPage);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(pageBean);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //关注摄影师
    public void followPhotographer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        int photographerId = Integer.parseInt(request.getParameter("photographer_id"));
        if (service.followPhotographer(userId, photographerId)) {
            myResponse.setCode(0);
            myResponse.setMessage("关注成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("关注失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //取消关注
    public void cancelFollow(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        int photographerId = Integer.parseInt(request.getParameter("photographer_id"));
        if (service.deleteFollow(userId, photographerId)) {
            myResponse.setCode(0);
            myResponse.setMessage("取消成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("取消失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //查询我的关注
    public void listFollows(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<List<Map<String, Object>>> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        List<Map<String, Object>> followMapList = service.listFollows(userId);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(followMapList);
        ResponseUtil.sendResponse(response, myResponse);
    }

    //评价摄影师
    public void reviewPhotographer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        UserService service = new UserService();
        int userId = (int) request.getSession().getAttribute("user_id");
        int photographerId = Integer.parseInt(request.getParameter("photographer_id"));
        String content = request.getParameter("content");
        int changeStarValue = Integer.parseInt(request.getParameter("change_star_value"));
        if (service.reviewPhotographer(userId, photographerId, content, changeStarValue)) {
            myResponse.setCode(0);
            myResponse.setMessage("评价成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("评价失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }
}
