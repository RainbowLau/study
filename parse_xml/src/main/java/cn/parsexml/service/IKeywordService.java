package cn.parsexml.service;

import cn.parsexml.entity.KeyWordCompareDto;
import cn.parsexml.entity.Keyword;

import java.util.List;

public interface IKeywordService {
    List <Keyword> parseKeywordByPath(String path);

    List<KeyWordCompareDto> parseKeywordByPickXml(String pick);

}
