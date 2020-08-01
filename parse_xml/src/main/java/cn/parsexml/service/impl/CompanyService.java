package cn.parsexml.service.impl;

import cn.parsexml.entity.Company;
import cn.parsexml.dao.CompanyDao;
import cn.parsexml.service.ICompanyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CompanyService implements ICompanyService {

    @Resource
    private CompanyDao companyMapper;

    @Override
    public void saveCompany(Company company) {
        companyMapper.saveCompany(company);
    }
}
