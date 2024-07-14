package in.ashokit.repo;

import in.ashokit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmailAndOtp(String email, String otp);
}
