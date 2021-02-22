package com.zzb.cache.caffeine.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserInfoEntity {
    private Integer id;
    private String name;
    private String sex;
    private Integer age;
}
