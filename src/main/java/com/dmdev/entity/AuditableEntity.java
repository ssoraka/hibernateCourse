package com.dmdev.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
public abstract class AuditableEntity<T extends Serializable> implements BaseEntity<T>{

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private String createdBy;

    protected AuditableEntity(AuditableEntityBuilder<T, ?, ?> b) {
        this.createdAt = b.createdAt;
        this.createdBy = b.createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public static abstract class AuditableEntityBuilder<T extends Serializable, C extends AuditableEntity<T>, B extends AuditableEntityBuilder<T, C, B>> {
        private Instant createdAt;
        private String createdBy;

        public B createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return self();
        }

        public B createdBy(String createdBy) {
            this.createdBy = createdBy;
            return self();
        }

        protected abstract B self();

        public abstract C build();

        public String toString() {
            return "AuditableEntity.AuditableEntityBuilder(createdAt=" + this.createdAt + ", createdBy=" + this.createdBy + ")";
        }
    }
}
