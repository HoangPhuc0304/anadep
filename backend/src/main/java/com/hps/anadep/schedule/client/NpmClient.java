//package com.hps.osvscanning.schedule.client;
//
//import com.hps.osvscanning.model.osv.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.URI;
//
//@Component
//public class NpmClient {
//    @Value("${url.osvStorage.npm}")
//    private String url;
//    @Autowired
//    private WebClient.Builder webClientBuilder;
//
//    public Storage getVulnerability() throws UnsupportedEncodingException {
//        URI uri = URI.create(url + "/o/npm%2Fall.zip");
//        return webClientBuilder.build().get()
//                .uri(uri)
//                .retrieve().bodyToMono(Storage.class).block();
//    }
//
//    public byte[] getVulnerabilityBulk(String downloadLink) {
//        URI uri = URI.create(downloadLink);
//        return webClientBuilder.build().get()
//                .uri(uri)
//                .accept(MediaType.APPLICATION_OCTET_STREAM)
//                .retrieve()
//                .bodyToFlux(DataBuffer.class)
//                .reduce(new ByteArrayOutputStream(), (outputStream, dataBuffer) -> {
//                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
//                    dataBuffer.read(bytes);
//                    DataBufferUtils.release(dataBuffer);
//                    try {
//                        outputStream.write(bytes);
//                    } catch (Exception e) {
//                        throw new RuntimeException("Error reading response body", e);
//                    }
//                    return outputStream;
//                })
//                .map(ByteArrayOutputStream::toByteArray)
//                .block();
//    }
//}
