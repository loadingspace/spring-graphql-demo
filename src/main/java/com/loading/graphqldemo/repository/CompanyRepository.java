package com.loading.graphqldemo.repository;

import com.loading.graphqldemo.entity.CompanyEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * desc:
 *
 * @author Lo_ading
 * @version 1.0.0
 * @date 2021/9/14
 */
@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {

  /**
   * 批量查询企业信息
   *
   * @param companyIds 企业id
   * @return List<CompanyEntity>
   */
  List<CompanyEntity> findByIdIn(List<String> companyIds);

}
