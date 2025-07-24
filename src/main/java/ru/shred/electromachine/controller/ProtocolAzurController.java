package ru.shred.electromachine.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.shred.electromachine.model.ProtocolAzur;
import ru.shred.electromachine.service.ProtocolAzurService;

import java.util.List;

/**
 * Created by KuhtaIA on 22.07.2025
 */
@RestController
@RequestMapping("/api/protocol-azur")
@RequiredArgsConstructor
@Tag(name = "Протокол АЗУР", description = "API для работы с протоколами АЗУР")
@PreAuthorize("hasRole('ADMIN')")
public class ProtocolAzurController {

    private final ProtocolAzurService protocolAzurService;

    @GetMapping
    public ResponseEntity<List<ProtocolAzur>> getAll() {
        return ResponseEntity.ok(protocolAzurService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProtocolAzur> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(protocolAzurService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProtocolAzur> create(@RequestBody ProtocolAzur protocolAzur) {
        return ResponseEntity.ok(protocolAzurService.save(protocolAzur));
    }

    @PutMapping
    public ResponseEntity<ProtocolAzur> update(@RequestBody ProtocolAzur protocolAzur) {
        return ResponseEntity.ok(protocolAzurService.update(protocolAzur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        protocolAzurService.delete(id);
        return ResponseEntity.ok().build();
    }
}
