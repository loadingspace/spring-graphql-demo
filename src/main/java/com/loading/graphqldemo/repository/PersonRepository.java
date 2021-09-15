package com.loading.graphqldemo.repository;

import com.loading.graphqldemo.entity.PersonEntity;
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
public interface PersonRepository extends JpaRepository<PersonEntity, String> {

  /**
   * 根据公司id查询人员
   *
   * @param companyId 公司id
   * @return List<PersonEntity>
   */
  List<PersonEntity> findByCompanyId(String companyId);

  /**
   * 根据公司id查询人员
   * @param companyIds 公司id
   * @return List<PersonEntity>
   */
  List<PersonEntity> findByCompanyIdIn(List<String> companyIds);

}
