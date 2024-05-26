package com.hps.anadep.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Namespace {
    private String path;
    private String manifestFilePath;
    private List<String> ignore;
}
