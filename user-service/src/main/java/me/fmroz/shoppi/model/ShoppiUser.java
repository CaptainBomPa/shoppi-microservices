package me.fmroz.shoppi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import me.fmroz.auth.AccountType;
import me.fmroz.shoppi.model.staticdata.Gender;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppiUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "First name is required")
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String password;

    @Column(nullable = false, updatable = false)
    @PastOrPresent
    @Builder.Default
    private ZonedDateTime registrationDate = ZonedDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Gender gender = Gender.UNKNOWN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AccountType accountType = AccountType.USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShippingInfo> shippingAddresses;

    @OneToOne(mappedBy = "user", orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private CompanyInfo companyInfo;

    @Column(nullable = true)
    private String refreshToken;

    @PrePersist
    @PreUpdate
    private void validateAccountType() {
        if (accountType == AccountType.USER && companyInfo != null) {
            throw new IllegalStateException("USER cannot have company information.");
        }
        if (accountType == AccountType.SELLER && (shippingAddresses != null && !shippingAddresses.isEmpty())) {
            throw new IllegalStateException("SELLER cannot have shipping addresses.");
        }
    }
}
