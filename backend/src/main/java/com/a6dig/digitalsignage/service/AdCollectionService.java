package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.dto.*;

import java.util.List;

public interface AdCollectionService {
    // get
    List<AdCollectionResponseDto<AdContentResponseDto>> getAllAdCollections();
    AdCollectionResponseDto<AdContentResponseDto> getAdCollectionById();
    // post
    AdCollectionResponseDto<AdContentResponseDto>
        createAdCollection(AdCollectionRequestDto<AdContentRequestDto> adCollection);

    // update
    AdCollectionResponseDto<AdContentResponseDto>
        updateAdCollectionById(Long id, AdCollectionRequestUpdateDto adCollection);

    // delete
    void deleteAdCollectionById(Long id);
    void deleteAllAdCollections();
}
