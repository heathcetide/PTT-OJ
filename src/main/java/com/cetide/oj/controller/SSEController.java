package com.cetide.oj.controller;

import com.cetide.oj.common.BaseResponse;
import com.cetide.oj.common.ResultUtils;
import com.cetide.oj.service.LeaderboardService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.Set;

@RestController
public class SSEController {

    @Resource
    private LeaderboardService leaderboardService;

    // 每隔5秒推送一次数据
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BaseResponse<Set<String>>> streamData() {
        return Flux.interval(Duration.ofSeconds(5))
                .map(sequence -> {
                    Set<String> topN = leaderboardService.getTopN(10);
                    System.out.println("数据来了"+topN);
                    return ResultUtils.success(topN);
                });
    }

}