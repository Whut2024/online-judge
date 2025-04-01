package service

import (
	"errors"
	"github.com/bsm/redislock"
	"github.com/gin-gonic/gin"
	"github.com/goccy/go-json"
	"go-oj/constant"
	"go-oj/entity/database"
	"go-oj/entity/request"
	"go-oj/entity/response"
	"go-oj/global"
	"go-oj/util"
	"math/rand"
	"strconv"
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

func (this *UserService) Login(body *request.Login, c *gin.Context) {
	// 查询数据库用户记录
	user := &database.User{}
	global.DB.Where(&database.User{UserAccount: body.UserAccount, UserPassword: util.MD5(body.UserPassword)}).Find(user)

	if user.Id == 0 {
		response.OkWithMessage("帐号或者密码错误", c)
		return
	}

	// 缓存用户信息和版本
	versionKey := constant.USER_LOGIN_VERSION_KEY + strconv.FormatInt(int64(user.Id), 10)
	version, e := global.Redis.Incr(versionKey).Result()
	global.Redis.Expire(versionKey, constant.USER_LOGIN_VERSION_TTL*time.Millisecond)
	if e != nil {
		response.FailWithMessage("请稍后再试", c)
		return
	}

	userVo := response.UserVo{
		Token:      util.MD5(constant.USER_CACHE + strconv.FormatInt(rand.Int63()+int64(user.Id), 10)),
		UserAvatar: user.UserAvatar,
		CreateTime: user.CreateTime,
		UpdateTime: user.UpdateTime,

		Id:          int64(user.Id),
		UserName:    user.UserName,
		UserAccount: user.UserAccount,
		UserRole:    user.UserRole,
		Version:     version,
	}
	cacheKey := constant.USER_CACHE + userVo.Token
	userJsonStr, _ := json.Marshal(userVo)
	global.Redis.Set(cacheKey, userJsonStr, constant.USER_CACHE_TTL*time.Millisecond)
	response.OkWithData(userVo, c)
}

func (this *UserService) GetLoginUser(c *gin.Context) {
	user, _ := c.Get(constant.USER_CACHE)
	response.OkWithData(user, c)
}
