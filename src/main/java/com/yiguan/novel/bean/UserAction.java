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
@TableName("userAction")
public class UserAction {
    Long userId;
    Long novelId;
    int userThumbsup;
    int userCollection;
    Float userScore;

    public UserAction(Long userId, Long novelId, int userThumbsup, int userCollection) {
        this.userId = userId;
        this.novelId = novelId;
        this.userThumbsup = userThumbsup;
        this.userCollection = userCollection;
    }
}
