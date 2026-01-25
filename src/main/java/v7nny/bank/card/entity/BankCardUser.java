package v7nny.bank.card.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Users")
public class BankCardUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<BankCard> bankCards;

    @ManyToMany
    @JoinTable(
            name = "Users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    public BankCardUser() {}

    public BankCardUser(String username, String password, String email, List<BankCard> bankCards) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.bankCards = bankCards;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<BankCard> getBankCards() {
        return bankCards;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BankCardUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
