package service

import (
	"github.com/gin-gonic/gin"
	"go-oj/entity/request"
)

type SubmissionService struct {
}

func (this *SubmissionService) Check(id int64, c *gin.Context) {

}
func (this *SubmissionService) DoSubmission(submission *request.SubmissionDoRequest, c *gin.Context) {

}
func (this *SubmissionService) Page(query *request.SubmissionQueryRequest, c *gin.Context) {

}
