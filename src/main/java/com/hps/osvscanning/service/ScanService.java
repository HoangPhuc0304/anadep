package com.hps.osvscanning.service;

import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ScanService {
    ResponseResult retrieve(Library libraryInfo);
//    ResponseResult retrieveBatch(List<Library> libraryInfo);

    ResponseResult scan(MultipartFile file, Boolean includeSafe) throws Exception;
}
