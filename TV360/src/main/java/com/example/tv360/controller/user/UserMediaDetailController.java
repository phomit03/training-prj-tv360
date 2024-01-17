package com.example.tv360.controller.user;

import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.dto.MediaDTO;
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


//    // media detail
//    @GetMapping({"movie/detail/{mediaId}", "video/detail/{mediaId}"})
//    public String getMediaDetail(@PathVariable Long mediaId,
//                                 @RequestParam(name = "episode", required = false, defaultValue = "1") Integer episode,
//                                 Model model) {
//        try {
//            List<MediaDetailResponse> listMediaDetails = mediaDetailService.getMediaDetailByMediaId(mediaId);
//
//            if (!listMediaDetails.isEmpty()) {
//                int selectedEpisodeIndex = episode - 1;
//                MediaDetailResponse selectedMediaDetail = listMediaDetails.get(selectedEpisodeIndex);
//                model.addAttribute("selectedMediaDetail", selectedMediaDetail);
//            } else {
//                return "error404";
//            }
//
//            model.addAttribute("listMediaDetails", listMediaDetails);
//
//            //related-media-by-category
//            List<Media> relatedMediaList = mediaDetailService.getRelatedMediaWithoutCurrent(mediaRepository.findById(mediaId).get());
//            model.addAttribute("relatedMediaList", relatedMediaList);
//
//            return "user_media_detail";
//        } catch (Exception e) {
//            return "error404";
//        }
//    }

    //phan trang
    @GetMapping({"movie/detail/{mediaId}", "video/detail/{mediaId}"})
    public String getMediaDetails1(@PathVariable Long mediaId,Model model){
        List<MediaDetailResponse> listMediaDetails = mediaDetailService.getMediaDetailByMediaId(mediaId);
        model.addAttribute("title", listMediaDetails.get(0).getTitle());
        return findPaginated(mediaId,1, 1, model);
    }
    @GetMapping({"movie/detail/{mediaId}/{pageNo}", "video/detail/{mediaId}/{pageNo}"})
    public String findPaginated(@PathVariable Long mediaId,
                                @PathVariable(value = "pageNo") int pageNo,
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

            //related-media-by-category
            List<Media> relatedMediaList = mediaDetailService.getRelatedMediaWithoutCurrent(mediaRepository.findById(mediaId).get());
            // can phan trang
            Page<Media> page = mediaDetailService.findPaginated1(pageNo, pageSize,relatedMediaList);
            List<Media> relatedList = page.getContent();

            model.addAttribute("listMediaDetails", listMediaDetails);
            model.addAttribute("relatedMediaList", relatedMediaList);
            model.addAttribute("title", listMediaDetails.get(0).getTitle());

            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("relatedList", relatedList);
            return "user_media_detail";
        } catch (Exception e) {
            return "error404";
        }
    }

}
