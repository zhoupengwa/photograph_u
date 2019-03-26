package com.photograph_u.domain;

import com.photograph_u.dao.BaseDao;

import java.util.List;
import java.util.Map;

public class VerifyDao extends BaseDao {
    //添加审核信息
    public boolean addVerify(int userId, String cardNo, String cardImage, String submitTime) {
        String sql = "insert into verify(user_id,card_no,card_image,submit_time) values(?,?,?,?)";
        int count = update(sql, new Object[]{userId, cardNo, cardImage, submitTime});
        return count == 1;
    }

    //根据审核状态查询记录条数
    public int countVerifiesByState(int state) {
        String sql = "select count(*) as count from verify where state=? and is_deleted=0";
        Map<String, Object> resultMap = queryToMap(sql, new Object[]{state});
        Object countObject = resultMap.get("count");
        if (countObject == null) {
            return 0;
        }
        return Integer.parseInt(String.valueOf(countObject));
    }

    /**
     * 检查是用户是否[正在审核]或者[审核通过]（未审核、审核失败才可以提交审核）
     * 0-未审核、1-审核成功、2-审核失败
     */
    public boolean checkVerifyByUserId(int userId) {
        String sql = "select count(*) as count from verify where user_id=? and state<>2  and is_deleted=0";
        long count = (long) queryToMap(sql, new Object[]{userId}).get("count");
        return count == 1;
    }

    /**
     * 检查该提交记录是否已经被审核
     */
    public boolean checkVerifyById(int verifyId) {
        String sql = "select count(*) as count from verify where id=? and state<>0 and is_deleted=0";
        long count = (long) queryToMap(sql, new Object[]{verifyId}).get("count");
        return count == 1;
    }

    //查询申请记录
    public List<Map<String, Object>> queryVerifys(int userId) {
        String sql = "select card_no,card_image,submit_time,verify_time,state from verify where user_id=? and is_deleted=0";
        List<Map<String, Object>> resultMapList = queryToMapList(sql, new Object[]{userId});
        return resultMapList;
    }

    //根据id查询审核信息
    public Map<String, Object> queryVerifyById(int verifyId) {
        String sql = "select id,user_id,card_no,card_image from verify where id=? and is_deleted=0";
        Map<String, Object> resultMap = queryToMap(sql, new Object[]{verifyId});
        return resultMap;
    }

    //审核用户申请
    public boolean changeVerifyState(int verifyId, int state, String verifyTime) {
        String sql = "update verify set state=?,verify_time=? where id=? and state=0 and is_deleted=0";
        int count = update(sql, new Object[]{state, verifyTime, verifyId});
        return count == 1;
    }

    //列出所有未审核信息
    public List<Map<String, Object>> queryUnTreatedVerifies() {
        String sql = "select id,user_id,card_no,card_image,submit_time,verify_time,state from verify where state=0 and is_deleted=0";
        List<Map<String, Object>> verifyMapList = queryToMapList(sql, null);
        return verifyMapList;
    }

    //带偏移量的列出所有未审核信息
    public List<Map<String, Object>> queryUnTreatedVerifiesWithOffset(int size, int offset) {
        String sql = "select a.id,a.user_id,a.card_no,a.card_image,a.submit_time,a.verify_time,a.state from verify a,(select id from verify where state=0 and is_deleted=0 limit ? offset ?) b where a.id=b.id";
        List<Map<String, Object>> verifyMapList = queryToMapList(sql, new Object[]{size, offset});
        return verifyMapList;
    }

    //列出所有已审核信息[审核通过][审核未通过]
    public List<Map<String, Object>> queryTreatedVerifies() {
        String sql = "select id,user_id,card_no,card_image,submit_time,verify_time,state from verify where (state=1 or state =2) and is_deleted=0";
        List<Map<String, Object>> verifyMapList = queryToMapList(sql, null);
        return verifyMapList;
    }

    //带偏移量列出所有已审核信息[审核通过][审核未通过]
    public List<Map<String, Object>> queryTreatedVerifiesWithOffset(int size, int offset) {
        String sql = "select a.id,a.user_id,a.card_no,a.card_image,a.submit_time,a.verify_time,a.state from verify a,(select id from verify where state<>0 and is_deleted=0 limit ? offset ?) b where a.id=b.id";
        List<Map<String, Object>> verifyMapList = queryToMapList(sql, new Object[]{size, offset});
        return verifyMapList;
    }
}
