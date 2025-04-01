package api

import (
	"github.com/gin-gonic/gin"
	"go-oj/entity/request"
	"go-oj/entity/response"
	"go-oj/service"
)

type QuestionApi struct {
}

func (this *QuestionApi) Get(c *gin.Context) {
	service.ServiceGroupObj.QuestionService.Get(1, c)
}
func (this *QuestionApi) GetVo(c *gin.Context) {
	service.ServiceGroupObj.QuestionService.GetVo(1, c)
}
func (this *QuestionApi) Page(c *gin.Context) {
	// 结构体解析
	b := new(request.QuestionQueryRequest)
	e := c.ShouldBindBodyWithJSON(b)
	if e != nil {
		response.FailWithMessage(e.Error(), c)
		return
	}

	service.ServiceGroupObj.QuestionService.Page(b, c)
}
func (this *QuestionApi) PageVo(c *gin.Context) {
	// 结构体解析
	b := new(request.QuestionQueryRequest)
	e := c.ShouldBindBodyWithJSON(b)
	if e != nil {
		response.FailWithMessage(e.Error(), c)
		return
	}

	service.ServiceGroupObj.QuestionService.PageVo(b, c)
}
func (this *QuestionApi) Add(c *gin.Context) {
	// 结构体解析
	b := new(request.QuestionAddRequest)
	e := c.ShouldBindBodyWithJSON(b)
	if e != nil {
		response.FailWithMessage(e.Error(), c)
		return
	}

	service.ServiceGroupObj.QuestionService.Add(b, c)
}
func (this *QuestionApi) Delete(c *gin.Context) {
	// 结构体解析
	b := new(request.QuestionDeleteRequest)
	e := c.ShouldBindBodyWithJSON(b)
	if e != nil {
		response.FailWithMessage(e.Error(), c)
		return
	}

	service.ServiceGroupObj.QuestionService.Delete(b.Id, c)
}
func (this *QuestionApi) Update(c *gin.Context) {
	// 结构体解析
	b := new(request.QuestionUpdateRequest)
	e := c.ShouldBindBodyWithJSON(b)
	if e != nil {
		response.FailWithMessage(e.Error(), c)
		return
	}

	service.ServiceGroupObj.QuestionService.Update(b, c)
}
