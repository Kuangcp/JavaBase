package com.github.kuangcp.mvc.annotation;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 * 如果一个类中有基本类型，并且基本类型是用spring的形式赋值的，这个时候，该类必须用xml来完成，不能用注解
 *
 * @author Administrator
 */
@Controller("personAction")
public class PersonAction {

  @Resource
  private PersonService personService;

  public void savePerson() {
    this.personService.savePerson();
  }
}
