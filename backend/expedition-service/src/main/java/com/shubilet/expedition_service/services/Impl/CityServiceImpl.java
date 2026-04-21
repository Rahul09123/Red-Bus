package com.shubilet.expedition_service.services.Impl;

import org.springframework.stereotype.Service;

import com.shubilet.expedition_service.repositories.CityRepository;
import com.shubilet.expedition_service.services.CityService;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }
    
    public boolean cityExists(String cityName) {
        return cityRepository.existsByName(cityName);
    }
}
