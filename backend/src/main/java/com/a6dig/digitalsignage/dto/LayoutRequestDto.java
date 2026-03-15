package com.a6dig.digitalsignage.dto;

public class LayoutRequestDto {
    private String name;
    private int layoutCol;
    private int layoutRow;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLayoutCol() {
        return layoutCol;
    }

    public void setLayoutCol(int layoutCol) {
        this.layoutCol = layoutCol;
    }

    public int getLayoutRow() {
        return layoutRow;
    }

    public void setLayoutRow(int layoutRow) {
        this.layoutRow = layoutRow;
    }
}
