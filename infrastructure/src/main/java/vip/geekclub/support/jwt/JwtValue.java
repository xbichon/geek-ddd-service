package vip.geekclub.support.jwt;

public record JwtValue<T>(String id, T data) {
}
