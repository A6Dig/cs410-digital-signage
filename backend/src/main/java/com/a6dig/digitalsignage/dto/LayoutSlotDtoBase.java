package com.a6dig.digitalsignage.dto;

public abstract class LayoutSlotDtoBase {

    private Long moduleId;
    private Integer colPos;
    private Integer rowPos;
    private Integer colSpan;
    private Integer rowSpan;
    private Integer zIndex;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getColPos() {
        return colPos;
    }

    public void setColPos(Integer colPos) {
        this.colPos = colPos;
    }

    public Integer getRowPos() {
        return rowPos;
    }

    public void setRowPos(Integer rowPos) {
        this.rowPos = rowPos;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public Integer getzIndex() {
        return zIndex;
    }

    public void setzIndex(Integer zIndex) {
        this.zIndex = zIndex;
    }
}
