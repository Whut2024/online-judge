package request

import (
	"go-oj/constant"
)

type SubmissionDoRequest struct {
	QuestionId    int64  `gorm:"column:question_id" json:"question_id"`
	SubmittedCode string `gorm:"column:submitted_code;type:text" json:"submitted_code"`
	Language      string `gorm:"column:language;size:8" json:"language"`
}

type SubmissionQueryRequest struct {
	constant.Sort
	Id         int64                     `gorm:"column:id;primaryKey" json:"id"`
	UserId     int64                     `gorm:"column:user_id" json:"user_id"`
	QuestionId int64                     `gorm:"column:question_id" json:"question_id"`
	Language   constant.Language         `gorm:"column:language;size:8" json:"language" binding:"required max=10"`
	Status     constant.SubmissionStatus `gorm:"column:status;default:2" json:"status"`
}
