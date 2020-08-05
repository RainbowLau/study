package cn.parsexml.service.impl;

import cn.parsexml.entity.Constant;
import cn.parsexml.entity.KeyWordCompareDto;
import cn.parsexml.entity.Keyword;
import cn.parsexml.service.IKeywordService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service("keywordService")
public class KeywordService implements IKeywordService {

    private Logger logger = LoggerFactory.getLogger(KeywordService.class);
    @Override
    public List<Keyword> parseKeywordByPath(String path) {
        List<Keyword> keywordList = new ArrayList<>();
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File(path));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element rootElement = document.getRootElement();
        for (Iterator<Element> it = rootElement.elementIterator("ErrorCode"); it.hasNext();){
            Element element = it.next();
            if (!"0".equals(element.getText())){
                System.out.println("处理出错"+element.getText());
            }
        }
        for (Iterator<Element> it =rootElement.elementIterator("results");it.hasNext(); ){
            Element results = it.next();
            for (Iterator<Element> keyIt = results.elementIterator("fields");keyIt.hasNext();){
                Keyword keyword = new Keyword();
                Element fields = keyIt.next();
                keyword.setIndex(fields.attributeValue("index"));
                keyword.setKeyword(fields.attribute("field_name").getValue());
                List<String> words = new ArrayList<>();
                for (Iterator<Element> field_values = fields.elementIterator("field_value");field_values.hasNext();){
                    Element field_value = field_values.next();
                    String fieldValueWord = field_value.attribute("word").getValue();
                    words.add(fieldValueWord);
                }
                keyword.setKeywordInfo(words);
                keywordList.add(keyword);
            }
        }
        //keywordList.forEach(keyword -> System.out.println(keyword));
        return keywordList;
    }

    @Override
    public List<KeyWordCompareDto> parseKeywordByPickXml(String pick) {
        //集合保存生成xlsx文件里的数据
        List<KeyWordCompareDto> keyWordCompareDtoList = new ArrayList<>();
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(new File(pick));
            Element extractresult = document.getRootElement();
            // 判断errorCode 不为0的话返回 处理关键字逻辑结束
            for (Iterator<Element> it = extractresult.elementIterator(Constant.ERROR_CODE); it.hasNext(); ) {
                Element element = it.next();
                if (!"0".equals(element.getText())) {
                    logger.error("处理关键字出错，错误码为 {} ", element.getText());
                }
            }
            for (Iterator<Element> it = extractresult.elementIterator(Constant.RESULTS); it.hasNext(); ) {
                Element results = it.next();
                //取出关键字
                for (Iterator<Element> keyIt = results.elementIterator(Constant.FIELDS); keyIt.hasNext(); ) {
                    KeyWordCompareDto keyWordCompareDto = new KeyWordCompareDto();
                    //取出resust下关键字元素
                    Element fields = keyIt.next();
                    //关键字name
                    keyWordCompareDto.setName(fields.attribute("field_name").getValue());
                    //关键字信息集合field_value
                    List<String> words = new ArrayList<>();
                    //取出关键字的field_value
                    for (Iterator<Element> fieldIt = fields.elementIterator(Constant.FIELD_VALUE); fieldIt.hasNext(); ) {
                        //取出field_value下的元素
                        Element fieldValue = fieldIt.next();
                        String fieldValueWord = fieldValue.attribute("word").getValue();
                        words.add(fieldValueWord);
                    }
                    keyWordCompareDto.setWords(words);
                    keyWordCompareDtoList.add(keyWordCompareDto);
                }
            }
            return keyWordCompareDtoList;
        } catch (Exception e) {
            logger.error("文件读取失败", e.getMessage());
        }
        return null;
    }


}
