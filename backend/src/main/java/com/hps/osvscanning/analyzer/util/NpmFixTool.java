package com.hps.osvscanning.analyzer.util;

import com.hps.osvscanning.model.Fix;
import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.osv.Affected;
import com.hps.osvscanning.model.osv.Event;
import com.hps.osvscanning.model.osv.Range;
import com.hps.osvscanning.model.osv.Vulnerability;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Component
public class NpmFixTool implements FixTool {
    @Override
    public Fix findFix(Vulnerability vul, Library library, Boolean enabledAvailableFix, Boolean enabledCustomFix) {
        Set<String> effectedVersions = new HashSet<>();
        Set<String> effectedVersionsInRange = new HashSet<>();
        for (Affected affected : vul.getAffected()) {
            if (!affected.getLibraryEcosystem().getName().equals(library.getName())) {
                continue;
            }
            if (!CollectionUtils.isEmpty(affected.getVersions())) {
                effectedVersions.addAll(affected.getVersions());
                effectedVersionsInRange.addAll(affected.getVersions());
            }

            if (!enabledAvailableFix) {
                continue;
            }
            for (Range range : affected.getRanges()) {
                for (Event event : range.getEvents()) {
                    if (StringUtils.hasText(event.getFixed())) {
                        return new Fix(
                                new Library(
                                        library.getName(),
                                        event.getFixed(),
                                        library.getEcosystem()
                                )
                        );
                    }
                }
            }
        }
        if (enabledCustomFix) {
            return findCustomFix(library, effectedVersions, effectedVersionsInRange);
//            if (fix != null) {
//                VulnerabilityList vulnerabilityList = osvService.findVulnerabilities(fix.getLibraryInfo());
//                if (vulnerabilityList.getVulns() == null) {
//                    return fix;
//                }
//            }
        }
        return null;
    }

    @Override
    public Fix findCustomFix(Library library, Set<String> versions, Set<String> versionsInRange) {
        return null;
    }
}
