package vip.geekclub.security.domain.service;

import org.springframework.stereotype.Service;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.security.domain.model.IdentifierType;
import vip.geekclub.security.domain.value.IdentifierValue;

import java.util.List;
import java.util.Optional;

@Service
public class IdentifierValidate {

    List<IdentifierType> identifierTypes = List.of(
            new IdentifierType("phone", "^1[3-9]\\d{9}$"),
            new IdentifierType("email", "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"),
            new IdentifierType("username", "^(?!\\d{11}$)(?![^@]*@.*$)[a-zA-Z0-9_]{4,16}$")
    );

    /**
     * 验证标识符
     *
     * @param identifierValue 标识符
     * @return 是否合法
     */
    public void validate(IdentifierValue identifierValue) {
        Optional<IdentifierType> identifierType = identifierTypes.stream()
                .filter(type -> type.getType().equals(identifierValue.type()))
                .findFirst();

        if (identifierType.isPresent() && !identifierType.get().match(identifierValue.value())) {
            throw new BusinessException(400, "标识符格式错误");
        }
    }

    /**
     * 批量验证标识符
     */
    public void validate(List<IdentifierValue> identifierValues) {
        identifierValues.forEach(this::validate);
    }
}