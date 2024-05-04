package com.hps.anadep.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Namespace {
    private String path;
    private String manifestFilePath;
}
