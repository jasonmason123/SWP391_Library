package com.springdemo.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinhLuanBlog extends JpaRepository<BinhLuanBlog, Integer> {
}
