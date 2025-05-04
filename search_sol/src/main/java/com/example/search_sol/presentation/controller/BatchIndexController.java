package com.example.search_sol.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.UUID;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchIndexController {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final DataSource dataDbSource;

    @GetMapping
    public String test() throws Exception {
        System.out.println("현재 데이터 소스: " + dataDbSource);
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("param", UUID.randomUUID().toString())
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("migrationToEsJob"), jobParameters);
        return "배치 테스트 확인";
    }
}
