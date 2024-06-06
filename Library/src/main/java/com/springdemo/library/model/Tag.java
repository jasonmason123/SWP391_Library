package com.springdemo.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
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
    @Setter(AccessLevel.NONE)
    @Column(name = "DateCreated")
    private Date dateCreated;
    @Column(name = "DateUpdated")
    private Date dateUpdated;

    @ManyToMany(mappedBy = "tags")
    private List<Blog> blogs;

    @Builder
    public Tag(String tenTag, Date dateCreated) {
        this.tenTag = tenTag;
        this.dateCreated = dateCreated;
    }
}
