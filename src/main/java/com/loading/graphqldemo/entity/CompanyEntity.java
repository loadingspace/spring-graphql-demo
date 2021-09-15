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
public class CompanyEntity {

  @Id
  private String id;

  private String name;

  private String partnerCompanyIds;

  public CompanyEntity(){

  }

  public CompanyEntity(String id, String name, String partnerCompanyIds){
    this.id = id;
    this.name = name;
    this.partnerCompanyIds = partnerCompanyIds;
  }



}
