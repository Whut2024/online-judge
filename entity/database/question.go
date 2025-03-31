package database

import "time"

type Question struct {
	Id               int64     `gorm:"column:id;primaryKey" json:"id"`
	UserId           int64     `gorm:"column:user_id" json:"user_id"`
	Title            string    `gorm:"column:title;size:32" json:"title"`
	Tags             string    `gorm:"column:tags;size:128;default:'[]'" json:"tags"`
	Content          string    `gorm:"column:content;type:text" json:"content"`
	JudgeCase        string    `gorm:"column:judge_case;type:text" json:"judge_case"`
	CoreCode         string    `gorm:"column:core_code;type:text" json:"core_code"`
	BaseCode         string    `gorm:"column:base_code;type:text" json:"base_code"`
	JudgeConfig      string    `gorm:"column:judge_config;size:128" json:"judge_config"`
	SubmissionNumber uint      `gorm:"column:submission_number;default:0" json:"submission_number"`
	AcceptanceNumber uint      `gorm:"column:acceptance_number;default:0" json:"acceptance_number"`
	CreateTime       time.Time `gorm:"column:create_time;default:CURRENT_TIMESTAMP" json:"create_time"`
	UpdateTime       time.Time `gorm:"column:update_time;default:CURRENT_TIMESTAMP" json:"update_time"`
	IsDelete         int8      `gorm:"column:is_delete;default:0" json:"is_delete"`
}

type JudgeCase struct {
	Input  string `json:"input"`
	Output string `json:"output"`
}

type JudgeConfig struct {
	MemoryLimit int `json:"memoryLimit"`
	TimeLimit   int `json:"timeLimit"`
}
