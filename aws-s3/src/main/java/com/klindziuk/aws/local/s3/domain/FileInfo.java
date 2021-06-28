/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.s3.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Table("file_info")
public class FileInfo {

  @Id private Long id;
  private String fileName;
  private String fileUrl;
  private boolean isUploadSuccessFull;

  public FileInfo(String fileName, String fileUrl, boolean isUploadSuccessFull) {
    this.fileName = fileName;
    this.fileUrl = fileUrl;
    this.isUploadSuccessFull = isUploadSuccessFull;
  }
}
