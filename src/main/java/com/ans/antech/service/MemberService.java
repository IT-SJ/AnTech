package com.ans.antech.service;

import org.springframework.stereotype.Service;

import com.ans.antech.mapper.MemberMapper;
import com.ans.antech.model.Member;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MemberService {
    @Autowired
    private MemberMapper memberMapper;

    // 회원가입
    public String registerMember(Member member) {
        int result = memberMapper.insertMember(member);
        return (result > 0) ? "회원가입 성공!" : "회원가입 실패!";
    }

    // 특정 회원 조회
    public Member getMemberById(String id) {
        return memberMapper.selectMemberById(id);
    }

    // 모든 회원 조회
    public ArrayList<Member> getAllMembers() {
        return memberMapper.selectAllMembers();
    }

}
