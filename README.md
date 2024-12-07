飞📖 🔗 https://h1nj7u0kkda.feishu.cn/docx/XwsDdJt2UoSyamxS8kfcWlisnud?from=from_copylink

# 整体架构

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=NjNiZDkwNjY4YzhlM2RhZjVhNDUyZmU1MjNmMzc1NjdfcXQwVXFsMHFyTFhtUG9nbHlKNlRMaXREenpUNnp6TXBfVG9rZW46TEVmUmJ2UFExb0FQSnV4R0t4NWNkaTUxbmhkXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)



# 核心流程

### 基本要求

- 单独启动一个进程去监控每个实例的健康状况
- 每个实例最多同时运行 50 个进程
- 每个 Docker 服务器运行 1 个实例
- 当项目启动时，默认开启一个 Docker 服务器的全部实例
- 可以快速的获取负载最小的实例

Nacos Redis 会作为数据中心，存储每个实例的地址，负载信息和运行状况

### 监控的实现

单独的服务实例会给每个实例发送一个执行输出 Hello World 的命令

如果超时或者返回错误则认为实例异常，提高该实例的记录负载量，暂时不让这个实例被继续选择

定时去监控这些异常的实例，在响应正常的情况下再次给实例上线

！！现在的监控节点是单机，可能存在网络拥塞的问题

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=YWZjZTk4MzQxZjQ1MzNlMTdmNWY1NDlmNDNhZWYzMWJfY3p5U3pRYUE1a0plN1ptanFLWEhuV3JQaEpTdHd5ZGNfVG9rZW46WHVsZmJzU29Yb01BYkd4Q1JwZWNqUmdjblNjXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)



### 负载均衡实现

当前采用监控集群 java 进程数量的方式去进行负载均衡

采用 Redis 的 sorted_set 方式去存储

member-实例 ID

score-运行用户代码的进程数

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=ZWFjYzIzMjljZTczMjcxNzhjMjc0NzYwZTdjOWM5ZDVfa1NPeEZsS21uVkgwOFBhYkIyT2w4MGw3SEdobWI0VVVfVG9rZW46Um1IcWJhT2Vab28xQmF4TjZMWWNZMmh1bnlnXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)



## 提交题目数据流

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=ZTFkM2QzNmQzYTA4YjA5OTJjMzY0ZDk1NGI3NTM5YmZfU1lWUXc5UXV2T25oalBRRE5BQmV1VDBDbmVUUEk2SVdfVG9rZW46TGpvcWJJMXdXbzViTXJ4Z0VOdWNpZ2pkbjFnXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)



# 数据库表

## User

```SQL
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user
(
    `id`            BIGINT COMMENT "题目 ID",
    `user_account`  VARCHAR(16)  NOT NULL COMMENT "账号",
    `user_avatar`   VARCHAR(512) NOT NULL DEFAULT '' COMMENT "头像网络地址",
    `user_name`     VARCHAR(16)  NOT NULL DEFAULT '' COMMENT "用户名",
    `user_password` CHAR(32)     NOT NULL COMMENT "用户密码",
    `user_role`     VARCHAR(8)   NOT NULL COMMENT "用户角色",
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "更新时间",
    `is_delete`     TINYINT      NOT NULL DEFAULT 0 COMMENT "逻辑删除 1 删除 0 未删除",
    primary key (id),
    unique index user_account_password_unique_index (user_account, user_password)
) COMMENT "用户表";
```

## Question

```SQL
DROP TABLE IF EXISTS t_question;
CREATE TABLE t_question
(
    `id`                BIGINT COMMENT "题目 ID",
    `user_id`           BIGINT       NOT NULL COMMENT "创建题目的用户 ID",
    `title`             VARCHAR(32)  NOT NULL COMMENT "题目标题",
    `tags`              VARCHAR(128) NOT NULL DEFAULT '[]' COMMENT "题目标签",
    `content`           TEXT         NOT NULL COMMENT "题目内容",
    `judge_case`        TEXT         NOT NULL COMMENT "运行测试案例",
    `core_code`         TEXT         NOT NULL COMMENT "运行校验代码",
    `judge_config`      VARCHAR(128) NOT NULL COMMENT "题目运行资源限制 时间 ms 内存 KB",
    `submission_number` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT "答案提交数",
    `acceptance_number` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT "答案提交通过数",
    `create_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
    `update_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "更新时间",
    `is_delete`         TINYINT      NOT NULL DEFAULT 0 COMMENT "逻辑删除 1 删除 0 未删除",
    primary key (id),
    index user_title_index (user_id, title)
) COMMENT "题目表";
```

