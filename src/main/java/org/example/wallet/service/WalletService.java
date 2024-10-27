package org.example.wallet.service;

import org.example.wallet.dto.WalletOperation;

public interface WalletService {

    void performOperation(WalletOperation reqData);

}
