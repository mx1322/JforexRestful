package com.JforexRestful.restful.Controllers;

import com.JforexRestful.restful.Services.IndicatorService;
import com.dukascopy.api.IIndicators;
import com.dukascopy.api.JFException;
import com.dukascopy.api.OfferSide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/indicators")
public class IndicatorController {

    @Autowired
    private IndicatorService indicatorService;

    public IndicatorController(IndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }

    @GetMapping("/bollinger")
    public String calculateBollinger(
            @RequestParam(required = false) OfferSide offerSide,
            @RequestParam(required = false) IIndicators.AppliedPrice appliedPrice,
            @RequestParam(required = false) Integer timePeriod,
            @RequestParam(required = false) Double nbDevUp,
            @RequestParam(required = false) Double nbDevDn,
            @RequestParam(required = false) IIndicators.MaType maType,
            @RequestParam(required = false) Integer shift) throws JFException {
        return indicatorService.calculateBollinger(offerSide, appliedPrice, timePeriod, nbDevUp, nbDevDn, maType, shift);
    }

    @GetMapping("/stochastic")
    public String calculateStochastic(
            @RequestParam(required = false) Integer fastKPeriod,
            @RequestParam(required = false) Integer slowKPeriod,
            @RequestParam(required = false) IIndicators.MaType KmaType,
            @RequestParam(required = false) Integer slowDPeriod,
            @RequestParam(required = false) IIndicators.MaType DmaType,
            @RequestParam(required = false) Integer shift) throws JFException {
        return indicatorService.calculateStochastic(fastKPeriod, slowKPeriod, KmaType, slowDPeriod, DmaType, shift);
    }

    @GetMapping("/macd")
    public String calculateMACD(
            @RequestParam(required = false) IIndicators.AppliedPrice appliedPrice,
            @RequestParam(required = false) Integer fastPeriod,
            @RequestParam(required = false) Integer slowPeriod,
            @RequestParam(required = false) Integer signalPeriod,
            @RequestParam(required = false) Integer shift) throws JFException {
        return indicatorService.calculateMACD(appliedPrice,fastPeriod, slowPeriod, signalPeriod, shift);
    }
}