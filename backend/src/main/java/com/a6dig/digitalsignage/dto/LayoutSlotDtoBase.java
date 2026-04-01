package com.a6dig.digitalsignage.dto;

public abstract class LayoutSlotDtoBase {

    private Long moduleId;
<<<<<<< HEAD
    private int colPos;
    private int rowPos;
    private int colSpan;
    private int rowSpan;
    private int zIndex;

=======
    private Integer colPos;
    private Integer rowPos;
    private Integer colSpan;
    private Integer rowSpan;
    private Integer zIndex;
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

<<<<<<< HEAD
    public int getColPos() {
        return colPos;
    }

    public void setColPos(int colPos) {
        this.colPos = colPos;
    }

    public int getRowPos() {
        return rowPos;
    }

    public void setRowPos(int rowPos) {
        this.rowPos = rowPos;
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setzIndex(int zIndex) {
=======
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
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
        this.zIndex = zIndex;
    }
}
