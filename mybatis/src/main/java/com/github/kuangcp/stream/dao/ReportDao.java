package com.github.kuangcp.stream.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.kuangcp.stream.Report;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author https://github.com/kuangcp on 2021-07-12 23:52
 */
@Repository
public interface ReportDao extends BaseMapper<Report> {

    @Select("SELECT user_id, inquiry_code, SUM(inquiry_count) AS inquiry_count" +
            ", SUM(have_goods_count) AS have_goods_count, SUM(purchase_count) AS purchase_count" +
            " FROM pq_inquiry_parts_report WHERE statistics_time BETWEEN #{start} AND #{end}" +
            " AND user_id IN (1,2,3,4,5,6)" +
            " GROUP BY inquiry_code")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = Integer.MIN_VALUE)
    @ResultType(Report.class)
    void selectAutoList(@Param("start") Date start,
                        @Param("end") Date end,
                        ResultHandler<Report> handler);

    @Select("SELECT user_id, inquiry_code, SUM(inquiry_count) AS inquiry_count" +
            ", SUM(have_goods_count) AS have_goods_count, SUM(purchase_count) AS purchase_count" +
            " FROM pq_inquiry_parts_report WHERE statistics_time BETWEEN #{start} AND #{end}" +
            " AND user_id IN (1,2,3,4,5,6)" +
            " GROUP BY inquiry_code LIMIT #{startIdx},#{size}")
    List<Report> queryByPage(@Param("start") Date start,
                             @Param("end") Date end,
                             @Param("startIdx") long startIdx,
                             @Param("size") long size);
}
