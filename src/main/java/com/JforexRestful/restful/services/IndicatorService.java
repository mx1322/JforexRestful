package com.JforexRestful.restful.Services;

import com.dukascopy.api.*;
import com.dukascopy.api.IIndicators.AppliedPrice;
import com.dukascopy.api.feed.FeedDescriptor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.JforexRestful.restful.Utils.FeedDescriptorUtils;

import org.slf4j.Logger;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class IndicatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreService.class);

    @Autowired
    private CoreService coreService;

    private FeedDescriptor feedDescriptor;

    private IIndicators.AppliedPrice appliedPrice;

    public IndicatorService(CoreService coreService) {
        this.coreService = coreService;
        try {
            this.feedDescriptor = FeedDescriptorUtils.createDefaultFeedDescriptor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> T defaultIfNull(T value, T defaultValue) {
        return Optional.ofNullable(value).orElse(defaultValue);
    }

    private String createJsonResponse(Map<String, double[]> data) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = "";
        try {
            jsonResponse = mapper.writeValueAsString(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    public String calculateBollinger(
            OfferSide offerSide,
            AppliedPrice appliedPrice,
            Integer timePeriod,
            Double nbDevUp,
            Double nbDevDn,
            IIndicators.MaType maType,
            Integer shift) throws JFException {

        // Initialize default values if parameters are null or not defined

        offerSide = defaultIfNull(offerSide, feedDescriptor.getOfferSide());
        appliedPrice = defaultIfNull(appliedPrice, AppliedPrice.CLOSE);
        timePeriod = defaultIfNull(timePeriod, 20);
        nbDevUp = defaultIfNull(nbDevUp, 2.0);
        nbDevDn = defaultIfNull(nbDevDn, 2.0);
        maType = defaultIfNull(maType, IIndicators.MaType.SMA);
        shift = defaultIfNull(shift, 0);

        IIndicators indicators = coreService.getIndicators();
        if (indicators == null) {
            throw new JFException("Failed to get indicators from context");
        }

        double[] bbands = indicators.bbands(feedDescriptor, appliedPrice, offerSide, timePeriod, nbDevUp, nbDevDn, maType).calculate(shift);

        LOGGER.info("calculateBollinger results: {}", bbands);

        Map<String, double[]> response = new LinkedHashMap<>();
        response.put("Bollinger Bands", bbands);

        return createJsonResponse(response);

    }

    public String calculateStochastic(Integer fastKPeriod,
                                      Integer slowKPeriod,
                                      IIndicators.MaType KmaType,
                                      Integer slowDPeriod,
                                      IIndicators.MaType DmaType,
                                      Integer shift) throws JFException {

        fastKPeriod = defaultIfNull(fastKPeriod, 14);
        slowKPeriod = defaultIfNull(slowKPeriod, 3);
        KmaType = defaultIfNull(KmaType, IIndicators.MaType.SMA);
        slowDPeriod = defaultIfNull(slowDPeriod, 3);
        DmaType = defaultIfNull(DmaType, IIndicators.MaType.SMA);
        shift = defaultIfNull(shift, 0);

        IIndicators indicators = coreService.getIndicators();
        if (indicators == null) {
            throw new JFException("Failed to get indicators from context");
        }

        if(feedDescriptor == null) {
            throw new JFException("FeedDescriptor is null");
        }

        double[] sto = indicators.stoch(feedDescriptor, feedDescriptor.getOfferSide(), fastKPeriod, slowKPeriod, KmaType, slowDPeriod, DmaType).calculate(shift);
        LOGGER.info("calculateSto results: {}", sto);

        LOGGER.info("FeedDescriptor details: {}", feedDescriptor.toString());

        Map<String, double[]> response = new LinkedHashMap<>();
        response.put("Stochastic", sto);

        return createJsonResponse(response);
    }

    public String calculateMACD(AppliedPrice appliedPrice,
                                Integer fastPeriod,
                                Integer slowPeriod,
                                Integer signalPeriod,
                                Integer shift) throws JFException {
        appliedPrice = defaultIfNull(appliedPrice, AppliedPrice.CLOSE);
        fastPeriod = defaultIfNull(fastPeriod, 12);
        slowPeriod = defaultIfNull(slowPeriod, 26);
        signalPeriod = defaultIfNull(signalPeriod, 9);
        shift = defaultIfNull(shift, 0);

        IIndicators indicators = coreService.getIndicators();
        if (indicators == null) {
            throw new JFException("Failed to get indicators from context");
        }

        double[] macd = indicators.macd(feedDescriptor, appliedPrice, feedDescriptor.getOfferSide(), fastPeriod, slowPeriod, signalPeriod).calculate(shift);

        LOGGER.info("calculateMACD results: {}", macd);

        Map<String, double[]> response = new LinkedHashMap<>();
        response.put("MACD", macd);

        return createJsonResponse(response);
    }
}
