package initialize

import (
	"fmt"
	"github.com/segmentio/kafka-go"
	"go-oj/global"
)

func InitKafka() {
	kafkaConfig := global.Config.Kafka
	w := &kafka.Writer{
		Addr:     kafka.TCP(fmt.Sprintf("%s:%d", kafkaConfig.Host, kafkaConfig.Port)),
		Topic:    kafkaConfig.Topic,
		Balancer: &kafka.Hash{}, // 即使指定了分区，也建议保留负载均衡器
	}

	global.Kafka = w
}
