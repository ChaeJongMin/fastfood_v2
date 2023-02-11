package com.example.demo.persistence;

import com.example.demo.domain.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardsRepository extends JpaRepository<Boards,Long> {

}
