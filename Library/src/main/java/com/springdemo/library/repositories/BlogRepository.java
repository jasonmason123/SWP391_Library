package com.springdemo.library.repositories;

import com.springdemo.library.model.Blog;
import com.springdemo.library.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {

    void deleteById(int id);
    List<Blog> findByFlagDel(int flagDel);
    List<Blog> findByFlagDelIn(List<Integer> flagDel);
    @Query("SELECT b FROM Blog b WHERE b.flagDel = :flagDel AND b.tacGia.Id = :tacGiaId")
    List<Blog> findByFlagDelAndTacGia(@Param("flagDel")int flagDel, @Param("tacGiaId") int tacGiaId);
    @Query("SELECT b.tacGia FROM Blog b GROUP BY b.tacGia ORDER BY AVG(b.danhGia) DESC")
    List<User> findTopTacGiaWithHighestAverageDanhGia(Pageable pageable);
    @Query("SELECT b FROM Blog b WHERE b.Id = :id AND b.flagDel = :flagDel")
    Optional<Blog> findByIdAndFlagDel(@Param("id") Integer id, @Param("flagDel") Integer flagDel);
    @Query("SELECT b FROM Blog b WHERE b.Id = :id AND b.tacGia.Id = :tacGiaId AND b.flagDel=-1")
    Optional<Blog> findDraftByIdAndTacGiaId(@Param("id") Integer id, @Param("tacGiaId") Integer tacGiaId);
}
