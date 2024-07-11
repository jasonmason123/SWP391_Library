package com.springdemo.library.model.dto;
import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogDto {
    private int id;
    private int tacGiaId;
    private String tieuDe;
    private String noiDung;
    private int danhGia;
    private Date ngayTao;
    private int flagDel;
    private List<Integer> tagIds;
    private List<String> binhLuanBlogs;
}