package router

import (
	"github.com/gin-gonic/gin"
	"go-oj/api"
)

type UserRouter struct {
}

func (this *UserRouter) InitUserRouter(publicGroup *gin.RouterGroup, privateGroup *gin.RouterGroup, adminGroup *gin.RouterGroup) {
	userApi := api.ApiGroupObj.UserApi
	{
		publicGroup = publicGroup.Group("user")
		publicGroup.Handle("POST", "login", userApi.Login)
		publicGroup.Handle("POST", "register", userApi.Register)
	}
	{
		privateGroup = privateGroup.Group("user")
		privateGroup.Handle("GET", "get/login", userApi.GetLoginUser)

	}
	{
		adminGroup = adminGroup.Group("user")

	}
}
