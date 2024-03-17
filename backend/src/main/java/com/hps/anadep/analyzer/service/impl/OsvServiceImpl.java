package com.hps.anadep.analyzer.service.impl;

import com.hps.anadep.analyzer.client.OsvClient;
import com.hps.anadep.model.mapper.LibraryMapper;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.osv.LibraryOSVBatchRequest;
import com.hps.anadep.model.osv.LibraryOSVRequest;
import com.hps.anadep.model.osv.VulnerabilityOSVBatchResponse;
import com.hps.anadep.model.osv.VulnerabilityOSVResponse;
import com.hps.anadep.analyzer.service.OsvService;
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
