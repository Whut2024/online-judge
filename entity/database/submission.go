package database

import (
	"go-oj/constant"
	"time"
)

type Submission struct {
	Id            int64                     `gorm:"column:id;primaryKey" json:"id"`
	UserId        int64                     `gorm:"column:user_id" json:"user_id"`
	QuestionId    int64                     `gorm:"column:question_id" json:"question_id"`
	SubmittedCode string                    `gorm:"column:submitted_code;type:text" json:"submitted_code"`
	JudgeInfo     string                    `gorm:"column:judge_info;type:text" json:"judge_info"`
	Language      constant.Language         `gorm:"column:language;size:8" json:"language"`
	Status        constant.SubmissionStatus `gorm:"column:status;default:2" json:"status"`
	CreateTime    time.Time                 `gorm:"column:create_time;default:CURRENT_TIMESTAMP" json:"create_time"`
	UpdateTime    time.Time                 `gorm:"column:update_time;default:CURRENT_TIMESTAMP" json:"update_time"`
	IsDelete      int8                      `gorm:"column:is_delete;default:0" json:"is_delete"`
}
