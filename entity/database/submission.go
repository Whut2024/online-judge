package database

import (
	"go-oj/constant"
	"time"
)

type AnswerSubmission struct {
	Id            int64                     `gorm:"column:id;primaryKey" json:"id"`
	UserId        int64                     `gorm:"column:user_id" json:"userId"`
	QuestionId    int64                     `gorm:"column:question_id" json:"questionId"`
	SubmittedCode string                    `gorm:"column:submitted_code;type:text" json:"submittedCode"`
	JudgeInfo     string                    `gorm:"column:judge_info;type:text" json:"judge_info"`
	Language      constant.Language         `gorm:"column:language;size:8" json:"language"`
	Status        constant.SubmissionStatus `gorm:"column:status;default:0" json:"status"`
	CreateTime    time.Time                 `gorm:"column:create_time;default:CURRENT_TIMESTAMP" json:"createTime"`
	UpdateTime    time.Time                 `gorm:"column:update_time;default:CURRENT_TIMESTAMP" json:"updateTime"`
	IsDelete      int8                      `gorm:"column:is_delete;default:0" json:"isDelete"`
}

type JudgeInfo struct {
	Time      int    `json:"time"`
	Memory    int    `json:"memory"`
	Message   string `json:"message"`
	Exception string `json:"exception"`
}

var DEFAULT_JUDGE_INFO = &JudgeInfo{-1, -1, "", ""}
