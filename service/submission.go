package service

import (
	"context"
	"github.com/gin-gonic/gin"
	"github.com/goccy/go-json"
	"github.com/segmentio/kafka-go"
	"go-oj/constant"
	"go-oj/entity/database"
	"go-oj/entity/request"
	"go-oj/entity/response"
	"go-oj/global"
	"go-oj/util"
	"gorm.io/gorm"
	"strconv"
)

type SubmissionService struct {
}

var (
	p = 1
)

func (this *SubmissionService) Check(id int64, c *gin.Context) {
	judgeInfoStr := global.Redis.Get(constant.JUDGE_INFO_PREFIX + strconv.FormatInt(id, 10)).Val()
	if len(judgeInfoStr) == 0 {
		response.OkWithData(database.DEFAULT_JUDGE_INFO, c)
		return
	}

	var judgeInfo database.JudgeInfo
	_ = json.Unmarshal([]byte(judgeInfoStr), &judgeInfo)
	response.OkWithData(judgeInfo, c)
}
func (this *SubmissionService) DoSubmission(submissionAdd *request.SubmissionDoRequest, c *gin.Context) {
	var question database.Question
	id, _ := strconv.ParseInt(submissionAdd.QuestionId, 10, 64)
	global.DB.Find(&question, "id = ?", id)
	if question.Id == 0 {
		response.FailWithMessage("题目不存在", c)
		return
	}

	data, _ := c.Get(constant.USER_CACHE)
	user := data.(*response.UserVo)

	submission := &database.AnswerSubmission{
		Id:            int64(util.Snowflake()),
		UserId:        user.Id,
		QuestionId:    question.Id,
		SubmittedCode: submissionAdd.SubmittedCode,
		Language:      submissionAdd.Language,
		Status:        0,
	}
	global.DB.Create(submission)

	_ = global.Kafka.WriteMessages(context.Background(), kafka.Message{
		Partition: p % 2,
		Value:     []byte(strconv.FormatInt(submission.Id, 10)),
	})
	p++
	global.Log.Info("write a message")
	response.OkWithData(strconv.FormatInt(submission.Id, 10), c)
}
func (this *SubmissionService) Page(query *request.SubmissionQueryRequest, c *gin.Context) {
	var submissionList []database.AnswerSubmission

	x := global.DB.Offset(query.PageSize * (query.Current - 1)).Limit(query.PageSize)
	this.buildQueryConds(query, x)
	x.Find(&submissionList)

	svList := make([]response.SubmissionVo, len(submissionList))
	for i, v := range submissionList {
		var judgeInfo database.JudgeInfo
		_ = json.Unmarshal([]byte(v.JudgeInfo), &judgeInfo)
		svList[i] = response.SubmissionVo{
			Id:            strconv.FormatInt(v.Id, 10),
			UserId:        v.UserId,
			QuestionId:    v.QuestionId,
			SubmittedCode: v.SubmittedCode,
			JudgeInfo:     judgeInfo,
			Language:      v.Language,
			Status:        v.Status,
			CreateTime:    v.CreateTime,
			UpdateTime:    v.UpdateTime,
		}
	}

	var total int64
	x.Table("t_answer_submission").Count(&total)
	page := response.Page{
		CountID:          strconv.Itoa(query.Current),
		Current:          int64(query.Current),
		MaxLimit:         20,
		OptimizeCountSql: false,
		Orders:           nil,
		Pages:            total / int64(query.PageSize),
		Records:          svList,
		SearchCount:      false,
		Size:             int64(query.PageSize),
		Total:            total,
	}

	response.OkWithData(page, c)
}
func (this *SubmissionService) buildQueryConds(query *request.SubmissionQueryRequest, x *gorm.DB) {
	if query.Id != 0 {
		x = x.Where("id = ?", query.Id)
		return
	}

	if query.UserId > 0 {
		x = x.Where("user_id = ?", query.UserId)
	}

	if query.QuestionId > 0 {
		x = x.Where("question_id = ?", query.QuestionId)
	}

	if len(query.Language) > 0 {
		x = x.Where("language = ?", query.Language)
	}

	/*if query.Status > -1 {
		x = x.Where("status = ?", query.Status)
	}*/

	if len(query.SortField) > 0 {
		x = x.Order(query.SortField + " " + string(query.SortOrder))
	}
}
