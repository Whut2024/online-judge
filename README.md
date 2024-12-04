# 整体架构

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=ODliMzFlNTI4MDFlYTM0NmMzMTMyZjdiNzhiYzNmZWJfeEd1b3RCSGlwRTRWWVU0U0h3T0F5b2lOZnUyNklEV1JfVG9rZW46WTAxemJodGRWb1pBc2F4Y3pIS2NsclNLbnJSXzE3MzMzMDQ2Mzg6MTczMzMwODIzOF9WNA)

飞书📖链接🔗 https://h1nj7u0kkda.feishu.cn/docx/XwsDdJt2UoSyamxS8kfcWlisnud?from=from_copylink

# 核心流程

### 基本要求

- 单独启动一个进程去监控每个实例的健康状况
- 每个实例最多同时运行 50 个进程
- 每个 Docker 服务器运行 1 个实例
- 当项目启动时，默认开启一个 Docker 服务器的全部实例
- 可以快速的获取负载最小的实例

Nacos Redis 会作为数据中心，存储每个实例的地址，负载信息和运行状况

### 监控的实现

单独的进程会给每个实例发送一个执行输出 Hello World 的命令

如果超时或者返回错误则认为实例错误，尝试在别的 Docker 服务器上开启一台新的实例

！！现在的监控节点是单机，可能存在网络拥塞的问题

### 负载均衡实现

当前采用监控集群 java 进程数量的方式去进行负载均衡

采用 Redis 的 sorted_set 方式去存储

member-实例 ID

score-运行用户代码的进程数

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=MjBlMzliYzhhNGI2OGRiZGU1MDBjMDllMDdkMDRjMzFfVWF4WTE2a1JoVFpwYU42T3l2b0tidGdyT3llYjRQZGpfVG9rZW46Q3FMTmJtclNQb1VCd2x4aDhSSmNKUzlrbmJmXzE3MzMzMDQ2Mzg6MTczMzMwODIzOF9WNA)



## 提交题目数据流

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=Yjc2MzMyNjU5YjE2MGRkNWM2NDkxOGQ4MmIwOTcwY2NfMjBGbGlzd3Y5WW8yd0dmTFdPZmRBNWgwbnZjcnd2ZmhfVG9rZW46QlF6Z2JiMnZVb2RIVFN4Z2tqQWNMTWszbkNkXzE3MzMzMDQ2Mzg6MTczMzMwODIzOF9WNA)



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

## 资源型负载均衡逻辑

每个实例对象会以管理这个实例的 Docker 服务器上启动的代码执行后台服务 id 作为实例名字的一部分，这一部分也会作为 Redis 的 Sorted_Set 中的 member 存储下来，需要时会选择负载最小的实例，返回给门户后台服务器，服务器获取到这个实例的名字后可以知道应该调用哪一个代码执行后台服务器，代码执行服务器拿到这个名字后也可以知道使用 Docker 服务器中的哪一个实例



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

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=ZmNhNGNlMGM0NjZjODQ5NmRhNGU4NzQzNjE2ZGU1Y2ZfenpvR01LV3E4eUtGQWswbzVQOVJHY0JreEh4ZmNlSW9fVG9rZW46UEowY2JRbHcwb1NwRlN4eEx1V2M5ckxubnRlXzE3MzMzMDQ2Mzg6MTczMzMwODIzOF9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=ZWRjNjhlZGNmMDI3N2RkMGRkZWQ3ZTdhYTI0ZjhjMDhfY05TdzRxb0Q4VW5XZWJpZzNSckNONHdDOHR5amFldmtfVG9rZW46RExGa2Jadjl1b1VhNUF4aFJ0c2NxQXhZbkdRXzE3MzMzMDQ2Mzg6MTczMzMwODIzOF9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=M2ZkNWRmM2NkNmZiMjU2Y2Q4NDRmNmU3NjlkODdhODZfRkZNQ2tYalhHdThqZFVVSVNLOHNtUXRua3pBM284bUZfVG9rZW46SHh6aGJOcU90bzNPZ3R4a0tjNmN1ZEZjblplXzE3MzMzMDQ2Mzg6MTczMzMwODIzOF9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=NzJmZmU2YWI2MDcwMDgyN2E5NjY4ZTY2OTI2MWQwNjdfV2ZWekJlZFBoSkMyT0dnUk9sZTRyQWZKeVNlQTc5bjhfVG9rZW46R3oxaGJVb3E3b1U1UTZ4eFRDemNnaW54bnFmXzE3MzMzMDQ2Mzg6MTczMzMwODIzOF9WNA)

#### 资源负载型

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=MDhhNmFkYjQyY2U5ZjYzODZhYTFkNzI4NWQ1ODkzOTdfamJvUUZCc3U2N3RIaUVwQVVOQlNIZHRadlpPWkRGTmZfVG9rZW46SFZtdmJyT0xUbzNTcVB4OGFFdWNTd3o2bmZjXzE3MzMzMDQ2Mzg6MTczMzMwODIzOF9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=YzEzMGNkOTljMzhhOWExNDM1YzJjYWYxZTQwOTNhYjdfNnNINzZwaXZtMk1XNnNaZ2d6VnhqbndpZ24yOFR2eGhfVG9rZW46QnNzOGJZeE9vb082djJ4dTBsWmNPREJJbmlnXzE3MzMzMDQ2Mzg6MTczMzMwODIzOF9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=YTA1MDA3ZmM4M2ExM2EwZGYwOGJhNzAwOWZmNzMwMDZfODJYdGt3NlhNc0NrWWdBaXUyaWdFWWFqSmZuSjdUTEVfVG9rZW46Q2lDaGJTY2FYb1NzdFR4U1V2YWN1NXBabjBkXzE3MzMzMDQ2Mzg6MTczMzMwODIzOF9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=OGY4MDU2MjY1ZmRhYjc3MTE0NmFlNTNhN2Y0YTQ2MGZfQjVRcFhpbWpvaVUxQktkc0JWcGRjeXBEQ21pcHlVRmJfVG9rZW46S0NWaGJrbEFwb2RGN2t4SG9xN2NlaE9kblNnXzE3MzMzMDQ2Mzg6MTczMzMwODIzOF9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=ZGQ4YmYwYjdjOGMxNGE5NTcxNGYzZDY3OGU4ZjNlZWNfZnpwQmlkMXVkV25IOEx3UlppT3lGeXFFNDR5UnZTQVpfVG9rZW46SjBwYmJ3WTR3b3IwenR4NkZGNWNqdnQ4bnNmXzE3MzMzMDQ2Mzg6MTczMzMwODIzOF9WNA)

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

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=MjU1ZmQ5OTdlZjkzNmE2ZGRmMThlMDRjODA3YmRkYTZfTTNtWFdtM0JXMGNCSGxNNmJpTnZEc3dMVTE4cG9rY0RfVG9rZW46U2NDYmJmamp4b3FhWHV4VmNidWNicEczbllkXzE3MzMzMDQ2Mzg6MTczMzMwODIzOF9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=ZWIwOWQ2ODUyNzgwMjRmMDkwYmY2MDQzNGU4NzdkYzVfcTA2MjdJNUxJTVJ0c2lrRTd0TmhQZ2FKd0ozdnhVSjNfVG9rZW46QXVEUGJJVUFRbzlMUE14QlpDS2N2WUgzbjNiXzE3MzMzMDQ2Mzg6MTczMzMwODIzOF9WNA)

### Nacos

虽然增加了一个单独的注册中心中间件，但是作为与 Dubbo 同公司产品，兼容性更好，后续的项目扩展性更强

## 实例健康监控

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