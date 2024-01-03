package com.example.tv360.controller.admin;

import com.example.tv360.dto.CountryDTO;
import com.example.tv360.entity.Cast;
import com.example.tv360.entity.Country;
import com.example.tv360.repository.CountryRepository;
import com.example.tv360.service.CountryService;
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
import java.util.List;
@Controller
@RequestMapping("/admin")
public class CountryController {
    private final CountryService countryService;
    private final CountryRepository countryRepository;

    public CountryController(CountryService countryService, CountryRepository countryRepository) {
        this.countryService = countryService;
        this.countryRepository = countryRepository;
    }

    @GetMapping("/country/create")
    public String showCreateOrUpdateCountry(Model model) {
        model.addAttribute("countryDTO", new CountryDTO());
        return "admin_country_form";
    }

    @GetMapping("/country/update/{id}")
    public String showCreateOrUpdateCountry(@PathVariable Long id, Model model) {
        CountryDTO countryDTO = countryService.getCountryById(id);
        if (countryDTO == null) {
            return "redirect:/admin/countries";
        }

        model.addAttribute("countryDTO", countryDTO);
        return "admin_country_form";
    }

    @PostMapping("/country/create/save")
    public String createOrUpdateCountry(@ModelAttribute CountryDTO countryDTO, RedirectAttributes redirectAttributes) {
        try {
            countryService.createCountry(countryDTO);
            redirectAttributes.addFlashAttribute("success", "Create Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to create!");
        }
        return "redirect:/admin/countries";
    }

    @PostMapping("/country/update/{id}")
    public String updateCountry(@PathVariable Long id, @ModelAttribute CountryDTO countryDTO, RedirectAttributes redirectAttributes) {
        try {
            countryService.updateCountry(id, countryDTO);
            redirectAttributes.addFlashAttribute("success", "Update Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to update!");
        }
        return "redirect:/admin/countries";
    }

    @GetMapping("/country/delete/{id}")
    public ResponseEntity<String> deleteCountry(@PathVariable Long id){
        try {
            countryService.softDeleteCountry(id);
            return ResponseEntity.ok("Delete country successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error!");
        }
    }

    @GetMapping("/countries")
    public String getAllCountries(Model model,
                                  @RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "status", required = false) Integer status
    ) {
        model.addAttribute("title", "Countries");
        return findPaginated(1, model, name, status);
    }

    @GetMapping("/countries/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                Model model,
                                @RequestParam(name = "name", required = false) String name,
                                @RequestParam(name = "status", required = false) Integer status
    ) {
        int pageSize = 6;

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Country> result = countryRepository.searchCategories(name, status, pageable);
        Page<Country> page = new PageImpl<>(result, pageable, countryRepository.searchCategories1(name, status).size());
        List<Country> countries = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("countries", countries);
        model.addAttribute("name", name);
        model.addAttribute("status", status);
        return "admin_country";
    }
}
