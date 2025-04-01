package request

import (
	"go-oj/constant"
	"go-oj/entity/database"
)

type QuestionAddRequest struct {
	Title       string               `json:"title"`
	Tags        []string             `json:"tags"`
	Content     string               `json:"content"`
	JudgeCase   []database.JudgeCase `json:"judgeCase"`
	JudgeConfig database.JudgeConfig `json:"judgeConfig"`
	CoreCode    string               `json:"coreCode"`
	BaseCode    string               `json:"baseCode"`
}

type QuestionDeleteRequest struct {
	Id int64 `gorm:"column:id;primaryKey" json:"id"`
}

type QuestionQueryRequest struct {
	constant.Sort
	Current int      `json:"current"`
	EndId   int64    `json:"endId"`
	Id      int64    `json:"id"`
	UserId  int64    `json:"userId"`
	Title   string   `json:"title"`
	Tags    []string `json:"tags"`
	Content string   `json:"content"`
}

type QuestionUpdateRequest struct {
	Id          int64                `json:"id"`
	Title       string               `json:"title"`
	Tags        []string             `json:"tags"`
	Content     string               `json:"content"`
	JudgeCase   []database.JudgeCase `json:"judgeCase"`
	JudgeConfig database.JudgeConfig `json:"judgeConfig"`
	CoreCode    string               `json:"coreCode"`
	BaseCode    string               `json:"baseCode"`
}
