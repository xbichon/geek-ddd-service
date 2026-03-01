package vip.geekclub.security.domain.authentication.model;

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

    @Column(name = "principal_id")
    private Long principalId;

    private String userType;

    public Identifier(String value, String type, String userType, Long principalId) {
        this.principalId = principalId;
        this.value = value;
        this.type = type;
        this.userType = userType;
    }

}