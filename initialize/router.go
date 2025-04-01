package initialize

import (
	"github.com/gin-gonic/gin"
	"go-oj/global"
	"go-oj/interceptor"
	"go-oj/router"
)

func InitRouter(engine *gin.Engine) {

	// 添加全局 interceptor
	engine.Use(interceptor.GinLogger, interceptor.GinRecovery(false))

	// 三种权限校验的路由组
	publicGroup := engine.Group(global.Config.ContextPath)
	privateGroup := engine.Group(global.Config.ContextPath)
	adminGroup := engine.Group(global.Config.ContextPath)

	// 添加 interceptor
	privateGroup.Use(interceptor.CheckToken)

	// 不同模块接口路由初始化方法的总集成对象
	routerGroup := router.RouterGroupObj

	// 开始调用不同模块接口路由初始化方法
	routerGroup.InitUserRouter(publicGroup, privateGroup, adminGroup)
	routerGroup.InitQuestionRouter(publicGroup, privateGroup, adminGroup)
	routerGroup.InitSubmissionRouter(publicGroup, privateGroup, adminGroup)

}
