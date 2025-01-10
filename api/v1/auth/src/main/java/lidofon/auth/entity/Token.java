package lidofon.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tokens")
public class Token {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "value",unique = true, nullable = false)
    private String value;
    @Column(name = "ip",nullable = false)
    private String ip;
    @Column(name = "user_agent",nullable = false)
    private String userAgent;
    @Column(name="user_id",nullable = false)
    private String userId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @Column(nullable = false,name = "expire_date")
    private LocalDateTime expireDate;
}
