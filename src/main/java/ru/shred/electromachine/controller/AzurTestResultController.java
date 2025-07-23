package ru.shred.electromachine.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shred.electromachine.model.AzurTestResult;
import ru.shred.electromachine.service.AzurTestResultService;

import java.util.List;

/**
 * Created by KuhtaIA on 23.07.2025
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/azur-test-result")
@Tag(name = "Результаты испытаний АЗУР", description = "API для работы с результатами испытаний АЗУР")
public class AzurTestResultController {

    private final AzurTestResultService azurTestResultService;

    @GetMapping("/{id}")
    public ResponseEntity<AzurTestResult> getAzurById(@PathVariable Long id) {
        return ResponseEntity.ok(azurTestResultService.getById(id));
    }

    @GetMapping("/protocol/{protocolId}")
    public ResponseEntity<List<AzurTestResult>> getAllByProtocolId(@PathVariable Long protocolId) {
        return ResponseEntity.ok(azurTestResultService.getAllByProtocolId(protocolId));
    }

    @PostMapping
    public ResponseEntity<AzurTestResult> create(@RequestBody AzurTestResult azurTestResult) {
        return ResponseEntity.ok(azurTestResultService.save(azurTestResult));
    }

    @PutMapping
    public ResponseEntity<AzurTestResult> update(@RequestBody AzurTestResult azurTestResult) {
        return ResponseEntity.ok(azurTestResultService.update(azurTestResult));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        azurTestResultService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/protocol/{protocolId}")
    public ResponseEntity<Void> deleteAllByProtocolId(@PathVariable Long protocolId) {
        azurTestResultService.deleteAllByProtocolId(protocolId);
        return ResponseEntity.ok().build();
    }
}
