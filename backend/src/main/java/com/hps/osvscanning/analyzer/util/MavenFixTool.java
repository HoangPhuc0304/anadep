package com.hps.osvscanning.analyzer.util;

import com.hps.osvscanning.analyzer.service.MavenService;
import com.hps.osvscanning.model.Fix;
import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.mvn.ArtifactDoc;
import com.hps.osvscanning.model.mvn.LibraryBulk;
import com.hps.osvscanning.model.osv.Affected;
import com.hps.osvscanning.model.osv.Event;
import com.hps.osvscanning.model.osv.Range;
import com.hps.osvscanning.model.osv.Vulnerability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MavenFixTool implements FixTool {
    @Autowired
    private MavenService mavenService;

    private static final int MAX_LIBRARY_RECORDS = 200;

    @Override
    public Fix findFix(Vulnerability vul, Library library, Boolean enabledAvailableFix, Boolean enabledCustomFix) {
        Set<String> effectedVersions = new HashSet<>();
        Set<String> effectedVersionsInRange = new HashSet<>();
        for (Affected affected : vul.getAffected()) {
            if (!affected.getLibraryEcosystem().getName().equals(library.getName()) ||
                    CollectionUtils.isEmpty(affected.getVersions())) {
                continue;
            }
            effectedVersions.addAll(affected.getVersions());
            if (!affected.getVersions().contains(library.getVersion())) {
                continue;
            }
            effectedVersionsInRange.addAll(affected.getVersions());

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
        int allVersions = mavenService.getLibraryCount(library);
        if (allVersions > MAX_LIBRARY_RECORDS) {
            allVersions = MAX_LIBRARY_RECORDS;
        }
        LibraryBulk libraryBulk = mavenService.findLibrary(library, allVersions);
        List<ArtifactDoc> docs = libraryBulk.getResponse().getDocs();
        if (CollectionUtils.isEmpty(docs)) {
            return null;
        }

        int idx = 0;
        for (int i = docs.size() - 1; i >= 0; i--) {
            versionsInRange.remove(docs.get(i).getV());
            if (versionsInRange.isEmpty()) {
                idx = i;
                break;
            }
        }

        Fix defaultFix = new Fix(
                new Library(
                        library.getName(),
                        docs.get(0).getV(),
                        library.getEcosystem()
                )
        );

        for (int i = idx - 1; i >= 0; i--) {
            if (!versions.contains(docs.get(i).getV())) {
                return new Fix(
                        new Library(
                                library.getName(),
                                docs.get(i).getV(),
                                library.getEcosystem()
                        )
                );
            }
        }
        return defaultFix;
    }
}
