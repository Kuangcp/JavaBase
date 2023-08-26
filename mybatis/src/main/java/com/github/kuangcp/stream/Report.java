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
@TableName("report")
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
    private String b;
    private Integer c;
    private Integer d;
    private Integer e;
    private Date statisticsTime;
}
