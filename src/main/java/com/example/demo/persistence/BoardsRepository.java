package com.example.demo.persistence;

import com.example.demo.domain.Boards;
import org.hibernate.sql.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardsRepository extends JpaRepository<Boards,Long> {
    @Query("SELECT b FROM Boards b ORDER BY b.no DESC")
    Page<Boards> findAllDesc(Pageable pageable);

    // 인자로 받아온 년도, 월과 Boards 테이블의 create_dates 컬럼의 년도, 월 같은 튜플 중 no 컬럼의 카운트하는 쿼리메소드
    // (현재 달 게시판 수)
    @Query("select count(b.no) from Boards b where month(b.createDates)= :month and year(b.createDates)= :year")
    long countCurrentMonthBorads(int month,int year);

    Page<Boards> findByTitleContainingIgnoreCaseOrderByNoDesc(String searchKeyword, Pageable pageable);

    Page<Boards> findByContentContainingIgnoreCaseOrderByNoDesc(String searchKeyword, Pageable pageable);

    Page<Boards> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByNoDesc(String searchTitle,String searchContent, Pageable pageable);

    Page<Boards> findByWriterContainingIgnoreCaseOrderByNoDesc(String searchKeyword, Pageable pageable);
}
