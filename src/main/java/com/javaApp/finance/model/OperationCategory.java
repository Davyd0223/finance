package com.javaApp.finance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "operation_categories")
public class OperationCategory extends AbstractBaseEntity {
    @ManyToOne
    private User user;
    private String name;
    private OperationKind kind;
}
