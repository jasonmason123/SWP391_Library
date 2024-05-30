package com.springdemo.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BinhLuanBlog")
public class BinhLuanBlog {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @ManyToOne
    @JoinColumn(name = "BlogId")
    private Blog blog;
    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;
    @Column(name = "NoiDung")
    private String noiDung;
    @Column(name = "NgayTao")
    private Date ngayTao;
}
