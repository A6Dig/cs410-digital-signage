package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.dto.AdCollectionRequestDto;
import com.a6dig.digitalsignage.dto.AdCollectionRequestUpdateDto;
import com.a6dig.digitalsignage.dto.AdCollectionResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdCollectionServiceImpl implements AdCollectionService{
    @Override
    public List<AdCollectionResponseDto> getAllAdCollections() {
        return null;
    }

    @Override
    public AdCollectionResponseDto getAdCollectionById(Long id) {
        return null;
    }

    @Override
    public AdCollectionResponseDto createAdCollection(AdCollectionRequestDto adCollection) {
        return null;
    }

    @Override
    public AdCollectionResponseDto updateAdCollectionById(Long id, AdCollectionRequestUpdateDto adCollection) {
        return null;
    }

    @Override
    public void deleteAdCollectionById(Long id) {

    }

    @Override
    public void deleteAllAdCollections() {

    }
}
