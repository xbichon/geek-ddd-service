package vip.geekclub.security.domain.service;

import org.springframework.stereotype.Service;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.security.domain.model.IdentifierRule;
import vip.geekclub.security.domain.value.IdentifierType;
import vip.geekclub.security.domain.value.IdentifierValue;

import java.util.List;
import java.util.Optional;

@Service
public class IdentifierValidate {

    List<IdentifierRule> identifierTypes = List.of(
            new IdentifierRule(IdentifierType.PHONE, "^1[3-9]\\d{9}$"),
            new IdentifierRule(IdentifierType.EMAIL, "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"),
            new IdentifierRule(IdentifierType.USERNAME, "^(?!\\d{11}$)(?![^@]*@.*$)[a-zA-Z0-9_]{4,16}$")
    );

    /**
     * 验证标识符
     *
     * @param identifierValue 标识符
     */
    public void validate(IdentifierValue identifierValue) {
        Optional<IdentifierRule> identifierType = identifierTypes.stream()
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