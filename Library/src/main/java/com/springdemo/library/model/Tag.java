package com.springdemo.library.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Tag")
public class Tag {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(name = "TenTag")
    private String tenTag;

    @ManyToMany(mappedBy = "tags")
    private List<Blog> blogs;

    @Builder
    public Tag(String tenTag) {
        this.tenTag = tenTag;
    }
}
