package org.mp.controller;

import org.mp.bean.Coordinate;
import org.mp.kafka.CoordinateProducer;
import org.mp.service.RegionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by MP on 23/3/16.
 */
@Controller
@RequestMapping("/sk")
public class PageActionMonitor {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView getIndexPage() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/coordinate", method = RequestMethod.POST)
    public ResponseEntity<String> postCoordinate(@ModelAttribute Coordinate coordinate) {
        CoordinateProducer.getInstance().produce(coordinate);
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }

    @RequestMapping(value = "/viewRegion", method = RequestMethod.GET)
    public ResponseEntity<Integer> getMaxRegion() {
        return new ResponseEntity<Integer>(RegionService.getActiveRegion(), HttpStatus.OK);
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView getMaxRegionView() {
        ModelAndView model = new ModelAndView("view");
        return model;
    }
}
