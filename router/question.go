package router

import (
	"github.com/gin-gonic/gin"
	"go-oj/api"
)

type QuestionRouterGroup struct {
}

func (this *QuestionRouterGroup) InitQuestionRouter(publicGroup *gin.RouterGroup, privateGroup *gin.RouterGroup, adminGroup *gin.RouterGroup) {
	questionApi := api.ApiGroupObj.QuestionApi
	{
		publicGroup = publicGroup.Group("question")
		publicGroup.Handle("GET", "get", questionApi.Get)
		publicGroup.Handle("GET", "get/vo", questionApi.GetVo)
		publicGroup.Handle("POST", "list/page", questionApi.Page)
		publicGroup.Handle("POST", "list/page/vo", questionApi.PageVo)

	}
	{
		privateGroup = privateGroup.Group("question")
		privateGroup.Handle("POST", "add", questionApi.Add)
		privateGroup.Handle("POST", "delete", questionApi.Delete)
	}
	{
		adminGroup = adminGroup.Group("question")
		adminGroup.Handle("POST", "update", questionApi.Update)
	}
}
