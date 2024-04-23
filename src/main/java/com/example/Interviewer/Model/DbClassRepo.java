package com.example.Interviewer.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbClassRepo extends JpaRepository<DbClass,Long> {
}
