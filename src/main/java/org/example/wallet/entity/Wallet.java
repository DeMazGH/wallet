package org.example.wallet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    private UUID walletId;

    private BigDecimal amount;

    public Wallet() {
    }

    public Wallet(UUID walletId, BigDecimal amount) {
        this.walletId = walletId;
        this.amount = amount;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Wallet wallet = (Wallet) o;
        return Objects.equals(walletId, wallet.walletId) && Objects.equals(amount, wallet.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(walletId, amount);
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "walletId=" + walletId +
                ", amount=" + amount +
                '}';
    }

}
