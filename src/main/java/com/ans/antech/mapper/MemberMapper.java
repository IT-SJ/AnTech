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

    // 이메일로 회원 조회
    public Member selectMemberByEmail(@Param("email") String email);
    
    // ID와 EMAIL로 사용자 조회
    public Member findMemberByIdAndEmail(@Param("id") String id, @Param("email") String email);

    // 비밀번호 찾기 -> 비밀번호 업데이트
    public void updatePassword(@Param("id") String id, @Param("newPassword") String newPassword);

    // 프로필 이미지 수정
    public int profileUpdate(@Param("userId") String userId, @Param("imagePath") String imagePath);

}
