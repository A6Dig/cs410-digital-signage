package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.dto.AdContentRequestDto;
import com.a6dig.digitalsignage.dto.AdContentRequestUpdateDto;
import com.a6dig.digitalsignage.dto.AdContentResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdContentServiceImpl implements AdContentService{
    @Override
    public List<AdContentResponseDto> getAllAdContents() {
        return null;
    }

    @Override
    public AdContentResponseDto getAdContentById(Long id) {
        return null;
    }

    @Override
    public AdContentResponseDto createAdContent(AdContentRequestDto adContent) {
        return null;
    }

    @Override
    public AdContentResponseDto updateAdContentById(Long id, AdContentRequestUpdateDto adContent) {
        return null;
    }

    @Override
    public void deleteAdContentById(Long id) {

    }

    @Override
    public void deleteAllContents() {

    }
}
