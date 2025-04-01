package request

import (
	"go-oj/constant"
	"go-oj/entity/database"
)

type QuestionAddRequest struct {
	Title       string                `gorm:"column:title;size:32" json:"title"`
	Tags        *[]string             `gorm:"column:tags;size:128;default:'[]'" json:"tags"`
	Content     string                `gorm:"column:content;type:text" json:"content"`
	JudgeCase   *[]database.JudgeCase `gorm:"column:judge_case;type:text" json:"judgeCase"`
	JudgeConfig *database.JudgeConfig `gorm:"column:judge_config;size:128" json:"judgeConfig"`
	CoreCode    string                `gorm:"column:core_code;type:text" json:"coreCode"`
	BaseCode    string                `gorm:"column:base_code;type:text" json:"baseCode"`
}

type QuestionDeleteRequest struct {
	Id int64 `gorm:"column:id;primaryKey" json:"id"`
}

type QuestionQueryRequest struct {
	constant.Sort
	Id      int64     `gorm:"column:id;primaryKey" json:"id"`
	UserId  int64     `gorm:"column:user_id" json:"userId"`
	Title   string    `gorm:"column:title;size:32" json:"title"`
	Tags    *[]string `gorm:"column:tags;size:128;default:'[]'" json:"tags"`
	Content string    `gorm:"column:content;type:text" json:"content"`
}

type QuestionUpdateRequest struct {
	Id          int64                 `gorm:"column:id;primaryKey" json:"id"`
	Title       string                `gorm:"column:title;size:32" json:"title"`
	Tags        *[]string             `gorm:"column:tags;size:128;default:'[]'" json:"tags"`
	Content     string                `gorm:"column:content;type:text" json:"content"`
	JudgeCase   *[]database.JudgeCase `gorm:"column:judge_case;type:text" json:"judgeCase"`
	JudgeConfig database.JudgeConfig  `gorm:"column:judge_config;size:128" json:"judgeConfig"`
	CoreCode    string                `gorm:"column:core_code;type:text" json:"coreCode"`
	BaseCode    string                `gorm:"column:base_code;type:text" json:"baseCode"`
}
