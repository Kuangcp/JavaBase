package com.github.kuangcp.hibernate.xml;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

public class PersonDaoImpl extends HibernateDaoSupport implements PersonDao {

  @Override
  public void savePerson(Person person) {
    // TODO Auto-generated method stub
    this.getHibernateTemplate().save(person);
  }

}
