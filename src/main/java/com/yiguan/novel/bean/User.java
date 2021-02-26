package com.yiguan.novel.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Data
@TableName("user")
public class User {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @TableField(value = "username")
    private String userName;
    @TableField(value = "password")
    private String password;
    @TableField(value = "image")
    private String image;
    @TableField(value = "token")
    private String token;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