## AnswerSubmission

```SQL
DROP TABLE IF EXISTS t_answer_submission;
CREATE TABLE t_answer_submission
(
    `id`             BIGINT COMMENT "答案提交 ID",
    `user_id`        BIGINT     NOT NULL COMMENT "创建答案提交的用户 ID",
    `question_id`    BIGINT     NOT NULL COMMENT "题目 ID",
    `submitted_code` TEXT       NOT NULL COMMENT "提交的代码",
    `judge_info`     TEXT COMMENT "答案提交的测试处理结果",
    `language`       VARCHAR(8) NOT NULL COMMENT "提交的代码的语言",
    `status`         TINYINT    NOT NULL DEFAULT 2 COMMENT "答案提交的状态 0 异常 1 通过 2 运行中",
    `create_time`    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
    `update_time`    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "更新时间",
    `is_delete`      TINYINT    NOT NULL DEFAULT 0 COMMENT "逻辑删除 1 删除 0 未删除",
    primary key (id),
    index user_question_index (user_id, question_id),
    index question_index (question_id)
) COMMENT "答案提交表";
```

# API 接口

## 用户

### 注册用户

```JavaScript
method: 'POST',
url: '/api/user/add'

userAccount?: string;
userAvatar?: string;
userName?: string;
userRole?: string;
```

### 获取用户信息

```JavaScript
method: 'GET',
url: '/api/user/get/login'
```

### 用户登录

```JavaScript
method: 'POST',
url: '/api/user/login'

userAccount?: string;
userPassword?: string;
```

## 题目

### 新增题目

```JavaScript
method: 'POST',
url: '/api/question/add'

{
    answer?: string;
    content?: string;
    judgeCase?: Array<JudgeCase>;
    judgeConfig?: JudgeConfig;
    tags?: Array<string>;
    title?: string;
};
```

### 删除题目

```JavaScript
method: 'POST',
url: '/api/question/delete'

{
    id?: number;
};
```

### 更新题目

```JavaScript
method: 'POST',
url: '/api/question/update'


{
    answer?: string;
    content?: string;
    id?: number;
    judgeCase?: Array<JudgeCase>;
    judgeConfig?: JudgeConfig;
    tags?: Array<string>;
    title?: string;
};
```

### 获取题目详细信息

```JavaScript
method: 'GET',
url: '/api/question/get',
query: {
    'id': id,
},
```

### 获取过滤后的题目信息

```JavaScript
method: 'GET',
url: '/api/question/get/vo',
query: {
    'id': id,
},
```

### 分页查询题目信息

```JavaScript
method: 'POST',
url: '/api/question/list/page'

{
    answer?: string;
    content?: string;
    current?: number;
    id?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    tags?: Array<string>;
    title?: string;
    userId?: number;
};
```

### 分页查询过滤后的题目信息

```JavaScript
method: 'POST',
url: '/api/question/list/page/vo',

{
    answer?: string;
    content?: string;
    current?: number;
    id?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    tags?: Array<string>;
    title?: string;
    userId?: number;
};
```

## 答案提交

### 新增答案提交

```JavaScript
method: 'POST',
url: '/api/question/question_submit/do',

{
    code?: string;
    language?: string;
    questionId?: number;
};
```

### 分页查询答案提交

```JavaScript
method: 'POST',
url: '/api/question/question_submit/list/page',

{
    current?: number;
    language?: string;
    pageSize?: number;
    questionId?: number;
    sortField?: string;
    sortOrder?: string;
    status?: number;
    userId?: number;
};
```

# 部分模块

## 限流模块

采用最基础的固定窗口限流，为了迎合前端人员遗留的代码缺陷，在一次处理时间不超过限制的请求结束后会直接释放锁，防止抖动的请求无法被处理

## 资源型负载均衡逻辑

