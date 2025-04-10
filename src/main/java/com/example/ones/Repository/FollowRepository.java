package com.example.ones.Repository;

import com.example.ones.Entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    Optional<Follow> findByFollowLoginidxAndFollowUseridx (Long loginidx , Long userid);
}
