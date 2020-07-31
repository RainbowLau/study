package cn.parsexml.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Company implements Serializable {
    private Integer id;
    private String name;
    private String address;
}
