package com.manajemen.asset.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manajemen.asset.entity.Place;
import com.manajemen.asset.repository.PlaceRepository;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public Place addPlace(Place place) {
        return placeRepository.save(place);
    }

    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    public Place getPlaceById(long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));
    }

    public Place updatePlace(long id, Place place) {
        Place existingPlace = placeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));
        existingPlace.setGedung(place.getGedung());
        existingPlace.setLantai(place.getLantai());
        existingPlace.setRuangan(place.getRuangan());
        return placeRepository.save(existingPlace);
    }

    public void deletePlace(long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));
        placeRepository.delete(place);
    }
}
