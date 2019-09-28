package com.github.kuangcp.proxy.dao.jdkproxy;

import com.github.kuangcp.aop.common.Person;
import java.util.List;

/**
 * 代理对象需实现接口
 */
public interface PersonDao {

  void savePerson();

  void updatePerson();

  void deletePerson();

  List<Person> getPerson();
}
