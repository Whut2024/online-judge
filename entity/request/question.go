package request

import (
	"go-oj/constant"
	"go-oj/entity/database"
)

type QuestionAddRequest struct {
	Title       string                `gorm:"column:title;size:32" json:"title"`
	Tags        string                `gorm:"column:tags;size:128;default:'[]'" json:"tags"`
	Content     string                `gorm:"column:content;type:text" json:"content"`
	JudgeCase   *[]database.JudgeCase `gorm:"column:judge_case;type:text" json:"judge_case"`
	JudgeConfig *database.JudgeConfig `gorm:"column:judge_config;size:128" json:"judge_config"`
	CoreCode    string                `gorm:"column:core_code;type:text" json:"core_code"`
	BaseCode    string                `gorm:"column:base_code;type:text" json:"base_code"`
}

type QuestionDeleteRequest struct {
	Id int64 `gorm:"column:id;primaryKey" json:"id"`
}

type QuestionQueryRequest struct {
	constant.Sort
	Id      int64  `gorm:"column:id;primaryKey" json:"id"`
	UserId  int64  `gorm:"column:user_id" json:"user_id"`
	Title   string `gorm:"column:title;size:32" json:"title"`
	Tags    string `gorm:"column:tags;size:128;default:'[]'" json:"tags"`
	Content string `gorm:"column:content;type:text" json:"content"`
}

type QuestionUpdateRequest struct {
	Id          int64                 `gorm:"column:id;primaryKey" json:"id"`
	Title       string                `gorm:"column:title;size:32" json:"title"`
	Tags        string                `gorm:"column:tags;size:128;default:'[]'" json:"tags"`
	Content     string                `gorm:"column:content;type:text" json:"content"`
	JudgeCase   *[]database.JudgeCase `gorm:"column:judge_case;type:text" json:"judge_case"`
	JudgeConfig database.JudgeConfig  `gorm:"column:judge_config;size:128" json:"judge_config"`
	CoreCode    string                `gorm:"column:core_code;type:text" json:"core_code"`
	BaseCode    string                `gorm:"column:base_code;type:text" json:"base_code"`
}
