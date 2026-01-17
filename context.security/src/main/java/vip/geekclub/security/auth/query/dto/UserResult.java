package vip.geekclub.security.auth.query.dto;

import vip.geekclub.security.auth.common.UserType;

public record UserResult(Long id, UserType userType, boolean isAdmin) {// 其他查询需要的字段
}