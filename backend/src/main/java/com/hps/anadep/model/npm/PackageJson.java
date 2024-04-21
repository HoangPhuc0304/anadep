package com.hps.anadep.model.npm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PackageJson {
    private String name;
    private String version;
    private String description;
    private String main;
    private String author;
    private String license;
    private Map<String,String> dependencies;
    private Map<String,String> devDependencies;
    private Map<String,String> scripts;
    private Map<String,Object> overrides;
}
