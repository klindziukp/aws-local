/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.s3.service;

import com.klindziuk.aws.local.s3.domain.FileInfo;
import com.klindziuk.aws.local.s3.repository.FileInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class FileInfoService {

  private final FileInfoRepository fileInfoRepository;

  @Autowired
  public FileInfoService(FileInfoRepository fileInfoRepository) {
    this.fileInfoRepository = fileInfoRepository;
  }

  public Flux<FileInfo> saveFilesInfo(Flux<FileInfo> fileInfo) {
    log.info("Saving file info: '{}'", fileInfo);
    return fileInfoRepository.saveAll(fileInfo);
  }

  public Flux<FileInfo> findAllFiles() {
    log.info("Retrieving all files info");
    return fileInfoRepository.findAll();
  }

  public Flux<FileInfo> getFileByName(String fileName) {
    log.info("Retrieving file by name '{}'", fileName);
    return fileInfoRepository.findByFileName(fileName);
  }
}
