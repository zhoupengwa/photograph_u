package com.photograph_u.util;

import com.photograph_u.exception.MyFileUploadException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUploadUtil {
    public static void upload(HttpServletRequest request, String savePath, int fieldCount, int fileCount) {
        Map<String, Object> parameterMap = new HashMap<>();
        List<String> fileList = new ArrayList<>();
        FileOutputStream out = null;
        InputStream in = null;
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new MyFileUploadException("表单属性设置不正确");
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(16 * 1024);//占用运行内存16K，多余的存内存
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(5 * 1024 * 1024);//总大小限制5M
        upload.setFileSizeMax(700 * 1024);//单张限制700K
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();//创建目录(允许多级)
        }
        List<FileItem> itemList;
        try {
            itemList = upload.parseRequest(request);//获取多重表单
        } catch (FileUploadException e) {
            throw new MyFileUploadException("文件上传异常", e);
        }
        //检查文件数量
        if (itemList.size() > (fieldCount + fileCount)) {
            throw new MyFileUploadException("文件数量过多");
        }
        //遍历表单
        for (FileItem item : itemList) {
            if (item.isFormField()) {//是表单属性
                try {
                    parameterMap.put(item.getFieldName(), item.getString("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new MyFileUploadException("多重表单编码异常", e);
                }
            } else {//是文件
                String fileName = item.getName();
                if (!"".equals(fileName)) {//选择了文件
                    //检查文件类型是否正确
                    String dotExtendName = fileName.substring(fileName.lastIndexOf("."));
                    boolean allow = false;
                    String[] allowType = {".jpg", ".png", ".bmp", ".gif"};
                    for (String type : allowType) {
                        if (type.equals(dotExtendName))
                            allow = true;
                    }
                    if (!allow) {
                        throw new MyFileUploadException("文件类型不支持");
                    }
                    //保存上传文件
                    String saveName = CommUtils.getUuidCode() + dotExtendName;
                    File saveFile = new File(savePath + saveName);
                    try {
                        out = new FileOutputStream(saveFile);
                        in = item.getInputStream();
                        int len;
                        while ((len = in.read()) != -1) {
                            out.write(len);
                        }
                        out.flush();
                        fileList.add(saveName);
                        parameterMap.put("files", fileList);
                    } catch (Exception e) {
                        throw new MyFileUploadException("文件上传异常", e.getCause());
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                            if (in != null) {
                                in.close();
                            }
                        } catch (IOException e) {
                            throw new MyFileUploadException("文件上传异常", e);
                        }
                    }

                }
            }
        }
        request.setAttribute("parameterMap", parameterMap);
    }
}
