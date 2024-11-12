package com.manajemen.asset.controller;

import com.manajemen.asset.entity.Place;
import com.manajemen.asset.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class PlaceController {
    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @Operation(summary = "Add a new place")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Place added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Place.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Place> addPlace(@RequestBody Place place) {
        Place newPlace = placeService.addPlace(place);
        return ResponseEntity.ok(newPlace);
    }

    @Operation(summary = "Get all places")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all places", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Place.class)))
    })
    @GetMapping
    public ResponseEntity<List<Place>> getAllPlace() {
        List<Place> places = placeService.getAllPlaces();
        return ResponseEntity.ok(places);
    }

    @Operation(summary = "Get a place by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Place found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Place.class))),
            @ApiResponse(responseCode = "404", description = "Place not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable long id) {
        Place place = placeService.getPlaceById(id);
        return ResponseEntity.ok(place);
    }

    @Operation(summary = "Delete a place by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Place deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Place not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable long id) {
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a place by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Place updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Place.class))),
            @ApiResponse(responseCode = "404", description = "Place not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable long id, @RequestBody Place place) {
        Place updatedPlace = placeService.updatePlace(id, place);
        if (updatedPlace == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPlace);
    }
}
