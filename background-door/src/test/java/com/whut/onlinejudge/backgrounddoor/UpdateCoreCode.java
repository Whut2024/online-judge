package com.whut.onlinejudge.backgrounddoor;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whut.onlinejudge.common.model.entity.Question;
import com.whut.onlinejudge.common.model.enums.LanguageEnum;
import com.whut.onlinejudge.common.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;




@SpringBootTest
public class UpdateCoreCode {

    @Autowired
    private QuestionService qs;

    @Test
    void update() {
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Question::getId, Question::getCoreCode);
        final List<Question> questionList = qs.list(wrapper);

        questionList.forEach(q -> {
            final String origin = q.getCoreCode();
            final JSONObject j = new JSONObject(origin);
            /*j.set(LanguageEnum.JAVA.getName(), new JSONObject(new JSONObject(origin).get("java")).get("java"));*/
            String goCode = "package main\n" +
                    "\n" +
                    "import (\n" +
                    "\t\"bufio\"\n" +
                    "\t\"fmt\"\n" +
                    "\t\"io\"\n" +
                    "\t\"os\"\n" +
                    ")\n" +
                    "\n" +
                    "func main() {\n" +
                    "\t// 创建读取器来读取标准输入\n" +
                    "\treader := bufio.NewReader(os.Stdin)\n" +
                    "\n" +
                    "\t// 持续读取直到EOF（Java关闭输入流时会触发EOF）\n" +
                    "\tfor {\n" +
                    "\t\tline, err := reader.ReadString('\\n')\n" +
                    "\t\tif err != nil {\n" +
                    "\t\t\tif err == io.EOF {\n" +
                    "\t\t\t\tbreak // 输入结束\n" +
                    "\t\t\t}\n" +
                    "\t\t\tfmt.Println(\"读取错误:\", err)\n" +
                    "\t\t\treturn\n" +
                    "\t\t}\n" +
                    "\n" +
                    "\t\t// 处理每行输入（去掉末尾的换行符）\n" +
                    "\t\tline = line[:len(line)-1]\n" +
                    "\t\tfmt.Println(line)\n" +
                    "\n" +
                    "\t}\n" +
                    "\n" +
                    "\tfmt.Println(10)\n" +
                    "\tfmt.Println(20)\n" +
                    "\tfmt.Println(true)\n" +
                    "\n" +
                    "}\n";
            j.set(LanguageEnum.GO.getName(), goCode);
            q.setCoreCode(j.toJSONString(1));
            System.out.println(q.getCoreCode());
        });

        qs.updateBatchById(questionList);
    }
}
