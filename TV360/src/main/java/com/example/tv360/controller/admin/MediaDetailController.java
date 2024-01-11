package com.example.tv360.controller.admin;

import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.dto.MediaDTO;
import com.example.tv360.dto.MediaDetailDTO;
import com.example.tv360.dto.response.MediaDetailResponse;
import com.example.tv360.entity.Media;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.repository.MediaRepository;
import com.example.tv360.service.MediaDetailService;
import com.example.tv360.service.MediaService;
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
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class MediaDetailController {
    @Value("${page.size}")
    private int pageSize;
    private final MediaDetailService mediaDetailService;
    private final MediaService mediaService;
    private final MediaRepository mediaRepository;
    private final MediaDetailRepository mediaDetailRepository;

    public MediaDetailController(MediaDetailRepository mediaDetailRepository ,MediaDetailService mediaDetailService, MediaService mediaService, MediaRepository mediaRepository) {
        this.mediaDetailRepository = mediaDetailRepository;
        this.mediaDetailService = mediaDetailService;
        this.mediaService = mediaService;
        this.mediaRepository = mediaRepository;
    }

    @GetMapping({"/video-source/form", "/video-source/form/{id}"})
    public String showFormVideoSource(@PathVariable(required = false) Long id, Model model){
        if (id != null) {
            MediaDetailDTO mediaDetailDTO = mediaDetailService.getMediaDetailById(id);
            model.addAttribute("mediaDetailDTO", mediaDetailDTO);
            return "admin_media_detail_form";
        } else {
            model.addAttribute("mediaDetailDTO", new MediaDetailDTO());
            return "admin_videos";
        }
    }

    @GetMapping({"/movie-source/form", "/movie-source/form/{id}"})
    public String showFormMovieSource(@PathVariable(required = false) Long id, Model model){
        if (id != null) {
            MediaDetailDTO mediaDetailDTO = mediaDetailService.getMediaDetailById(id);
            model.addAttribute("mediaDetailDTO", mediaDetailDTO);
            return "admin_media_detail_form";
        } else {
            model.addAttribute("mediaDetailDTO", new MediaDetailDTO());
            return "admin_movies";
        }
    }

    @PostMapping("/media/source/save")
    public String createOrUpdateMediaSource(@ModelAttribute MediaDetailDTO mediaDetailDTO,
                                    RedirectAttributes redirectAttributes) {
        try {
            if (mediaDetailDTO.getId() == null) {
                mediaDetailService.createMediaDetail(mediaDetailDTO);
                redirectAttributes.addFlashAttribute("success", "Add source media successfully!");
            } else {
                mediaDetailService.updateMediaDetail(mediaDetailDTO.getId(), mediaDetailDTO);
                redirectAttributes.addFlashAttribute("success", "Update Successfully!");
            }
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed!");
        }

        if (mediaDetailDTO.getId() == null) {
            if (mediaDetailDTO.getMedia().getType() == 1){
                return "redirect:/admin/movies";
            } else {
                return "redirect:/admin/videos";
            }
        } else {
            return "redirect:/admin/media-details";
        }
    }

    @PostMapping("/media-series/source/save")
    public String createOrUpdateMediaSeriesSource(@ModelAttribute MediaDetailDTO mediaDetailDTO,
                                          RedirectAttributes redirectAttributes,
                                          HttpServletRequest request) {
        try {
            if (mediaDetailDTO.getId() == null) {
                int maxEpisode = mediaDetailService.getMaxEpisodeByMediaId(mediaDetailDTO.getMedia().getId());

                String[] sourceUrls = request.getParameterValues("sourceUrl");
                String[] durations = request.getParameterValues("duration");
                String[] rates = request.getParameterValues("rate");
                String[] qualities = request.getParameterValues("quality");

                List<MediaDetail> mediaDetails = new ArrayList<>();

                for (int i = 0; i < sourceUrls.length; i++) {
                    MediaDetail mediaDetail = new MediaDetail();

                    mediaDetail.setSourceUrl(sourceUrls[i]);

                    if (maxEpisode < 1) {
                        mediaDetail.setEpisode(1);
                    } else {
                        mediaDetail.setEpisode(maxEpisode + 1 + i);
                    }

                    mediaDetail.setDuration(durations[i]);
                    mediaDetail.setRate(Integer.parseInt(rates[i]));
                    mediaDetail.setQuality(qualities[i]);
                    mediaDetail.setStatus(1);
                    mediaDetail.setMedia(mediaRepository.findById(mediaDetailDTO.getMedia().getId()).orElse(null));

                    mediaDetails.add(mediaDetail);
                }

                mediaDetailService.createListMediaDetail(mediaDetails);
                redirectAttributes.addFlashAttribute("success", "Add source media series successfully!");
            } else {
                mediaDetailService.updateMediaDetail(mediaDetailDTO.getId(), mediaDetailDTO);
                redirectAttributes.addFlashAttribute("success", "Update Successfully!");
            }
        } catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed!");
        }

        if (mediaDetailDTO.getId() == null) {
            return "redirect:/admin/movies";
        } else {
            return "redirect:/admin/media-details";
        }
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
    public String getAllMediaDetails(Model model,
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
