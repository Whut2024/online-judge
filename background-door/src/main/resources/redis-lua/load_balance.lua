local member = redis.call('zrange', KEYS[1], 0, 0)

if member == nil then
    return -1
end

redis.call('zincrby', KEYS[1], 1, member[1])
return member[1]