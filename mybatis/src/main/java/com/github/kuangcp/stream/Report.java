package com.github.kuangcp.stream;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * @author https://github.com/kuangcp on 2021-07-12 23:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("pq_inquiry_parts_report")
public class Report {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 询价编码
     */
    private String inquiryCode;
    /**
     * 询价次数
     */
    private Integer inquiryCount;
    /**
     * 有货报出数
     */
    private Integer haveGoodsCount;
    /**
     * 采购次数
     */
    private Integer purchaseCount;
    /**
     * 统计日期
     */
    private Date statisticsTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    public static final String ID = "id";
    public static final String USER_ID = "user_id";
    public static final String INQUIRY_CODE = "inquiry_code";
    public static final String INQUIRY_COUNT = "inquiry_count";
    public static final String HAVE_GOODS_COUNT = "have_goods_count";
    public static final String PURCHASE_COUNT = "purchase_count";
    public static final String STATISTICS_TIME = "statistics_time";
    public static final String CREATE_TIME = "create_time";
    public static final String UPDATE_TIME = "update_time";
}
