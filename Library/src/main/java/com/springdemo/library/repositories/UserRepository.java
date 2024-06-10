package com.springdemo.library.repositories;

import com.springdemo.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.tenUser = :userName")
    Optional<User> findUserByTenUser(@Param("userName") String userName);

    @Query("SELECT u FROM User u WHERE u.Id = :id AND u.tenUser = :userName")
    Optional<User> findUserByIdAndTenUser(@Param("id") int id ,@Param("userName") String userName);

    @Query("SELECT u FROM User u WHERE u.soDienThoai = :soDienThoai")
    Optional<User> findUserBySoDienThoai(@Param("soDienThoai") String soDienThoai);

    @Query("SELECT u FROM User u WHERE u.soCCCD = :soCCCD")
    Optional<User> findUserBySoCCCD(@Param("soCCCD") String soCCCD);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);
}
