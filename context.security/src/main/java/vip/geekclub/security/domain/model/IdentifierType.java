package vip.geekclub.security.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IdentifierType {

    private String type;
    private String regex;

    /**
     * 匹配标识符
     */
    public boolean match(String identifier) {
        return identifier.matches(regex);
    }
}
