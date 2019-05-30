package com.github.kuangcp.di.di.xml.set;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Person {

  private Long pid;//包装类型
  private String pname;//String类型
  private Student student;//引用类型

  private List lists;

  private Set sets;

  public Long getPid() {
    return pid;
  }

  public void setPid(Long pid) {
    this.pid = pid;
  }

  public String getPname() {
    return pname;
  }

  public void setPname(String pname) {
    this.pname = pname;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public List getLists() {
    return lists;
  }

  public void setLists(List lists) {
    this.lists = lists;
  }

  public Set getSets() {
    return sets;
  }

  public void setSets(Set sets) {
    this.sets = sets;
  }

  public Map getMap() {
    return map;
  }

  public void setMap(Map map) {
    this.map = map;
  }

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  private Map map;

  private Properties properties;
}
