package com.hps.anadepscheduler.model.osv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Range {
    private String type;
    private List<Event> events;
}
