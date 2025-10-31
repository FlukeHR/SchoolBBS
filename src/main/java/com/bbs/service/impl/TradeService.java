package com.bbs.service.impl;

import com.bbs.mapper.TradeMapper;
import com.bbs.entity.Trade;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

public class TradeService implements TradeMapper {
    private SqlSessionFactory factory() throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Override
    public void createTrade(Trade trade) throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.initSchema();
            try { mapper.addImagesColumn(); } catch (Exception ignore) {}
            try { mapper.migrateImages(); } catch (Exception ignore) {}
            try { mapper.fixImagesPath(); } catch (Exception ignore) {}
            mapper.createTrade(trade);
            session.commit();
        }
    }

    @Override
    public List<Trade> listTrades() throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.initSchema();
            try { mapper.addImagesColumn(); } catch (Exception ignore) {}
            try { mapper.migrateImages(); } catch (Exception ignore) {}
            try { mapper.fixImagesPath(); } catch (Exception ignore) {}
            try { mapper.addPaymentColumns(); } catch (Exception ignore) {}
            try { mapper.initPaymentStatus(); } catch (Exception ignore) {}
            List<Trade> list = mapper.listTrades();
            session.commit();
            return list;
        }
    }

    @Override
    public List<Trade> listTradesByAuthor(String author) throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.initSchema();
            try { mapper.addImagesColumn(); } catch (Exception ignore) {}
            try { mapper.migrateImages(); } catch (Exception ignore) {}
            try { mapper.fixImagesPath(); } catch (Exception ignore) {}
            try { mapper.addPaymentColumns(); } catch (Exception ignore) {}
            try { mapper.initPaymentStatus(); } catch (Exception ignore) {}
            List<Trade> list = mapper.listTradesByAuthor(author);
            session.commit();
            return list;
        }
    }

    @Override
    public void deleteTrade(int id) throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.initSchema();
            try { mapper.addImagesColumn(); } catch (Exception ignore) {}
            try { mapper.migrateImages(); } catch (Exception ignore) {}
            try { mapper.fixImagesPath(); } catch (Exception ignore) {}
            mapper.deleteTrade(id);
            session.commit();
        }
    }

    @Override
    public void initSchema() throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.initSchema();
            try { mapper.addImagesColumn(); } catch (Exception ignore) {}
            try { mapper.migrateImages(); } catch (Exception ignore) {}
            try { mapper.fixImagesPath(); } catch (Exception ignore) {}
            session.commit();
        }
    }

    public Trade selectById(int id) throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.initSchema();
            try { mapper.addImagesColumn(); } catch (Exception ignore) {}
            try { mapper.migrateImages(); } catch (Exception ignore) {}
            try { mapper.fixImagesPath(); } catch (Exception ignore) {}
            try { mapper.addPaymentColumns(); } catch (Exception ignore) {}
            try { mapper.initPaymentStatus(); } catch (Exception ignore) {}
            Trade t = mapper.selectById(id);
            session.commit();
            return t;
        }
    }

    public List<Trade> searchTrades(String keyword) throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.initSchema();
            try { mapper.addImagesColumn(); } catch (Exception ignore) {}
            try { mapper.migrateImages(); } catch (Exception ignore) {}
            try { mapper.fixImagesPath(); } catch (Exception ignore) {}
            try { mapper.addPaymentColumns(); } catch (Exception ignore) {}
            try { mapper.initPaymentStatus(); } catch (Exception ignore) {}
            List<Trade> list = mapper.searchTrades(keyword);
            session.commit();
            return list;
        }
    }

    @Override
    public void updateTrade(Trade trade) throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.initSchema();
            try { mapper.addImagesColumn(); } catch (Exception ignore) {}
            try { mapper.migrateImages(); } catch (Exception ignore) {}
            try { mapper.fixImagesPath(); } catch (Exception ignore) {}
            mapper.updateTrade(trade);
            session.commit();
        }
    }

    @Override
    public void addImagesColumn() throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.addImagesColumn();
            session.commit();
        }
    }

    @Override
    public void migrateImages() throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.migrateImages();
            session.commit();
        }
    }

    public void fixImagesPath() throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.fixImagesPath();
            session.commit();
        }
    }

    public boolean updatePaymentStatus(int id, String status, String buyer, Timestamp paymentTime) throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.initSchema();
            try { mapper.addPaymentColumns(); } catch (Exception ignore) {}
            
            Trade trade = new Trade();
            trade.setId(id);
            trade.setStatus(status);
            trade.setBuyer(buyer);
            trade.setPaymentTime(paymentTime);
            
            int result = mapper.updatePaymentStatus(trade);
            session.commit();
            return result > 0;
        }
    }

    @Override
    public void addPaymentColumns() throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.addPaymentColumns();
            session.commit();
        }
    }

    @Override
    public void initPaymentStatus() throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.initPaymentStatus();
            session.commit();
        }
    }

    @Override
    public int updatePaymentStatus(Trade trade) throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            int result = mapper.updatePaymentStatus(trade);
            session.commit();
            return result;
        }
    }

    public List<Trade> listTradesByBuyer(String buyer) throws Exception {
        try (SqlSession session = factory().openSession()) {
            TradeMapper mapper = session.getMapper(TradeMapper.class);
            mapper.initSchema();
            try { mapper.addImagesColumn(); } catch (Exception ignore) {}
            try { mapper.migrateImages(); } catch (Exception ignore) {}
            try { mapper.fixImagesPath(); } catch (Exception ignore) {}
            try { mapper.addPaymentColumns(); } catch (Exception ignore) {}
            try { mapper.initPaymentStatus(); } catch (Exception ignore) {}
            List<Trade> list = mapper.listTradesByBuyer(buyer);
            session.commit();
            return list;
        }
    }
}
