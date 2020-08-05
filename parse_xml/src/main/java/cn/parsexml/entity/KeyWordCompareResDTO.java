package cn.parsexml.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class KeyWordCompareResDTO {
    private String name;

    private List<String> oldWords;

    private List<String> newWords;
}
