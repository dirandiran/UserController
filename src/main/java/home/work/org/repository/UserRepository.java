package home.work.org.repository;

import home.work.org.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(
            value = "SELECT * FROM users u WHERE u.id IN (?1,?2) FOR UPDATE ",
            nativeQuery = true)
    List<User> getUsersForMerge(Long firstId, Long secondId);

    @Modifying
    @Query(
            value = "DELETE FROM users u WHERE u.id IN (?1,?2) ",
            nativeQuery = true)
    void deleteMergedUsers(Long firstId, Long secondId);
}
