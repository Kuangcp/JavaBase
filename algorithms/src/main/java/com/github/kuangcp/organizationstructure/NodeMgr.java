package com.github.kuangcp.organizationstructure;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 18-9-16-下午11:44
 */
@Slf4j
public class NodeMgr implements NodeAction {

  private static final int SUCCESS = 0;
  private static final int FAILED = 1;
  private static final String ERROR = "";

  /**
   * 存放任意多棵树
   */
  private Map<String, Node> nodes = new ConcurrentHashMap<>();

  // TODO 列出整个树
  public String tree(String parentId) {
    Node node = getNode(parentId);

    if (!Objects.isNull(node.getParent())) {
      return ERROR;
    }
    return "";
  }

  // TODO 列出子节点
  public String treeByNode(String parentId) {
    return "";
  }

  private Node getNode(String nodeId) {
    return nodes.get(nodeId);
  }

  @Override
  public int move(Node target, Node parent) {
    if (Objects.isNull(parent)) {
      return FAILED;
    }
    String originParent = target.getParent();
    target.setParent(parent.getId());

    rebuildByParentId(originParent);
    rebuildByParentId(parent.getId());

    return SUCCESS;
  }

  /**
   * add to the end of child by default
   *
   * @param target just need id and name field
   */
  @Override
  public int add(Node target, Node parent) {
    // add root node
    if (Objects.isNull(parent)) {
      target.setParent(null);
      target.setSort(1);
      target.setLeaf(true);
    } else {
      target.setParent(parent.getId());
      target.setSort(getChildCount(parent.getId() + 1));
      target.setLeaf(true);
    }

    nodes.put(target.getId(), target);

    return SUCCESS;
  }

  @Override
  public int delete(Node target) {
    if (Objects.isNull(target)) {
      return FAILED;
    }

    removeChild(target.getId());
    nodes.remove(target.getId());

    rebuildByParentId(target.getParent());
    return SUCCESS;
  }

  private int getChildCount(String parentId) {
    return getChildSet(parentId).size();
  }

  private Map<String, Node> getChildSet(String parentId) {
    if (Objects.isNull(parentId)) {
      return new HashMap<>();
    }
    return nodes.values().stream()
        .filter(node -> node.getParent().equals(parentId))
        .collect(Collectors.toMap(Node::getId, node -> node));
  }

  private void removeChild(String parentId) {
    Map<String, Node> childSet = getChildSet(parentId);
    if (childSet.isEmpty()) {
      return;
    }

    childSet.values().forEach(node -> nodes.remove(node.getId()));
  }

  /**
   * 重建所有
   */
  private void rebuildAll() {

  }

  /**
   * 只重建兄弟节点
   */
  private void rebuildByParentId(String parentId) {
    Map<String, Node> childSet = getChildSet(parentId);
    if (childSet.isEmpty()) {
      return;
    }


  }
}
