package com.example.demo.persistence;

import com.example.demo.domain.Boards;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardsRepository extends JpaRepository<Boards,Long> {
    @Query("SELECT b FROM Boards b ORDER BY b.no DESC")
    Page<Boards> findAllDesc(Pageable pageable);



    Page<Boards> findByTitleContainingIgnoreCaseOrderByNoDesc(String searchKeyword, Pageable pageable);

    Page<Boards> findByContentContainingIgnoreCaseOrderByNoDesc(String searchKeyword, Pageable pageable);

    Page<Boards> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByNoDesc(String searchTitle,String searchContent, Pageable pageable);

    Page<Boards> findByWriterContainingIgnoreCaseOrderByNoDesc(String searchKeyword, Pageable pageable);
}
