package com.hps.anadepscheduler.model.osv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Storage {
    private String kind;
    private String id;
    private String selfLink;
    private String mediaLink;
    private String name;
    private String bucket;
    private String generation;
    private String metageneration;
    private String contentType;
    private String storageClass;
    private String size;
    private String timeCreated;
    private String updated;
    private String timeStorageClassUpdated;
}
