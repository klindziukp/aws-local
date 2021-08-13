/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.s3.repository;

import com.klindziuk.aws.local.s3.domain.FileInfo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface FileInfoRepository extends ReactiveCrudRepository<FileInfo, Long> {

  Flux<FileInfo> findByFileName(String name);
}
