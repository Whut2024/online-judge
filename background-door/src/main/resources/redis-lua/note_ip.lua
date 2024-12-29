if not (redis.call('get', KEYS[1]) == nil) then
    redis.call('expire', KEYS[1], ARGV[1])
    return 0
end
redis.call('setex', KEYS[1], ARGV[1], 1)
return 1