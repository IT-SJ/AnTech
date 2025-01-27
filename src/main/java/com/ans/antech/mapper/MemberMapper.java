package com.ans.antech.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ans.antech.model.Member;

@Mapper
public interface MemberMapper {

    // 회원 추가
    public void insertMember(Member member);

    // ID로 회원 조회
    public Member selectMemberById(@Param("id") String id);

    // 모든 회원 조회
    public ArrayList<Member> selectAllMembers();

    // ID와 PW로 사용자 조회
    public Member selectMemberByIdAndPw(@Param("id") String id, @Param("pw") String pw);

}
