package request

import (
	"go-oj/constant"
)

type Register struct {
	UserAccount  string            `json:"userAccount" binding:"required,max=20"`
	UserPassword string            `json:"userPassword" binding:"required,max=16"`
	UserName     string            `json:"userName" binding:"required,max=16"`
	UserAvatar   string            `json:"userAvatar" binding:"required,max=128"`
	UserRole     constant.UserRole `json:"userRole" binding:"required"`
}

type Login struct {
	UserAccount  string `json:"userAccount" binding:"required,max=20"`
	UserPassword string `json:"userPassword" binding:"required,max=16"`
}
