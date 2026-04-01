package com.a6dig.digitalsignage.dto;

import java.util.List;

public abstract class LayoutDtoBase<T extends LayoutSlotDtoBase> {
    private String name;
<<<<<<< HEAD
    private int cols;
    private int rows;
=======
    private Integer cols;
    private Integer rows;
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92

    private List<T> slots;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

<<<<<<< HEAD
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
=======
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
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
        this.rows = rows;
    }

    public List<T> getSlots() {
        return slots;
    }

    public void setSlots(List<T> slots) {
        this.slots = slots;
    }
}
