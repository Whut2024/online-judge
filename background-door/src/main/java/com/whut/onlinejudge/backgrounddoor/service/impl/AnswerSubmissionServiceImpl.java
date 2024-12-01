package com.whut.onlinejudge.backgrounddoor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.onlinejudge.backgrounddoor.mapper.AnswerSubmissionMapper;
import com.whut.onlinejudge.backgrounddoor.utils.SqlUtils;
import com.whut.onlinejudge.backgrounddoor.utils.UserHolder;
import com.whut.onlinejudge.common.constant.CommonConstant;
import com.whut.onlinejudge.common.model.dto.answersubmission.AnswerSubmissionAddRequest;
import com.whut.onlinejudge.common.model.dto.answersubmission.AnswerSubmissionQueryRequest;
import com.whut.onlinejudge.common.model.entity.AnswerSubmission;
import com.whut.onlinejudge.common.model.entity.User;
import com.whut.onlinejudge.common.model.enums.UserRoleEnum;
import com.whut.onlinejudge.common.model.vo.AnswerSubmissionVo;
import com.whut.onlinejudge.common.service.AnswerSubmissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AnswerSubmissionServiceImpl extends ServiceImpl<AnswerSubmissionMapper, AnswerSubmission>
        implements AnswerSubmissionService {

    @Override
    public Long doQuestionSubmit(AnswerSubmissionAddRequest answerSubmissionAddRequest) {
        return 0L;
    }

    @Override
    public Page<AnswerSubmissionVo> listQuestionSubmitByPage(AnswerSubmissionQueryRequest answerSubmissionQueryRequest) {
        final Page<AnswerSubmission> page = this.page(new Page<>(answerSubmissionQueryRequest.getCurrent(),
                answerSubmissionQueryRequest.getPageSize()), getAnswerSubmissionWrapper(answerSubmissionQueryRequest));

        final List<AnswerSubmissionVo> answerSubmissionVoList = new ArrayList<>((int) page.getSize());
        for (AnswerSubmission answerSubmission : page.getRecords()) {
            answerSubmissionVoList.add(AnswerSubmissionVo.getAnswerSubmissionVo(answerSubmission));
        }
        final Page<AnswerSubmissionVo> answerSubmissionVoPage = new Page<>(answerSubmissionQueryRequest.getCurrent(),
                answerSubmissionQueryRequest.getPageSize());
        answerSubmissionVoPage.setRecords(answerSubmissionVoList);

        return answerSubmissionVoPage;
    }

    private QueryWrapper<AnswerSubmission> getAnswerSubmissionWrapper(AnswerSubmissionQueryRequest answerSubmissionQueryRequest) {
        final Long userId = answerSubmissionQueryRequest.getUserId();
        final Long questionId = answerSubmissionQueryRequest.getQuestionId();
        final String language = answerSubmissionQueryRequest.getLanguage();
        final Integer status = answerSubmissionQueryRequest.getStatus();
        final String sortField = answerSubmissionQueryRequest.getSortField();
        final String sortOrder = answerSubmissionQueryRequest.getSortOrder();

        final QueryWrapper<AnswerSubmission> wrapper = new QueryWrapper<>();

        final User user = UserHolder.get();
        if (!UserRoleEnum.isAdmin(user))
            wrapper.eq("user_id", user.getId());
        else
            wrapper.eq(userId != null, "user_id", userId);

        wrapper.eq(questionId != null, "question_id", questionId);
        wrapper.eq(language != null, "language", language);
        wrapper.eq(status != null, "status", status);

        wrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);

        return wrapper;
    }
}




