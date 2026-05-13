package ai.userservice.user;


import jakarta.persistence.*;


@Entity
@Table(name = "user_entity")
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;


    private String username;

    private String displayName;

    private String password;

    private String email;

    private Long userId;




    public UserEntity(Long id, String username, String password, String displayName, String email, Long userId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.email = email;
        this.userId = userId;



    }
    public UserEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }






}
