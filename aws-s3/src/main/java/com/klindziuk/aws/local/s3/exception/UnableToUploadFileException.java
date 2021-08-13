/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.s3.exception;

public class UnableToUploadFileException extends RuntimeException {

  public UnableToUploadFileException(String fileName) {
    super("Unable to upload file with name: " + fileName);
  }
}
