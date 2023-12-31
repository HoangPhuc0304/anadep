package com.hps.osvscanning.service.impl;

import com.hps.osvscanning.client.OsvClient;
import com.hps.osvscanning.mapper.LibraryMapper;
import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.LibraryVersion;
import com.hps.osvscanning.model.osv.VulnerabilityList;
import com.hps.osvscanning.service.OsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OsvServiceImpl implements OsvService {
    @Autowired
    private OsvClient osvClient;
    @Autowired
    private LibraryMapper libraryMapper;

    @Override
    public VulnerabilityList findVulnerabilities(Library libraryInfo) {
        LibraryVersion libraryVersion = libraryMapper.libraryToVersion(libraryInfo);
        return osvClient.getVulnerability(libraryVersion);
    }
}
