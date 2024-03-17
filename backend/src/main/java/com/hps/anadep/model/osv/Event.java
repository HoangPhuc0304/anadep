package com.hps.anadep.model.osv;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private String introduced;
    private String fixed;
    @JsonProperty("last_affected")
    private String lastAffected;
    private String limit;
}
