package com.example.tv360.controller.user;

import com.example.tv360.dto.CastDTO;
import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.dto.MediaDTO;
import com.example.tv360.dto.MediaDetailDTO;
import com.example.tv360.dto.response.CastItem;
import com.example.tv360.dto.response.CategoryItem;
import com.example.tv360.dto.response.MediaDetailResponse;
import com.example.tv360.entity.Media;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.repository.MediaRepository;
import com.example.tv360.service.CategoryService;
import com.example.tv360.service.MediaDetailService;
import com.example.tv360.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/movie/detail")
public class UserMediaDetailController {

    @Value("${page.size}")
    private int pageSize;

    private final CategoryService categoryService;
    private final MediaDetailService mediaDetailService;
    private final MediaService mediaService;
    private final MediaRepository mediaRepository;
    private final MediaDetailRepository mediaDetailRepository;

    public UserMediaDetailController(CategoryService categoryService, MediaDetailRepository mediaDetailRepository , MediaDetailService mediaDetailService, MediaService mediaService, MediaRepository mediaRepository) {
        this.categoryService = categoryService;
        this.mediaDetailRepository = mediaDetailRepository;
        this.mediaDetailService = mediaDetailService;
        this.mediaService = mediaService;
        this.mediaRepository = mediaRepository;
    }

    @GetMapping("/media-detail/create")
    public String showCreateMediaDetail(Model model){
        List<MediaDTO> mediaList = mediaService.getAllMedias();
        model.addAttribute("mediaList", mediaList);

        model.addAttribute("mediaDetailDTO", new MediaDetailDTO());
        return "admin_media_detail_form";
    }

    @PostMapping("/media-detail/create/save")
    public String createMediaDetail(@ModelAttribute MediaDetailDTO mediaDetailDTO, RedirectAttributes redirectAttributes) {
        try {
            mediaDetailService.createMediaDetail(mediaDetailDTO);
            redirectAttributes.addFlashAttribute("success", "Create successfully!");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to create!");
        }
        return "redirect:/admin/media-details";
    }

    @GetMapping("/media-detail/update/{id}")
    public String showUpdateMediaDetail(@PathVariable Long id, Model model){
        MediaDetailDTO mediaDetailDTO = mediaDetailService.getMediaDetailById(id);
        model.addAttribute("mediaDetailDTO", mediaDetailDTO);

        List<MediaDTO> mediaList = mediaService.getAllMedias();
        model.addAttribute("mediaList", mediaList);

        if (mediaDetailDTO == null){
            return "redirect:/admin/media-details";
        }

        return "admin_media_detail_form";
    }

    @PostMapping("/media-detail/update/{id}")
    public String updateMediaDetail(@PathVariable Long id, @ModelAttribute("mediaDetailDTO") MediaDetailDTO mediaDetailDTO, RedirectAttributes attributes){
        try {
            mediaDetailService.updateMediaDetail(id, mediaDetailDTO);
            attributes.addFlashAttribute("success", "Update Successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update");
        }
        return "redirect:/admin/media-details";
    }

    @GetMapping("/media-detail/delete/{id}")
    public ResponseEntity<String> deleteMediaDetail(@PathVariable Long id){
        try {
            mediaDetailService.softDeleteMediaDetail(id);
            return ResponseEntity.ok("Delete media detail successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error!");
        }
    }

    // media detail
//    @GetMapping("/{mediaId}")
//    public String getMediaDetailByIdASCEpisodes(@PathVariable Long mediaId,
//                                                @RequestParam(name = "episode", required = false) Integer episode,
//                                                Model model) {
//        List<CategoryDTO> categories = mediaDetailService.getCategoriesByMediaDetailId(mediaId);
//        model.addAttribute("categoriesWithMovie", categories);
//        try {
//            List<MediaDetailResponse> mediaDetails = mediaDetailService.getMediaDetailByIdASCEpisodes(mediaId);
//
//            if (!mediaDetails.isEmpty()) {
//                // Lấy record đầu tiên của MediaDetail
//                MediaDetailResponse firstMediaDetail = mediaDetails.get(0);
//
//                model.addAttribute("firstMediaDetail", firstMediaDetail);
//                model.addAttribute("mediaDetails", mediaDetails);
//            } else {
//                return null;
//            }
//            return "user_movie_detail";
//        } catch (Exception e) {
//            // Xử lý lỗi nếu cần
//            return "error404"; // Trả về trang lỗi
//        }
//    }

