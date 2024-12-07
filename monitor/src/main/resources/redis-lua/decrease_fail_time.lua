--- 降低失败次数
local failTime = redis.call('GET', KEY[1])
if failTime == nil then
    return -1
end

--- 计算出降低后的失败次数
if failTime >= 2 * ARGV[1] then
    failTime = failTime / 2
else
    failTime = failTime - 1
end

if failTime > ARGV[1] then
    --- 更新次数缓存
    redis.call('SETEX', KEY[1], ARGV[2], failTime)
    return 1
end

--- 节点恢复

redis.call('DEL', KEY[1])

--- 获取对应权重
local score = redis.call('ZMSCORE', KEY[2], ARGV[3])[1]
if score == nil then
    return -1
end

--- 校验权重大小
if score < ARGV[4] then
    return 1
end

--- 降低权重
redis.call('ZINCRBY', KEY[2], -ARGV[4], ARGV[3])