每个实例对象会以管理这个实例的 Docker 服务器上启动的代码执行后台服务 id 作为实例名字的一部分，这一部分也会作为 Redis 的 Sorted_Set 中的 member 存储下来，需要时会选择负载最小的实例，返回给门户后台服务器，服务器获取到这个实例的名字后可以知道应该调用哪一个代码执行后台服务器，代码执行服务器拿到这个名字后也可以知道使用 Docker 服务器中的哪一个实例

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=YzdiZGUwNDg3NTEwOGIzNzViZGVmYzljNzUxZTczZGNfTW9BQzh0WlJNcG5jQmVVV2lXbG1lQWtEWXRsTlV0UjhfVG9rZW46QnNZRWJNZ0F0b0M4eUZ4OXo0SWNhaXlFbktkXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)



### 策略选择

当前以各个实例的相关资源消耗为基础来进行负载，开始准备采用轮询的方式，但考虑到用户提交的代码执行时间可能不同，需要进一步的细化为以当前运行用户代码的进程数来进行负载，所以选择实现了 Dubbo 框架下的一个 AbstractLoadBalance 类，去选择出当前运行着最少进程的实例，然后对轮询策略和资源负载策略进行了基础的测试，结果如下

#### 测试代码

二十个线程每个每隔 0.5 秒发送一次请求

```SQL
package com.whut.onlinejudge.backgrounddoor;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.nio.charset.StandardCharsets;

/**
 * @author liuqiao
 * @since 2024-12-04
 */
public class ApiTest {
    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            new Thread(() -> {
                while (true) {
                    HttpResponse response = HttpRequest
                            .post("http://localhost:8101/api/answer_submission/do")
                            .header("token", "738b7daf6e2bae47113a2fbf8f8fad96")
                            .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                            .header("Content-Type", "application/json")
                            .header("Accept", "*/*")
                            .header("Host", "localhost:8101")
                            .header("Connection", "keep-alive")
                            .body("{\n" +
                                    "    \"language\": \"JAVA\",\n" +
                                    "    \"questionId\": \"1863821439531016194\",\n" +
                                    "    \"submittedCode\": \"import java.util.Random;  public class Solution {      public int add(int a, int b) {         System.out.println(\\\"a = \\\" + a);         System.out.println(\\\"b = \\\" + b);         try {             Thread.sleep(new Random().nextInt(10) * 200L);         } catch (InterruptedException e) {             throw new RuntimeException(e);         }         return a + b;     } } \\n\"\n" +
                                    "}")
                            .execute();

                    System.out.println(new String(response.bodyBytes(), StandardCharsets.UTF_8));
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }
}
```

#### 轮询型

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=YTRkNjRmOTQ3NDU4M2YwODZkODQ5NGNlZThmYzcxY2VfZFNwNG96VkZPS2JrNDdxTm9CME1vSHRhWU5mWkpkV3BfVG9rZW46UEowY2JRbHcwb1NwRlN4eEx1V2M5ckxubnRlXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=NjlhM2RlMzU0ZDdjOTI2YmQ0YTRjN2QyZGRhZmI1ODJfYjI3VTI2cmxnNWdoQWE3V3BzRDI3VDhIcVVwQk53dWpfVG9rZW46RExGa2Jadjl1b1VhNUF4aFJ0c2NxQXhZbkdRXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=NzZiNGZjYjhlZWVjYTMxNTBmMGNlZDk0MjY3ZmZlZjZfdndTTG16eGVWbm9jYndaNjl3czJwYU80V2tRVm1pemFfVG9rZW46SHh6aGJOcU90bzNPZ3R4a0tjNmN1ZEZjblplXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=ZGIyODViZTE3ZDBlYzMzZmUxZGQwMzUxZTZiY2E5ZmVfM2VTYmhqTzFQODNYcVNqWGVXQ3p4enBoOUZMVlMyQVJfVG9rZW46R3oxaGJVb3E3b1U1UTZ4eFRDemNnaW54bnFmXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)

