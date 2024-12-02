#!/bin/sh

# 获取对应参数
# 源代码 存储路径 编译后保存路径
SOURCE_CODE=$1
SOURCE_CODE_PATH=$2
COMPILED_CODE_PATH=$3

echo "$SOURCE_CODE"

# 存储源代码
echo "$SOURCE_CODE" > "$SOURCE_CODE_PATH"

# 编译源代码
javac -d "$COMPILED_CODE_PATH" -encoding utf-8 "$SOURCE_CODE_PATH"

# 删除源代码文件
#rm -f "$SOURCE_CODE_PATH"