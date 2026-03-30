package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.AdCollection;
import com.a6dig.digitalsignage.entity.AdContent;
import com.a6dig.digitalsignage.exception.AdCollectionNotFoundException;
import com.a6dig.digitalsignage.exception.AdContentNotFoundException;
import com.a6dig.digitalsignage.mapper.AdCollectionMapper;
import com.a6dig.digitalsignage.repository.AdCollectionRepository;
import com.a6dig.digitalsignage.repository.AdContentRepository;
import com.a6dig.digitalsignage.util.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdCollectionServiceImpl implements AdCollectionService{

    @Autowired
    private AdCollectionRepository adCollectionRepository;

    @Autowired
    private AdContentRepository adContentRepository;

    @Autowired
    private AdCollectionMapper adCollectionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AdCollectionResponseDto> getAllAdCollections() {

        return this.adCollectionRepository
                .findAll()
                .stream()
                .map(adCollection -> this.adCollectionMapper.toAdCollectionResponseDto(adCollection))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdCollectionResponseDto getAdCollectionById(Long id) {
        AdCollection adCollection = this.adCollectionRepository
                .findById(id)
                .orElseThrow(() -> new AdCollectionNotFoundException(
                        AppConstant.ExceptionMessage.AdCollection.NOT_FOUND,
                        List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.AdCollection.idDoesNotExist(id)))
                ));
        return this.adCollectionMapper.toAdCollectionResponseDto(adCollection);
    }

    @Override
    @Transactional
    public AdCollectionResponseDto createAdCollection(AdCollectionRequestDto adCollection) {

        Set<AdContent> adContents = new HashSet<>();

        // not allowed to create any new ad content
        // only allow to assign an existing ad content to the collection
        if (adCollection.getAdContents() != null && !adCollection.getAdContents().isEmpty()) {

            Set<Long> ids = adCollection.getAdContents().stream()
                    .map(AdContentRequestUpdateDto::getId)
                    .collect(Collectors.toSet());

            if (ids.contains(null)) {
                throw new AdContentNotFoundException(
                        AppConstant.ExceptionMessage.AdContent.INVALID_DATA_PROVIDED,
                        List.of(ErrorMessage.createErrorMessage(
                                AppConstant.ExceptionMessage.AdContent.ID_NOT_PROVIDED))
                );
            }

            adContents = new HashSet<>(adContentRepository.findAllById(ids));
            Set<Long> foundIds = adContents.stream().map(AdContent::getId).collect(Collectors.toSet());

            ids.removeAll(foundIds);
            if (!ids.isEmpty()) {
                List<Map<String, String>> errors = ids.stream()
                        .map(id -> ErrorMessage.createErrorMessage(
                                AppConstant.ExceptionMessage.AdContent.idDoesNotExist(id)))
                        .toList();

                throw new AdContentNotFoundException(
                        AppConstant.ExceptionMessage.AdContent.NOT_FOUND,
                        errors
                );
            }
        }

        AdCollection newAdCollection = new AdCollection();
        newAdCollection.setName(adCollection.getName());
        newAdCollection.setUrl(adCollection.getUrl());
        newAdCollection.setAdContents(adContents);

        AdCollection saved = adCollectionRepository.save(newAdCollection);

        return this.adCollectionMapper.toAdCollectionResponseDto(saved);
    }

    @Override
    public AdCollectionResponseDto updateAdCollectionById(Long adCollectionId, AdCollectionRequestUpdateDto adCollection) {
        AdCollection existing = this.adCollectionRepository.findById(adCollectionId)
                .orElseThrow(() -> new AdCollectionNotFoundException(
                        AppConstant.ExceptionMessage.AdCollection.NOT_FOUND,
                        List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.AdCollection.idDoesNotExist(adCollectionId)))
                ));

        Set<AdContent> adContents = new HashSet<>();

        // not allowed to create any new ad content
        // only allow to assign an existing ad content to the collection
        if (adCollection.getAdContents() != null && !adCollection.getAdContents().isEmpty()) {

            Set<Long> ids = adCollection.getAdContents().stream()
                    .map(AdContentRequestUpdateDto::getId)
                    .collect(Collectors.toSet());

            if (ids.contains(null)) {
                throw new AdContentNotFoundException(
                        AppConstant.ExceptionMessage.AdContent.INVALID_DATA_PROVIDED,
                        List.of(ErrorMessage.createErrorMessage(
                                AppConstant.ExceptionMessage.AdContent.ID_NOT_PROVIDED))
                );
            }

            adContents = new HashSet<>(adContentRepository.findAllById(ids));
            Set<Long> foundIds = adContents.stream().map(AdContent::getId).collect(Collectors.toSet());

            ids.removeAll(foundIds);
            if (!ids.isEmpty()) {
                List<Map<String, String>> errors = ids.stream()
                        .map(id -> ErrorMessage.createErrorMessage(
                                AppConstant.ExceptionMessage.AdContent.idDoesNotExist(id)))
                        .toList();

                throw new AdContentNotFoundException(
                        AppConstant.ExceptionMessage.AdContent.NOT_FOUND,
                        errors
                );
            }
        }

        existing.setName(adCollection.getName() == null ? existing.getName() : adCollection.getName());
        existing.setUrl(adCollection.getUrl() == null ? existing.getUrl() : adCollection.getUrl());
        existing.setAdContents(adCollection.getAdContents() == null ? existing.getAdContents() : adContents);

        AdCollection saved = adCollectionRepository.save(existing);

        return this.adCollectionMapper.toAdCollectionResponseDto(saved);
    }

    @Override
    public void deleteAdCollectionById(Long id) {
        AdCollection adCollection = this.adCollectionRepository.findById(id)
                .orElseThrow(() -> new AdCollectionNotFoundException(
                        AppConstant.ExceptionMessage.AdCollection.NOT_FOUND,
                        List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.AdCollection.idDoesNotExist(id)))
                ));
        this.adCollectionRepository.delete(adCollection);
    }

    @Override
    public void deleteAllAdCollections() {
        this.adCollectionRepository.deleteAll();
    }
}
