package com.hps.osvscanning.analyzer.service;

import com.hps.osvscanning.model.Fix;
import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.osv.Vulnerability;

import java.util.Set;

public interface FixService {
    Fix findFix(Vulnerability vul, Library library);
    Fix findCustomFix(Library library, Set<String> versions, Set<String> versionsInRange);
}
