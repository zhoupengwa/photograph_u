package com.photograph_u.action;

import com.photograph_u.consts.FileUriConsts;
import com.photograph_u.domain.MyResponse;
import com.photograph_u.domain.PageBean;
import com.photograph_u.domain.Style;
import com.photograph_u.service.AdminService;
import com.photograph_u.service.UserService;
import com.photograph_u.util.FileUploadUtil;
import com.photograph_u.util.ResponseUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/*")
public class AdminAction extends BaseAction {
    //登录
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        AdminService service = new AdminService();
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        int adminId = service.login(account, password);
        if (adminId == 0) {
            myResponse.setCode(1);
            myResponse.setMessage("账号或密码错误");
        } else {
            request.getSession().setMaxInactiveInterval(1000*60*60*24*30);
            request.getSession().setAttribute("admin_id", adminId);
            myResponse.setCode(0);
            myResponse.setMessage("登录成功");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //列出未审核申请
    public void listUnTreatedVerifies(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<List<Map<String, Object>>> myResponse = new MyResponse<>();
        AdminService service = new AdminService();
        List<Map<String, Object>> mapList = service.listUnTreatedVerifies();
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(mapList);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //分页列出未审核申请
    public void listUnTreatedVerifiesWithPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<PageBean<List<Map<String, Object>>>> myResponse = new MyResponse<>();
        AdminService service = new AdminService();
        int pageSize = Integer.parseInt(request.getParameter("page_size"));
        int currentPage = Integer.parseInt(request.getParameter("current_page"));
        PageBean<List<Map<String, Object>>> pageBean = service.listUnTreatedVerifiesWithPage(pageSize, currentPage);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(pageBean);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //列出已审核申请
    public void listTreatedVerifies(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<List<Map<String, Object>>> myResponse = new MyResponse<>();
        AdminService service = new AdminService();
        List<Map<String, Object>> mapList = service.listTreatedVerifies();
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(mapList);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //分页列出已审核申请
    public void listTreatedVerifiesWithPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<PageBean<List<Map<String, Object>>>> myResponse = new MyResponse<>();
        AdminService service = new AdminService();
        int pageSize = Integer.parseInt(request.getParameter("page_size"));
        int currentPage = Integer.parseInt(request.getParameter("current_page"));
        PageBean<List<Map<String, Object>>> pageBean = service.listTreatedVerifiesWithPage(pageSize, currentPage);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(pageBean);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //审核摄影师通过
    public void verifyPhotographerQualified(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        AdminService service = new AdminService();
        int verifyId = Integer.parseInt(request.getParameter("verify_id"));
        if (service.verifyPhotographerQualified(verifyId)) {
            myResponse.setCode(0);
            myResponse.setMessage("审核成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("审核失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //审核摄影师未通过
    public void verifyPhotographerUnQualified(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        AdminService service = new AdminService();
        int verifyId = Integer.parseInt(request.getParameter("verify_id"));
        if (service.verifyPhotographerUnQualified(verifyId)) {
            myResponse.setCode(0);
            myResponse.setMessage("审核成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("审核失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //列出所有风格
    public void listStyles(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<List<Style>> myResponse = new MyResponse<>();
        AdminService service = new AdminService();
        List<Style> styleList = service.listAllStyles();
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(styleList);
        ResponseUtil.sendResponse(response, myResponse);
    }

    //添加风格
    public void addStyle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        AdminService service = new AdminService();
        FileUploadUtil.upload(request, FileUriConsts.STYLE_IMAGE_URI, 1, 1);
        Map parameterMap = (Map) request.getAttribute("parameterMap");
        String name = String.valueOf(parameterMap.get("name"));
        List fileList = (List) parameterMap.get("files");
        String image = String.valueOf(fileList.get(0));
        if (service.addStyle(name, image)) {
            myResponse.setCode(0);
            myResponse.setMessage("添加成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("添加失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //删除风格
    public void deleteStyle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        AdminService service = new AdminService();
        int styleId = Integer.parseInt(request.getParameter("style_id"));
        if (service.deleteStyle(styleId)) {
            myResponse.setCode(0);
            myResponse.setMessage("删除成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("删除失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }
}
