package cn.parsexml.service;

import cn.parsexml.entity.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CompanServiceTest {

    @Autowired
    private ICompanyService companyService;

    @Test
    public void name() {
        Company company =  new Company();
        company.setName("test");
        company.setId(UUID.randomUUID().toString().replace("-",""));
        company.setAddress("test");
        companyService.saveCompany(company);
    }
}
