package config

type Config struct {
	ContextPath string
	Mysql
	Redis
	Zap
	Kafka
}
