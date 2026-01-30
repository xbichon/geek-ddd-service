package vip.geekclub.security.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "security_identifier")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Identifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String value;
    private String type;
    private Long credentialId;
    private String userType;

    public Identifier(Long credentialId, String value, String type, String userType) {
        this.value = value;
        this.type = type;
        this.credentialId = credentialId;
        this.userType = userType;
    }

}
