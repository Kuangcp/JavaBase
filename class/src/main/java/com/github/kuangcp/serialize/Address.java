package com.github.kuangcp.serialize;

import java.io.Serializable;
import lombok.Data;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2020-08-13 15:19
 */
@Data
public class Address implements Serializable {

  private String country;

  private Street street;

}

@Data
class Street {

  private String street;
}