#### 资源负载型

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=Y2FlMDAzOWI5YzY5ZWM5YjQyMTBiNTQ3ZTNmYzY0ZmNfZ3V5VUNsMEE3T3BLRGx6OXhOQWFqdHZoajgwVXhwZlpfVG9rZW46SFZtdmJyT0xUbzNTcVB4OGFFdWNTd3o2bmZjXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=YzNkYjdjNDdjZjhiZjQzOGY3ODcyOThhMGE2YzRlNDNfY3FlSllmaHUzZ0hxcW8wN2Rid1VSVkpBRXlHNFgwN0JfVG9rZW46QnNzOGJZeE9vb082djJ4dTBsWmNPREJJbmlnXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=MWZhOWIxMDFkNjhjNTFkYzllNGIzNWEzMjA3NTEwOWNfeHVQTElXbmdzTzNYTGpKNFRKdWpxR0tLcVc2aGZnNHhfVG9rZW46Q2lDaGJTY2FYb1NzdFR4U1V2YWN1NXBabjBkXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=NzFiMTY2NTY1ZGM2Y2YxZGUwNzVjOWQ2NmVmOGIzMTRfRzJmQkw0QnFuSFV4Nno2S3FqeEs5b0I5dlhKcFk4b2NfVG9rZW46S0NWaGJrbEFwb2RGN2t4SG9xN2NlaE9kblNnXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=MTA1NjI0NDE1NTRhMGI3NTk0OTdlYWNkZDc2ZTk0MTJfSlpRcUxYbnhvamJsaHczRERINENzTWx6cTJZNTVERmJfVG9rZW46SjBwYmJ3WTR3b3IwenR4NkZGNWNqdnQ4bnNmXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)

#### 结论

资源负载型的每个实例在执行过程中运行用户代码的时间分布比轮询型分布更加均匀，方差，极差也更小

### 竞争饥饿解决

根据 sorted set 的排序结果来获取当前最合适的实例会存在问题

sorted set 不仅会根据 socre 排序，还会根据 member 本身的字典序排序，在相同的 score 情况下，字典序更小的 member 有天然的优先级，导致负载会向字典序更小的 member 实例倾斜

#### 第一版

```SQL
local member = redis.call('zrange', KEYS[1], 0, 0)

if member == nil then
    return -1
end

redis.call('zincrby', KEYS[1], 1, member[1])
return member[1]
```

#### 第二版

```SQL
local key = KEYS[1]
local minScore = redis.call('zrange', key, 0, 0, 'withscores')[2]
local index = math.floor(ARGV[1] * redis.call('zcount', key, minScore, minScore))
local res = redis.call('zrange', key, index, index)[1]
redis.call('zincrby', key, 1, res)
return res
```

## Dubbo 注册中心选择

### Redis

负载均衡的数据来源为 Redis，在配置难度相同的情况下选择 Redis 可以减少项目中中间件的数量，简化开发难度，但是测试后发现非常严重的问题，这种注册中心的数据存储是 K-V 型，单独值单独键，没有层次结构，导致一个 Key 存储的数据量比较大，在服务的发现过程中会存在 BigKey 问题，但是这个 Key 的使用频率不高

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=NTZjZjI2NjcxODdiYmU1OTlmYThlZjE4ZWNjMGRkYTRfdEZvWHcyMkwzVDl2eGVFaVRCMTJSQ1pYbFJzek9OTHFfVG9rZW46U2NDYmJmamp4b3FhWHV4VmNidWNicEczbllkXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=NTFjY2E1ZjYwNWEzYjU1MTlmNWFkZTc3NzBjODkwZWNfSDBrbVBINXh6NGZvNmRRTmtjWE5mdkhvcmJCaXpFcjlfVG9rZW46QXVEUGJJVUFRbzlMUE14QlpDS2N2WUgzbjNiXzE3MzM1NTc0NDY6MTczMzU2MTA0Nl9WNA)

### Nacos

虽然增加了一个单独的注册中心中间件，但是作为与 Dubbo 同公司产品，兼容性更好，后续的项目扩展性更强

## 实例健康监控

对实例的负载记录，状态修改都是通过 Dubbo 组件中的 LoadBalancer 和 Filter 通过 SPI 加载实现

负载均衡器选择的实例服务调用失败后会在 Redis 中增加失败的次数，当次数达到临界值时会将该实例 ID 存入待下线的 Redis set 中，监控服务会定时读取这个 set，再次尝试调用，如果任然失败则直接下线，成功则减少失败次数，当次数小于等于临界值时会恢复上线节点

