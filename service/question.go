package service

import (
	"github.com/gin-gonic/gin"
	"go-oj/entity/request"
)

type QuestionService struct {
}

func (this *QuestionService) Get(id int64, c *gin.Context) {

}
func (this *QuestionService) GetVo(id int64, c *gin.Context) {

}
func (this *QuestionService) Page(query *request.QuestionQueryRequest, c *gin.Context) {

}
func (this *QuestionService) PageVo(query *request.QuestionQueryRequest, c *gin.Context) {

}
func (this *QuestionService) Add(question *request.QuestionAddRequest, c *gin.Context) {

}
func (this *QuestionService) Delete(id int64, c *gin.Context) {

}
func (this *QuestionService) Update(question *request.QuestionUpdateRequest, c *gin.Context) {

}
