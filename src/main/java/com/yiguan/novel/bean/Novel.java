package com.yiguan.novel.bean;


import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("novel")
public class Novel{
    @TableId(value = "novel_id",type = IdType.AUTO)
    private Long novelId;
    private String novelName;
    private String novelAuthor;
    private String novelType;
    private String novelNum;
    private String novelImg;
    private String novelIntroduction;
    private String novelNote;
    private Long userId;
    private Long novelThumbsup;
    private Long novelCollection;
    private Float novelScore;

    public Novel(String novelName, String novelAuthor, String novelType, String novelNum, String novelIntroduction, String novelNote, Long userId) {
        this.novelName = novelName;
        this.novelAuthor = novelAuthor;
        this.novelType = novelType;
        this.novelNum = novelNum;
        this.novelIntroduction = novelIntroduction;
        this.novelNote = novelNote;
        this.userId = userId;
    }
}
