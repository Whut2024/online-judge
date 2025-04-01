package config

type Zap struct {
	Filename       string
	MaxSize        int
	MaxBackups     int
	MaxAge         int
	IsConsolePrint bool
	Level          string
}
