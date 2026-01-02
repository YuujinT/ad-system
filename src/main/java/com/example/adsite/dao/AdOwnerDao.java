package com.example.adsite.dao;

import com.example.adsite.model.AdOwner;

import java.util.Optional;

public interface AdOwnerDao {
    Optional<AdOwner> findByUsername(String username);
}
