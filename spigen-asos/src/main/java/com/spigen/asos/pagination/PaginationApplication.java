package com.spigen.asos.pagination;


import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.spigen.asos.model.Data5;
import com.spigen.asos.repository.PageRepository;

//@SpringBootApplication
public class PaginationApplication {

//    public static void main(String[] args) {
//        SpringApplication.run(PaginationApplication.class, args);
//    }

    /**
     * 154 개의 사용자와 게시물을 생성
     * @param userRepository
     * @param boardRepository
     * @return
     */
    @Bean
    public CommandLineRunner initData(PageRepository pageRepository) {
        return args -> 
            IntStream.rangeClosed(1, 150).forEach(i -> {
            	
                Data5 board = Data5.builder()

                        .name("tester" + i)
                        .description("desc" + i)
                        .build();
                
                //pageRepository.save(board);
            });
    }
}