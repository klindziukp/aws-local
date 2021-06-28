/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.s3.exception;

public class UnableToFindFileException extends RuntimeException {

  public UnableToFindFileException(String fileName) {
    super("Unable to find file with name: " + fileName);
  }
}
