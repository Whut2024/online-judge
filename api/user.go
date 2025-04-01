package api

import (
	"github.com/gin-gonic/gin"
	"go-oj/entity/request"
	"go-oj/entity/response"
	"go-oj/service"
)

type UserApi struct {
}

func (this *UserApi) Login(c *gin.Context) {
	// 结构体解析
	b := new(request.Login)
	e := c.ShouldBindBodyWithJSON(b)
	if e != nil {
		response.FailWithMessage(e.Error(), c)
		return
	}

	service.ServiceGroupObj.UserService.Login(b, c)
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
	// 结构体解析
	service.ServiceGroupObj.UserService.GetLoginUser(c)
}