    // media detail
    @GetMapping("/{mediaId}")
    public String getMediaDetailByIdASCEpisodes(@PathVariable Long mediaId,
                                                @RequestParam(name = "episode", required = false, defaultValue = "1") Integer episode,
                                                Model model) {
        List<CategoryDTO> categories = mediaDetailService.getCategoriesByMediaDetailId(mediaId);
        model.addAttribute("categoriesWithMovie", categories);

        try {
            List<MediaDetailResponse> mediaDetails = mediaDetailService.getMediaDetailByIdASCEpisodes(mediaId);
                if (!mediaDetails.isEmpty()) {
                    int selectedEpisodeIndex = episode - 1;
//                MediaDetailResponse firstMediaDetail = mediaDetails.get(0);
//                model.addAttribute("firstMediaDetail", firstMediaDetail);
                MediaDetailResponse selectedMediaDetail = mediaDetails.get(selectedEpisodeIndex);
                model.addAttribute("selectedMediaDetail", selectedMediaDetail);
            } else {
                return null;
            }

            model.addAttribute("mediaDetails", mediaDetails);
            return "user_movie_detail";
        } catch (Exception e) {
            // Xử lý lỗi nếu cần
            return "error404"; // Trả về trang lỗi
        }
    }


    @GetMapping("/media/{mediaId}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByMediaDetailId(@PathVariable Long mediaId) {
        try {
            List<CategoryDTO> categories = mediaDetailService.getCategoriesByMediaDetailId(mediaId);
            return ResponseEntity.ok(categories );
        } catch (Exception e) {
            // Handle exceptions or return an appropriate response
            return ResponseEntity.status(500).body(null);
        }
    }


    @GetMapping()
    public String movieDetail(Model model) {
        model.addAttribute("title", "Movie Detail");
        return "user_movie_detail";
    }

    @GetMapping("/by-category/{categoryName}")
    public ResponseEntity<List<MediaDetailResponse>> getMediaDetailsByCategoryName(@PathVariable String categoryName) {
        try {
            List<MediaDetailResponse> mediaDetails = mediaDetailService.getMediaDetailsByCategoryName(categoryName);

            if (mediaDetails != null && !mediaDetails.isEmpty()) {
                return ResponseEntity.ok(mediaDetails);
            } else {
                // Trả về 404 Not Found nếu không có kết quả nào được tìm thấy
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ hoặc trả về một giá trị mặc định
            return ResponseEntity.status(500).body(null);
        }
    }


    //phan trang
    @GetMapping("/media-details")
    public String getAllPlayers(Model model,
                                @RequestParam(name = "title", required = false) String title,
                                @RequestParam(name = "quality", required = false) String quality,
                                @RequestParam(name = "episode", required = false) Integer episode,
                                @RequestParam(name = "status", required = false) Integer  status
    ) {
        model.addAttribute("title", "Media Details");
        return findPaginated(1, model, title, quality,episode,status);
    }

    @GetMapping("/media-details/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                Model model,
                                @RequestParam(name = "title", required = false) String title,
                                @RequestParam(name = "quality", required = false) String quality,
                                @RequestParam(name = "episode", required = false) Integer episode,
                                @RequestParam(name = "status", required = false) Integer  status
    ) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<MediaDetail> result = mediaDetailRepository.searchMediaDetails(title, quality,episode,status, pageable);
        Page<MediaDetail> page = new PageImpl<>(result, pageable,mediaDetailRepository.searchMediaDetails1(title, quality,episode,status).size());
        List<MediaDetail> mediaDetails = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("mediaDetails", mediaDetails);
        model.addAttribute("title", title);
        model.addAttribute("quality", quality);
        model.addAttribute("episode", episode);
        model.addAttribute("status", status);
        return "admin_media_details";
    }

}
