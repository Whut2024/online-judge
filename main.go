package main

import (
	"github.com/gin-gonic/gin"
	"go-oj/initialize"
)

func main() {
	initialize.InitConfig()
	initialize.InitGorm()
	initialize.ConnectRedis()
	initialize.InitLogger()
	initialize.InitKafka()

	engine := gin.Default()
	initialize.InitRouter(engine)

	engine.Run("0.0.0.0:8101")

}
