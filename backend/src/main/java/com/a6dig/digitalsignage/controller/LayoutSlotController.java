package com.a6dig.digitalsignage.controller;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.LayoutRequestDto;
import com.a6dig.digitalsignage.dto.LayoutSlotRequestUpdateDto;
import com.a6dig.digitalsignage.service.LayoutSlotService;
import com.a6dig.digitalsignage.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/layouts/{id}/slots")
public class LayoutSlotController {

    @Autowired
    private LayoutSlotService layoutSlotService;


    @DeleteMapping
    public ResponseEntity<APIResponse<Void>> deleteSelectedLayouts(@PathVariable(name = "id") Long layoutId, @RequestBody LayoutRequestDto<LayoutSlotRequestUpdateDto> layout) {
        this.layoutSlotService.deleteLayoutSlotsByLayoutId(layoutId, layout);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(AppConstant.SuccessMessage.LAYOUT_SLOT_DELETED_SELECTED));
    }

    @DeleteMapping("/all")
    public ResponseEntity<APIResponse<Void>> deleteAllLayouts(@PathVariable(name = "id") Long layoutId) {
        this.layoutSlotService.deleteAllSlotsByLayoutId(layoutId);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(AppConstant.SuccessMessage.allLayoutSlotsOfTheLayoutDeleted(layoutId)));
    }
}
