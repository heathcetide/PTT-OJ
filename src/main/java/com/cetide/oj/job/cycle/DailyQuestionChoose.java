package com.cetide.oj.job.cycle;

import cn.hutool.core.util.RandomUtil;
import com.cetide.oj.mapper.DailyQuestionMapper;
import com.cetide.oj.model.entity.DailyQuestion;
import com.cetide.oj.model.entity.Post;
import com.cetide.oj.model.entity.Question;
import com.cetide.oj.service.PostService;
import com.cetide.oj.service.QuestionService;
import com.cetide.oj.utils.WordCloudUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class DailyQuestionChoose {

    @Resource
    private QuestionService questionService;

    @Resource
    private DailyQuestionMapper dailyQuestionMapper;

    @Resource
    private PostService postService;

//    @Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "0/5 * * * * ?")
    public void executeTask() {
        List<Question> list = questionService.list();

        // 直接生成一个随机索引
        Random random = new Random();
        int randomIndex = random.nextInt(list.size());

        // 使用随机索引获取问题对象
        Question selectedQuestion = list.get(randomIndex);
        List<Post> postList = postService.list();
        List<String> listStrings = new ArrayList<>();
        for (Post post : postList) {
            listStrings.add(post.getContent());
        }
        String generate = WordCloudUtil.generate(RandomUtil.randomNumbers(10), listStrings);
        // 创建 DailyQuestion 对象并插入数据库
        DailyQuestion dailyQuestion = new DailyQuestion();
        dailyQuestion.setQuestion_id(selectedQuestion.getId());
        dailyQuestion.setDate(new Date());
        dailyQuestion.setQuestion_img(generate);
        dailyQuestionMapper.insert(dailyQuestion);
    }
}
