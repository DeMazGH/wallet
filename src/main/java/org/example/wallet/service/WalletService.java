package org.example.wallet.service;

import org.example.wallet.dto.WalletOperation;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {

    void performOperation(WalletOperation reqData);

    BigDecimal getBalance(UUID walletId);

}
