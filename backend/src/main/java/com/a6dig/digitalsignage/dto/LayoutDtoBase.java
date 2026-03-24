package com.a6dig.digitalsignage.dto;

import java.util.List;

public abstract class LayoutDtoBase<T extends LayoutSlotDtoBase> {
    private String name;
    private int cols;
    private int rows;

    private List<T> slots;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public List<T> getSlots() {
        return slots;
    }

    public void setSlots(List<T> slots) {
        this.slots = slots;
    }
}
