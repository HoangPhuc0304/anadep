package com.hps.anadep.analyzer.service.impl;

import com.hps.anadep.analyzer.client.AnadepClient;
import com.hps.anadep.analyzer.service.AnadepService;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.mapper.LibraryMapper;
import com.hps.anadep.model.osv.LibraryOSVBatchRequest;
import com.hps.anadep.model.osv.LibraryOSVRequest;
import com.hps.anadep.model.osv.VulnerabilityOSVBatchResponse;
import com.hps.anadep.model.osv.VulnerabilityOSVResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AnadepServiceImpl implements AnadepService {
    @Autowired
    private AnadepClient anadepClient;
    @Autowired
    private LibraryMapper libraryMapper;

    @Override
    public VulnerabilityOSVResponse findVulnerabilities(Library libraryInfo) {
        LibraryOSVRequest libraryOSVRequest = libraryMapper.libraryToOSVRequest(libraryInfo);
        return anadepClient.getVulnerability(libraryOSVRequest);
    }

    @Override
    public VulnerabilityOSVBatchResponse findVulnerabilities(Set<Library> libraryBulk) {
        LibraryOSVBatchRequest libraryOSVBatchRequest = new LibraryOSVBatchRequest();
        List<LibraryOSVRequest> libraryOSVRequests = new ArrayList<>();
        for (Library library : libraryBulk) {
            libraryOSVRequests.add(libraryMapper.libraryToOSVRequest(library));
        }
        libraryOSVBatchRequest.setQueries(libraryOSVRequests);
        return anadepClient.getVulnerabilityBulk(libraryOSVBatchRequest);
    }
}
