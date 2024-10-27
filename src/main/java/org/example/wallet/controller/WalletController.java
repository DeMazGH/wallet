package org.example.wallet.controller;

import org.example.wallet.dto.WalletOperation;
import org.example.wallet.exception.InsufficientFundsException;
import org.example.wallet.exception.UnsupportedOperationException;
import org.example.wallet.exception.WalletDoesNotExistException;
import org.example.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class WalletController {

    private final WalletService service;

    public WalletController(WalletService service) {
        this.service = service;
    }

    @PostMapping("/wallet")
    public ResponseEntity<?> performOperation(@RequestBody WalletOperation reqData) {

        try {
            service.performOperation(reqData);
        } catch (WalletDoesNotExistException e) {

            return ResponseEntity.notFound()
                                 .build();
        } catch (InsufficientFundsException | UnsupportedOperationException e) {

            return ResponseEntity.badRequest()
                                 .build();
        } catch (Exception e) {

            return ResponseEntity.internalServerError()
                                 .build();
        }

        return ResponseEntity.ok()
                             .build();
    }

}
