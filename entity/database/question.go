package database

import (
	"time"
)

type Question struct {
	Id               int64     `gorm:"column:id;primaryKey" json:"id"`
	UserId           int64     `gorm:"column:user_id" json:"userId"`
	Title            string    `gorm:"column:title;size:32" json:"title"`
	Tags             string    `gorm:"column:tags;size:128;default:'[]'" json:"tags"`
	Content          string    `gorm:"column:content;type:text" json:"content"`
	JudgeCase        string    `gorm:"column:judge_case;type:text" json:"judgeCase"`
	CoreCode         string    `gorm:"column:core_code;type:text" json:"coreCode"`
	BaseCode         string    `gorm:"column:base_code;type:text" json:"baseCode"`
	JudgeConfig      string    `gorm:"column:judge_config;size:128" json:"judgeConfig"`
	SubmissionNumber uint      `gorm:"column:submission_number;default:0" json:"submissionNumber"`
	AcceptanceNumber uint      `gorm:"column:acceptance_number;default:0" json:"acceptanceNumber"`
	CreateTime       time.Time `gorm:"column:create_time;default:CURRENT_TIMESTAMP" json:"createTime"`
	UpdateTime       time.Time `gorm:"column:update_time;default:CURRENT_TIMESTAMP" json:"updateTime"`
	IsDelete         int8      `gorm:"column:is_delete;default:0" json:"isDelete"`
}

type JudgeCase struct {
	Input  string `json:"input"`
	Output string `json:"output"`
}

type JudgeConfig struct {
	MemoryLimit int `json:"memoryLimit"`
	TimeLimit   int `json:"timeLimit"`
}
