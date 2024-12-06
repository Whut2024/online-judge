--- 获取对应权重
local score = redis.call('ZMSCORE', KEY[1], ARGV[1])[1]
if score == nil then
    return -1
end

--- 校验权重大小
if score < ARGV[2] then
    return
end

--- 降低权重
redis.call('ZINCRBY', KEY[1], -ARGV[2], ARGV[1])