package org.example.wallet.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletOperation(UUID valletId, OperationType operationType, BigDecimal amount) {
}
