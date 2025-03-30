package main

import (
	"github.com/gin-gonic/gin"
	"go-oj/initialize"
)

func main() {
	initialize.InitConfig()
	initialize.InitGorm()
	initialize.ConnectRedis()

	engine := gin.Default()
	initialize.InitRouter(engine)

	engine.Run("127.0.0.1:2001")

}
