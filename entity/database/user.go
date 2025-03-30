package database

import (
	"go-oj/constant"
	"go-oj/util"
	"gorm.io/gorm"
	"time"
)

// User 用户表
type User struct {
	Id           uint64            `gorm:"column:id;primaryKey"`                   // 假设有主键id，数据库中的bigint
	UserAccount  string            `gorm:"column:user_account;size:16"`            // varchar(16)
	UserAvatar   string            `gorm:"column:user_avatar;size:512;default:''"` // varchar(512) = ""
	UserName     string            `gorm:"column:user_name;size:16"`               // varchar(16)
	UserPassword string            `gorm:"column:user_password;size:32"`           // char(32)
	UserRole     constant.UserRole `gorm:"column:user_role;size:8"`                // varchar(8)
	CreateTime   time.Time         `gorm:"column:create_time;autoCreateTime"`      // datetime = CURRENT_TIMESTAMP
	UpdateTime   time.Time         `gorm:"column:update_time;autoUpdateTime"`      // datetime = CURRENT_TIMESTAMP
	IsDelete     int8              `gorm:"column:is_delete;default:0"`             // tinyint = 0
}

func (u *User) BeforeCreate(*gorm.DB) error {
	if u.Id == 0 {
		u.Id = util.Snowflake()
	}
	return nil
}
