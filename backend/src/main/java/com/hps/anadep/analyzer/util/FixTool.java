package com.hps.anadep.analyzer.util;

import com.hps.anadep.model.Fix;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.osv.Vulnerability;

import java.util.Set;

public interface FixTool {
    Fix findFix(Vulnerability vul, Library library, Boolean enabledAvailableFix, Boolean enabledCustomFix);
    Fix findCustomFix(Library library, Set<String> versions, Set<String> versionsInRange);
}
