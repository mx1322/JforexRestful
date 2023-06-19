package com.JforexRestful.restful.Controllers;

import com.JforexRestful.restful.Services.IndicatorService;
import com.dukascopy.api.IIndicators.MaType;
import com.dukascopy.api.IIndicators.AppliedPrice;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.Period;
import com.dukascopy.api.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/indicators")
public class IndicatorController {

    @Autowired
    private IndicatorService indicatorService;

    @GetMapping("/bollinger")
    public String calculateBollinger() throws JFException {
        return indicatorService.calculateBollinger();
    }
}
