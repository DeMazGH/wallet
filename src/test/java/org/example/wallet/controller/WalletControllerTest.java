package org.example.wallet.controller;

import org.example.wallet.dto.OperationType;
import org.example.wallet.entity.Wallet;
import org.example.wallet.repository.WalletRepository;
import org.example.wallet.service.WalletServiceImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private WalletServiceImpl service;

    @MockBean
    private WalletRepository repository;

    @Test
    void shouldReturnOkInPerformOperationMethod() throws Exception {
        UUID walletId = UUID.randomUUID();
        OperationType operationType = OperationType.DEPOSIT;
        BigDecimal reqAmount = BigDecimal.valueOf(100);
        BigDecimal amount = BigDecimal.valueOf(300);
        JSONObject reqDataJson = createWalletOperationJson(walletId, operationType, reqAmount);
        Wallet oldWalletObject = new Wallet(walletId, amount);
        Wallet newWalletObject = new Wallet(walletId, amount.add(reqAmount));

        Mockito.when(repository.findWalletByWalletId(walletId))
               .thenReturn(oldWalletObject);

        mockMvc.perform(MockMvcRequestBuilders
                       .post("/api/v1/wallet")
                       .content(reqDataJson.toString())
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());

        verify(repository, atLeastOnce()).findWalletByWalletId(walletId);
        verify(repository, atLeastOnce()).save(newWalletObject);
    }

    @Test
    void shouldReturnNotFoundInPerformOperationMethod() throws Exception {
        UUID walletId = UUID.randomUUID();
        OperationType operationType = OperationType.DEPOSIT;
        BigDecimal reqAmount = BigDecimal.valueOf(100);
        JSONObject reqDataJson = createWalletOperationJson(walletId, operationType, reqAmount);

        Mockito.when(repository.findWalletByWalletId(walletId))
               .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                       .post("/api/v1/wallet")
                       .content(reqDataJson.toString())
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());

        verify(repository, atLeastOnce()).findWalletByWalletId(walletId);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldReturnBadRequestInPerformOperationMethod() throws Exception {
        UUID walletId = UUID.randomUUID();
        OperationType operationType = OperationType.WITHDRAW;
        BigDecimal reqAmount = BigDecimal.valueOf(500);
        BigDecimal amount = BigDecimal.valueOf(300);
        JSONObject reqDataJson = createWalletOperationJson(walletId, operationType, reqAmount);
        Wallet oldWalletObject = new Wallet(walletId, amount);

        Mockito.when(repository.findWalletByWalletId(walletId))
               .thenReturn(oldWalletObject);

        mockMvc.perform(MockMvcRequestBuilders
                       .post("/api/v1/wallet")
                       .content(reqDataJson.toString())
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest());

        verify(repository, atLeastOnce()).findWalletByWalletId(walletId);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldReturnOkInGetBalanceMethod() throws Exception {
        UUID walletId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(500);
        Wallet walletObject = new Wallet(walletId, amount);

        Mockito.when(repository.findWalletByWalletId(walletId))
               .thenReturn(walletObject);

        mockMvc.perform(MockMvcRequestBuilders
                       .get("/api/v1/wallets/{walletId}", walletId)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string(amount.toString()));

        verify(repository, atLeastOnce()).findWalletByWalletId(walletId);
    }

    @Test
    void shouldReturnNotFoundInGetBalanceMethod() throws Exception {
        UUID walletId = UUID.randomUUID();

        Mockito.when(repository.findWalletByWalletId(walletId))
               .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                       .get("/api/v1/wallets/{walletId}", walletId)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());

        verify(repository, atLeastOnce()).findWalletByWalletId(walletId);
    }

    private JSONObject createWalletOperationJson(UUID walletId,
                                                 OperationType operationType,
                                                 BigDecimal amount) throws JSONException {
        JSONObject reqDataJson = new JSONObject();
        reqDataJson.put("valletId", walletId.toString());
        reqDataJson.put("operationType", operationType.toString());
        reqDataJson.put("amount", amount);

        return reqDataJson;
    }

}