package com.hps.osvscanning.service.impl;

import com.hps.osvscanning.client.MavenClient;
import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.mvn.LibraryBulk;
import com.hps.osvscanning.service.MavenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MavenServiceImpl implements MavenService {
    @Autowired
    private MavenClient mavenClient;

    @Override
    public LibraryBulk findLibrary(Library libraryInfo, int rows) {
        StringBuilder sb = new StringBuilder();
        sb.append("g:").append(libraryInfo.getGroupId()).append("+AND+").append("a:").append(libraryInfo.getArtifactId());
        String s = sb.toString();
        return mavenClient.getLibraryBulk(sb.toString(), "gav", rows, "json");
    }

    @Override
    public int getLibraryCount(Library libraryInfo) {
        return findLibrary(libraryInfo, 0).getResponse().getNumFound();
    }
}