package com.github.kuangcp.organizationstructure;

import lombok.Data;

/**
 * created by https://gitee.com/gin9
 * 存储一个组织的树结构, 整体有序的, 任意删除, 移动, 新增
 * @author kuangcp on 18-9-16-下午11:36
 */
@Data
public class Node {

  private String id;
  private String name;
  private String parent;
  /**
   * 这个字段只是同级, (可以不用叶子节点的属性, 当sort为奇数则是没有叶子, 偶数则是有)
   */
  private int sort;
  private boolean isLeaf;
}
