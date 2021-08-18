/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.dynamodb.exception;

public class UnableToFindItemInfoException extends RuntimeException {

  public UnableToFindItemInfoException(String fileName) {
    super("Unable to find file with name: " + fileName);
  }
}
