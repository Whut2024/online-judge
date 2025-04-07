package com.whut.onlinejudge.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author liuqiao
 * @since 2025-04-06
 */
public class GoOutputTest {

    public static void main(String[] args) {
        try {
            String path = "/Users/laowang/developer/codes/online-judge/core/target/1/compiled/1872929013556322306/go/Main.go";
            // 1. 创建ProcessBuilder来执行Go程序
            ProcessBuilder pb = new ProcessBuilder("go", "run", path);
            pb.redirectErrorStream(true); // 合并标准错误和标准输出

            // 2. 启动进程
            Process process = pb.start();

            // 3. 获取进程的输入流(用于向Go程序写入数据)
            OutputStream stdin = process.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(stdin, StandardCharsets.UTF_8));

            // 4. 从控制台读取多行输入（模拟不确定的多行数据）
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入多行内容（输入空行结束）:");

            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.isEmpty()) {
                    break; // 输入空行结束
                }
                writer.write(line);
                writer.newLine(); // 每行结束添加换行符
            }

            // 5. 关闭输入流，表示输入结束
            writer.flush();
            writer.close();

            // 6. 读取Go程序的输出
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));

            System.out.println("\nGo程序输出:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 7. 等待程序结束
            int exitCode = process.waitFor();
            System.out.println("Go程序退出码: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
