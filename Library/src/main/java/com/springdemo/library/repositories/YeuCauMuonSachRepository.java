package com.springdemo.library.repositories;

import com.springdemo.library.model.YeuCauMuonSach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YeuCauMuonSachRepository extends JpaRepository<YeuCauMuonSach, Integer> {
    @Query(value = "SELECT y.* FROM YeuCauMuonSach y WHERE (y.NgayTra - GETDATE()) = 3", nativeQuery = true)
    List<YeuCauMuonSach> findYeuCauWhereDueDateIsIn3Days();

    @Query(value = "SELECT y.* FROM YeuCauMuonSach y WHERE y.NgayTra = GETDATE()", nativeQuery = true)
    List<YeuCauMuonSach> findYeuCauWhereDueDateIsToday();

    @Query(value = "SELECT y.* FROM YeuCauMuonSach y WHERE y.NgayTra < GETDATE()", nativeQuery = true)
    List<YeuCauMuonSach> findAllOverdueYeuCau();
}
