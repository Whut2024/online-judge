package initialize

import (
	"go-oj/config"
	"go-oj/global"
)

func InitConfig() {
	selfConfig := &config.Config{}
	global.Config = selfConfig

	// server
	{
		selfConfig.ContextPath = "api"
	}

	// mysql
	{
		mysql := config.Mysql{
			Host:         "aliyun",
			Port:         3300,
			Config:       "charset=utf8mb4&parseTime=True&loc=Local",
			DBName:       "oj",
			Username:     "root",
			Password:     "whut2023",
			MaxIdleConns: 10,
			MaxOpenConns: 3,
			LogMode:      "info",
		}
		selfConfig.Mysql = mysql
	}

	// redis
	{
		redis := config.Redis{
			Address:  "aliyun:6300",
			Password: "whut",
			DB:       1,
		}
		selfConfig.Redis = redis
	}

}
