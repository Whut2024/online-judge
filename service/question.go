package service

import (
	"github.com/gin-gonic/gin"
	"github.com/goccy/go-json"
	"go-oj/constant"
	"go-oj/entity/database"
	"go-oj/entity/request"
	"go-oj/entity/response"
	"go-oj/global"
	"go-oj/util"
	"gorm.io/gorm"
	"strconv"
)

type QuestionService struct {
}

func (this *QuestionService) Get(id int64, c *gin.Context) {
	question := &database.Question{}
	global.DB.Find(question, "id = ?", id)
	if question.Id == 0 {
		response.OkWithMessage("题目不存在", c)
		return
	}
	response.OkWithData(question, c)
}
func (this *QuestionService) GetVo(id int64, c *gin.Context) {
	var question database.Question
	global.DB.Find(&question, "id = ?", id)
	if question.Id == 0 {
		response.OkWithMessage("题目不存在", c)
		return
	}
	response.OkWithData(question, c)

}
func (this *QuestionService) Page(query *request.QuestionQueryRequest, c *gin.Context) {
	var questionList []database.Question

	x := global.DB.Offset(query.PageSize * (query.Current - 1)).Limit(query.PageSize)
	buildQueryConds(query, x)
	x.Find(&questionList)

	response.OkWithData(questionList, c)
}

func buildQueryConds(query *request.QuestionQueryRequest, x *gorm.DB) {
	if query.Id != 0 {
		x = x.Where("id = ?", query.Id)
		return
	}
	if len(query.Tags) > 0 {
		inner := global.DB.Where("")
		for _, v := range query.Tags {
			inner.Or("tags like ?", "%"+v+"%")
		}
		x = x.Where(inner)
	}
	if query.EndId > 0 {
		x = x.Where("id > ?", query.EndId)

	}

	if len(query.Content) > 0 {
		x = x.Where("content like ?", "%"+query.Content+"%")

	}

	if len(query.Title) > 0 {
		x = x.Where("title like ?", "%"+query.Title+"%")
	}

	if len(query.SortField) > 0 {
		x = x.Order(query.SortField + " " + query.SortOrder)
	}
}

func (this *QuestionService) PageVo(query *request.QuestionQueryRequest, c *gin.Context) {
	var questionList []database.Question

	x := global.DB.Offset(query.PageSize * (query.Current - 1)).Limit(query.PageSize)
	buildQueryConds(query, x)
	x.Find(&questionList)

	response.OkWithData(questionList, c)
}
func (this *QuestionService) Add(questionAdd *request.QuestionAddRequest, c *gin.Context) {
	data, _ := c.Get(constant.USER_CACHE)
	user := data.(*response.UserVo)
	tagStr, _ := json.Marshal(questionAdd.Tags)
	judgeCaseStr, _ := json.Marshal(questionAdd.JudgeCase)
	judgeConfigStr, _ := json.Marshal(questionAdd.JudgeConfig)
	question := &database.Question{
		Id:               int64(util.Snowflake()),
		UserId:           user.Id,
		Title:            questionAdd.Title,
		Tags:             string(tagStr),
		Content:          questionAdd.Content,
		JudgeCase:        string(judgeCaseStr),
		CoreCode:         questionAdd.CoreCode,
		BaseCode:         questionAdd.BaseCode,
		JudgeConfig:      string(judgeConfigStr),
		SubmissionNumber: 0,
		AcceptanceNumber: 0,
	}
	global.DB.Create(question)
	response.OkWithData(question.Id, c)
}
func (this *QuestionService) Delete(id int64, c *gin.Context) {
	data, _ := c.Get(constant.USER_CACHE)
	user := data.(*response.UserVo)

	x := global.DB.Table("t_question").Where("id = ?", id).Where("is_delete != ?", 1)
	if constant.Admin != user.UserRole {
		x.Where("user_id = ?", user.Id)
	}
	result := x.Update("is_delete", 1)
	if result.Error != nil || result.RowsAffected == 0 {
		if result.Error != nil {
			global.Log.Info(result.Error.Error())
		}
		global.Log.Info("affected row is " + strconv.FormatInt(result.RowsAffected, 10))
		response.FailWithMessage("删除失败", c)
		return
	}

	response.OkWithMessage("删除成功", c)
}
func (this *QuestionService) Update(questionUpdate *request.QuestionUpdateRequest, c *gin.Context) {
	tagStr, _ := json.Marshal(questionUpdate.Tags)
	judgeCaseStr, _ := json.Marshal(questionUpdate.JudgeCase)
	judgeConfigStr, _ := json.Marshal(questionUpdate.JudgeConfig)
	question := &database.Question{
		Id:          questionUpdate.Id,
		Title:       questionUpdate.Title,
		Tags:        string(tagStr),
		Content:     questionUpdate.Content,
		JudgeCase:   string(judgeCaseStr),
		CoreCode:    questionUpdate.CoreCode,
		BaseCode:    questionUpdate.BaseCode,
		JudgeConfig: string(judgeConfigStr),
	}

	data, _ := c.Get(constant.USER_CACHE)
	user := data.(*response.UserVo)

	x := global.DB.Where("id = ?", question.Id)
	if constant.Admin != user.UserRole {
		x.Where("user_id = ?", user.Id)
	}
	result := x.Save(question)
	if result.Error != nil || result.RowsAffected == 0 {
		response.FailWithMessage("更新失败", c)
		return
	}

	response.OkWithMessage("更新成功", c)
}
