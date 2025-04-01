package interceptor

import (
	"github.com/gin-gonic/gin"
	"github.com/goccy/go-json"
	"go-oj/constant"
	"go-oj/entity/response"
	"go-oj/global"
	"strconv"
)

func CheckToken(c *gin.Context) {
	// 获取 token
	token := c.GetHeader("token")
	if token == "" {
		response.FailWithMessage("token 错误", c)
		c.Abort()
		return
	}

	// 获取存储在 redis 中 user 对象
	key := constant.USER_CACHE + token
	userJsonStr, e := global.Redis.Get(key).Result()
	if e != nil {
		response.FailWithMessage("请稍后再试", c)
		c.Abort()
		return
	}
	if len(userJsonStr) == 0 {
		response.FailWithMessage("未登录", c)
		c.Abort()
		return
	}

	user := &response.UserVo{}
	err := json.Unmarshal([]byte(userJsonStr), user)
	if err != nil {
		response.FailWithMessage("请稍后再试", c)
		c.Abort()
		return
	}

	// 获取存储在 redis 中 user 版本信息
	key = constant.USER_LOGIN_VERSION_KEY + strconv.FormatInt(user.Id, 10)
	version, e := global.Redis.Get(key).Int64()
	if e != nil {
		response.FailWithMessage("请稍后再试", c)
		c.Abort()
		return
	}

	if version != user.Version {
		response.FailWithMessage("未登录", c)
		c.Abort()
		return
	}
	c.Set(constant.USER_CACHE, user)
	c.Next()
}
