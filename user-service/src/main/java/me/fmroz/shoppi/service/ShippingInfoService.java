package me.fmroz.shoppi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.fmroz.shoppi.exception.DifferentUserShippingInfoException;
import me.fmroz.shoppi.exception.ShippingInfoNotFoundException;
import me.fmroz.shoppi.exception.UserNotFoundException;
import me.fmroz.shoppi.model.ShippingInfo;
import me.fmroz.shoppi.model.ShoppiUser;
import me.fmroz.shoppi.repository.ShippingInfoRepository;
import me.fmroz.shoppi.repository.ShoppiUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ShippingInfoService {

    private final ShippingInfoRepository shippingInfoRepository;
    private final ShoppiUserRepository shoppiUserRepository;

    @Transactional
    public ShippingInfo addShippingInfo(Long userId, ShippingInfo shippingInfo) {
        ShoppiUser user = findUserById(userId);
        shippingInfo.setUser(user);
        return shippingInfoRepository.save(shippingInfo);
    }

    @Transactional
    public ShippingInfo updateShippingInfo(Long userId, Long shippingInfoId, ShippingInfo updatedShippingInfo) {
        ShippingInfo existingShippingInfo = validateUserOwnership(userId, shippingInfoId);

        if (StringUtils.hasText(updatedShippingInfo.getFirstName())) {
            existingShippingInfo.setFirstName(updatedShippingInfo.getFirstName());
        }
        if (StringUtils.hasText(updatedShippingInfo.getLastName())) {
            existingShippingInfo.setLastName(updatedShippingInfo.getLastName());
        }
        if (StringUtils.hasText(updatedShippingInfo.getPostalCode())) {
            existingShippingInfo.setPostalCode(updatedShippingInfo.getPostalCode());
        }
        if (StringUtils.hasText(updatedShippingInfo.getCity())) {
            existingShippingInfo.setCity(updatedShippingInfo.getCity());
        }
        if (StringUtils.hasText(updatedShippingInfo.getStreet())) {
            existingShippingInfo.setStreet(updatedShippingInfo.getStreet());
        }
        if (StringUtils.hasText(updatedShippingInfo.getCountry())) {
            existingShippingInfo.setCountry(updatedShippingInfo.getCountry());
        }
        if (StringUtils.hasText(updatedShippingInfo.getPhone())) {
            existingShippingInfo.setPhone(updatedShippingInfo.getPhone());
        }
        if (StringUtils.hasText(updatedShippingInfo.getCountryCode())) {
            existingShippingInfo.setCountryCode(updatedShippingInfo.getCountryCode());
        }

        return shippingInfoRepository.save(existingShippingInfo);
    }

    @Transactional
    public void deleteShippingInfo(Long userId, Long shippingInfoId) {
        ShippingInfo shippingInfo = validateUserOwnership(userId, shippingInfoId);
        shippingInfoRepository.delete(shippingInfo);
    }

    private ShoppiUser findUserById(Long userId) {
        return shoppiUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    private ShippingInfo findShippingInfoById(Long shippingInfoId) {
        return shippingInfoRepository.findById(shippingInfoId)
                .orElseThrow(() -> new ShippingInfoNotFoundException("Shipping info with ID " + shippingInfoId + " not found"));
    }

    private ShippingInfo validateUserOwnership(Long userId, Long shippingInfoId) {
        ShippingInfo shippingInfo = findShippingInfoById(shippingInfoId);
        if (!shippingInfo.getUser().getId().equals(userId)) {
            throw new DifferentUserShippingInfoException("This shipping info does not belong to the user");
        }
        return shippingInfo;
    }
}
