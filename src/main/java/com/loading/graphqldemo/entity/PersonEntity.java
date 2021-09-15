package com.loading.graphqldemo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.Proxy;

/**
 * desc:
 *
 * @author Lo_ading
 * @version 1.0.0
 * @date 2021/9/14
 */
@Data
@Proxy(lazy = false)
@Entity
public class PersonEntity {

  @Id
  private String id;

  private String name;

  private String companyId;

  public PersonEntity(){

  }

  public PersonEntity(String id, String name, String companyId){
    this.id = id;
    this.name = name;
    this.companyId = companyId;
  }

}
