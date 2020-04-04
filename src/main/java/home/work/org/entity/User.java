package home.work.org.entity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "users")
public class User implements Comparable<User> {
    private static Map<String, Integer> riskProfilePriority;

    static {
        riskProfilePriority = new HashMap<>();
        riskProfilePriority.put("LOW", 0);
        riskProfilePriority.put("NORMAL", 1);
        riskProfilePriority.put("HIGH", 2);
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private RiskProfile riskProfile;

    public User() {
    }

    public User(Long id, RiskProfile riskProfile) {
        this.id = id;
        this.riskProfile = riskProfile;
    }

    public User(RiskProfile riskProfile) {
        this.riskProfile = riskProfile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RiskProfile getRiskProfile() {
        return riskProfile;
    }

    public void setRiskProfile(RiskProfile riskProfile) {
        this.riskProfile = riskProfile;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", riskProfile='" + riskProfile + '\'' +
                '}';
    }

    @Override
    public int compareTo(User otherUser) {
        int tiskPriorityThis = riskProfilePriority.get(this.getRiskProfile().toString());
        int tiskPriorityOther = riskProfilePriority.get(otherUser.getRiskProfile().toString());
        if (tiskPriorityThis > tiskPriorityOther) {
            return 1;
        } else if (tiskPriorityThis < tiskPriorityOther) {
            return -1;
        } else {
            return 0;
        }
    }
}
