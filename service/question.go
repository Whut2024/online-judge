package service

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"go-oj/entity/request"
	"go-oj/entity/response"
)

type QuestionService struct {
}

func (this *QuestionService) Get(id int64, c *gin.Context) {
	fmt.Println(id)
	response.Ok(c)

}
func (this *QuestionService) GetVo(id int64, c *gin.Context) {
	fmt.Println(id)
	response.Ok(c)

}
func (this *QuestionService) Page(query *request.QuestionQueryRequest, c *gin.Context) {
	fmt.Println(*query)
	response.Ok(c)

}
func (this *QuestionService) PageVo(query *request.QuestionQueryRequest, c *gin.Context) {
	fmt.Println(*query)
	response.Ok(c)
}
func (this *QuestionService) Add(question *request.QuestionAddRequest, c *gin.Context) {
	fmt.Println(*question)
	response.Ok(c)
}
func (this *QuestionService) Delete(id int64, c *gin.Context) {
	fmt.Println(id)
	response.Ok(c)

}
func (this *QuestionService) Update(question *request.QuestionUpdateRequest, c *gin.Context) {
	fmt.Println(*question)
	response.Ok(c)
}
