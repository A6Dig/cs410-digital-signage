package com.a6dig.digitalsignage.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "layout")
public class Layout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(name = "layoutCol", nullable = false)
    private int layoutCol;

    @Column(name = "layoutRow", nullable = false)
    private int layoutRow;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "layout", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<LayoutSlot> layoutSlotList;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<LayoutSlot> getLayoutSlotList() {
        return layoutSlotList;
    }

    public void setLayoutSlotList(List<LayoutSlot> layoutSlotList) {
        this.layoutSlotList = layoutSlotList;
    }

    public void addLayoutSlot(LayoutSlot slot) {
        if(this.layoutSlotList == null) this.layoutSlotList = new ArrayList<>();
        this.layoutSlotList.add(slot);
    }
}
