package com.hps.osvscanning.model.osv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reference {
    private String type;
    private String url;
}
