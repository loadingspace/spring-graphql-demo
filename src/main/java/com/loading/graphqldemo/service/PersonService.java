package com.loading.graphqldemo.service;

import com.loading.graphqldemo.entity.PersonEntity;
import com.loading.graphqldemo.repository.PersonRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
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
public class PersonService {

  @Autowired
  private PersonRepository personRepository;

  public List<PersonEntity> all() {
    return personRepository.findAll();
  }

  public PersonEntity findById(String id) {
    return personRepository.getById(id);
  }

  public List<List<PersonEntity>> findByCompanyIds(List<String> companyIds) {
    Map<String, List<PersonEntity>> companyToPerson = personRepository.findByCompanyIdIn(companyIds)
        .stream().collect(Collectors.groupingBy(PersonEntity::getCompanyId));
    List<List<PersonEntity>> result = new ArrayList<>();
    for (String companyId : companyIds) {
      result.add(companyToPerson.get(companyId));
    }
    return result;
  }

  public PersonEntity save(PersonEntity person) {
    person.setId(UUID.randomUUID().toString());
    return personRepository.save(person);
  }

}
