package com.aicnfirst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "file")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class FilePersist extends MyAuditable {

    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    @Type(type = "org.hibernate.type.TextType")
    private String imageBytes;
    @NotNull
    @Column(unique = true)
    private String imageName;
    @NotNull
    private String result;

    private String description;
}
