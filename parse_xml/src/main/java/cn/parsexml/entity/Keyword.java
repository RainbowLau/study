package cn.parsexml.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Keyword {
    private String index;
    private String keyword;
    private List<String> keywordInfo;
}
