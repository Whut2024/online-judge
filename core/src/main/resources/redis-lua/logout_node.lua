-- 实现服务节点的下线从 sorted set 中删除当前节点

-- key KEYS[1] value ARGV[1]
redis.call('ZREM', KEYS[1], ARGV[1])

if redis.call('ZCARD', KEYS[1]) == 0 then
    redis.call('del', KEYS[1])
end