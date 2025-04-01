package api

import (
	"github.com/gin-gonic/gin"
	"go-oj/entity/request"
	"go-oj/entity/response"
	"go-oj/service"
	"strconv"
)

type SubmissionApi struct {
}

func (this *SubmissionApi) Check(c *gin.Context) {
	id, e := strconv.ParseInt(c.Param("id"), 10, 64)
	if e != nil {
		response.FailWithMessage("id 错误", c)
		return
	}
	service.ServiceGroupObj.SubmissionService.Check(id, c)
}
func (this *SubmissionApi) DoSubmission(c *gin.Context) {
	// 结构体解析
	b := new(request.SubmissionDoRequest)
	e := c.ShouldBindBodyWithJSON(b)
	if e != nil {
		response.FailWithMessage(e.Error(), c)
		return
	}

	service.ServiceGroupObj.SubmissionService.DoSubmission(b, c)
}
func (this *SubmissionApi) Page(c *gin.Context) {
	// 结构体解析
	b := new(request.SubmissionQueryRequest)
	e := c.ShouldBindBodyWithJSON(b)
	if e != nil {
		response.FailWithMessage(e.Error(), c)
		return
	}

	service.ServiceGroupObj.SubmissionService.Page(b, c)
}
