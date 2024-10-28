package org.example.wallet.service;

import org.example.wallet.dto.OperationType;
import org.example.wallet.dto.WalletOperation;
import org.example.wallet.entity.Wallet;
import org.example.wallet.exception.InsufficientFundsException;
import org.example.wallet.exception.UnsupportedOperationException;
import org.example.wallet.exception.WalletDoesNotExistException;
import org.example.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository repository;

    public WalletServiceImpl(WalletRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void performOperation(WalletOperation reqData) {
        Wallet wallet = repository.findWalletByWalletId(reqData.valletId());

        if (wallet == null) {
            throw new WalletDoesNotExistException();
        }

        if (OperationType.DEPOSIT.equals(reqData.operationType())) {
            wallet.setAmount(wallet.getAmount()
                                   .add(reqData.amount()));
            repository.save(wallet);
        } else if (OperationType.WITHDRAW.equals(reqData.operationType())) {
            if (wallet.getAmount()
                      .compareTo(reqData.amount()) < 0) {
                throw new InsufficientFundsException();
            }

            wallet.setAmount(wallet.getAmount()
                                   .subtract(reqData.amount()));
            repository.save(wallet);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public BigDecimal getBalance(UUID walletId) {
        Wallet wallet = repository.findWalletByWalletId(walletId);

        if (wallet != null) {
            return wallet.getAmount();
        } else {
            throw new WalletDoesNotExistException();
        }
    }

}
