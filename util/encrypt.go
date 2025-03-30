package util

import (
	"crypto/md5"
	"encoding/hex"
	"github.com/sony/sonyflake"
)

var (
	flake = sonyflake.NewSonyflake(sonyflake.Settings{})
)

func MD5(text string) string {
	hash := md5.Sum([]byte(text))
	return hex.EncodeToString(hash[:])
}

func Snowflake() uint64 {
	id, err := flake.NextID()
	if err != nil {
		panic(err)
	}
	return id
}
