package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.config.DomainCache;
import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.AdContentRequestDto;
import com.a6dig.digitalsignage.dto.AdContentRequestUpdateDto;
import com.a6dig.digitalsignage.dto.AdContentResponseDto;
import com.a6dig.digitalsignage.entity.AdContent;
import com.a6dig.digitalsignage.exception.AdContentNotFoundException;
import com.a6dig.digitalsignage.mapper.AdContentMapper;
import com.a6dig.digitalsignage.repository.AdContentRepository;
import com.a6dig.digitalsignage.util.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdContentServiceImpl implements AdContentService{
    @Autowired
    private AdContentRepository adContentRepository;

    @Autowired
    private DomainCache domainCache;

    @Autowired
    private AdContentMapper adContentMapper;


    @Override
    @Transactional(readOnly = true)
    public List<AdContentResponseDto> getAllAdContents() {
        return this.adContentRepository
                .findAll()
                .stream()
                .map(adContent -> this.adContentMapper.toAdContentResponseDto(adContent))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdContentResponseDto getAdContentById(Long id) {
        AdContent adContent = this.adContentRepository
                .findById(id)
                .orElseThrow(() -> new AdContentNotFoundException(
                        AppConstant.ExceptionMessage.AdContent.NOT_FOUND,
                        List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.AdContent.idDoesNotExist(id)))
                ));
        return this.adContentMapper.toAdContentResponseDto(adContent);
    }

    @Override
    @Transactional
    public AdContentResponseDto createAdContent(AdContentRequestDto adContent) {
        AdContent newAdContent = new AdContent();
        newAdContent.setName(adContent.getName());
        newAdContent.setUrl(adContent.getUrl());
        newAdContent.setDomain(this.domainCache.buildDomain(adContent.getType()));

        AdContent saved = this.adContentRepository.save(newAdContent);

        return this.adContentMapper.toAdContentResponseDto(saved);
    }

    @Override
    public AdContentResponseDto updateAdContentById(Long id, AdContentRequestUpdateDto adContent) {
        AdContent existing = this.adContentRepository
                .findById(id)
                .orElseThrow(() -> new AdContentNotFoundException(
                        AppConstant.ExceptionMessage.AdContent.NOT_FOUND,
                        List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.AdContent.idDoesNotExist(id)))
                ));

        existing.setName(adContent.getName() == null ? existing.getName() : adContent.getName());
        existing.setUrl(adContent.getUrl() == null ? existing.getUrl() : adContent.getUrl());
        existing.setDomain(adContent.getType() == null ? existing.getDomain() : this.domainCache.buildDomain(adContent.getType()));

        AdContent updated = this.adContentRepository.save(existing);

        return this.adContentMapper.toAdContentResponseDto(updated);
    }

    @Override
    public void deleteAdContentById(Long id) {
        AdContent adContent = this.adContentRepository
                .findById(id)
                .orElseThrow(() -> new AdContentNotFoundException(
                        AppConstant.ExceptionMessage.AdContent.NOT_FOUND,
                        List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.AdContent.idDoesNotExist(id)))
                ));
        this.adContentRepository.delete(adContent);
    }

    @Override
    public void deleteAllContents() {
        this.adContentRepository.deleteAll();
    }
}
