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
     * 引导代码
     */
    private String coreCode;

    /**
     * 基础框架代码
     */
    private String baseCode;

    private static final long serialVersionUID = 1L;

    public static QuestionVo getQuestionVo(Question q) {
        final QuestionVo qvo = new QuestionVo();
        qvo.setId(q.getId());
        qvo.setUserId(q.getUserId());
        qvo.setSubmissionNumber(q.getSubmissionNumber());
        qvo.setAcceptanceNumber(q.getAcceptanceNumber());
        qvo.setTags(JSONUtil.toBean(q.getTags(), new TypeReference<List<String>>() {}, false));
        qvo.setContent(q.getContent());
        qvo.setTitle(q.getTitle());
        qvo.setCoreCode(q.getCoreCode());
        qvo.setCreateTime(q.getCreateTime());
        qvo.setUpdateTime(q.getUpdateTime());
        qvo.setJudgeConfig(JSONUtil.toBean(q.getJudgeConfig(), JudgeConfig.class));
        qvo.setBaseCode(q.getBaseCode());

        return qvo;
    }
}