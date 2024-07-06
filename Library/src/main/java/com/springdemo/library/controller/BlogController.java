package com.springdemo.library.controller;

import com.springdemo.library.model.Blog;
import com.springdemo.library.model.Tag;
import com.springdemo.library.model.User;
import com.springdemo.library.repositories.BlogRepository;
import com.springdemo.library.repositories.TagRepository;
import com.springdemo.library.services.GenerateViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping
public class BlogController {
    private BlogRepository blogRepository;
    private TagRepository tagRepository;
    private GenerateViewService generateViewService;
    private final List<Blog> blogList = new ArrayList<>();
    @GetMapping("/blog")
    public ModelAndView blog(
            @RequestParam(name = "blog", required = false) Integer blogId,
            @RequestParam(name = "search", required = false) String searchString,
            @RequestParam(name = "tag", required = false) List<Integer> tagIdParams,
            @RequestParam(name = "tacgia", required = false) Integer tacGiaIdParam,
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
                if(tacGiaIdParam==null) {
                    filteredBlogList = new ArrayList<>(blogList);
                } else {
                    filteredBlogList = blogList.stream().filter(blog -> blog.getTacGia().getId()==tacGiaIdParam).toList();
                    modelClass.put("chosenTacGiaId", tacGiaIdParam);
                }
                if(tagIdParams!=null && !tagIdParams.isEmpty()) {
                    filteredBlogList = filteredBlogList.stream()
                            .filter(blog -> blog.getTags().stream()
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
            List<User> top7TacGia = blogRepository.findTopTacGiaWithHighestAverageDanhGia(PageRequest.of(0,7));

            modelClass.put("blogList", blogListPaged);
            modelClass.put("tagList", tagList);
            modelClass.put("totalPages", totalPages);
            modelClass.put("dateList", getPastMonths(5));
            modelClass.put("topTacGia", top7TacGia);
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
            String breadCrumb =  """
            <ul>
                <li><a href="/Library/home">Trang chủ</a></li>
                <li><a href="/Library/blog">Blog</a></li>
                <li><a href="#" class="active">""" + blog.getTieuDe() + """
            </a></li>
            </ul>""";
            ModelAndView blogDetailViewModel = generateViewService.generateCustomerView(blog.getTieuDe(), breadCrumb, "blog-details", authentication);
            blogDetailViewModel.addObject("modelClass", blog);
            return blogDetailViewModel;
        }
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
