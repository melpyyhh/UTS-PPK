package com.manajemen.asset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.manajemen.asset.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

}
