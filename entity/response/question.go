package response

import (
	"encoding/json"
	"go-oj/entity/database"
	"strconv"
	"time"
)

type QuestionVo struct {
	Id               string               `gorm:"column:id;primaryKey" json:"id"`
	UserId           string               `gorm:"column:user_id" json:"userId"`
	Title            string               `gorm:"column:title;size:32" json:"title"`
	Tags             []string             `gorm:"column:tags;size:128;default:'[]'" json:"tags"`
	Content          string               `gorm:"column:content;type:text" json:"content"`
	JudgeCase        []database.JudgeCase `gorm:"column:judge_case;type:text" json:"judgeCase"`
	JudgeConfig      database.JudgeConfig `gorm:"column:judge_config;size:128" json:"judgeConfig"`
	CoreCode         string               `gorm:"column:core_code;type:text" json:"coreCode"`
	BaseCode         string               `gorm:"column:base_code;type:text" json:"baseCode"`
	SubmissionNumber uint                 `gorm:"column:submission_number;default:0" json:"submissionNumber"`
	AcceptanceNumber uint                 `gorm:"column:acceptance_number;default:0" json:"acceptanceNumber"`
	CreateTime       time.Time            `gorm:"column:create_time;default:CURRENT_TIMESTAMP" json:"createTime"`
	UpdateTime       time.Time            `gorm:"column:update_time;default:CURRENT_TIMESTAMP" json:"updateTime"`
}

func ConvQuestionVo(question *database.Question) *QuestionVo {
	var tagList []string
	_ = json.Unmarshal([]byte(question.Tags), &tagList)
	var judgeCase []database.JudgeCase
	_ = json.Unmarshal([]byte(question.JudgeCase), &judgeCase)
	var judgeConfig database.JudgeConfig
	_ = json.Unmarshal([]byte(question.JudgeConfig), &judgeConfig)

	return &QuestionVo{
		Id:               strconv.FormatInt(question.Id, 10),
		UserId:           strconv.FormatInt(question.UserId, 10),
		Title:            question.Title,
		Tags:             tagList,
		Content:          question.Content,
		JudgeCase:        judgeCase,
		JudgeConfig:      judgeConfig,
		CoreCode:         question.CoreCode,
		BaseCode:         question.BaseCode,
		SubmissionNumber: question.SubmissionNumber,
		AcceptanceNumber: question.AcceptanceNumber,
		CreateTime:       question.CreateTime,
		UpdateTime:       question.UpdateTime,
	}
}
