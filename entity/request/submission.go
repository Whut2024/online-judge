package request

import (
	"go-oj/constant"
)

type SubmissionDoRequest struct {
	QuestionId    string            `gorm:"column:question_id" json:"questionId" binding:"required"`
	SubmittedCode string            `gorm:"column:submitted_code;type:text" json:"submittedCode" binding:"required"`
	Language      constant.Language `gorm:"column:language;size:8" json:"language" binding:"required"`
}

type SubmissionQueryRequest struct {
	constant.Sort
	Id         int64                     `gorm:"column:id;primaryKey" json:"id"`
	UserId     int64                     `gorm:"column:user_id" json:"userId"`
	QuestionId int64                     `gorm:"column:question_id" json:"questionId"`
	Language   constant.Language         `gorm:"column:language;size:8" json:"language" binding:"max=10"`
	Status     constant.SubmissionStatus `gorm:"column:status;default:-1" json:"status"`
}
