package initialize

import (
	"go-oj/global"
	"gorm.io/gorm/schema"
	"os"

	"go.uber.org/zap"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
)

// 自定义数据库表名和实例类名的关联关系
var customNamingStrategy = schema.NamingStrategy{
	TablePrefix:   "t_",  // 表名前缀
	SingularTable: true,  // 使用单数表名
	NameReplacer:  nil,   // 名称替换器
	NoLowerCase:   false, // 是否禁用小写转换
}

// InitGorm 初始化并返回一个使用 Mysql 配置的 GORM 数据库连接
func InitGorm() *gorm.DB {
	mysqlCfg := global.Config.Mysql

	// 使用给定的 DSN（数据源名称）和日志级别打开 Mysql 数据库连接
	db, err := gorm.Open(mysql.Open(mysqlCfg.Dsn()), &gorm.Config{
		Logger:         logger.Default.LogMode(mysqlCfg.LogLevel()), // 设置日志级别
		NamingStrategy: customNamingStrategy,                        // 自定义数据库表名和实例类名的关联关系
	})
	if err != nil {
		global.Log.Error("Failed to connect to Mysql:", zap.Error(err))
		os.Exit(1)
	}

	// 获取底层的 SQL 数据库连接对象
	sqlDB, _ := db.DB()
	// 设置数据库连接池中的最大空闲连接数
	sqlDB.SetMaxIdleConns(mysqlCfg.MaxIdleConns)
	// 设置数据库的最大打开连接数
	sqlDB.SetMaxOpenConns(mysqlCfg.MaxOpenConns)

	// todo
	global.DB = db

	return db
}
