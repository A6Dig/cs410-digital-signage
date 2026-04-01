package com.a6dig.digitalsignage.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "Module")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="adCollectionId")
    private AdCollection adCollection;

    private String name;

    @ManyToOne
    @JoinColumn(name = "domainId")
    private Domain domain;

    @Column(columnDefinition = "TEXT")
    private String config;


    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;



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

    public AdCollection getAdCollection() {
        return adCollection;
    }

    public void setAdCollection(AdCollection adCollection) {
        this.adCollection = adCollection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
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

    public static ModuleBuilder builder() {
        return new ModuleBuilder();
    }

    public static class ModuleBuilder {
        private Long id;
        private AdCollection adCollection;
        private String name;
        private Domain domain;
        private String config;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;


        public ModuleBuilder Id(Long id) {
            this.id = id;
            return this;
        }

        public ModuleBuilder adCollection(AdCollection adCollection) {
            this.adCollection = adCollection;
            return this;
        }

        public ModuleBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ModuleBuilder domain(Domain domain) {
            this.domain = domain;
            return this;
        }

        public ModuleBuilder config(String config) {
            this.config = config;
            return this;
        }

        public ModuleBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ModuleBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }


        public Module build() {
            Module module = new Module();
            module.setId(this.id);
            module.setAdCollection(this.adCollection);
            module.setName(this.name);
            module.setDomain(this.domain);
            module.setConfig(this.config);
            module.setCreatedAt(this.createdAt);
            module.setUpdatedAt(this.updatedAt);
            return module;
        }
    }


}
