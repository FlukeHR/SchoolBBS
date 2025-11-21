package com.bbs.mapper;

import com.bbs.entity.Trade;
import java.util.List;

public interface TradeMapper {
    void createTrade(Trade trade) throws Exception;
    List<Trade> listTrades() throws Exception;
    List<Trade> listTradesByAuthor(String author) throws Exception;
    void deleteTrade(int id) throws Exception;
    Trade selectById(int id) throws Exception;
    List<Trade> searchTrades(String keyword) throws Exception;
    void updateTrade(Trade trade) throws Exception;
    void addImagesColumn() throws Exception;
    void migrateImages() throws Exception;
    void fixImagesPath() throws Exception;
    void addPaymentColumns() throws Exception;
    void initPaymentStatus() throws Exception;
    int updatePaymentStatus(Trade trade) throws Exception;
    List<Trade> listTradesByBuyer(String buyer) throws Exception;
}
