package lidofon.country.controller;

import lidofon.country.dto.CreateCountryDto;
import lidofon.country.service.impl.CountryServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryServiceImpl countryService;

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getPagedList() throws SQLException {
        return null;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody CreateCountryDto createCountryDto) throws SQLException {
        return countryService.create(createCountryDto,"countries");
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Map<String, Object>> update(@RequestBody CreateCountryDto createCountryDto,@PathVariable String id) throws SQLException {
        return countryService.update(createCountryDto,id,"countries");
    }

    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Map<String,Object>> softDelete(@PathVariable String id) throws SQLException {
        return countryService.softDelete(id,"countries");
    }

    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<Map<String,Object>> hardDelete(@PathVariable String id) throws SQLException {
        return countryService.hardDelete(id,"countries");
    }


    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getCountriesList() throws SQLException {
        List<Map<String, Object>> result = countryService.getCountriesList();
        Map<String, Object> response = new HashMap<>();
        response.put("data", result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOneCountry() throws SQLException {
        return null;
    }

}
