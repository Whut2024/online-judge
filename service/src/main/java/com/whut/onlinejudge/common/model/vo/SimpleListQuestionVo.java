package com.whut.onlinejudge.common.model.vo;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.whut.onlinejudge.common.model.entity.Question;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class SimpleListQuestionVo implements Serializable {

    /**
     * 题目 ID
     */
    private Long id;


    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目标签
     */
    private List<String> tags;


    /**
     * 答案提交数
     */
    private Integer submissionNumber;

    /**
     * 答案提交通过数
     */
    private Integer acceptanceNumber;

    /**
     * 创建时间
     */
    private Date createTime;


    private static final long serialVersionUID = 1L;

    public static SimpleListQuestionVo getSimpleListQuestionVo(Question q) {
        final SimpleListQuestionVo slqvo = new SimpleListQuestionVo();
        slqvo.setId(q.getId());
        slqvo.setSubmissionNumber(q.getSubmissionNumber());
        slqvo.setAcceptanceNumber(q.getAcceptanceNumber());
        slqvo.setTags(JSONUtil.toBean(q.getTags(), new TypeReference<List<String>>() {}, false));
        slqvo.setTitle(q.getTitle());
        slqvo.setCreateTime(q.getCreateTime());

        return slqvo;
    }
}