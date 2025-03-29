package me.fmroz.shoppi.bootstrap;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.fmroz.auth.AccountType;
import me.fmroz.shoppi.model.CompanyInfo;
import me.fmroz.shoppi.model.ShippingInfo;
import me.fmroz.shoppi.model.ShoppiUser;
import me.fmroz.shoppi.model.staticdata.Gender;
import me.fmroz.shoppi.repository.CompanyInfoRepository;
import me.fmroz.shoppi.repository.ShoppiUserRepository;
import me.fmroz.shoppi.service.ShoppiUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDataInitializer implements CommandLineRunner {

    private final ShoppiUserRepository userRepository;
    private final ShoppiUserService shoppiUserService;
    private final CompanyInfoRepository companyInfoRepository;
    private final Random random = new Random();
    @Value("${app.data.populate:false}")
    private boolean shouldPopulate;

    @Override
    @Transactional
    public void run(String... args) {
        if (!shouldPopulate) {
            log.info("User population disabled. Skipping...");
            return;
        }

        if (userRepository.count() > 0) {
            log.info("Users already exist. Skipping initialization...");
            return;
        }

        log.info("Populating test users...");

        for (int i = 1; i <= 30; i++) {
            ShoppiUser user = generateRegularUser(i);
            shoppiUserService.createUser(user);
        }

        for (int i = 31; i <= 100; i++) {
            ShoppiUser user = generateEmptySeller(i);
            ShoppiUser savedUser = shoppiUserService.createUser(user);

            CompanyInfo company = generateCompanyInfo(savedUser);
            companyInfoRepository.save(company);

            savedUser.setCompanyInfo(company);
            userRepository.save(savedUser);
        }

        log.info("100 users created: 30 users, 70 sellers");
    }


    private ShoppiUser generateEmptySeller(long id) {
        return ShoppiUser.builder()
                .email("seller" + id + "@test.com")
                .firstName("Seller" + id)
                .lastName("Corp" + id)
                .password("sellerpass")
                .gender(Gender.FEMALE)
                .accountType(AccountType.SELLER)
                .build();
    }

    private CompanyInfo generateCompanyInfo(ShoppiUser user) {
        return CompanyInfo.builder()
                .user(user)
                .companyName("Seller Company " + user.getFirstName().replace("Seller", ""))
                .postalCode("50-0" + (user.getFirstName().hashCode() % 10) + "0")
                .city("SellerCity" + user.getFirstName().replace("Seller", ""))
                .street("Market Street " + user.getFirstName().replace("Seller", ""))
                .country("Poland")
                .phone("987654321")
                .countryCode("+48")
                .build();
    }

    private ShoppiUser generateRegularUser(long id) {
        ShoppiUser user = ShoppiUser.builder()
                .email("user" + id + "@test.com")
                .firstName("User" + id)
                .lastName("Last" + id)
                .password("password123")
                .gender(Gender.MALE)
                .accountType(AccountType.USER)
                .build();

        int addressCount = 1 + random.nextInt(4);
        List<ShippingInfo> addresses = new ArrayList<>();

        for (int j = 1; j <= addressCount; j++) {
            ShippingInfo address = ShippingInfo.builder()
                    .user(user)
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .postalCode("30-0" + ((id + j) % 10) + "0")
                    .city("City" + id + "-" + j)
                    .street("Street " + j + " for User " + id)
                    .country("Poland")
                    .phone("12345678" + j)
                    .countryCode("+48")
                    .build();
            addresses.add(address);
        }

        user.setShippingAddresses(addresses);
        return user;
    }
}
