package com.hps.osvscanning.analyzer.util;

import com.hps.osvscanning.model.Fix;
import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.osv.Vulnerability;

import java.util.Set;

public interface FixTool {
    Fix findFix(Vulnerability vul, Library library, Boolean enabledAvailableFix, Boolean enabledCustomFix);
    Fix findCustomFix(Library library, Set<String> versions, Set<String> versionsInRange);
}
