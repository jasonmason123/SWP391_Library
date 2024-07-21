package com.springdemo.library.repositories;

import com.springdemo.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.tenUser = :userName")
    Optional<User> findUserByTenUser(@Param("userName") String userName);

    @Query("SELECT u FROM User u WHERE u.Id = :id AND u.tenUser = :userName AND u.flagDel=0")
    Optional<User> findUserByIdAndTenUser(@Param("id") int id ,@Param("userName") String userName);

    @Query("SELECT u FROM User u WHERE u.soDienThoai = :soDienThoai")
    Optional<User> findUserBySoDienThoai(@Param("soDienThoai") String soDienThoai);

    @Query("SELECT u FROM User u WHERE u.soCCCD = :soCCCD")
    Optional<User> findUserBySoCCCD(@Param("soCCCD") String soCCCD);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.flagDel=0")
    Optional<User> findActiveUserByEmail(@Param("email") String email);

    @Query(value = "WITH AllMonths AS ( " +
            "    SELECT 1 AS MonthNum " +
            "    UNION ALL SELECT 2 " +
            "    UNION ALL SELECT 3 " +
            "    UNION ALL SELECT 4 " +
            "    UNION ALL SELECT 5 " +
            "    UNION ALL SELECT 6 " +
            "    UNION ALL SELECT 7 " +
            "    UNION ALL SELECT 8 " +
            "    UNION ALL SELECT 9 " +
            "    UNION ALL SELECT 10 " +
            "    UNION ALL SELECT 11 " +
            "    UNION ALL SELECT 12 " +
            "), " +
            "YearMonths AS ( " +
            "    SELECT MonthNum " +
            "    FROM AllMonths " +
            "    WHERE MonthNum BETWEEN 1 AND MONTH(GETDATE()) " +  // Adjust to the current month
            ") " +
            "SELECT ym.MonthNum AS Thang, COALESCE(COUNT(u.dateCreated), 0) AS SoLuongTao FROM YearMonths ym " +
            "LEFT JOIN [User] u ON ym.MonthNum = MONTH(u.dateCreated) AND YEAR(u.dateCreated) = YEAR(GETDATE()) " +  // Filter by current year
            "GROUP BY ym.MonthNum ORDER BY ym.MonthNum", nativeQuery = true)
    List<Object[]> countUserAccountByMonth();
}
