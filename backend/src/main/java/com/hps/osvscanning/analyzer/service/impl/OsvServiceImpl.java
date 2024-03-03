package com.hps.osvscanning.analyzer.service.impl;

import com.hps.osvscanning.analyzer.client.OsvClient;
import com.hps.osvscanning.model.mapper.LibraryMapper;
import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.osv.LibraryOSVBatchRequest;
import com.hps.osvscanning.model.osv.LibraryOSVRequest;
import com.hps.osvscanning.model.osv.VulnerabilityOSVBatchResponse;
import com.hps.osvscanning.model.osv.VulnerabilityOSVResponse;
import com.hps.osvscanning.analyzer.service.OsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OsvServiceImpl implements OsvService {
    @Autowired
    private OsvClient osvClient;
    @Autowired
    private LibraryMapper libraryMapper;

    @Override
    public VulnerabilityOSVResponse findVulnerabilities(Library libraryInfo) {
        LibraryOSVRequest libraryOSVRequest = libraryMapper.libraryToOSVRequest(libraryInfo);
        return osvClient.getVulnerability(libraryOSVRequest);
    }

    @Override
    public VulnerabilityOSVBatchResponse findVulnerabilities(List<Library> libraryBulk) {
        LibraryOSVBatchRequest libraryOSVBatchRequest = new LibraryOSVBatchRequest();
        List<LibraryOSVRequest> libraryOSVRequests = new ArrayList<>();
        for (Library library : libraryBulk) {
            libraryOSVRequests.add(libraryMapper.libraryToOSVRequest(library));
        }
        libraryOSVBatchRequest.setQueries(libraryOSVRequests);
        return osvClient.getVulnerabilityBulk(libraryOSVBatchRequest);
    }
}
