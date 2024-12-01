package com.whut.onlinejudge.common.model.vo;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.common.model.entity.Question;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class QuestionVo implements Serializable {

    /**
     * 题目 ID
     */
    private Long id;

    /**
     * 创建题目的用户 ID
     */
    private Long userId;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目标签
     */
    private List<String> tags;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 题目运行资源限制
     */
    private JudgeConfig judgeConfig;

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

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 题目创建人
     */
    private UserVo userVo;

    /**
     * 程序启动引导代码
     */
    private String coreCode;

    private static final long serialVersionUID = 1L;

    public static QuestionVo getQuestionVo(Question question) {
        final QuestionVo questionVo = new QuestionVo();
        questionVo.setId(question.getId());
        questionVo.setUserId(question.getUserId());
        questionVo.setSubmissionNumber(question.getSubmissionNumber());
        questionVo.setAcceptanceNumber(question.getAcceptanceNumber());
        questionVo.setTags(JSONUtil.toBean(question.getTags(), new TypeReference<List<String>>() {}, false));
        questionVo.setContent(question.getContent());
        questionVo.setTitle(question.getTitle());
        questionVo.setCoreCode(question.getCoreCode());
        questionVo.setCreateTime(question.getCreateTime());
        questionVo.setUpdateTime(question.getUpdateTime());
        questionVo.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));
        return questionVo;
    }
}