package com.loading.graphqldemo.service;

import com.loading.graphqldemo.entity.CompanyEntity;
import com.loading.graphqldemo.repository.CompanyRepository;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * desc:
 *
 * @author Lo_ading
 * @version 1.0.0
 * @date 2021/9/14
 */
@Slf4j
@Service
public class CompanyService {

  @Autowired
  private CompanyRepository companyRepository;

  public List<CompanyEntity> all() {
    return companyRepository.findAll();
  }

  public CompanyEntity findById(String id) {
    return companyRepository.getById(id);
  }

  public List<CompanyEntity> findByIds(String... ids) {
    List<String> companyIds = Arrays.asList(ids);
    return findByIds(companyIds);
  }

  public List<CompanyEntity> findByIds(List<String>  ids) {
    return companyRepository.findByIdIn(ids);
  }

  public CompanyEntity save(CompanyEntity company) {
    company.setId(UUID.randomUUID().toString());
    return companyRepository.save(company);
  }

}
