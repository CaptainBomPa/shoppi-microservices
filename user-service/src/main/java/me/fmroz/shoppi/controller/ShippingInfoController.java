package me.fmroz.shoppi.controller;

import lombok.RequiredArgsConstructor;
import me.fmroz.shoppi.model.ShippingInfo;
import me.fmroz.shoppi.service.ShippingInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipping-info")
@RequiredArgsConstructor
public class ShippingInfoController {

    private final ShippingInfoService shippingInfoService;

    @PostMapping("/{userId}")
    public ResponseEntity<ShippingInfo> addShippingInfo(@PathVariable Long userId, @RequestBody ShippingInfo shippingInfo) {
        return ResponseEntity.ok(shippingInfoService.addShippingInfo(userId, shippingInfo));
    }

    @PutMapping("/{userId}/{shippingInfoId}")
    public ResponseEntity<ShippingInfo> updateShippingInfo(@PathVariable Long userId, @PathVariable Long shippingInfoId, @RequestBody ShippingInfo updatedShippingInfo) {
        return ResponseEntity.ok(shippingInfoService.updateShippingInfo(userId, shippingInfoId, updatedShippingInfo));
    }

    @DeleteMapping("/{userId}/{shippingInfoId}")
    public ResponseEntity<Void> deleteShippingInfo(@PathVariable Long userId, @PathVariable Long shippingInfoId) {
        shippingInfoService.deleteShippingInfo(userId, shippingInfoId);
        return ResponseEntity.noContent().build();
    }
}
