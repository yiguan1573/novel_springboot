package com.yiguan.novel.bean;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@TableName("commentAction")
public class CommentAction {
    private Long commentId;
    private Long userId;
    private Long novelId;
    private int commentPositive;
    private int commentNegative;
}
