package response

import (
	"go-oj/constant"
	"time"
)

type UserVo struct {
	ID         int64             `json:"id"`
	Token      string            `json:"token"`
	UserAvatar string            `json:"userAvatar"`
	UserName   string            `json:"userName"`
	UserRole   constant.UserRole `json:"userRole"`
	CreateTime time.Time         `json:"createTime"`
	UpdateTime time.Time         `json:"updateTime"`
}
