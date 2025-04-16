package nigglenandu.foodigo.foodigo.Security.Verification.EmailVerification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationEmailTokenRepo extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
