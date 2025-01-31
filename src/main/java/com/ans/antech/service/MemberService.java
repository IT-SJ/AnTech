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

    // ID와 PW로 사용자 조회
    public Member login(String id, String pw) {
        return mapper.selectMemberByIdAndPw(id, pw);
    }

    // 이메일로 회원 조회
    public Member findMemberByEmail(String email) {
        return mapper.selectMemberByEmail(email);
    }

    // id와 email로 사용자 조회
    public Member findMemberByIdAndEmail(String id, String email) {
        return mapper.findMemberByIdAndEmail(id, email);
    }

    // 비밀번호 찾기 -> 비밀번호 업데이트
    public void updatePassword(String id, String newPassword) {
        mapper.updatePassword(id, newPassword);
    }

    // 프로필 이미지 업데이트
    public boolean updateProfileImage(String userId, String imagePath) {
        return mapper.updateProfileImage(userId, imagePath) > 0;
    }
}
