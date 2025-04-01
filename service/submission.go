package service

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"go-oj/entity/request"
	"go-oj/entity/response"
)

type SubmissionService struct {
}

func (this *SubmissionService) Check(id int64, c *gin.Context) {
	fmt.Println(id)
	response.Ok(c)
}
func (this *SubmissionService) DoSubmission(submission *request.SubmissionDoRequest, c *gin.Context) {
	fmt.Println(*submission)
	response.Ok(c)
}
func (this *SubmissionService) Page(query *request.SubmissionQueryRequest, c *gin.Context) {
	fmt.Println(*query)
	response.Ok(c)
}
