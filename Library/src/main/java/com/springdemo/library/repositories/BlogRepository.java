package com.springdemo.library.repositories;

import com.springdemo.library.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {

    void deleteById(Integer id);
    List<Blog> findByFlagDel(int flagDel);
    List<Blog> findByFlagDelIn(List<Integer> flagDel);
    @Query("WITH AllMonths AS ( " +
            "SELECT 1 AS MonthNum " +
            "UNION ALL SELECT 2 " +
            "UNION ALL SELECT 3 " +
            "UNION ALL SELECT 4 " +
            "UNION ALL SELECT 5 " +
            "UNION ALL SELECT 6 " +
            "UNION ALL SELECT 7 )" +
            "SELECT am.MonthNum AS Thang, " +
            "COUNT(b.ngayTao) AS SoLuotTao " +
            "FROM AllMonths am " +
            "LEFT JOIN Blog b ON am.MonthNum = MONTH(b.ngayTao) " +
            "GROUP BY am.MonthNum " +
            "ORDER BY am.MonthNum")
    List<Object[]> countBlogByMonth();
}
