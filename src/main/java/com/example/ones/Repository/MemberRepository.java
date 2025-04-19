package com.example.ones.Repository;

import com.example.ones.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    @Query("SELECT p FROM Member p WHERE " +
            "(p.userName LIKE %:keyword% OR p.introduce LIKE %:keyword%)" +
            "ORDER BY " +
            "p.userAt DESC")
    List<Member> findBySearchKeyword(@Param("keyword") String keyword);

    Optional<Member> findByIdx(Long id); // Long 타입을 받는 메서드 추가

    Optional<Member> findByUserId(String userid); // 유저의 로그인한 ID 가져오기


    boolean existsByUserId(String userid); // 회원 가입 할때 체크
    boolean existsByUserPhoneNumber(String phoneNumber);  // 회원 가입 할때 체크
    boolean existsByUserEmail(String email);  // 회원 가입 할때 체크
    boolean existsByUserName(String username);  // 회원 가입 할때 체크 
}
