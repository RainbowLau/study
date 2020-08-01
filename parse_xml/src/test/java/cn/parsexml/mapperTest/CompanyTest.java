package cn.parsexml.mapperTest;

import cn.parsexml.entity.Company;
import cn.parsexml.dao.CompanyDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CompanyTest {


    @Resource
    private CompanyDao companyMapper;

    @Test
    public void insert() {
        Company company =  new Company();
        company.setName("test");
        company.setId(UUID.randomUUID().toString());
        company.setAddress("test");
        companyMapper.saveCompany(company);
    }
}
