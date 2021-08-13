/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.s3.api.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.klindziuk.aws.local.s3.constant.PathVariableParam;
import com.klindziuk.aws.local.s3.domain.FileInfo;
import com.klindziuk.aws.local.s3.service.AwsS3Service;
import com.klindziuk.aws.local.s3.service.FileInfoService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Configuration
@Slf4j
public class AwsS3Handler {

  private final AwsS3Service awsS3Service;
  private final FileInfoService fileInfoService;

  @Autowired
  public AwsS3Handler(AwsS3Service awsS3Service, FileInfoService fileInfoService) {
    this.awsS3Service = awsS3Service;
    this.fileInfoService = fileInfoService;
  }

  @NonNull
  public Mono<ServerResponse> fileInfoFromDb(ServerRequest request) {
    final String fileName = request.pathVariable(PathVariableParam.NAME);
    Flux<FileInfo> fileInfo = fileInfoService.getFileByName(fileName);
    return ok().contentType(APPLICATION_JSON)
        .body(fileInfo, FileInfo.class)
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  @NonNull
  public Mono<ServerResponse> viewAllFromDb(ServerRequest request) {
    return ok().contentType(APPLICATION_JSON).body(fileInfoService.findAllFiles(), FileInfo.class);
  }

  @NonNull
  public Mono<ServerResponse> viewFromS3(ServerRequest request) {
    return ok().contentType(APPLICATION_JSON)
        .body(Flux.fromIterable(awsS3Service.listObjects()), S3ObjectSummary.class);
  }

  @NonNull
  public Mono<ServerResponse> upload2S3(ServerRequest request) {
    return request
        .multipartData()
        .flatMap(
            pMultiValueMap -> {
              // get files from request
              List<Part> file = pMultiValueMap.get(PathVariableParam.FILES);
              // upload files to s3
              Flux<FileInfo> uploadResult =
                  Flux.fromIterable(file)
                      .cast(FilePart.class)
                      .flatMap(
                          pFilePart -> {
                            String fileName = pFilePart.filename();
                            return pFilePart
                                .content()
                                .filter(
                                    pDataBuffer ->
                                        new byte[pDataBuffer.readableByteCount()].length > 0)
                                .flatMap(
                                    pDataBuffer -> {
                                      log.info("Upload file '{}' started", fileName);
                                      FileInfo fileInfo = new FileInfo();
                                      try {
                                        byte[] data = new byte[pDataBuffer.readableByteCount()];
                                        pDataBuffer.read(data);
                                        fileInfo = awsS3Service.uploadObjectToS3(fileName, data);
                                        log.info("Upload file '{}' finished", fileName);
                                      } catch (Exception ex) {
                                        log.info("Upload file '{}' failed", fileName, ex);
                                      }
                                      return Mono.just(fileInfo);
                                    });
                          });
              Flux<FileInfo> fileInfoFlux = fileInfoService.saveFilesInfo(uploadResult);
              return ok().contentType(APPLICATION_JSON).body(fileInfoFlux, FileInfo.class);
            });
  }

  @NonNull
  public Mono<ServerResponse> downloadFromS3(ServerRequest request) {
    String name = request.pathVariable(PathVariableParam.NAME);
    InputStreamResource inputStreamResource =
        new InputStreamResource(awsS3Service.downloadFileFromS3Bucket(name));
    return ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(BodyInserters.fromResource(inputStreamResource));
  }
}
