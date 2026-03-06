package com.robostore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturersController {

    @Autowired
    private MyManufacturersRepository repo;

    // GET /api/manufacturers
    @GetMapping
    public List<myManufacturers> getAll() {
        return repo.findAll();
    }

    // POST /api/manufacturers
    @PostMapping
    public ResponseEntity<?> create(@RequestBody myManufacturers body) {
        if (body.getName() == null || body.getName().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "廠商名稱不得為空"));
        }
        return ResponseEntity.ok(repo.save(body));
    }

    // PUT /api/manufacturers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody myManufacturers body) {
        return repo.findById(id).map(existing -> {
            existing.setName(body.getName());
            existing.setPhone(body.getPhone());
            existing.setStatus(body.getStatus());
            existing.setTaxId(body.getTaxId());
            existing.setAddress(body.getAddress());
            existing.setBranchName(body.getBranchName());
            existing.setGroupName(body.getGroupName());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/manufacturers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok(Map.of("deleted", id));
    }
}
