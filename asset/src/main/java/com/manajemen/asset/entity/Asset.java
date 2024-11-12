package com.manajemen.asset.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nama;
    private String tipe;
    private String kondisi;
    private int jumlah;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_place")
    private Place place;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Maintenance> maintenances;
}
