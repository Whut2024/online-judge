package api

import (
	"github.com/gin-gonic/gin"
	"go-oj/entity/request"
	"go-oj/entity/response"
	"go-oj/service"
	"net/http"
)

type UserApi struct {
}

func (this *UserApi) Login(c *gin.Context) {
	c.JSON(http.StatusOK, gin.H{
		"user": "test",
	})
}

func (this *UserApi) Register(c *gin.Context) {
	// 结构体解析
	b := new(request.Register)
	e := c.ShouldBindBodyWithJSON(b)
	if e != nil {
		response.FailWithMessage(e.Error(), c)
		return
	}

	service.ServiceGroupObj.UserService.Register(b, c)
}

func (this *UserApi) GetLoginUser(c *gin.Context) {
	c.JSON(http.StatusOK, gin.H{
		"user": "test",
	})
}
