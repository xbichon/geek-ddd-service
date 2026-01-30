package vip.geekclub.test.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vip.geekclub.security.domain.model.PasswordCredential;
import vip.geekclub.security.domain.repository.PasswordCredentialRepository;

import java.util.Optional;

@SpringBootTest
public class PasswordCredentialRepositoryTest {
    @Autowired
    private  PasswordCredentialRepository passwordCredentialRepository;

    @Test
    public void test01() {
        boolean exists = passwordCredentialRepository.existsByIdentifier("admin@example.com", "teacher");
        Assertions.assertTrue(exists);
    }

    @Test
    public void test02() {
        Optional<PasswordCredential> teacher = passwordCredentialRepository.findByIdentifiersValueAndIdentifiersUserType("admin@example.com", "teacher");

    }
}
