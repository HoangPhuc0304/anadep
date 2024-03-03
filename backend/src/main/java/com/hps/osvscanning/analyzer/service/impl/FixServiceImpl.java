package com.hps.osvscanning.analyzer.service.impl;

import com.hps.osvscanning.analyzer.service.FixService;
import com.hps.osvscanning.analyzer.util.FixTool;
import com.hps.osvscanning.analyzer.util.MavenFixTool;
import com.hps.osvscanning.analyzer.util.NpmFixTool;
import com.hps.osvscanning.model.Fix;
import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.enums.Ecosystem;
import com.hps.osvscanning.model.osv.Vulnerability;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class FixServiceImpl implements FixService {
    @Value("${library.available-fix.enable}")
    private Boolean enabledAvailableFix;
    @Value("${library.custom-fix.enable}")
    private Boolean enabledCustomFix;

    @Override
    public Fix findFix(Vulnerability vul, Library library) {
        FixTool fixTool;
        switch (Ecosystem.getEcosystem(library.getEcosystem())) {
            case MAVEN -> {
                fixTool = new MavenFixTool();
            }
            case NPM -> {
                fixTool = new NpmFixTool();
            }
            default -> {
                return null;
            }
        }
        return fixTool.findFix(vul, library, enabledAvailableFix, enabledCustomFix);
    }

    @Override
    public Fix findCustomFix(Library library, Set<String> versions, Set<String> versionsInRange) {
        FixTool fixTool;
        switch (Ecosystem.getEcosystem(library.getEcosystem())) {
            case MAVEN -> {
                fixTool = new MavenFixTool();
            }
            case NPM -> {
                fixTool = new NpmFixTool();
            }
            default -> {
                return null;
            }
        }
        return fixTool.findCustomFix(library, versions, versionsInRange);
    }
}
