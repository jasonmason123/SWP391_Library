package com.springdemo.library.repositories;

import com.springdemo.library.model.BinhLuanBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinhLuanBlogRepository extends JpaRepository<BinhLuanBlog, Integer> {
}
