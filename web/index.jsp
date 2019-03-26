<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户接口测试</title>
</head>
<body>
用户接口测试|<a href="admin.html">管理员接口测试</a>|<a href="photographer.html">摄影师接口测试</a>
<form action="user/requestMessageCode" method="post">
    <input type="text" name="phone">
    <input type="submit" value="获取验证码">
</form>
<hr/>
<form action="user/register" method="post">
    电话<input type="text" name="phone"> <br/>
    密码<input type="password" name="password"> <br/>
    验证码 <input type="text" name="message_code"><br/>
    <input type="submit" value="提交">
</form>
<hr/>
<form action="user/login" method="post">
    手机 <input type="text" name="phone"> <br/>
    密码<input type="text" name="password"> <br/>
    <input type="submit" value="登录">
</form>
<hr/>
<form action="user/lookInfo" method="post">
    <input type="submit" value="查看个人信息">
</form>
<hr/>
修改密码
<form action="user/updatePassword" method="post">
    原密码 <input type="text" name="password"> <br/>
    新密码<input type="text" name="new_password"> <br/>
    <input type="submit" value="修改">
</form>
<hr/>
密码重置
<form action="user/resetPassword" method="post">
    手机<input type="text" name="phone"> <br/>
    新密码<input type="text" name="new_password"> <br/>
    验证码 <input type="text" name="message_code"><br/>
    <input type="submit" value="重置">
</form>
<hr/>
修改个人信息
<form action="user/updateInfo" method="post">
    昵称 <input type="text" name="nickname"> <br/>
    性别<input type="text" name="sex"> <br/>
    生日 <input type="text" name="birthday" value="2018-06-20"> <br/>
    学校 <input type="text" name="school"> <br/>
    <input type="submit" value="修改">
</form>
<hr/>
上传头像
<form action="user/setHeadImage" enctype="multipart/form-data" method="post">
    <input type="file" name="file">
    <input type="submit" value="上传">
</form>
<hr/>
查询所有风格
<form action="user/listAllStyles">
    <input type="submit" value="查询">
</form>
<hr/>
发布帖子
<form action="user/releaseNote" enctype="multipart/form-data" method="post">
    风格ID <input type="text" name="style_id">
    帖子内容<input type="text" name="content">
    <input type="file" name="file" multiple="multiple">
    <input type="submit" value="发布">
</form>
<hr/>
列出所有帖子
<form action="user/listAllNotes" method="post">
    风格ID <input type="text" name="style_id">
    <input type="submit" value="查询">
</form>
<hr/>
分页列出所有帖子
<form action="user/listAllNotesWithPage" method="post">
    风格ID <input type="text" name="style_id">
    页面大小 <input type="text" name="page_size">
    当前页面<input type="text" name="current_page">
    <input type="submit" value="查询">
</form>
评论帖子
<form action="user/commentNote" method="post">
    帖子ID <input type="text" name="note_id">
    评论内容<input type="text" name="content">
    父评论Id<input type="text" name="father_id">
    <input type="submit" value="评论">
</form>
<hr/>
点赞帖子
<form action="user/admireNote" method="post">
    帖子ID <input type="text" name="note_id">
    <input type="submit" value="赞一个">
</form>
<hr/>
点赞帖子
<form action="user/deleteAdmire" method="post">
    帖子ID <input type="text" name="note_id">
    <input type="submit" value="取消点赞">
</form>

<hr/>
删除评论
<form action="user/deleteMyComment" method="post">
    我评论的帖子的ID <input type="text" name="comment_id">
    <input type="submit" value="删除1">
</form>
<form action="user/deleteOthersComment" method="post">
    评论我的帖子的ID <input type="text" name="comment_id">
    <input type="submit" value="删除2">
</form>
<hr/>
列出我发的帖子
<form action="user/listMyNotes" method="post">
    <input type="submit" value="列出我发的帖子">
</form>
<hr/>
删除我的帖子
<form action="user/deleteMyNote" method="post">
    的帖子的ID <input type="text" name="note_id">
    <input type="submit" value="删除">
</form>
<hr/>
根据ID查看帖子详情
<form action="user/findNoteById" method="post">
    帖子ID <input type="text" name="note_id">
    <input type="submit" value="查询">
</form>
<hr/>
查看他人信息
<form action="user/lookOthersInfo" method="post">
    用户ID <input type="text" name="user_id">
    <input type="submit" value="查询">
</form>
<hr/>
查询自己是否摄影师
<form action="user/queryMyIdentity" method="post">
    <input type="submit" value="检查身份">
</form>
<hr/>
查询他人是否摄影师
<form action="user/queryOthersIdentity" method="post">
    用户ID<input type="text" name="user_id">
    <input type="submit" value="检查身份">
</form>
<hr/>
申请成为摄影师
<form action="user/requestToBePhotographer" enctype="multipart/form-data" method="post">
    学号ID<input type="text" name="card_no">
    学生证正面照片<input type="file" name="file">
    <input type="submit" value="确认">
</form>
<hr/>
查询申请记录
<form action="user/listVerifyRecord" enctype="multipart/form-data" method="post">
    <input type="submit" value="查询申请记录">
</form>
<hr/>
列出学校所有摄影师
<form action="user/listPhotographersBySchool" method="post">
    学校<input type="text" name="school" value="西华大学">
    <input type="submit" value="列出">
</form>
<hr/>
分页列出学校所有摄影师
<form action="user/listPhotographersBySchoolWithPage" method="post">
    学校<input type="text" name="school" value="西华大学">
    页面大小<input type="text" name="page_size">
    当前页<input type="text" name="current_page">
    <input type="submit" value="列出">
</form>
<hr/>
查看摄影师信息
<form action="user/findPhotographerById" method="post">
    摄影师编号<input type="text" name="photographer_id">
    <input type="submit" value="列出">
</form>
<hr/>
预约摄影师
<form action="user/orderPhotographer" method="post">
    摄影师编号<input type="text" name="photographer_id">
    地址<input type="text" name="address">
    备注<input type="text" name="other">
    <input type="submit" value="预约">
</form>
<hr/>
取消预约
<form action="user/cancelOrder" method="post">
    预约订单编号<input type="text" name="orderinfo_id">
    <input type="submit" value="取消">
</form>
<hr/>
列出我的预约订单
<form action="user/listOrders" method="post">
    <input type="submit" value="列出">
</form>
<hr/>
分页列出我的预约订单
<form action="user/listOrdersWithPage" method="post">
    页面大小 <input type="text" name="page_size">
    当前页面<input type="text" name="current_page">
    <input type="submit" value="列出">
</form>
<hr/>
关注摄影师
<form action="user/followPhotographer" method="post">
    摄影师编号<input type="text" name="photographer_id">
    <input type="submit" value="关注">
</form>
<hr/>
取消关注
<form action="user/cancelFollow" method="post">
    摄影师编号<input type="text" name="photographer_id">
    <input type="submit" value="取消">
</form>
<hr/>
查询我关注的摄影师
<form action="user/listFollows" method="post">
    <input type="submit" value="查询">
</form>
<hr/>
评价摄影师
<form action="user/reviewPhotographer" method="post">
    摄影师编号<input type="text" name="photographer_id">
    内容<input type="text" name="content">
    星值变化<input type="text" name="change_star_value">
    <input type="submit" value="评价">
</form>
</body>
</html>
