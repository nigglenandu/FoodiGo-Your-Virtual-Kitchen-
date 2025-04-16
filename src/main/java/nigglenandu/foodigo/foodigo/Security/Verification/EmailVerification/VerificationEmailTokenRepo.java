package nigglenandu.foodigo.foodigo.Security.Verification.EmailVerification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationEmailTokenRepo extends JpaRepository<VerificationToken, Long> {
}
