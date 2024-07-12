package com.springdemo.library.controller;

import com.springdemo.library.model.*;
import com.springdemo.library.model.dto.BinhLuanDto;
import com.springdemo.library.repositories.BinhLuanBlogRepository;
import com.springdemo.library.repositories.BlogRepository;
import com.springdemo.library.repositories.TagRepository;
import com.springdemo.library.security.userdetails.CustomUserDetails;
import com.springdemo.library.services.GenerateViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/blog")
public class BlogController {
    private BlogRepository blogRepository;
    private BinhLuanBlogRepository binhLuanBlogRepository;
    private TagRepository tagRepository;
    private GenerateViewService generateViewService;
    private final List<Blog> blogList = new ArrayList<>();
    @GetMapping
    public ModelAndView blog(
            @RequestParam(name = "blog", required = false) Integer blogId,
            @RequestParam(name = "search", required = false) String searchString,
            @RequestParam(name = "tag", required = false) List<Integer> tagIdParams,
            @RequestParam(name = "date", required = false) String dateParam,
            @RequestParam(name = "page", required = false) Integer pageNumberParam,
            Authentication authentication
    ) {
        if(blogId==null) {
            int pageNumber = pageNumberParam!=null ? pageNumberParam-1 : 0;
            int pageSize = 5;
            int maxPagesToShow = 5;

            if(blogList.isEmpty()) {
                blogList.addAll(blogRepository.findByFlagDel(0));
            }

            Map<String, Object> modelClass = new HashMap<>();

            List<Blog> filteredBlogList;
            if(searchString!=null && !searchString.isEmpty()) {
                filteredBlogList = blogList.stream().filter(blog ->
                    (blog.getTieuDe().contains(searchString)) || (blog.getTacGia().getTenUser().contains(searchString))
                ).toList();
                modelClass.put("searchString", searchString);
            } else {
                if(tagIdParams==null) {
                    filteredBlogList = new ArrayList<>(blogList);
                } else {
                    filteredBlogList = blogList.stream().filter(blog -> blog.getTags().stream()
                            .anyMatch(tag -> tagIdParams.contains(tag.getId()))).toList();
                    modelClass.put("chosenTagIdList", tagIdParams);
                }
                if(dateParam!=null && !dateParam.isEmpty()) {
                    YearMonth yearMonth = YearMonth.parse(dateParam, DateTimeFormatter.ofPattern("MM-yyyy"));
                    filteredBlogList = filteredBlogList.stream()
                            .filter(blog -> {
                                YearMonth blogYearMonth = YearMonth.of(
                                        blog.getNgayTao().getYear() + 1900,
                                        blog.getNgayTao().getMonth() + 1
                                );
                                return blogYearMonth.equals(yearMonth);
                            }).toList();
                    modelClass.put("chosenDate", dateParam);
                }
            }

            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Blog> blogListPaged = generateViewService.generatePagedList(filteredBlogList, pageable);
            List<Tag> tagList = tagRepository.findAll();
            int totalPages = blogListPaged.getTotalPages();
            int totalItems = (int) blogListPaged.getTotalElements();
            int startItem = pageNumber * pageSize + 1;
            int endItem = Math.min(startItem + pageSize - 1, totalItems);

            String breadCrumb =  """
            <ul>
                <li><a href="/Library/home">Trang chủ</a></li>
                <li><a href="/Library/blog" class="active">Blog</a></li>
            </ul>""";
            ModelAndView blogViewModel = generateViewService.generateCustomerView("Blog", breadCrumb, "blog", authentication);

            modelClass.put("blogList", blogListPaged);
            modelClass.put("tagList", tagList);
            modelClass.put("totalPages", totalPages);
            modelClass.put("dateList", getPastMonths(5));
            modelClass.put("currentPage", pageNumber);
            modelClass.put("pageNumbers", generateViewService.generatePageNumbers(pageNumber, totalPages, maxPagesToShow));
            modelClass.put("startItem", startItem);
            modelClass.put("endItem", endItem);
            modelClass.put("totalItems", totalItems);
            blogViewModel.addObject("modelClass", modelClass);
            return blogViewModel;
        } else {
            Blog blog = blogRepository.findByIdAndFlagDel(blogId, 0).orElse(null);
            if(blog==null) {
                return new ModelAndView("redirect:/error");
            }
            List<BinhLuanBlog> binhLuanBlogList = blog.getBinhLuanBlogList().stream().filter(x -> x.getNoiDung()!=null && !x.getNoiDung().isEmpty()).toList();
            String breadCrumb =  """
            <ul>
                <li><a href="/Library/home">Trang chủ</a></li>
                <li><a href="/Library/blog">Blog</a></li>
                <li><a href="#" class="active">""" + blog.getTieuDe() + """
            </a></li>
            </ul>""";
            Map<String, Object> modelClass = new HashMap<>();
            modelClass.put("blog", blog);
            modelClass.put("binhLuanBlogList", binhLuanBlogList);
            ModelAndView blogDetailViewModel = generateViewService.generateCustomerView(blog.getTieuDe(), breadCrumb, "blog-details", authentication);
            blogDetailViewModel.addObject("modelClass", modelClass);
            return blogDetailViewModel;
        }
    }

    @PostMapping("/comment")
    public ResponseEntity<String> addComment(
            @RequestBody BinhLuanDto binhLuanDto,
            Authentication authentication
    ) {
        try {
            User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
            Blog blog = blogRepository.findById(binhLuanDto.getPostId()).orElse(null);
            if(blog!=null) {
                BinhLuanBlog binhLuanBlog = BinhLuanBlog.builder().blog(blog).user(user)
                        .noiDung(binhLuanDto.getNoiDung()).ngayTao(new Date()).build();
                binhLuanBlogRepository.save(binhLuanBlog);
                int soLuotDanhGia = blog.getSoLuotDanhGia();
                blog.setDanhGia((blog.getDanhGia() * soLuotDanhGia + binhLuanDto.getDanhGia()) / (soLuotDanhGia + 1));
                blog.setSoLuotDanhGia(soLuotDanhGia + 1);
                blogRepository.save(blog);
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }

    private List<String> getPastMonths(int numberOfMonths) {
        List<String> monthsList = new ArrayList<>();
        YearMonth currentYearMonth = YearMonth.now();
        for (int i = 0; i < numberOfMonths; i++) {
            YearMonth pastYearMonth = currentYearMonth.minusMonths(i);
            LocalDate pastDate = pastYearMonth.atDay(1); // First day of the month
            String formattedMonth = pastDate.format(DateTimeFormatter.ofPattern("MM-yyyy"));
            monthsList.add(formattedMonth);
        }
        return monthsList;
    }
}
