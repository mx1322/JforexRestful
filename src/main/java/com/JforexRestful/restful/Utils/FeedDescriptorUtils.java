package com.JforexRestful.restful.Utils;

import com.JforexRestful.restful.Services.CoreService;
import com.dukascopy.api.*;
import com.dukascopy.api.feed.FeedDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedDescriptorUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreService.class);

    public static FeedDescriptor createDefaultFeedDescriptor() {
        FeedDescriptor feedDescriptor = new FeedDescriptor();
        feedDescriptor.setInstrument(Instrument.EURUSD);
        feedDescriptor.setPeriod(Period.FIFTEEN_MINS);
        feedDescriptor.setFilter(Filter.NO_FILTER);
        feedDescriptor.setOfferSide(OfferSide.BID);
        feedDescriptor.setDataType(DataType.TIME_PERIOD_AGGREGATION);
        return feedDescriptor;
    }
    public static void printFeedDescriptor(FeedDescriptor feedDescriptor) {
        LOGGER.info("FeedDescriptor details: Instrument={}, Period={}, Filter={}, OfferSide={}",
                feedDescriptor.getInstrument(), feedDescriptor.getPeriod(),
                feedDescriptor.getFilter(), feedDescriptor.getOfferSide());
    }
}
