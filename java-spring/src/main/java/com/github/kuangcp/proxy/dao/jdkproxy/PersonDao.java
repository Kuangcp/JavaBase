package com.github.kuangcp.proxy.dao.jdkproxy;

import com.github.kuangcp.proxy.dao.base.Person;
import java.util.List;

/**
 * 目标接口
 *
 * @author Administrator
 */
public interface PersonDao {

  void savePerson();

  void updatePerson();

  void deletePerson();

  List<Person> getPerson();
}
