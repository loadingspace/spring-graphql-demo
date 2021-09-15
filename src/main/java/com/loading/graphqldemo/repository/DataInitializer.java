package com.loading.graphqldemo.repository;

import com.loading.graphqldemo.entity.CompanyEntity;
import com.loading.graphqldemo.entity.PersonEntity;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private PersonRepository personRepository;

  @Override
  public void run(ApplicationArguments args) {

    CompanyEntity companyEntity1 = new CompanyEntity("1", "公司1", "2|3");
    CompanyEntity companyEntity2 = new CompanyEntity("2", "公司2", "1");
    CompanyEntity companyEntity3 = new CompanyEntity("3", "公司3", "1");
    List<CompanyEntity> companyEntities = new ArrayList<>();
    companyEntities.add(companyEntity1);
    companyEntities.add(companyEntity2);
    companyEntities.add(companyEntity3);
    companyRepository.saveAll(companyEntities);

    PersonEntity personEntity1 = new PersonEntity("1", "人员1", "1");
    PersonEntity personEntity2 = new PersonEntity("2", "人员2", "1");
    PersonEntity personEntity3 = new PersonEntity("3", "人员3", "1");
    PersonEntity personEntity4 = new PersonEntity("4", "人员4", "2");
    PersonEntity personEntity5 = new PersonEntity("5", "人员5", "3");
    List<PersonEntity> personEntities = new ArrayList<>();
    personEntities.add(personEntity1);
    personEntities.add(personEntity2);
    personEntities.add(personEntity3);
    personEntities.add(personEntity4);
    personEntities.add(personEntity5);
    personRepository.saveAll(personEntities);

  }

}
