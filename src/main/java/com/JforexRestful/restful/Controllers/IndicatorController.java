package com.JforexRestful.restful.Controllers;

import com.JforexRestful.restful.Services.IndicatorService;
import com.JforexRestful.restful.Services.IndicatorService.IndicatorResponse;
import com.dukascopy.api.IIndicators.AppliedPrice;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IndicatorController {
    @Autowired
    private IndicatorService indicatorService;

    @GetMapping("/sma")
    public IndicatorResponse getSMA(@RequestParam String instrument,
                                    @RequestParam String period,
                                    @RequestParam int timePeriod,
                                    @RequestParam(defaultValue = "CLOSE") String price,
                                    @RequestParam(defaultValue = "ASK") String side,
                                    @RequestParam(defaultValue = "0") int shift) throws JFException {
        return indicatorService.calculateSMA(Instrument.valueOf(instrument),
                        Period.valueOf(period),
                        timePeriod,
                OfferSide.valueOf(side),
                AppliedPrice.valueOf(price),
                        shift);
    }
}
