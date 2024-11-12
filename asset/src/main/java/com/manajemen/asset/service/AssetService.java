package com.manajemen.asset.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manajemen.asset.entity.Asset;
import com.manajemen.asset.entity.Maintenance;
import com.manajemen.asset.entity.Place;
import com.manajemen.asset.repository.AssetRepository;
import com.manajemen.asset.repository.MaintenanceRepository;
import com.manajemen.asset.repository.PlaceRepository;

@Service
public class AssetService {
    private final AssetRepository assetRepository;
    private final PlaceRepository placeRepository;
    private final MaintenanceRepository maintenanceRepository;

    @Autowired
    public AssetService(AssetRepository assetRepository, PlaceRepository placeRepository,
            MaintenanceRepository maintenanceRepository) {
        this.assetRepository = assetRepository;
        this.placeRepository = placeRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    public Asset addAsset(Asset asset) {
        Place place = placeRepository.findById(asset.getPlace().getId())
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));
        asset.setPlace(place);
        return assetRepository.save(asset);
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Asset getAssetById(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found"));
    }

    public Asset updateAsset(Long id, Asset asset) {
        Asset existingAsset = assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found"));
        if (existingAsset == null) {
            return null;
        }
        existingAsset.setNama(asset.getNama());
        existingAsset.setTipe(asset.getTipe());
        existingAsset.setPlace(asset.getPlace());
        existingAsset.setKondisi(asset.getKondisi());
        existingAsset.setJumlah(asset.getJumlah());
        return assetRepository.save(existingAsset);
    }

    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found"));
        assetRepository.delete(asset);
    }

    public Maintenance addMaintenance(Long assetId, Maintenance maintenance) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        maintenance.setAsset(asset);
        return maintenanceRepository.save(maintenance);
    }

    public List<Asset> findAssetsByPlaceId(Long placeid) {
        return assetRepository.findByPlaceId(placeid);
    }

    public List<Asset> findAssetsByKeyword(String keyword) {
        return assetRepository.findByKeyword(keyword);
    }
}
