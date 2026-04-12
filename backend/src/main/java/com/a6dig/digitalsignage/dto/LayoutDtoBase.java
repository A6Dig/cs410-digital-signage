package com.a6dig.digitalsignage.dto;

import java.util.List;

public abstract class LayoutDtoBase<T extends LayoutSlotDtoBase> {
    private String name;
    private Integer cols;
    private Integer rows;

    private List<T> slots;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCols() {
        return cols;
    }

    public void setCols(Integer cols) {
        this.cols = cols;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public List<T> getSlots() {
        return slots;
    }

    public void setSlots(List<T> slots) {
        this.slots = slots;
    }
}
