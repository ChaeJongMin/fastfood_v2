package com.example.demo.persistence;

import com.example.demo.domain.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardsRepository extends JpaRepository<Boards,Long> {
    @Query("SELECT b FROM Boards b ORDER BY b.no DESC")
    List<Boards> findAllDesc();
}
