package com.example.tv360.Controller.admin;

import com.example.tv360.DTO.CastDTO;
import com.example.tv360.DTO.CountryDTO;
import com.example.tv360.Repository.CastRepository;
import com.example.tv360.Repository.CountryRepository;
import com.example.tv360.Service.CastService;
import com.example.tv360.Service.CountryService;
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

    @GetMapping("/country")
    public String getAllCountries(Model model) {

        model.addAttribute("title", "Country");
        List<CountryDTO> countries = countryService.getAllCountries();
        model.addAttribute("countries", countries);
        return "admin_country";
    }

    @GetMapping("/country/create")
    public String showCreateCountry(Model model){
        model.addAttribute("countryDTO", new CountryDTO());
        return "admin_country_create";
    }

    @PostMapping("/country/create/save")
    public String createCountry(@ModelAttribute CountryDTO countryDTO){
        try {
            countryService.createCountry(countryDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/admin/country";
    }

    @GetMapping("/country/update/{id}")
    public String showUpdateCountry(@PathVariable Long id, Model model){
        CountryDTO countryDTO = countryService.getCountryById(id);
        if (countryDTO == null){
            return "redirect:/admin/cast";
        }

        model.addAttribute("countryDTO", countryDTO);
        return "admin_country_update";
    }

    @PostMapping("/country/update/{id}")
    public String updateCountry(@PathVariable Long id, @ModelAttribute("castDTO") CountryDTO countryDTO, RedirectAttributes attributes){
        try {
            countryService.updateCountry(countryDTO);
            attributes.addFlashAttribute("success", "Update Successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update");
        }
        return "redirect:/admin/country";
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
}
