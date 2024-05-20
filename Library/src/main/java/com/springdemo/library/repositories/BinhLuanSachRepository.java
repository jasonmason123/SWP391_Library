package com.springdemo.library.repositories;

import com.springdemo.library.model.BinhLuanSach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinhLuanSachRepository extends JpaRepository<BinhLuanSach, Integer> {
}
