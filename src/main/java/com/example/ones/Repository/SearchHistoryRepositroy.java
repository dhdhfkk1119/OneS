package com.example.ones.Repository;

import com.example.ones.Entity.Search_history;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepositroy extends JpaRepository<Search_history,Long> {
    List<Search_history> findBySearchUseridx(Long useridx);
}
