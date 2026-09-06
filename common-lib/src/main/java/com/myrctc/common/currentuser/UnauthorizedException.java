package com.myrctc.common.currentuser;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Missing userID header in the request");
    }
}
