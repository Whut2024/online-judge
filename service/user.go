package service

import (
	"errors"
	"github.com/bsm/redislock"
	"github.com/gin-gonic/gin"
	"go-oj/constant"
	"go-oj/entity/database"
	"go-oj/entity/request"
	"go-oj/entity/response"
	"go-oj/global"
	"go-oj/util"
	"time"
)

type UserService struct {
}

func (this *UserService) Register(body *request.Register, c *gin.Context) {
	// 分布式锁降低数据库压力
	lock, e := global.DistributedLock.Obtain(constant.USER_REGISTER_PREFIX+body.UserAccount, constant.USER_REGISTER_TTL*time.Minute, nil)
	if errors.Is(redislock.ErrNotObtained, e) {
		response.FailWithMessage("重复创建用户", c)
		return
	} else if e != nil {
		global.Log.Warn("api/user.go:26 加分布式锁失败, user account is " + body.UserAccount)
		response.FailWithMessage("创建失败,请稍后重试", c)
		return
	}
	defer func(lock *redislock.Lock) {
		err := lock.Release()
		if err != nil {
			global.Log.Warn("api/user.go:26 加分布式锁释放失败, user account is " + body.UserAccount)
		}
	}(lock)

	// 查询是否存在这个用户帐号
	user := &database.User{}
	global.DB.First(user, "user_account = ?", body.UserAccount)
	if user.Id != 0 {
		response.OkWithMessage("用户已经存在", c)
		return
	}

	// 数据库写入
	user.Id = util.Snowflake()
	user.UserAccount = body.UserAccount
	user.UserPassword = util.MD5(body.UserPassword)
	user.UserName = body.UserName
	user.UserAvatar = body.UserAvatar
	user.UserRole = body.UserRole
	global.DB.Create(user)

	// 响应
	response.Ok(c)
}
