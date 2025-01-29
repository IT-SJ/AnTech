package com.ans.antech.service;

import org.springframework.stereotype.Service;

import com.ans.antech.mapper.NewsMapper;
import com.ans.antech.model.Member;
import com.ans.antech.model.News;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;


@Service
public class NewsService {
    @Autowired
    private NewsMapper mapper;

    // 모든 회원 조회
    public ArrayList<News> getAllNews() {
        return mapper.selectTitle();
    }    
}
