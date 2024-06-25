package com.springdemo.library.repositories;

import com.springdemo.library.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {

    void deleteById(Integer id);
    List<Blog> findByFlagDel(int flagDel);
    List<Blog> findByFlagDelIn(List<Integer> flagDel);
}
