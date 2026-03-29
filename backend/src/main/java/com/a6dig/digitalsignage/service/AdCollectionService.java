package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.dto.*;

import java.util.List;

public interface AdCollectionService {
    // get
    List<AdCollectionResponseDto> getAllAdCollections();
    AdCollectionResponseDto getAdCollectionById(Long id);
    // post
    AdCollectionResponseDto
        createAdCollection(AdCollectionRequestDto adCollection);

    // update
    AdCollectionResponseDto
        updateAdCollectionById(Long id, AdCollectionRequestUpdateDto adCollection);

    // delete
    void deleteAdCollectionById(Long id);
    void deleteAllAdCollections();
}
