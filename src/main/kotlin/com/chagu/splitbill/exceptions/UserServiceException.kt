package com.chagu.splitbill.exceptions

class UserServiceException(val exceptionMessage: String) : RuntimeException(exceptionMessage)