package com.hps.anadep.analyzer.service.impl;

import com.hps.anadep.analyzer.client.MavenClient;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.mvn.LibraryBulk;
import com.hps.anadep.analyzer.service.MavenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MavenServiceImpl implements MavenService {
    @Autowired
    private MavenClient mavenClient;

    @Override
    public LibraryBulk findLibrary(Library libraryInfo, int rows) {
        String groupId = libraryInfo.getName().split(":")[0];
        String artifactId = libraryInfo.getName().split(":")[1];
        StringBuilder sb = new StringBuilder();
        sb.append("g:").append(groupId).append("+AND+").append("a:").append(artifactId);
        String s = sb.toString();
        return mavenClient.getLibraryBulk(sb.toString(), "gav", rows, "json");
    }

    @Override
    public int getLibraryCount(Library libraryInfo) {
        return findLibrary(libraryInfo, 0).getResponse().getNumFound();
    }
}
