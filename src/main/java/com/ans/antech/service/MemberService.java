package com.ans.antech.service;

import org.springframework.stereotype.Service;

import com.ans.antech.mapper.MemberMapper;
import com.ans.antech.model.Member;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MemberService {
    @Autowired
    private MemberMapper mapper;

    // 회원가입
    public void registerMember(Member member) {
        mapper.insertMember(member);
    }

    // ID로 회원 검색
    public Member getMemberById(String id) {
        return mapper.selectMemberById(id);
    }

    // 모든 회원 조회
    public ArrayList<Member> getAllMembers() {
        return mapper.selectAllMembers();
    }


}
