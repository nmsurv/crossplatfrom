package com.kfu.crossplatform.exeptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String s){
        super(s);
    }
}
