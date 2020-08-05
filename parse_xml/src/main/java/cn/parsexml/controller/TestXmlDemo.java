package cn.parsexml.controller;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import java.io.File;
import java.io.InputStream;
import java.util.Iterator;

public class TestXmlDemo {
    public static void main(String[] args) throws DocumentException {
        SAXReader  reader = new SAXReader();


        // 创建输入流 进行数据文件操作
        // InputStream inputStream = TestXmlDemo.class.getClassLoader().getResourceAsStream("demo01.xml");


        Document document = reader.read(new File("D:\\zxr\\ocr_pic\\保险资管\\new_pick.xml"));

        // 获取根节点
        Element rootElement = document.getRootElement();

        // 跟元素的名字companys
        System.out.println("跟元素的名字"+rootElement.getName());
        Iterator iterator = rootElement.elementIterator();
        while (iterator.hasNext()){
            Element element = (Element) iterator.next();
            Attribute  idAttr = element.attribute("results");
            System.out.print(idAttr.getValue());
            Iterator elementIterator = element.elementIterator();
            while (elementIterator.hasNext()){
                Element element2 = (Element) elementIterator.next();
                String name = element2.getName();
                String textTrim = element2.getTextTrim();
                String namespacePrefix = element2.getNamespacePrefix();
                System.out.println(name+" "+textTrim);
                System.out.println("----------");

            }
            //System.out.println();
        }


    }
}
