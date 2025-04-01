package router

import (
	"github.com/gin-gonic/gin"
	"go-oj/api"
)

type SubmissionRouterGroup struct {
}

func (this *SubmissionRouterGroup) InitSubmissionRouter(publicGroup *gin.RouterGroup, privateGroup *gin.RouterGroup, adminGroup *gin.RouterGroup) {
	submissionApi := api.ApiGroupObj.SubmissionApi
	{
		publicGroup = publicGroup.Group("answer_submission")
	}
	{
		privateGroup = privateGroup.Group("answer_submission")
		privateGroup.Handle("GET", "check/:id", submissionApi.Check)
		privateGroup.Handle("POST", "do", submissionApi.DoSubmission)
		privateGroup.Handle("POST", "list/page", submissionApi.Page)

	}
	{
		adminGroup = adminGroup.Group("answer_submission")
	}
}
