package me.fmroz.shoppi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.fmroz.shoppi.exception.CompanyInfoAlreadyExistsException;
import me.fmroz.shoppi.exception.CompanyInfoNotFoundException;
import me.fmroz.shoppi.exception.DifferentUserShippingInfoException;
import me.fmroz.shoppi.exception.UserNotFoundException;
import me.fmroz.shoppi.model.CompanyInfo;
import me.fmroz.shoppi.model.ShoppiUser;
import me.fmroz.shoppi.repository.CompanyInfoRepository;
import me.fmroz.shoppi.repository.ShoppiUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CompanyInfoService {

    private final CompanyInfoRepository companyInfoRepository;
    private final ShoppiUserRepository shoppiUserRepository;

    @Transactional
    public CompanyInfo addCompanyInfo(Long userId, CompanyInfo companyInfo) {
        ShoppiUser user = findUserById(userId);

        if (user.getCompanyInfo() != null) {
            throw new CompanyInfoAlreadyExistsException("User with ID " + userId + " already has company information");
        }

        companyInfo.setUser(user);
        return companyInfoRepository.save(companyInfo);
    }

    @Transactional
    public CompanyInfo findCompanyInfoById(Long companyInfoId) {
        return companyInfoRepository.findById(companyInfoId).orElseThrow(() -> new CompanyInfoNotFoundException("Company Info with ID " + companyInfoId + " not found"));
    }

    @Transactional
    public CompanyInfo updateCompanyInfo(Long userId, CompanyInfo updatedCompanyInfo) {
        ShoppiUser user = findUserById(userId);

        CompanyInfo existingCompanyInfo = validateUserOwnership(userId);

        if (StringUtils.hasText(updatedCompanyInfo.getCompanyName())) {
            existingCompanyInfo.setCompanyName(updatedCompanyInfo.getCompanyName());
        }
        if (StringUtils.hasText(updatedCompanyInfo.getPostalCode())) {
            existingCompanyInfo.setPostalCode(updatedCompanyInfo.getPostalCode());
        }
        if (StringUtils.hasText(updatedCompanyInfo.getCity())) {
            existingCompanyInfo.setCity(updatedCompanyInfo.getCity());
        }
        if (StringUtils.hasText(updatedCompanyInfo.getStreet())) {
            existingCompanyInfo.setStreet(updatedCompanyInfo.getStreet());
        }
        if (StringUtils.hasText(updatedCompanyInfo.getCountry())) {
            existingCompanyInfo.setCountry(updatedCompanyInfo.getCountry());
        }
        if (StringUtils.hasText(updatedCompanyInfo.getPhone())) {
            existingCompanyInfo.setPhone(updatedCompanyInfo.getPhone());
        }
        existingCompanyInfo.setUser(user);

        return companyInfoRepository.save(existingCompanyInfo);
    }

    private ShoppiUser findUserById(Long userId) {
        return shoppiUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    private CompanyInfo validateUserOwnership(Long userId) {
        return companyInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new DifferentUserShippingInfoException("Company information for user with ID " + userId + " not found"));
    }

}
