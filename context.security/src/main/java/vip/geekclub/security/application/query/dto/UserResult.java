package vip.geekclub.security.application.query.dto;

import vip.geekclub.security.domain.value.UserType;

public record UserResult(Long id, UserType userType, boolean isAdmin) {// 其他查询需要的字段
}