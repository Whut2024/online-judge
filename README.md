# 整体架构

暂时无法在飞书文档外展示此内容

# 核心流程

## docker 代码运行集群

### 基本要求

- 单独启动一个进程去监控每个实例的健康状况
- 每个实例最多同时运行 50 个进程
- 每个 Docker 服务器最多运行 3 个实例
- 当项目启动时，默认开启一个 Docker 服务器的全部实例
- 当一个实例异常后在有余量的 Docker 服务器上开启新的实例，并且邮箱告警
- 可以快速的获取负载最小的实例

Redis 会作为数据中心，存储每个实例的地址，负载信息和运行状况

### 监控的实现

单独的进程会给每个实例发送一个执行输出 Hello World 的命令

如果超时或者返回错误则认为实例错误，尝试在别的 Docker 服务器上开启一台新的实例

！！现在的监控节点是单机，可能存在网络拥塞的问题

### 负载均衡实现

当前采用监控集群 java 进程数量的方式去进行负载均衡

采用 Redis 的 sorted_set 方式去存储

member-实例 ID

score-java 进程数

将地址编号，负载信息和运行状况压缩为一个 long 字段作为 score，其中的负载信息为高位

暂时无法在飞书文档外展示此内容

## 提交题目数据流

暂时无法在飞书文档外展示此内容

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
    `content`           TEXT         NOT NULL COMMENT "题目内容",
    `judge_case`        TEXT         NOT NULL COMMENT "运行测试案例",
    `core_code`         TEXT         NOT NULL COMMENT "运行校验代码",
    `judge_config`      VARCHAR(128) NOT NULL COMMENT "题目运行资源限制",
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

# 模块

## 限流模块

## Redis 分布式锁

## 多编程语言策略

## 负载均衡逻辑

每个实例对象会以管理这个实例的 Docker 服务器上启动的代码执行后台服务 id 作为实例名字的一部分，这一部分也会作为 Redis 的 Sorted_Set 中的 member 存储下来，需要时会选择负载最小的实例，返回给门户后台服务器，服务器获取到这个实例的名字后可以知道应该调用哪一个代码执行后台服务器，代码执行服务器拿到这个名字后也可以知道使用 Docker 服务器中的哪一个实例

暂时无法在飞书文档外展示此内容

## Docker 实例池化

## Dubbo 注册中心选择

### Redis

负载均衡的数据来源为 Redis，在配置难度相同的情况下选择 Redis 可以减少项目中中间件的数量，简化开发难度，但是测试后发现非常严重的问题，这种注册中心的数据存储是 K-V 型，单独值单独键，没有层次结构，导致一个 Key 存储的数据量比较大，在服务的发现过程中会存在 BigKey 问题，但是这个 Key 的使用频率不高

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=Y2YxMDVkNjAwN2U4OGY4OWQwMjMzOTExMzc2MGQ4ZjNfYnJLNWRhUEhRM0hKRWJsRG9oejJiVzh1b2QyMUJ0WXZfVG9rZW46U2NDYmJmamp4b3FhWHV4VmNidWNicEczbllkXzE3MzMyMDEzMDQ6MTczMzIwNDkwNF9WNA)

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=Yzk5ZGYzMjMxODQ4MTEwZjJjY2NhYmJmYzQ5M2Q5ZDhfWmNqaXkxRTJsSzV1T2pEZE9xbmdMN2swbUwwY2VnMllfVG9rZW46QXVEUGJJVUFRbzlMUE14QlpDS2N2WUgzbjNiXzE3MzMyMDEzMDQ6MTczMzIwNDkwNF9WNA)

### Nacos

虽然增加了一个单独的注册中心中间件，但是作为与 Dubbo 同公司产品，兼容性更好，后续的项目扩展性更强

## 实例健康监控

## Docker 代码客户端交互

- 创建实例
- 提交代码
- 文件保存
- 代码执行

引导文件类和 Solution 类会独立成一个特定文件夹下面的两个文件，方便管理

![img](https://h1nj7u0kkda.feishu.cn/space/api/box/stream/download/asynccode/?code=OThlYjdjN2EyZTg1N2EzZjQyZDkzNGY4N2Y4MGVjMzFfbHNibkJGcHJjZ09jdWNDN2lGVEtRQUhMVlBuSzJ2TzVfVG9rZW46SmQ4V2JVS0s0b1hSUXB4Y3AzNGNNcnZKbmVoXzE3MzMyMDEzMDQ6MTczMzIwNDkwNF9WNA)