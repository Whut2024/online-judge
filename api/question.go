package api

import (
	"github.com/gin-gonic/gin"
	"go-oj/entity/request"
	"go-oj/entity/response"
	"go-oj/service"
	"strconv"
)

type QuestionApi struct {
}

func (this *QuestionApi) Get(c *gin.Context) {
	data := c.Query("id")
	if len(data) == 0 {
		response.FailWithMessage("题目 id 错误", c)
		return
	}
	id, e := strconv.ParseInt(data, 10, 64)
	if e != nil {
		response.FailWithMessage("题目 id 错误", c)
		return
	}
	service.ServiceGroupObj.QuestionService.Get(id, c)
}
func (this *QuestionApi) GetVo(c *gin.Context) {
	data := c.Query("id")
	if len(data) == 0 {
		response.FailWithMessage("题目 id 错误", c)
		return
	}
	id, e := strconv.ParseInt(data, 10, 64)
	if e != nil {
		response.FailWithMessage("题目 id 错误", c)
		return
	}
	service.ServiceGroupObj.QuestionService.GetVo(id, c)
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
