package com.yiguan.novel.bean;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@TableName("comment")
public class Comment {
    @TableId(value = "comment_id",type = IdType.AUTO)
    private Long commentId;
    private Long novelId;
    private Long userId;
    private String commentContent;
    private String commentTime;
    @TableField(value = "isHavingReplay")
    private int isHavingReplay;
    private Long commentPnum;
    private Long commentNnum;
    private String userName;
    private String userImg;
    private Long parentId;

    public Comment(Long commentId, Long novelId, Long userId, String commentContent, String commentTime, int isHavingReplay, Long commentPnum, Long commentNnum, String userName) {
        this.commentId = commentId;
        this.novelId = novelId;
        this.userId = userId;
        this.commentContent = commentContent;
        this.commentTime = commentTime;
        this.isHavingReplay = isHavingReplay;
        this.commentPnum = commentPnum;
        this.commentNnum = commentNnum;
        this.userName = userName;
    }
}
