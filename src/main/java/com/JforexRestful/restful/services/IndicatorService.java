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
            this.appliedPrice = AppliedPrice.CLOSE;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object defaultIfNull(Object value, Object defaultValue) {
        return value != null ? value : defaultValue;
    }

    private String createJsonResponse(double[] bbands) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = "";
        try {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("Bollinger Bands", bbands);
            jsonResponse = mapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }


    public String calculateBollinger() throws JFException {
        // Initializing parameters
        Instrument instrument = Instrument.EURUSD;
        Period period = Period.FIFTEEN_MINS;
        OfferSide offerSide = OfferSide.BID;
        AppliedPrice appliedPrice = AppliedPrice.CLOSE;
        int timePeriod = 5;
        int BBtimePeriod = 20;
        double nbDevUp = 2.0;
        double nbDevDn = 2.0;
        IIndicators.MaType maType = IIndicators.MaType.SMA;
        int shift = 0;

        IIndicators indicators = coreService.getIndicators();
        if (indicators == null) {
            throw new JFException("Failed to get indicators from context");
        }

        maType = (IIndicators.MaType) defaultIfNull(maType, IIndicators.MaType.SMA);

        LOGGER.info("calculateBollinger is called with: appliedPrice={}, BBtimePeriod={}, nbDevUp={}, nbDevDn={}, maType={}, shift={}",
                appliedPrice, BBtimePeriod, nbDevUp, nbDevDn, maType, shift);

        LOGGER.info("FeedDescriptor details: {}", feedDescriptor.toString());

        //double[] bollinger = indicators.bbands(feedDescriptor, appliedPrice, feedDescriptor.getOfferSide(), BBtimePeriod, nbDevUp, nbDevDn, maType).calculate(shift);
        double[] bbands = indicators.bbands(
                instrument, period, offerSide, appliedPrice,
                timePeriod, nbDevUp, nbDevDn, maType,
                shift);

        LOGGER.info("calculateBollinger results: {}", bbands);

        double[] bbands1 = indicators.bbands(feedDescriptor, appliedPrice, offerSide, timePeriod, nbDevUp, nbDevDn, maType).calculate(shift);

        LOGGER.info("calculateBollinger results1: {}", bbands1);


        //return createJsonResponse(new Object[]{"Bollinger Bands", BBtimePeriod, maType.name(), shift, bollinger});
        return (createJsonResponse(bbands));
    }
}
