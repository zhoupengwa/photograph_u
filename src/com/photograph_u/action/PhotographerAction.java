package com.photograph_u.action;

import com.photograph_u.consts.FileUriConsts;
import com.photograph_u.domain.MyResponse;
import com.photograph_u.domain.PageBean;
import com.photograph_u.service.PhotographerService;
import com.photograph_u.util.FileUploadUtil;
import com.photograph_u.util.ResponseUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/photographer/*")
public class PhotographerAction extends BaseAction {
    //摄影师查看个人信息
    public void lookInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<Map<String, Object>> myResponse = new MyResponse<>();
        PhotographerService service = new PhotographerService();
        int userId = (int) request.getSession().getAttribute("photographer_user_id");
        Map<String, Object> photographerMap = service.lookInfo(userId);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(photographerMap);
        ResponseUtil.sendResponse(response, myResponse);
    }

    //修改信息
    public void updateInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        PhotographerService service = new PhotographerService();
        int userId = (int) request.getSession().getAttribute("photographer_user_id");
        String serverContent = request.getParameter("server_content");
        String introduce = request.getParameter("introduce");
        double price = Double.parseDouble(request.getParameter("price"));
        if (service.updateInfo(userId, price, serverContent, introduce)) {
            myResponse.setCode(0);
            myResponse.setMessage("修改成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("修改失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //摄影师上传个人作品
    public void addPhoto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        PhotographerService service = new PhotographerService();
        int userId = (int) request.getSession().getAttribute("photographer_user_id");
        FileUploadUtil.upload(request, FileUriConsts.PHOTOGRAPHER_PHOTO_URI, 0, 5);
        Map parameterMap = (Map) request.getAttribute("parameterMap");
        List fileList = (List) parameterMap.get("files");
        if (service.addPhoto(userId, fileList)) {
            myResponse.setCode(0);
            myResponse.setMessage("上传成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("上传失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //删除个人作品
    public void deletePhoto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        PhotographerService service = new PhotographerService();
        int userId = (int) request.getSession().getAttribute("photographer_user_id");
        int photoId = Integer.parseInt(request.getParameter("photo_id"));
        if (service.deletePhoto(userId, photoId)) {
            myResponse.setCode(0);
            myResponse.setMessage("删除成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("删除失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //列出所有未处理订单
    public void listUnTreatedOrders(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PhotographerService service = new PhotographerService();
        MyResponse<List<Map<String, Object>>> myResponse = new MyResponse<>();
        int userId = (int) request.getSession().getAttribute("photographer_user_id");
        List<Map<String, Object>> orderMapList = service.listUnTreatedOrders(userId);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(orderMapList);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //分页列出所有未处理订单
    public void listUnTreatedOrdersWithPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PhotographerService service = new PhotographerService();
        MyResponse<PageBean<List<Map<String, Object>>>> myResponse = new MyResponse<>();
        int userId = (int) request.getSession().getAttribute("photographer_user_id");
        int pageSize = Integer.parseInt(request.getParameter("page_size"));
        int currentPage = Integer.parseInt(request.getParameter("current_page"));
        PageBean<List<Map<String, Object>>> pageBean = service.listUnTreatedOrdersWithPage(userId, pageSize, currentPage);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(pageBean);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //列出所有已处理订单
    public void listTreatedOrders(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<List<Map<String, Object>>> myResponse = new MyResponse<>();
        PhotographerService service = new PhotographerService();
        int userId = (int) request.getSession().getAttribute("photographer_user_id");
        List<Map<String, Object>> orderMapList = service.listTreatedOrders(userId);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(orderMapList);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //分页列出所有已处理订单
    public void listTreatedOrdersWithPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<PageBean<List<Map<String, Object>>>> myResponse = new MyResponse<>();
        PhotographerService service = new PhotographerService();
        int userId = (int) request.getSession().getAttribute("photographer_user_id");
        int pageSize = Integer.parseInt(request.getParameter("page_size"));
        int currentPage = Integer.parseInt(request.getParameter("current_page"));
        PageBean<List<Map<String, Object>>> pageBean = service.listTreatedOrdersWithPage(userId, pageSize, currentPage);
        myResponse.setCode(0);
        myResponse.setMessage("查询成功");
        myResponse.setData(pageBean);
        ResponseUtil.sendResponseWithDateTime(response, myResponse);
    }

    //接单
    public void receiveOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<String> myResponse = new MyResponse<>();
        PhotographerService service = new PhotographerService();
        int userId = (int) request.getSession().getAttribute("photographer_user_id");
        int orderInfoId = Integer.parseInt(request.getParameter("orderinfo_id"));
        if (service.receiveOrder(userId, orderInfoId)) {
            myResponse.setCode(0);
            myResponse.setMessage("接单成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("接单失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

    //拒单
    public void refuseOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MyResponse<PageBean<List<Map<String, Object>>>> myResponse = new MyResponse<>();
        PhotographerService service = new PhotographerService();
        int userId = (int) request.getSession().getAttribute("photographer_user_id");
        int orderInfoId = Integer.parseInt(request.getParameter("orderinfo_id"));
        if (service.refuseOrder(userId, orderInfoId)) {
            myResponse.setCode(0);
            myResponse.setMessage("拒单成功");
        } else {
            myResponse.setCode(1);
            myResponse.setMessage("拒单失败");
        }
        ResponseUtil.sendResponse(response, myResponse);
    }

}
