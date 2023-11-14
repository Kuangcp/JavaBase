package com.github.kuangcp.reflects;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author @author <a href="https://github.com/kuangcp">Github</a>
 * 2023-11-14 20:23
 */
@Data
public class BeanA {

    private Long applyId;
    private Long userId;
    private String baseTableName;
    private String tableAlias;
    private String projectName;
    private String newProjectName;
    private String templateName;
    private Integer requireAttr;
    private String readableUsers;
    private Integer requireTimeType;
    private Date dataExpire;
    private String templateType;

    private String applicantName;
    private String applicantDepartment;
    private String createTime;
    private String applyType;
    private Integer currentAuditProcessStatus;

    private Integer currentIndex;

    private String currentHandlers;

    private String currentHandlerNames;
    private String lastHandlerName;
    private String applyComment;
    private String docName;

    private Integer currentAuditNodeStatus;
    private Integer handleStatus;
    private String handleReason;
    private List<Integer> docAuthorization;
    private Integer configType;
    private Integer type;
    private String sql;
    private Integer workflowId;
    private Boolean enableScheduledTask;
    private String cron;
    private Long assetId;
    private String assetName;
    private String syncTaskName;
    private String serviceType;
    private String state;
    private String applyReason;

}