下线的节点信息会缓存到本地监控服务器，定时去检测实例服务是否上线，当检测通过时则恢复节点状态

### 增加失败次数代码

```SQL
--- 异常次数加一
if redis.call('INCR', KEY[1]) == ARGV[3] + 1 then
    --- 异常次数超过预定值 加入节点到待下线节点
    redis.call('SADD', KEY[2], ARGV[1])
end

--- 刷新 key 超时时间
redis.call('EXPIRE', KEY[1], ARGV[2])
```

### 下线节点代码

```SQL
--- 获取对应权重
local score = redis.call('ZMSCORE', KEY[1], ARGV[1])[1]
if score == nil then
    return -1
end

--- 校验权重大小
if score >= ARGV[2] then
    return
end

--- 增加权重
redis.call('ZINCRBY', KEY[1], ARGV[2], ARGV[1])

--- 删除待下线 set 中的信息
redis.call('SREM', KEY[2], ARGV[1])
```

### 减少失败次数代码

```SQL
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
```

### 上线节点代码

```SQL
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
```

## 核心代码模式样板

### 引导代码

```SQL
/**
 * @author liuqiao
 * @since 2024-12-02
 */
public class Main {

    private static int memoryLimit;

    private static int timeLimit;

    private static final Runtime runtime = Runtime.getRuntime();

    private static long startMemory;

    private static long startTime;

    /**
     * 检查当前的内存消耗是否大于最大限制内存，当大于最大限制内存时会输出相关的提示
     */
    private static boolean checkMemory() {
        if ((startMemory - runtime.freeMemory()) / 1024 / 1024 > memoryLimit) {
            // 内存溢出
            System.out.println("内存溢出");
            System.out.println(false);
            System.out.println(false);
            return false;
        }
        return true;
    }

    /**
     * 检查当前的时间消耗是否大于最大限制时间，当大于最大限制时间时会输出相关的提示
     */
    private static boolean checkTime() {
        if ((System.currentTimeMillis() - startTime) / 1000 > timeLimit) {
            // 运行超时
            System.out.println("运行超时");
            System.out.println(false);
            System.out.println(false);
            return false;
        }
        return true;
    }

    /**
     * 当出现异常时输出相关提示
     */
    private static void occurException(Throwable e) {
        // 出现异常
        System.out.println(e);
        System.out.println(true);
        System.out.println(false);
    }

    /**
     * 当结果不符合预期时输出相关提示
     *
     * 这个方法因该由题目提供者自定义
     */
    private static boolean checkResult(int src, int dent) {
        if (src != dent) {
            // 返回错误
            System.out.println(false);
            System.out.println(false);
        }
        return true;
    }

    /**
     * 测试通过时输出相关的内存消耗和时间消耗
     */
    private static void pass(int caseNumber) {
        System.out.println((startMemory - runtime.freeMemory()) / 1024 / 1024 / caseNumber);
        System.out.println((System.currentTimeMillis() - startTime) / caseNumber);
        System.out.println(true);
    }

    public static void main(String[] args) {
        // 固定
        memoryLimit = Integer.parseInt(args[0]);
        timeLimit = Integer.parseInt(args[1]);
        final int caseNumber = Integer.parseInt(args[2]);
        final Solution solution = new Solution();

        // 变化
        final int[] a = new int[caseNumber], b = new int[caseNumber], c = new int[caseNumber];
        for (int i = 0; i < caseNumber; i++) {
            a[i] = Integer.parseInt(args[3 * i + 3]);
            b[i] = Integer.parseInt(args[3 * i + 4]);
            c[i] = Integer.parseInt(args[3 * i + 5]);
        }

        startTime = System.currentTimeMillis();
        startMemory = runtime.freeMemory();
        try {
            for (int i = 0; i < caseNumber; i++) {
                if (!checkResult(solution.add(a[i], b[i]), c[i]))
                    return;

                if (!checkMemory())
                    return;

                if (!checkTime())
                    return;
            }
        } catch (Throwable e) {
            occurException(e);
            return;
        }

        // 通过
        pass(caseNumber);
    }
}
```

### 测试代码

```SQL
public class Solution {

    public int add(int a, int b) {
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        try {
            Thread.sleep(new Random().nextInt(10) * 200L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return a + b;
    }
}
```