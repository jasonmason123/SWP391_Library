package com.springdemo.library.services.interfaces;

public interface IAuthTokenService<Identifier> {
    String generateToken(Identifier identifierData);
    boolean validateToken(String token);
}
