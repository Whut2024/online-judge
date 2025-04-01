package global

import (
	"github.com/bsm/redislock"
	"github.com/go-redis/redis"
	"github.com/segmentio/kafka-go"
	"go-oj/config"
	"go.uber.org/zap"
	"gorm.io/gorm"
)

var (
	Config          *config.Config
	Log             *zap.Logger
	DB              *gorm.DB
	Redis           *redis.Client
	DistributedLock *redislock.Client
	Kafka           *kafka.Conn
)
