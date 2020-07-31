package cn.parsexml.controller;

import cn.parsexml.entity.Company;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TextCompanyDemo {
    public static void main(String[] args) throws DocumentException {
        // 存入对象
        List<Company> companies = new ArrayList<>();

        // 读取xml格式的string 字符串
        // DocumentHelper.parseText()

        SAXReader reader = new SAXReader();
        Document document = reader.read("D:\\demo01.xml");
        // 获取根节点
        Element rootElement = document.getRootElement();

        Company  company = null;


        for (Iterator<?> iterator = rootElement.elementIterator(); iterator.hasNext();){
            Element element = (Element) iterator.next();
            Attribute id = element.attribute("id");
            company = new Company();
            company.setId(Integer.valueOf(id.getValue()));

            for (Iterator elementIterator = element.elementIterator();elementIterator.hasNext();){
                Element element1 = (Element) elementIterator.next();
                company.setName(element1.getName());
                company.setAddress(element1.getTextTrim());
            }
            companies.add(company);
        }

        System.out.println(companies);

    }
}
