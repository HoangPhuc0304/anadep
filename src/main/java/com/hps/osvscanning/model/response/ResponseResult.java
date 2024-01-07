package com.hps.osvscanning.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult {
    private List<LibraryScan> libs;
    private String ecosystem;
    private int issuesCount;
    private int libraryCount;
    private boolean includeSafe;
    private long responseTime;

    public ResponseResult(List<LibraryScan> libs, String ecosystem, int libraryCount, boolean includeSafe, long responseTime) {
        this.libs = libs;
        this.ecosystem = ecosystem;
        this.libraryCount = libraryCount;
        this.includeSafe = includeSafe;
        this.responseTime = responseTime;
    }
}
