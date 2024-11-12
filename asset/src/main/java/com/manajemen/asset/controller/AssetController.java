package com.manajemen.asset.controller;

import com.manajemen.asset.entity.Asset;
import com.manajemen.asset.entity.Maintenance;
import com.manajemen.asset.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @Operation(summary = "Add a new asset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Asset.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Asset> addAsset(@RequestBody Asset asset) {
        Asset savedAsset = assetService.addAsset(asset);
        return ResponseEntity.ok(savedAsset);
    }

    @Operation(summary = "Get all assets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all assets", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Asset.class)))
    })
    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        List<Asset> assets = assetService.getAllAssets();
        return ResponseEntity.ok(assets);
    }

    @Operation(summary = "Get an asset by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Asset.class))),
            @ApiResponse(responseCode = "404", description = "Asset not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        Asset asset = assetService.getAssetById(id);
        return ResponseEntity.ok(asset);
    }

    @Operation(summary = "Get assets by place ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of assets for the specified place ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Asset.class)))
    })
    @GetMapping("/place/{idPlace}")
    public ResponseEntity<List<Asset>> getAssetsByPlaceId(@PathVariable Long idPlace) {
        List<Asset> assets = assetService.findAssetsByPlaceId(idPlace);
        return new ResponseEntity<>(assets, HttpStatus.OK);
    }

    @Operation(summary = "Search assets by keyword")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of assets matching the keyword", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Asset.class)))
    })
    @GetMapping("/search")
    public List<Asset> searchAssets(@RequestParam("keyword") String keyword) {
        return assetService.findAssetsByKeyword(keyword);
    }

    @Operation(summary = "Delete an asset by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Asset deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Asset not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update an asset by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Asset.class))),
            @ApiResponse(responseCode = "404", description = "Asset not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable Long id, @RequestBody Asset asset) {
        Asset updatedAsset = assetService.updateAsset(id, asset);
        if (updatedAsset == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAsset);
    }

    @Operation(summary = "Add a maintenance record to an asset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Maintenance record added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Maintenance.class))),
            @ApiResponse(responseCode = "404", description = "Asset not found", content = @Content)
    })
    @PostMapping("/{id}/maintenance")
    public ResponseEntity<Maintenance> addMaintenance(@PathVariable Long id, @RequestBody Maintenance maintenance) {
        Maintenance newMaintenance = assetService.addMaintenance(id, maintenance);
        return ResponseEntity.ok(newMaintenance);
    }
}
