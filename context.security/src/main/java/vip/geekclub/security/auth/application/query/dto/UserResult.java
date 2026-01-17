package vip.geekclub.security.auth.application.query.dto;

import vip.geekclub.security.auth.domain.UserType;

public record UserResult(Long id, UserType userType, boolean isAdmin) {// 其他查询需要的字段
}