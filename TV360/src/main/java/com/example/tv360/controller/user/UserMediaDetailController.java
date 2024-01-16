package com.example.tv360.controller.user;

import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.dto.response.MediaDetailResponse;
import com.example.tv360.entity.Media;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.repository.MediaRepository;
import com.example.tv360.service.CategoryService;
import com.example.tv360.service.MediaDetailService;
import com.example.tv360.service.MediaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping()
public class UserMediaDetailController {

    @Value("${page.size}")
    private int pageSize;

    private final MediaDetailService mediaDetailService;
    private final MediaRepository mediaRepository;
    private final MediaDetailRepository mediaDetailRepository;

    public UserMediaDetailController(MediaDetailRepository mediaDetailRepository,
                                     MediaDetailService mediaDetailService,
                                     MediaRepository mediaRepository) {
        this.mediaDetailRepository = mediaDetailRepository;
        this.mediaDetailService = mediaDetailService;
        this.mediaRepository = mediaRepository;
    }

    // media detail
    @GetMapping({"movie/detail/{mediaId}", "video/detail/{mediaId}"})
    public String getMediaDetail(@PathVariable Long mediaId,
                                 @RequestParam(name = "episode", required = false, defaultValue = "1") Integer episode,
                                 Model model) {
        try {
            List<MediaDetailResponse> listMediaDetails = mediaDetailService.getMediaDetailByMediaId(mediaId);

            if (!listMediaDetails.isEmpty()) {
                int selectedEpisodeIndex = episode - 1;
                MediaDetailResponse selectedMediaDetail = listMediaDetails.get(selectedEpisodeIndex);
                model.addAttribute("selectedMediaDetail", selectedMediaDetail);
            } else {
                return "error404";
            }

            model.addAttribute("listMediaDetails", listMediaDetails);

            //related-media-by-category
            List<Media> relatedMediaList = mediaDetailService.getRelatedMediaWithoutCurrent(mediaRepository.findById(mediaId).get());
            model.addAttribute("relatedMediaList", relatedMediaList);

            return "user_media_detail";
        } catch (Exception e) {
            return "error404";
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
