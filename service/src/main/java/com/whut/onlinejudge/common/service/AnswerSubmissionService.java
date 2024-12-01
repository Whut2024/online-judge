package com.whut.onlinejudge.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.onlinejudge.common.model.dto.answersubmission.AnswerSubmissionAddRequest;
import com.whut.onlinejudge.common.model.dto.answersubmission.AnswerSubmissionQueryRequest;
import com.whut.onlinejudge.common.model.entity.AnswerSubmission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.onlinejudge.common.model.vo.AnswerSubmissionVo;

/**
* @author laowang
* @description 针对表【t_answer_submission(答案提交表)】的数据库操作Service
* @createDate 2024-11-29 19:59:25
*/
public interface AnswerSubmissionService extends IService<AnswerSubmission> {

    /**
     * 提交问题答案
     * 此方法用于处理用户提交的答案信息，将答案保存到系统中
     *
     * @param answerSubmissionAddRequest 包含用户提交答案的相关信息，如用户ID、问题ID及提交的答案内容
     * @return 返回提交答案后生成的唯一标识ID，用于后续查询或修改答案
     */
    Long doQuestionSubmit(AnswerSubmissionAddRequest answerSubmissionAddRequest);

    /**
     * 分页查询问题提交记录
     * 此方法允许用户根据指定条件分页查询已提交的问题答案记录，便于用户浏览和管理自己的提交历史
     *
     * @param answerSubmissionQueryRequest 包含查询条件和分页信息，如用户ID、问题ID及页码、每页大小等
     * @return 返回一个分页对象，其中包含满足查询条件的问题提交记录列表及总记录数等信息
     */
    Page<AnswerSubmissionVo> listQuestionSubmitByPage(AnswerSubmissionQueryRequest answerSubmissionQueryRequest);

}
