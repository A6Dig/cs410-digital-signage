package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.config.DomainCache;
import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.ModuleRequestDto;
import com.a6dig.digitalsignage.dto.ModuleResponseDto;
import com.a6dig.digitalsignage.entity.AdCollection;
import com.a6dig.digitalsignage.entity.Domain;
import com.a6dig.digitalsignage.exception.AdCollectionNotFoundException;
import com.a6dig.digitalsignage.exception.InvalidDomainException;
import com.a6dig.digitalsignage.repository.AdCollectionRepository;
import com.a6dig.digitalsignage.util.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModuleValidator {
    @Autowired
    private DomainCache domainCache;


    public <T extends ModuleRequestDto>void validateType(T dto) {
        if (dto.getType() == null) {
            throw new InvalidDomainException(
                    AppConstant.ExceptionMessage.Domain.TYPE_NOT_PROVIDED,
                    List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.Domain.TYPE_CANNOT_BE_EMPTY))
            );
        }

        this.domainCache.validate(dto.getType().name(), AppConstant.SystemConstant.DOMAIN_TYPE_MODULE);

    }


}
