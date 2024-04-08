package com.hps.anadepscheduler.util;

import com.hps.anadepscheduler.model.Ecosystem;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.maven.artifact.versioning.ComparableVersion;

@AllArgsConstructor
@Data
public class SemverUtil {
    private String version;
    private Ecosystem ecosystem;

    public boolean isEqualTo(String v) {
        ComparableVersion comparableVersion = new ComparableVersion(version);
        return comparableVersion.compareTo(new ComparableVersion(v)) == 0;
    }
    public boolean isGreaterThan(String v) {
        ComparableVersion comparableVersion = new ComparableVersion(version);
        return comparableVersion.compareTo(new ComparableVersion(v)) > 0;
    }
    public boolean isGreaterThanOrEqualTo(String v) {
        ComparableVersion comparableVersion = new ComparableVersion(version);
        return comparableVersion.compareTo(new ComparableVersion(v)) >= 0;
    }
    public boolean isLowerThan(String v) {
        ComparableVersion comparableVersion = new ComparableVersion(version);
        return comparableVersion.compareTo(new ComparableVersion(v)) < 0;
    }
    public boolean isLowerThanOrEqualTo(String v) {
        ComparableVersion comparableVersion = new ComparableVersion(version);
        return comparableVersion.compareTo(new ComparableVersion(v)) <= 0;
    }
}
