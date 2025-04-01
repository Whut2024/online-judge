package initialize

import (
	"context"
	"fmt"
	"github.com/segmentio/kafka-go"
	"go-oj/global"
	"log"
)

func InitKafka() {
	kafkaConfig := global.Config.Kafka
	conn, err := kafka.DialLeader(context.Background(), "tcp",
		fmt.Sprintf("%s:%d", kafkaConfig.Host, kafkaConfig.Port),
		kafkaConfig.Topic, kafkaConfig.Partition)
	if err != nil {
		log.Fatal("failed to dial leader:", err)
	}

	global.Kafka = conn
}
