package com.chagu.splitbill.exceptions

class BillNotFoundException(val exceptionMessage: String) : RuntimeException(exceptionMessage)