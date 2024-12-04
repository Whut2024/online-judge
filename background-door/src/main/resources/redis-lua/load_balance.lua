local key = KEYS[1]
local minScore = redis.call('zrange', key, 0, 0, 'withscores')[2]
local index = math.floor(ARGV[1] * redis.call('zcount', key, minScore, minScore))
local res = redis.call('zrange', key, index, index)[1]
redis.call('zincrby', key, 1, res)
return res