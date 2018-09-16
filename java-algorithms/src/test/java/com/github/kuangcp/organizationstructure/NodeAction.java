package com.github.kuangcp.organizationstructure;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 18-9-16-下午11:44
 */
public interface NodeAction {

  int move(Node target, Node parent);

  int add(Node target, Node parent);

  int delete(Node target);

}
