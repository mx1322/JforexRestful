package com.JforexRestful.restful.Services;

import com.dukascopy.api.*;
import com.dukascopy.api.IIndicators.AppliedPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndicatorService {

    @Autowired
    private CoreService coreService;

    public IndicatorService(CoreService coreService) {
        this.coreService = coreService;
    }

    public IndicatorResponse calculateSMA(Instrument instrument, Period period, int timePeriod, OfferSide side, AppliedPrice price, int shift) throws JFException {
        IIndicators indicators = coreService.getIndicators();
        if (indicators == null) {
            throw new JFException("Failed to get indicators from context");
        }

        // Check if the side, price, and shift parameters were provided by the user, if not, assign default values
        if (side == null) {
            side = OfferSide.BID; // default side
        }

        if (price == null) {
            price = AppliedPrice.CLOSE; // default applied price
        }

        if (shift < 0) {
            shift = 0; // default shift
        }

        double sma = indicators.sma(instrument, period, side, price, timePeriod, shift);
        return new IndicatorResponse("SMA", instrument.name(), period.name(), timePeriod, side.name(), price.name(), shift, sma);
    }

    public static class IndicatorResponse {
        private String indicatorName;
        private String instrument;
        private String period;
        private int timePeriod;
        private String side;
        private String price;
        private int shift;
        private double result;

        public IndicatorResponse(String indicatorName, String instrument, String period, int timePeriod, String side, String price, int shift, double result) {
            this.indicatorName = indicatorName;
            this.instrument = instrument;
            this.period = period;
            this.timePeriod = timePeriod;
            this.side = side;
            this.price = price;
            this.shift = shift;
            this.result = result;
        }

        public String getIndicatorName() {
            return indicatorName;
        }

        public void setIndicatorName(String indicatorName) {
            this.indicatorName = indicatorName;
        }

        public String getInstrument() {
            return instrument;
        }

        public void setInstrument(String instrument) {
            this.instrument = instrument;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public int getTimePeriod() {
            return timePeriod;
        }

        public void setTimePeriod(int timePeriod) {
            this.timePeriod = timePeriod;
        }

        public String getSide() {
            return side;
        }

        public void setSide(String side) {
            this.side = side;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getShift() {
            return shift;
        }

        public void setShift(int shift) {
            this.shift = shift;
        }

        public double getResult() {
            return result;
        }

        public void setResult(double result) {
            this.result = result;
        }
    }
}
