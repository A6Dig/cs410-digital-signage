package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.dto.*;

import java.util.List;

public interface AdContentService {
    // get
    List<AdContentResponseDto> getAllAdContents();
    AdContentResponseDto getAdContentById();
    // post
    AdContentResponseDto
        createAdContent(AdContentRequestDto adContent);

    // update
    AdContentResponseDto
        updateAdContentById(Long id, AdContentRequestUpdateDto adContent);

    // delete
    void deleteAdContentById(Long id);
    void deleteAllContents();
}
