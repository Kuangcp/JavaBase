package com.github.kuangcp.hibernate.annotation;

import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository("personDao")
public class PersonDaoImpl implements PersonDao {

  @Resource(name = "hibernateTemplate")
  private HibernateTemplate hibernateTemplate;

  @Override
  public void savePerson(Person person) {
    this.hibernateTemplate.save(person);
  }
}
