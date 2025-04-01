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

	// zap logger
	{
		zapConfig := config.Zap{
			Filename:       "/Users/laowang/developer/codes/go-oj/._log",
			MaxSize:        1024,
			MaxBackups:     30,
			MaxAge:         30,
			IsConsolePrint: true,
			Level:          "info",
		}
		selfConfig.Zap = zapConfig
	}

	{
		kafkaConfig := config.Kafka{
			Host:      "hserver",
			Port:      9095,
			Topic:     "go-test",
			Partition: 0,
		}
		selfConfig.Kafka = kafkaConfig
	}

}
