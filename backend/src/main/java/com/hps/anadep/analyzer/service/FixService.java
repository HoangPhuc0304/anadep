package com.hps.anadep.analyzer.service;

import com.hps.anadep.model.Fix;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.osv.Vulnerability;

import java.util.Set;

public interface FixService {
    Fix findFix(Vulnerability vul, Library library);
    Fix findCustomFix(Library library, Set<String> versions, Set<String> versionsInRange);
}
