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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credential_id")
    private PasswordCredential credential;

    private String userType;

    public Identifier(String value, String type, String userType, PasswordCredential credential) {
        this.credential = credential;
        this.value = value;
        this.type = type;
        this.userType = userType;
    }

}