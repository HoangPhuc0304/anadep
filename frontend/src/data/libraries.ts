import { LibraryScanUI } from '../model/library'

export const libraries: LibraryScanUI[] = [
    {
        info: {
            name: 'org.springframework.security:spring-security-core',
            version: '6.0.1',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-x873-6rgc-94jc',
            summary: 'Spring Security logout not clearing security context',
            details:
                'In Spring Security, versions 5.7.x prior to 5.7.8, versions 5.8.x prior to 5.8.3, and versions 6.0.x prior to 6.0.3, the logout support does not properly clean the security context if using serialized versions. Additionally, it is not possible to explicitly save an empty security context to the HttpSessionSecurityContextRepository. This vulnerability can keep users authenticated even after they performed logout. Users of affected versions should apply the following mitigation. 5.7.x users should upgrade to 5.7.8. 5.8.x users should upgrade to 5.8.3. 6.0.x users should upgrade to 6.0.3.',
            aliases: ['CVE-2023-20862'],
            modified: '2024-02-16T08:23:55.197987Z',
            published: '2023-04-19T21:30:26Z',
            database_specific: {
                severity: 'MODERATE',
                github_reviewed_at: '2023-05-05T22:58:08Z',
                github_reviewed: true,
                cwe_ids: ['CWE-459'],
                nvd_published_at: '2023-04-19T20:15:10Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-20862',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/spring-projects/spring-security',
                },
                {
                    type: 'WEB',
                    url: 'https://security.netapp.com/advisory/ntap-20230526-0002',
                },
                {
                    type: 'WEB',
                    url: 'https://spring.io/security/cve-2023-20862',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:L/UI:N/S:U/C:L/I:L/A:L',
                    baseScore: 6.3,
                    ranking: 'Medium',
                },
            ],
            fixed: '6.0.3',
        },
    },
    {
        info: {
            name: 'org.springframework.boot:spring-boot-autoconfigure',
            version: '3.0.2',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-xf96-w227-r7c4',
            summary: 'Spring Boot Welcome Page Denial of Service',
            details:
                "In Spring Boot versions 3.0.0 - 3.0.6, 2.7.0 - 2.7.11, 2.6.0 - 2.6.14, 2.5.0 - 2.5.14 and older unsupported versions, there is potential for a denial-of-service (DoS) attack if Spring MVC is used together with a reverse proxy cache.\n\nSpecifically, an application is vulnerable if all of the conditions are true:\n\n* The application has Spring MVC auto-configuration enabled. This is the case by default if Spring MVC is on the classpath.\n* The application makes use of Spring Boot's welcome page support, either static or templated.\n* Your application is deployed behind a proxy which caches 404 responses.\n\nYour application is NOT vulnerable if any of the following are true:\n\n* Spring MVC auto-configuration is disabled. This is true if WebMvcAutoConfiguration is explicitly excluded, if Spring MVC is not on the classpath, or if spring.main.web-application-type is set to a value other than SERVLET.\n* The application does not use Spring Boot's welcome page support.\n* You do not have a proxy which caches 404 responses.\n\n\nAffected Spring Products and Versions\n\nSpring Boot\n\n3.0.0 to 3.0.6 2.7.0 to 2.7.11 2.6.0 to 2.6.14 2.5.0 to 2.5.14\n\nOlder, unsupported versions are also affected\nMitigation\n\nUsers of affected versions should apply the following mitigations:\n\n* 3.0.x users should upgrade to 3.0.7+\n* 2.7.x users should upgrade to 2.7.12+\n* 2.6.x users should upgrade to 2.6.15+\n* 2.5.x users should upgrade to 2.5.15+\n\nUsers of older, unsupported versions should upgrade to 3.0.7+ or 2.7.12+.\n\nWorkarounds: configure the reverse proxy not to cache 404 responses and/or not to cache responses to requests to the root (/) of the application.",
            aliases: ['CVE-2023-20883'],
            modified: '2024-02-16T08:17:32.176981Z',
            published: '2023-05-26T18:30:21Z',
            database_specific: {
                severity: 'HIGH',
                github_reviewed_at: '2023-05-26T22:09:06Z',
                github_reviewed: true,
                cwe_ids: ['CWE-400'],
                nvd_published_at: '2023-05-26T17:15:14Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-20883',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/spring-projects/spring-boot/issues/35552',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/spring-projects/spring-boot/commit/418dd1ba5bdad79b55a043000164bfcbda2acd78',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/spring-projects/spring-boot',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/spring-projects/spring-boot/releases/tag/v2.5.15',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/spring-projects/spring-boot/releases/tag/v2.6.15',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/spring-projects/spring-boot/releases/tag/v2.7.12',
                },
                {
                    type: 'WEB',
                    url: 'https://security.netapp.com/advisory/ntap-20230703-0008/',
                },
                {
                    type: 'WEB',
                    url: 'https://spring.io/security/cve-2023-20883',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:N/A:H',
                    baseScore: 7.5,
                    ranking: 'High',
                },
            ],
            fixed: '3.0.7',
        },
    },
    {
        info: {
            name: 'org.springframework:spring-expression',
            version: '6.0.4',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-564r-hj7v-mcr5',
            summary:
                'Spring Framework vulnerable to denial of service via specially crafted SpEL expression',
            details:
                'In Spring Framework versions 6.0.0 - 6.0.6, 5.3.0 - 5.3.25, 5.2.0.RELEASE - 5.2.22.RELEASE, and older unsupported versions, it is possible for a user to provide a specially crafted SpEL expression that may cause a denial-of-service (DoS) condition.',
            aliases: ['CVE-2023-20861'],
            modified: '2024-02-16T08:18:09.902102Z',
            published: '2023-03-23T21:30:19Z',
            database_specific: {
                severity: 'MODERATE',
                github_reviewed_at: '2023-03-23T23:10:59Z',
                github_reviewed: true,
                cwe_ids: ['CWE-917'],
                nvd_published_at: '2023-03-23T21:15:00Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-20861',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/spring-projects/spring-framework/commit/430fc25acad2e85cbdddcd52b64481691f03ebd1',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/spring-projects/spring-framework',
                },
                {
                    type: 'WEB',
                    url: 'https://security.netapp.com/advisory/ntap-20230420-0007/',
                },
                {
                    type: 'WEB',
                    url: 'https://spring.io/security/cve-2023-20861',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:L/UI:N/S:U/C:N/I:N/A:H',
                    baseScore: 6.5,
                    ranking: 'Medium',
                },
            ],
            fixed: '6.0.7',
        },
    },
    {
        info: {
            name: 'org.springframework:spring-expression',
            version: '6.0.4',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-wxqc-pxw9-g2p8',
            summary: 'Spring Framework vulnerable to denial of service',
            details:
                'In Spring Framework versions prior to 5.2.24.release+ , 5.3.27+ and 6.0.8+ , it is possible for a user to provide a specially crafted Spring Expression Language (SpEL) expression that may cause a denial-of-service (DoS) condition.',
            aliases: ['CVE-2023-20863'],
            modified: '2024-02-16T07:55:35.012704Z',
            published: '2023-04-13T21:30:27Z',
            database_specific: {
                severity: 'HIGH',
                github_reviewed_at: '2023-04-17T17:19:53Z',
                github_reviewed: true,
                cwe_ids: ['CWE-400', 'CWE-770', 'CWE-917'],
                nvd_published_at: '2023-04-13T20:15:00Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-20863',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/spring-projects/spring-framework/commit/b73f5fcac22555f844cf27a7eeb876cb9d7f7f7e',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/spring-projects/spring-framework',
                },
                {
                    type: 'WEB',
                    url: 'https://spring.io/security/cve-2023-20863',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:N/A:H',
                    baseScore: 7.5,
                    ranking: 'High',
                },
            ],
            fixed: '6.0.8',
        },
    },
    {
        info: {
            name: 'org.springframework.security:spring-security-config',
            version: '6.0.1',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-3h6f-g5f3-gc4w',
            summary: 'Access Control Bypass in Spring Security',
            details:
                'Using "**" as a pattern in Spring Security configuration for WebFlux creates a mismatch in pattern matching between Spring Security and Spring WebFlux, and the potential for a security bypass.\n\n',
            aliases: ['CVE-2023-34034'],
            modified: '2024-02-16T08:23:02.201553Z',
            published: '2023-07-19T15:30:26Z',
            database_specific: {
                severity: 'CRITICAL',
                github_reviewed_at: '2023-07-31T21:19:15Z',
                github_reviewed: true,
                cwe_ids: ['CWE-284'],
                nvd_published_at: '2023-07-19T15:15:11Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-34034',
                },
                {
                    type: 'WEB',
                    url: 'https://ossindex.sonatype.org/vulnerability/CVE-2023-34034',
                },
                {
                    type: 'WEB',
                    url: 'https://security.netapp.com/advisory/ntap-20230814-0008',
                },
                {
                    type: 'WEB',
                    url: 'https://security.snyk.io/vuln/SNYK-JAVA-ORGSPRINGFRAMEWORKSECURITY-5777893',
                },
                {
                    type: 'WEB',
                    url: 'https://spring.io/security/cve-2023-34034',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:N',
                    baseScore: 9.1,
                    ranking: 'Critical',
                },
            ],
            fixed: '6.0.5',
        },
    },
    {
        info: {
            name: 'org.springframework.security:spring-security-config',
            version: '6.0.1',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-4vpr-xfrp-cj64',
            summary:
                "Spring Security's authorization rules can be misconfigured when using multiple servlets",
            details:
                'Spring Security versions 5.8 prior to 5.8.5, 6.0 prior to 6.0.5, and 6.1 prior to 6.1.2 could be susceptible to authorization rule misconfiguration if the application uses requestMatchers(String) and multiple servlets, one of them being Spring MVC’s DispatcherServlet. (DispatcherServlet is a Spring MVC component that maps HTTP endpoints to methods on @Controller-annotated classes.)\n\nSpecifically, an application is vulnerable when all of the following are true:\n\n  *  Spring MVC is on the classpath\n  *  Spring Security is securing more than one servlet in a single application (one of them being Spring MVC’s DispatcherServlet)\n  *  The application uses requestMatchers(String) to refer to endpoints that are not Spring MVC endpoints\n\n\nAn application is not vulnerable if any of the following is true:\n\n  *  The application does not have Spring MVC on the classpath\n  *  The application secures no servlets other than Spring MVC’s DispatcherServlet\n  *  The application uses requestMatchers(String) only for Spring MVC endpoints',
            aliases: ['CVE-2023-34035'],
            modified: '2024-02-16T08:20:17.503929Z',
            published: '2023-07-18T18:30:36Z',
            database_specific: {
                severity: 'HIGH',
                github_reviewed_at: '2023-07-19T22:10:34Z',
                github_reviewed: true,
                cwe_ids: ['CWE-863'],
                nvd_published_at: '2023-07-18T16:15:11Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-34035',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/spring-projects/spring-security-samples/commit/4e3bec904a5467db28ea33e25ac9d90524b53d66',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/spring-projects/spring-security/commit/bb46a5427005e33e637b15948de8adae244ce547',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/spring-projects/spring-security/commit/df239b6448ccf138b0c95b5575a88f33ac35cd9a',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/spring-projects/spring-security-samples/tree/main/servlet/java-configuration/authentication/preauth',
                },
                {
                    type: 'WEB',
                    url: 'https://spring.io/security/cve-2023-34035',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:L/I:L/A:L',
                    baseScore: 7.3,
                    ranking: 'High',
                },
            ],
            fixed: '6.0.5',
        },
    },
    {
        info: {
            name: 'org.yaml:snakeyaml',
            version: '1.33',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-mjmj-j48q-9wg2',
            summary:
                'SnakeYaml Constructor Deserialization Remote Code Execution',
            details:
                "### Summary\nSnakeYaml's `Constructor` class, which inherits from `SafeConstructor`, allows any type be deserialized given the following line:\n\nnew Yaml(new Constructor(TestDataClass.class)).load(yamlContent);\n\nTypes do not have to match the types of properties in the target class. A `ConstructorException` is thrown, but only after a malicious payload is deserialized.\n\n### Severity\nHigh, lack of type checks during deserialization allows remote code execution.\n\n### Proof of Concept\nExecute `bash run.sh`. The PoC uses Constructor to deserialize a payload\nfor RCE. RCE is demonstrated by using a payload which performs a http request to\nhttp://127.0.0.1:8000.\n\nExample output of successful run of proof of concept:\n\n```\n$ bash run.sh\n\n[+] Downloading snakeyaml if needed\n[+] Starting mock HTTP server on 127.0.0.1:8000 to demonstrate RCE\nnc: no process found\n[+] Compiling and running Proof of Concept, which a payload that sends a HTTP request to mock web server.\n[+] An exception is expected.\nException:\nCannot create property=payload for JavaBean=Main$TestDataClass@3cbbc1e0\n in 'string', line 1, column 1:\n    payload: !!javax.script.ScriptEn ... \n    ^\nCan not set java.lang.String field Main$TestDataClass.payload to javax.script.ScriptEngineManager\n in 'string', line 1, column 10:\n    payload: !!javax.script.ScriptEngineManag ... \n             ^\n\n\tat org.yaml.snakeyaml.constructor.Constructor$ConstructMapping.constructJavaBean2ndStep(Constructor.java:291)\n\tat org.yaml.snakeyaml.constructor.Constructor$ConstructMapping.construct(Constructor.java:172)\n\tat org.yaml.snakeyaml.constructor.Constructor$ConstructYamlObject.construct(Constructor.java:332)\n\tat org.yaml.snakeyaml.constructor.BaseConstructor.constructObjectNoCheck(BaseConstructor.java:230)\n\tat org.yaml.snakeyaml.constructor.BaseConstructor.constructObject(BaseConstructor.java:220)\n\tat org.yaml.snakeyaml.constructor.BaseConstructor.constructDocument(BaseConstructor.java:174)\n\tat org.yaml.snakeyaml.constructor.BaseConstructor.getSingleData(BaseConstructor.java:158)\n\tat org.yaml.snakeyaml.Yaml.loadFromReader(Yaml.java:491)\n\tat org.yaml.snakeyaml.Yaml.load(Yaml.java:416)\n\tat Main.main(Main.java:37)\nCaused by: java.lang.IllegalArgumentException: Can not set java.lang.String field Main$TestDataClass.payload to javax.script.ScriptEngineManager\n\tat java.base/jdk.internal.reflect.UnsafeFieldAccessorImpl.throwSetIllegalArgumentException(UnsafeFieldAccessorImpl.java:167)\n\tat java.base/jdk.internal.reflect.UnsafeFieldAccessorImpl.throwSetIllegalArgumentException(UnsafeFieldAccessorImpl.java:171)\n\tat java.base/jdk.internal.reflect.UnsafeObjectFieldAccessorImpl.set(UnsafeObjectFieldAccessorImpl.java:81)\n\tat java.base/java.lang.reflect.Field.set(Field.java:780)\n\tat org.yaml.snakeyaml.introspector.FieldProperty.set(FieldProperty.java:44)\n\tat org.yaml.snakeyaml.constructor.Constructor$ConstructMapping.constructJavaBean2ndStep(Constructor.java:286)\n\t... 9 more\n[+] Dumping Received HTTP Request. Will not be empty if PoC worked\nGET /proof-of-concept HTTP/1.1\nUser-Agent: Java/11.0.14\nHost: localhost:8000\nAccept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\nConnection: keep-alive\n```\n\n### Further Analysis\nPotential mitigations include, leveraging SnakeYaml's SafeConstructor while parsing untrusted content.\n\nSee https://bitbucket.org/snakeyaml/snakeyaml/issues/561/cve-2022-1471-vulnerability-in#comment-64581479 for discussion on the subject.\n\nA fix was released in version 2.0. See https://bitbucket.org/snakeyaml/snakeyaml/issues/561/cve-2022-1471-vulnerability-in#comment-64876314 for more information.\n\n### Timeline\n**Date reported**: 4/11/2022\n**Date fixed**: \n**Date disclosed**: 10/13/2022",
            aliases: ['CVE-2022-1471'],
            modified: '2024-02-17T05:36:02.734259Z',
            published: '2022-12-12T21:19:47Z',
            database_specific: {
                severity: 'HIGH',
                github_reviewed_at: '2022-12-12T21:19:47Z',
                github_reviewed: true,
                cwe_ids: ['CWE-20', 'CWE-502'],
                nvd_published_at: '2022-12-01T11:15:00Z',
            },
            references: [
                {
                    type: 'WEB',
                    url: 'https://github.com/google/security-research/security/advisories/GHSA-mjmj-j48q-9wg2',
                },
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2022-1471',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://bitbucket.org/snakeyaml/snakeyaml',
                },
                {
                    type: 'WEB',
                    url: 'https://bitbucket.org/snakeyaml/snakeyaml/commits/5014df1a36f50aca54405bb8433bc99a8847f758',
                },
                {
                    type: 'WEB',
                    url: 'https://bitbucket.org/snakeyaml/snakeyaml/commits/acc44099f5f4af26ff86b4e4e4cc1c874e2dc5c4',
                },
                {
                    type: 'WEB',
                    url: 'https://bitbucket.org/snakeyaml/snakeyaml/issues/561/cve-2022-1471-vulnerability-in#comment-64581479',
                },
                {
                    type: 'WEB',
                    url: 'https://bitbucket.org/snakeyaml/snakeyaml/issues/561/cve-2022-1471-vulnerability-in#comment-64634374',
                },
                {
                    type: 'WEB',
                    url: 'https://bitbucket.org/snakeyaml/snakeyaml/issues/561/cve-2022-1471-vulnerability-in#comment-64876314',
                },
                {
                    type: 'WEB',
                    url: 'https://bitbucket.org/snakeyaml/snakeyaml/wiki/CVE-2022-1471',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/mbechler/marshalsec',
                },
                {
                    type: 'WEB',
                    url: 'https://groups.google.com/g/kubernetes-security-announce/c/mwrakFaEdnc',
                },
                {
                    type: 'WEB',
                    url: 'https://security.netapp.com/advisory/ntap-20230818-0015',
                },
                {
                    type: 'WEB',
                    url: 'https://snyk.io/blog/unsafe-deserialization-snakeyaml-java-cve-2022-1471',
                },
                {
                    type: 'WEB',
                    url: 'https://www.github.com/mbechler/marshalsec/blob/master/marshalsec.pdf?raw=true',
                },
                {
                    type: 'WEB',
                    url: 'http://packetstormsecurity.com/files/175095/PyTorch-Model-Server-Registration-Deserialization-Remote-Code-Execution.html',
                },
                {
                    type: 'WEB',
                    url: 'http://www.openwall.com/lists/oss-security/2023/11/19/1',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:L/UI:N/S:U/C:H/I:H/A:L',
                    baseScore: 8.3,
                    ranking: 'High',
                },
            ],
            fixed: '2.0',
        },
    },
    {
        info: {
            name: 'ch.qos.logback:logback-core',
            version: '1.4.5',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-vmq6-5m68-f53m',
            summary: 'logback serialization vulnerability',
            details:
                'A serialization vulnerability in logback receiver component part of logback allows an attacker to mount a Denial-Of-Service attack by sending poisoned data.\n\nThis is only exploitable if logback receiver component is deployed. See https://logback.qos.ch/manual/receivers.html',
            aliases: ['CVE-2023-6378'],
            modified: '2024-02-16T08:07:48.816850Z',
            published: '2023-11-29T12:30:16Z',
            database_specific: {
                severity: 'HIGH',
                github_reviewed_at: '2023-11-29T21:33:01Z',
                github_reviewed: true,
                cwe_ids: ['CWE-502'],
                nvd_published_at: '2023-11-29T12:15:07Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-6378',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/qos-ch/logback/issues/745#issuecomment-1836227158',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/qos-ch/logback/commit/9c782b45be4abdafb7e17481e24e7354c2acd1eb',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/qos-ch/logback/commit/b8eac23a9de9e05fb6d51160b3f46acd91af9731',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/qos-ch/logback/commit/bb095154be011267b64e37a1d401546e7cc2b7c3',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/qos-ch/logback',
                },
                {
                    type: 'WEB',
                    url: 'https://logback.qos.ch/manual/receivers.html',
                },
                {
                    type: 'WEB',
                    url: 'https://logback.qos.ch/news.html#1.2.13',
                },
                {
                    type: 'WEB',
                    url: 'https://logback.qos.ch/news.html#1.3.12',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:L/AC:L/PR:N/UI:N/S:C/C:N/I:N/A:H',
                    baseScore: 7.1,
                    ranking: 'High',
                },
            ],
            fixed: '1.4.12',
        },
    },
    {
        info: {
            name: 'ch.qos.logback:logback-classic',
            version: '1.4.5',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-vmq6-5m68-f53m',
            summary: 'logback serialization vulnerability',
            details:
                'A serialization vulnerability in logback receiver component part of logback allows an attacker to mount a Denial-Of-Service attack by sending poisoned data.\n\nThis is only exploitable if logback receiver component is deployed. See https://logback.qos.ch/manual/receivers.html',
            aliases: ['CVE-2023-6378'],
            modified: '2024-02-16T08:07:48.816850Z',
            published: '2023-11-29T12:30:16Z',
            database_specific: {
                severity: 'HIGH',
                github_reviewed_at: '2023-11-29T21:33:01Z',
                github_reviewed: true,
                cwe_ids: ['CWE-502'],
                nvd_published_at: '2023-11-29T12:15:07Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-6378',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/qos-ch/logback/issues/745#issuecomment-1836227158',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/qos-ch/logback/commit/9c782b45be4abdafb7e17481e24e7354c2acd1eb',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/qos-ch/logback/commit/b8eac23a9de9e05fb6d51160b3f46acd91af9731',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/qos-ch/logback/commit/bb095154be011267b64e37a1d401546e7cc2b7c3',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/qos-ch/logback',
                },
                {
                    type: 'WEB',
                    url: 'https://logback.qos.ch/manual/receivers.html',
                },
                {
                    type: 'WEB',
                    url: 'https://logback.qos.ch/news.html#1.2.13',
                },
                {
                    type: 'WEB',
                    url: 'https://logback.qos.ch/news.html#1.3.12',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:L/AC:L/PR:N/UI:N/S:C/C:N/I:N/A:H',
                    baseScore: 7.1,
                    ranking: 'High',
                },
            ],
            fixed: '1.4.12',
        },
    },
    {
        info: {
            name: 'net.minidev:json-smart',
            version: '2.4.8',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-493p-pfq6-5258',
            summary: 'json-smart Uncontrolled Recursion vulnerabilty',
            details:
                '### Impact\nAffected versions of [net.minidev:json-smart](https://github.com/netplex/json-smart-v1) are vulnerable to Denial of Service (DoS) due to a StackOverflowError when parsing a deeply nested JSON array or object.\n\nWhen reaching a ‘[‘ or ‘{‘ character in the JSON input, the code parses an array or an object respectively. It was discovered that the 3PP does not have any limit to the nesting of such arrays or objects. Since the parsing of nested arrays and objects is done recursively, nesting too many of them can cause stack exhaustion (stack overflow) and crash the software.\n\n### Patches\nThis vulnerability was fixed in json-smart version 2.4.9, but the maintainer recommends upgrading to 2.4.10, due to a remaining bug.\n\n### Workarounds\nN/A\n\n### References\n- https://www.cve.org/CVERecord?id=CVE-2023-1370\n- https://nvd.nist.gov/vuln/detail/CVE-2023-1370\n- https://security.snyk.io/vuln/SNYK-JAVA-NETMINIDEV-3369748',
            aliases: ['CVE-2023-1370'],
            modified: '2024-02-16T08:14:04.871958Z',
            published: '2023-03-23T20:32:03Z',
            database_specific: {
                severity: 'HIGH',
                github_reviewed_at: '2023-03-23T20:32:03Z',
                github_reviewed: true,
                cwe_ids: ['CWE-674'],
                nvd_published_at: '2023-03-22T06:15:00Z',
            },
            references: [
                {
                    type: 'WEB',
                    url: 'https://github.com/oswaldobapvicjr/jsonmerge/security/advisories/GHSA-493p-pfq6-5258',
                },
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-1370',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/netplex/json-smart-v2/issues/137',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/netplex/json-smart-v2/commit/5b3205d051952d3100aa0db1535f6ba6226bd87a',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/netplex/json-smart-v2/commit/e2791ae506a57491bc856b439d706c81e45adcf8',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/oswaldobapvicjr/jsonmerge',
                },
                {
                    type: 'WEB',
                    url: 'https://research.jfrog.com/vulnerabilities/stack-exhaustion-in-json-smart-leads-to-denial-of-service-when-parsing-malformed-json-xray-427633',
                },
                {
                    type: 'WEB',
                    url: 'https://security.snyk.io/vuln/SNYK-JAVA-NETMINIDEV-3369748',
                },
                {
                    type: 'WEB',
                    url: 'https://www.cve.org/CVERecord?id=CVE-2023-1370',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:N/A:H',
                    baseScore: 7.5,
                    ranking: 'High',
                },
            ],
            fixed: '2.4.9',
        },
    },
    {
        info: {
            name: 'com.jayway.jsonpath:json-path',
            version: '2.7.0',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-pfh2-hfmq-phg5',
            summary: 'json-path Out-of-bounds Write vulnerability',
            details:
                'json-path v2.8.0 was discovered to contain a stack overflow via the `Criteria.parse()` method.',
            aliases: ['CVE-2023-51074'],
            modified: '2024-02-16T08:08:35.450144Z',
            published: '2023-12-27T21:31:01Z',
            database_specific: {
                severity: 'MODERATE',
                github_reviewed_at: '2024-01-09T19:28:14Z',
                github_reviewed: true,
                cwe_ids: ['CWE-787'],
                nvd_published_at: '2023-12-27T21:15:08Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-51074',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/json-path/JsonPath/issues/973',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/json-path/JsonPath/commit/71a09c1193726c010917f1157ecbb069ad6c3e3b',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/json-path/JsonPath',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/json-path/JsonPath/releases/tag/json-path-2.9.0',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:N/A:L',
                    baseScore: 5.3,
                    ranking: 'Medium',
                },
            ],
            fixed: '2.9.0',
        },
    },
    {
        info: {
            name: 'org.springframework.boot:spring-boot',
            version: '3.0.2',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-jjfh-589g-3hjx',
            summary: 'Spring Boot denial of service vulnerability',
            details:
                'In Spring Boot versions 2.7.0 - 2.7.17, 3.0.0-3.0.12 and 3.1.0-3.1.5, it is possible for a user to provide specially crafted HTTP requests that may cause a denial-of-service (DoS) condition.\n\nSpecifically, an application is vulnerable when all of the following are true:\n\n  *  the application uses Spring MVC or Spring WebFlux\n  *  org.springframework.boot:spring-boot-actuator is on the classpath',
            aliases: ['CVE-2023-34055'],
            modified: '2024-02-16T08:17:49.624329Z',
            published: '2023-11-28T09:30:27Z',
            database_specific: {
                severity: 'MODERATE',
                github_reviewed_at: '2023-11-28T20:53:48Z',
                github_reviewed: true,
                cwe_ids: [],
                nvd_published_at: '2023-11-28T09:15:07Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-34055',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/spring-projects/spring-boot',
                },
                {
                    type: 'WEB',
                    url: 'https://security.netapp.com/advisory/ntap-20231221-0010',
                },
                {
                    type: 'WEB',
                    url: 'https://spring.io/security/cve-2023-34055',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:L/UI:N/S:U/C:N/I:N/A:H',
                    baseScore: 6.5,
                    ranking: 'Medium',
                },
            ],
            fixed: '3.0.13',
        },
    },
    {
        info: {
            name: 'org.springframework:spring-webmvc',
            version: '6.0.4',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-v94h-hvhg-mf9h',
            summary: 'Spring Framework vulnerable to denial of service',
            details:
                'In Spring Framework versions 6.0.0 - 6.0.13, it is possible for a user to provide specially crafted HTTP requests that may cause a denial-of-service (DoS) condition.\n\nSpecifically, an application is vulnerable when all of the following are true:\n\n  *  the application uses Spring MVC or Spring WebFlux\n  *  io.micrometer:micrometer-core is on the classpath\n  *  an ObservationRegistry is configured in the application to record observations\n\n\nTypically, Spring Boot applications need the org.springframework.boot:spring-boot-actuator dependency to meet all conditions.',
            aliases: ['CVE-2023-34053'],
            modified: '2024-02-16T08:00:04.748337Z',
            published: '2023-11-28T09:30:27Z',
            database_specific: {
                severity: 'HIGH',
                github_reviewed_at: '2023-11-28T20:53:36Z',
                github_reviewed: true,
                cwe_ids: [],
                nvd_published_at: '2023-11-28T09:15:06Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-34053',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/spring-projects/spring-framework',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/spring-projects/spring-framework/compare/v6.0.13...v6.0.14',
                },
                {
                    type: 'WEB',
                    url: 'https://security.netapp.com/advisory/ntap-20231214-0007',
                },
                {
                    type: 'WEB',
                    url: 'https://spring.io/security/cve-2023-34053',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:N/A:H',
                    baseScore: 7.5,
                    ranking: 'High',
                },
            ],
            fixed: '6.0.14',
        },
    },
    {
        info: {
            name: 'org.apache.tomcat.embed:tomcat-embed-core',
            version: '10.1.5',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-fccv-jmmp-qg76',
            summary: 'Apache Tomcat Improper Input Validation vulnerability',
            details:
                'Improper Input Validation vulnerability in Apache Tomcat. Tomcat from 11.0.0-M1 through 11.0.0-M10, from 10.1.0-M1 through 10.1.15, from 9.0.0-M1 through 9.0.82, and from 8.5.0 through 8.5.95 did not correctly parse HTTP trailer headers. A trailer header that exceeded the header size limit could cause Tomcat to treat a single request as multiple requests leading to the possibility of request smuggling when behind a reverse proxy.\n\nUsers are recommended to upgrade to version 11.0.0-M11 onwards, 10.1.16 onwards, 9.0.83 onwards or 8.5.96 onwards, which fix the issue.',
            aliases: ['BIT-tomcat-2023-46589', 'CVE-2023-46589'],
            modified: '2024-02-16T08:04:34.334152Z',
            published: '2023-11-28T18:30:23Z',
            database_specific: {
                severity: 'HIGH',
                github_reviewed_at: '2023-11-28T23:28:54Z',
                github_reviewed: true,
                cwe_ids: ['CWE-20', 'CWE-444'],
                nvd_published_at: '2023-11-28T16:15:06Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-46589',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/tomcat/commit/6f181e1062a472bc5f0234980f66cbde42c1041b',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/tomcat/commit/7a2d8818fcea0b51747a67af9510ce7977245ebd',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/tomcat/commit/aa92971e879a519384c517febc39fd04c48d4642',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/tomcat/commit/b5776d769bffeade865061bc8ecbeb2b56167b08',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/apache/tomcat',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.apache.org/thread/0rqq6ktozqc42ro8hhxdmmdjm1k1tpxr',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.debian.org/debian-lts-announce/2024/01/msg00001.html',
                },
                {
                    type: 'WEB',
                    url: 'https://security.netapp.com/advisory/ntap-20231214-0009/',
                },
                {
                    type: 'WEB',
                    url: 'https://tomcat.apache.org/security-10.html',
                },
                {
                    type: 'WEB',
                    url: 'https://tomcat.apache.org/security-11.html',
                },
                {
                    type: 'WEB',
                    url: 'https://tomcat.apache.org/security-8.html',
                },
                {
                    type: 'WEB',
                    url: 'https://tomcat.apache.org/security-9.html',
                },
                {
                    type: 'WEB',
                    url: 'https://www.openwall.com/lists/oss-security/2023/11/28/2',
                },
                {
                    type: 'WEB',
                    url: 'http://www.openwall.com/lists/oss-security/2023/11/28/2',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:H/A:N',
                    baseScore: 7.5,
                    ranking: 'High',
                },
            ],
            fixed: '10.1.16',
        },
    },
    {
        info: {
            name: 'org.apache.tomcat.embed:tomcat-embed-core',
            version: '10.1.5',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-g8pj-r55q-5c2v',
            summary: 'Apache Tomcat Incomplete Cleanup vulnerability',
            details:
                'Incomplete Cleanup vulnerability in Apache Tomcat.\n\nWhen recycling various internal objects in Apache Tomcat from 11.0.0-M1 through 11.0.0-M11, from 10.1.0-M1 through 10.1.13, from 9.0.0-M1 through 9.0.80 and from 8.5.0 through 8.5.93, an error could cause Tomcat to skip some parts of the recycling process leading to information leaking from the current request/response to the next.\n\nUsers are recommended to upgrade to version 11.0.0-M12 onwards, 10.1.14 onwards, 9.0.81 onwards or 8.5.94 onwards, which fixes the issue.',
            aliases: ['BIT-tomcat-2023-42795', 'CVE-2023-42795'],
            modified: '2024-02-16T08:24:07.928876Z',
            published: '2023-10-10T18:31:35Z',
            database_specific: {
                severity: 'MODERATE',
                github_reviewed_at: '2023-10-10T22:30:05Z',
                github_reviewed: true,
                cwe_ids: ['CWE-459'],
                nvd_published_at: '2023-10-10T18:15:18Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-42795',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/apache/tomcat',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.apache.org/thread/065jfyo583490r9j2v73nhpyxdob56lw',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.debian.org/debian-lts-announce/2023/10/msg00020.html',
                },
                {
                    type: 'WEB',
                    url: 'https://security.netapp.com/advisory/ntap-20231103-0007',
                },
                {
                    type: 'WEB',
                    url: 'https://www.debian.org/security/2023/dsa-5521',
                },
                {
                    type: 'WEB',
                    url: 'https://www.debian.org/security/2023/dsa-5522',
                },
                {
                    type: 'WEB',
                    url: 'http://www.openwall.com/lists/oss-security/2023/10/10/9',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:L/I:N/A:N',
                    baseScore: 5.3,
                    ranking: 'Medium',
                },
            ],
            fixed: '10.1.14',
        },
    },
    {
        info: {
            name: 'org.apache.tomcat.embed:tomcat-embed-core',
            version: '10.1.5',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-q3mw-pvr8-9ggc',
            summary: 'Apache Tomcat Open Redirect vulnerability',
            details:
                "URL Redirection to Untrusted Site ('Open Redirect') vulnerability in FORM authentication feature Apache Tomcat. This issue affects Apache Tomcat: from 11.0.0-M1 through 11.0.0-M10, from 10.1.0-M1 through 10.0.12, from 9.0.0-M1 through 9.0.79 and from 8.5.0 through 8.5.92.\n\nThe vulnerability is limited to the ROOT (default) web application.",
            aliases: ['BIT-tomcat-2023-41080', 'CVE-2023-41080'],
            modified: '2024-02-17T05:31:37.094178Z',
            published: '2023-08-25T21:30:48Z',
            database_specific: {
                severity: 'MODERATE',
                github_reviewed_at: '2023-08-25T22:05:01Z',
                github_reviewed: true,
                cwe_ids: ['CWE-601'],
                nvd_published_at: '2023-08-25T21:15:09Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-41080',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/tomcat/commit/4998ad745b67edeadefe541c94ed029b53933d3b',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/tomcat/commit/77c0ce2d169efa248b64b992e547aad549ec906b',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/tomcat/commit/bb4624a9f3e69d495182ebfa68d7983076407a27',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/tomcat/commit/e3703c9abb8fe0d5602f6ba8a8f11d4b6940815a',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/apache/tomcat',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.apache.org/thread/71wvwprtx2j2m54fovq9zr7gbm2wow2f',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.debian.org/debian-lts-announce/2023/10/msg00020.html',
                },
                {
                    type: 'WEB',
                    url: 'https://security.netapp.com/advisory/ntap-20230921-0006',
                },
                {
                    type: 'WEB',
                    url: 'https://www.debian.org/security/2023/dsa-5521',
                },
                {
                    type: 'WEB',
                    url: 'https://www.debian.org/security/2023/dsa-5522',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:N/UI:R/S:C/C:L/I:L/A:N',
                    baseScore: 6.1,
                    ranking: 'Medium',
                },
            ],
            fixed: '10.1.13',
        },
    },
    {
        info: {
            name: 'org.apache.tomcat.embed:tomcat-embed-core',
            version: '10.1.5',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-qppj-fm5r-hxr3',
            summary: 'HTTP/2 Stream Cancellation Attack',
            details:
                '## HTTP/2 Rapid reset attack\nThe HTTP/2 protocol allows clients to indicate to the server that a previous stream should be canceled by sending a RST_STREAM frame. The protocol does not require the client and server to coordinate the cancellation in any way, the client may do it unilaterally. The client may also assume that the cancellation will take effect immediately when the server receives the RST_STREAM frame, before any other data from that TCP connection is processed.\n\nAbuse of this feature is called a Rapid Reset attack because it relies on the ability for an endpoint to send a RST_STREAM frame immediately after sending a request frame, which makes the other endpoint start working and then rapidly resets the request. The request is canceled, but leaves the HTTP/2 connection open. \n\nThe HTTP/2 Rapid Reset attack built on this capability is simple: The client opens a large number of streams at once as in the standard HTTP/2 attack, but rather than waiting for a response to each request stream from the server or proxy, the client cancels each request immediately.\n\nThe ability to reset streams immediately allows each connection to have an indefinite number of requests in flight. By explicitly canceling the requests, the attacker never exceeds the limit on the number of concurrent open streams. The number of in-flight requests is no longer dependent on the round-trip time (RTT), but only on the available network bandwidth.\n\nIn a typical HTTP/2 server implementation, the server will still have to do significant amounts of work for canceled requests, such as allocating new stream data structures, parsing the query and doing header decompression, and mapping the URL to a resource. For reverse proxy implementations, the request may be proxied to the backend server before the RST_STREAM frame is processed. The client on the other hand paid almost no costs for sending the requests. This creates an exploitable cost asymmetry between the server and the client.\n\nMultiple software artifacts implementing HTTP/2 are affected. This advisory was originally ingested from the `swift-nio-http2` repo advisory and their original conent follows.\n\n## swift-nio-http2 specific advisory\nswift-nio-http2 is vulnerable to a denial-of-service vulnerability in which a malicious client can create and then reset a large number of HTTP/2 streams in a short period of time. This causes swift-nio-http2 to commit to a large amount of expensive work which it then throws away, including creating entirely new `Channel`s to serve the traffic. This can easily overwhelm an `EventLoop` and prevent it from making forward progress.\n\nswift-nio-http2 1.28 contains a remediation for this issue that applies reset counter using a sliding window. This constrains the number of stream resets that may occur in a given window of time. Clients violating this limit will have their connections torn down. This allows clients to continue to cancel streams for legitimate reasons, while constraining malicious actors.',
            aliases: [
                'BIT-apisix-2023-44487',
                'BIT-aspnet-core-2023-44487',
                'BIT-contour-2023-44487',
                'BIT-dotnet-2023-44487',
                'BIT-dotnet-sdk-2023-44487',
                'BIT-envoy-2023-44487',
                'BIT-golang-2023-44487',
                'BIT-jenkins-2023-44487',
                'BIT-kong-2023-44487',
                'BIT-nginx-2023-44487',
                'BIT-nginx-ingress-controller-2023-44487',
                'BIT-node-2023-44487',
                'BIT-solr-2023-44487',
                'BIT-tomcat-2023-44487',
                'BIT-varnish-2023-44487',
                'CVE-2023-44487',
                'GHSA-2m7v-gc89-fjqf',
                'GHSA-vx74-f528-fxqg',
                'GHSA-xpw8-rcwv-8f8p',
            ],
            modified: '2024-02-16T08:11:57.219888Z',
            published: '2023-10-10T21:28:24Z',
            database_specific: {
                severity: 'MODERATE',
                github_reviewed_at: '2023-10-10T21:28:24Z',
                github_reviewed: true,
                cwe_ids: ['CWE-400'],
                nvd_published_at: '2023-10-10T14:15:10Z',
            },
            references: [
                {
                    type: 'WEB',
                    url: 'https://github.com/apple/swift-nio-http2/security/advisories/GHSA-qppj-fm5r-hxr3',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/h2o/h2o/security/advisories/GHSA-2m7v-gc89-fjqf',
                },
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-44487',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/Azure/AKS/issues/3947',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/akka/akka-http/issues/4323',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/alibaba/tengine/issues/1872',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/apisix/issues/10320',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/caddyserver/caddy/issues/5877',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/dotnet/announcements/issues/277',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/eclipse/jetty.project/issues/10679',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/etcd-io/etcd/issues/16740',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/golang/go/issues/63417',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/haproxy/haproxy/issues/2312',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/hyperium/hyper/issues/3337',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/junkurihara/rust-rpxy/issues/97',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/kazu-yamamoto/http2/issues/93',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/ninenines/cowboy/issues/1615',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/openresty/openresty/issues/930',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/opensearch-project/data-prepper/issues/3474',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/tempesta-tech/tempesta/issues/1986',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/varnishcache/varnish-cache/issues/3996',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/httpd-site/pull/10',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/trafficserver/pull/10564',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/envoyproxy/envoy/pull/30055',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/facebook/proxygen/pull/466',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/grpc/grpc-go/pull/6703',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/h2o/h2o/pull/3291',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/kubernetes/kubernetes/pull/121120',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/line/armeria/pull/5232',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/linkerd/website/pull/1695/commits/4b9c6836471bc8270ab48aae6fd2181bc73fd632',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/microsoft/CBL-Mariner/pull/6381',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/nghttp2/nghttp2/pull/1961',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/nodejs/node/pull/50121',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/projectcontour/contour/pull/5826',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/kazu-yamamoto/http2/commit/f61d41a502bd0f60eb24e1ce14edc7b6df6722a1',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/netty/netty/commit/58f75f665aa81a8cbcf6ffa74820042a285c5e61',
                },
                {
                    type: 'WEB',
                    url: 'https://access.redhat.com/security/cve/cve-2023-44487',
                },
                {
                    type: 'WEB',
                    url: 'https://arstechnica.com/security/2023/10/how-ddosers-used-the-http-2-protocol-to-deliver-attacks-of-unprecedented-size/',
                },
                {
                    type: 'WEB',
                    url: 'https://aws.amazon.com/security/security-bulletins/AWS-2023-011/',
                },
                {
                    type: 'WEB',
                    url: 'https://blog.cloudflare.com/technical-breakdown-http2-rapid-reset-ddos-attack/',
                },
                {
                    type: 'WEB',
                    url: 'https://blog.cloudflare.com/zero-day-rapid-reset-http2-record-breaking-ddos-attack/',
                },
                {
                    type: 'WEB',
                    url: 'https://blog.litespeedtech.com/2023/10/11/rapid-reset-http-2-vulnerablilty/',
                },
                {
                    type: 'WEB',
                    url: 'https://blog.qualys.com/vulnerabilities-threat-research/2023/10/10/cve-2023-44487-http-2-rapid-reset-attack',
                },
                {
                    type: 'WEB',
                    url: 'https://blog.vespa.ai/cve-2023-44487/',
                },
                {
                    type: 'WEB',
                    url: 'https://bugzilla.proxmox.com/show_bug.cgi?id=4988',
                },
                {
                    type: 'WEB',
                    url: 'https://bugzilla.redhat.com/show_bug.cgi?id=2242803',
                },
                {
                    type: 'WEB',
                    url: 'https://bugzilla.suse.com/show_bug.cgi?id=1216123',
                },
                {
                    type: 'WEB',
                    url: 'https://cgit.freebsd.org/ports/commit/?id=c64c329c2c1752f46b73e3e6ce9f4329be6629f9',
                },
                {
                    type: 'WEB',
                    url: 'https://chaos.social/@icing/111210915918780532',
                },
                {
                    type: 'WEB',
                    url: 'https://cloud.google.com/blog/products/identity-security/google-cloud-mitigated-largest-ddos-attack-peaking-above-398-million-rps/',
                },
                {
                    type: 'WEB',
                    url: 'https://cloud.google.com/blog/products/identity-security/how-it-works-the-novel-http2-rapid-reset-ddos-attack',
                },
                {
                    type: 'WEB',
                    url: 'https://community.traefik.io/t/is-traefik-vulnerable-to-cve-2023-44487/20125',
                },
                {
                    type: 'WEB',
                    url: 'https://discuss.hashicorp.com/t/hcsec-2023-32-vault-consul-and-boundary-affected-by-http-2-rapid-reset-denial-of-service-vulnerability-cve-2023-44487/59715',
                },
                {
                    type: 'WEB',
                    url: 'https://edg.io/lp/blog/resets-leaks-ddos-and-the-tale-of-a-hidden-cve',
                },
                {
                    type: 'WEB',
                    url: 'https://forums.swift.org/t/swift-nio-http2-security-update-cve-2023-44487-http-2-dos/67764',
                },
                {
                    type: 'WEB',
                    url: 'https://gist.github.com/adulau/7c2bfb8e9cdbe4b35a5e131c66a0c088',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/Kong/kong/discussions/11741',
                },
                {
                    type: 'ADVISORY',
                    url: 'https://github.com/advisories/GHSA-qppj-fm5r-hxr3',
                },
                {
                    type: 'ADVISORY',
                    url: 'https://github.com/advisories/GHSA-vx74-f528-fxqg',
                },
                {
                    type: 'ADVISORY',
                    url: 'https://github.com/advisories/GHSA-xpw8-rcwv-8f8p',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/httpd/blob/afcdbeebbff4b0c50ea26cdd16e178c0d1f24152/modules/http2/h2_mplx.c#L1101-L1113',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/tomcat/tree/main/java/org/apache/coyote/http2',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/apple/swift-nio-http2',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/arkrwn/PoC/tree/main/CVE-2023-44487',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/bcdannyboy/CVE-2023-44487',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/caddyserver/caddy/releases/tag/v2.7.5',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/dotnet/core/blob/e4613450ea0da7fd2fc6b61dfb2c1c1dec1ce9ec/release-notes/6.0/6.0.23/6.0.23.md?plain=1#L73',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/grpc/grpc-go/releases',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/icing/mod_h2/blob/0a864782af0a942aa2ad4ed960a6b32cd35bcf0a/mod_http2/README.md?plain=1#L239-L244',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/micrictor/http2-rst-stream',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/nghttp2/nghttp2/releases/tag/v1.57.0',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/oqtane/oqtane.framework/discussions/3367',
                },
                {
                    type: 'WEB',
                    url: 'https://go.dev/cl/534215',
                },
                {
                    type: 'WEB',
                    url: 'https://go.dev/cl/534235',
                },
                {
                    type: 'WEB',
                    url: 'https://go.dev/issue/63417',
                },
                {
                    type: 'WEB',
                    url: 'https://groups.google.com/g/golang-announce/c/iNNxDTCjZvo',
                },
                {
                    type: 'WEB',
                    url: 'https://groups.google.com/g/golang-announce/c/iNNxDTCjZvo/m/UDd7VKQuAAAJ',
                },
                {
                    type: 'WEB',
                    url: 'https://istio.io/latest/news/security/istio-security-2023-004/',
                },
                {
                    type: 'WEB',
                    url: 'https://linkerd.io/2023/10/12/linkerd-cve-2023-44487/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.apache.org/thread/5py8h42mxfsn8l1wy6o41xwhsjlsd87q',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.debian.org/debian-lts-announce/2023/10/msg00020.html',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.debian.org/debian-lts-announce/2023/10/msg00023.html',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.debian.org/debian-lts-announce/2023/10/msg00024.html',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.debian.org/debian-lts-announce/2023/10/msg00045.html',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.debian.org/debian-lts-announce/2023/10/msg00047.html',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.debian.org/debian-lts-announce/2023/11/msg00001.html',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.debian.org/debian-lts-announce/2023/11/msg00012.html',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/2MBEPPC36UBVOZZNAXFHKLFGSLCMN5LI/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/3N4NJ7FR4X4FPZUGNTQAPSTVB2HB2Y4A/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/BFQD3KUEMFBHPAPBGLWQC34L4OWL5HAZ/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/CLB4TW7KALB3EEQWNWCN7OUIWWVWWCG2/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/E72T67UPDRXHIDLO3OROR25YAMN4GGW5/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/FNA62Q767CFAFHBCDKYNPBMZWB7TWYVU/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/HT7T2R4MQKLIF4ODV4BDLPARWFPCJ5CZ/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/JIZSEFC3YKCGABA2BZW6ZJRMDZJMB7PJ/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/JMEXY22BFG5Q64HQCM5CK2Q7KDKVV4TY/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/KSEGD2IWKNUO3DWY4KQGUQM5BISRWHQE/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/LKYHSZQFDNR7RSA7LHVLLIAQMVYCUGBG/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/LNMZJCDHGLJJLXO4OXWJMTVQRNWOC7UL/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/VHUHTSXLXGXS7JYKBXTA3VINUPHTNGVU/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/VSRDIV77HNKUSM7SJC5BKE5JSHLHU2NK/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/WE2I52RHNNU42PX6NZ2RBUHSFFJ2LVZX/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/WLPRQ5TWUQQXYWBJM7ECYDAIL2YVKIUH/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/X6QXN4ORIVF6XBW4WWFE7VNPVC74S45Y/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/XFOIBB4YFICHDM7IBOP7PWXW3FX4HLL2/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/ZB43REMKRQR62NJEI7I5NQ4FSXNLBKRT/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/ZKQSIKIAT5TJ3WSLU3RDBQ35YX4GY4V3/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce%40lists.fedoraproject.org/message/ZLU6U2R2IC2K64NDPNMV55AUAO65MAF4/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/3N4NJ7FR4X4FPZUGNTQAPSTVB2HB2Y4A/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/BFQD3KUEMFBHPAPBGLWQC34L4OWL5HAZ/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/CLB4TW7KALB3EEQWNWCN7OUIWWVWWCG2/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/E72T67UPDRXHIDLO3OROR25YAMN4GGW5/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/FNA62Q767CFAFHBCDKYNPBMZWB7TWYVU/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/HT7T2R4MQKLIF4ODV4BDLPARWFPCJ5CZ/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/JIZSEFC3YKCGABA2BZW6ZJRMDZJMB7PJ/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/JMEXY22BFG5Q64HQCM5CK2Q7KDKVV4TY/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/KSEGD2IWKNUO3DWY4KQGUQM5BISRWHQE/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/LKYHSZQFDNR7RSA7LHVLLIAQMVYCUGBG/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/LNMZJCDHGLJJLXO4OXWJMTVQRNWOC7UL/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/VHUHTSXLXGXS7JYKBXTA3VINUPHTNGVU/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/VSRDIV77HNKUSM7SJC5BKE5JSHLHU2NK/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/WLPRQ5TWUQQXYWBJM7ECYDAIL2YVKIUH/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/X6QXN4ORIVF6XBW4WWFE7VNPVC74S45Y/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/XFOIBB4YFICHDM7IBOP7PWXW3FX4HLL2/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/ZB43REMKRQR62NJEI7I5NQ4FSXNLBKRT/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/ZKQSIKIAT5TJ3WSLU3RDBQ35YX4GY4V3/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.fedoraproject.org/archives/list/package-announce@lists.fedoraproject.org/message/ZLU6U2R2IC2K64NDPNMV55AUAO65MAF4/',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.w3.org/Archives/Public/ietf-http-wg/2023OctDec/0025.html',
                },
                {
                    type: 'WEB',
                    url: 'https://mailman.nginx.org/pipermail/nginx-devel/2023-October/S36Q5HBXR7CAIMPLLPRSSSYR4PCMWILK.html',
                },
                {
                    type: 'WEB',
                    url: 'https://martinthomson.github.io/h2-stream-limits/draft-thomson-httpbis-h2-stream-limits.html',
                },
                {
                    type: 'WEB',
                    url: 'https://msrc.microsoft.com/blog/2023/10/microsoft-response-to-distributed-denial-of-service-ddos-attacks-against-http/2/',
                },
                {
                    type: 'WEB',
                    url: 'https://msrc.microsoft.com/update-guide/vulnerability/CVE-2023-44487',
                },
                {
                    type: 'WEB',
                    url: 'https://my.f5.com/manage/s/article/K000137106',
                },
                {
                    type: 'WEB',
                    url: 'https://netty.io/news/2023/10/10/4-1-100-Final.html',
                },
                {
                    type: 'WEB',
                    url: 'https://news.ycombinator.com/item?id=37830987',
                },
                {
                    type: 'WEB',
                    url: 'https://news.ycombinator.com/item?id=37830998',
                },
                {
                    type: 'WEB',
                    url: 'https://news.ycombinator.com/item?id=37831062',
                },
                {
                    type: 'WEB',
                    url: 'https://news.ycombinator.com/item?id=37837043',
                },
                {
                    type: 'WEB',
                    url: 'https://openssf.org/blog/2023/10/10/http-2-rapid-reset-vulnerability-highlights-need-for-rapid-response/',
                },
                {
                    type: 'WEB',
                    url: 'https://seanmonstar.com/post/730794151136935936/hyper-http2-rapid-reset-unaffected',
                },
                {
                    type: 'WEB',
                    url: 'https://security.gentoo.org/glsa/202311-09',
                },
                {
                    type: 'WEB',
                    url: 'https://security.netapp.com/advisory/ntap-20231016-0001/',
                },
                {
                    type: 'WEB',
                    url: 'https://security.paloaltonetworks.com/CVE-2023-44487',
                },
                {
                    type: 'WEB',
                    url: 'https://tomcat.apache.org/security-10.html#Fixed_in_Apache_Tomcat_10.1.14',
                },
                {
                    type: 'WEB',
                    url: 'https://tomcat.apache.org/security-11.html#Fixed_in_Apache_Tomcat_11.0.0-M12',
                },
                {
                    type: 'WEB',
                    url: 'https://tomcat.apache.org/security-8.html#Fixed_in_Apache_Tomcat_8.5.94',
                },
                {
                    type: 'WEB',
                    url: 'https://tomcat.apache.org/security-9.html#Fixed_in_Apache_Tomcat_9.0.81',
                },
                {
                    type: 'WEB',
                    url: 'https://ubuntu.com/security/CVE-2023-44487',
                },
                {
                    type: 'WEB',
                    url: 'https://www.bleepingcomputer.com/news/security/new-http-2-rapid-reset-zero-day-attack-breaks-ddos-records/',
                },
                {
                    type: 'WEB',
                    url: 'https://www.cisa.gov/news-events/alerts/2023/10/10/http2-rapid-reset-vulnerability-cve-2023-44487',
                },
                {
                    type: 'WEB',
                    url: 'https://www.darkreading.com/cloud/internet-wide-zero-day-bug-fuels-largest-ever-ddos-event',
                },
                {
                    type: 'WEB',
                    url: 'https://www.debian.org/security/2023/dsa-5521',
                },
                {
                    type: 'WEB',
                    url: 'https://www.debian.org/security/2023/dsa-5522',
                },
                {
                    type: 'WEB',
                    url: 'https://www.debian.org/security/2023/dsa-5540',
                },
                {
                    type: 'WEB',
                    url: 'https://www.debian.org/security/2023/dsa-5549',
                },
                {
                    type: 'WEB',
                    url: 'https://www.debian.org/security/2023/dsa-5558',
                },
                {
                    type: 'WEB',
                    url: 'https://www.debian.org/security/2023/dsa-5570',
                },
                {
                    type: 'WEB',
                    url: 'https://www.haproxy.com/blog/haproxy-is-not-affected-by-the-http-2-rapid-reset-attack-cve-2023-44487',
                },
                {
                    type: 'WEB',
                    url: 'https://www.netlify.com/blog/netlify-successfully-mitigates-cve-2023-44487/',
                },
                {
                    type: 'WEB',
                    url: 'https://www.nginx.com/blog/http-2-rapid-reset-attack-impacting-f5-nginx-products/',
                },
                {
                    type: 'WEB',
                    url: 'https://www.openwall.com/lists/oss-security/2023/10/10/6',
                },
                {
                    type: 'WEB',
                    url: 'https://www.phoronix.com/news/HTTP2-Rapid-Reset-Attack',
                },
                {
                    type: 'WEB',
                    url: 'https://www.theregister.com/2023/10/10/http2_rapid_reset_zeroday/',
                },
                {
                    type: 'WEB',
                    url: 'http://www.openwall.com/lists/oss-security/2023/10/13/4',
                },
                {
                    type: 'WEB',
                    url: 'http://www.openwall.com/lists/oss-security/2023/10/13/9',
                },
                {
                    type: 'WEB',
                    url: 'http://www.openwall.com/lists/oss-security/2023/10/18/4',
                },
                {
                    type: 'WEB',
                    url: 'http://www.openwall.com/lists/oss-security/2023/10/18/8',
                },
                {
                    type: 'WEB',
                    url: 'http://www.openwall.com/lists/oss-security/2023/10/19/6',
                },
                {
                    type: 'WEB',
                    url: 'http://www.openwall.com/lists/oss-security/2023/10/20/8',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:N/A:L',
                    baseScore: 5.3,
                    ranking: 'Medium',
                },
            ],
            fixed: '10.1.14',
        },
    },
    {
        info: {
            name: 'org.apache.tomcat.embed:tomcat-embed-core',
            version: '10.1.5',
            ecosystem: 'Maven',
        },
        vuln: {
            id: 'GHSA-r6j3-px5g-cq3x',
            summary: 'Apache Tomcat Improper Input Validation vulnerability',
            details:
                'Improper Input Validation vulnerability in Apache Tomcat.\n\nTomcat from 11.0.0-M1 through 11.0.0-M11, from 10.1.0-M1 through 10.1.13, from 9.0.0-M1 through 9.0.81 and from 8.5.0 through 8.5.93 did not correctly parse HTTP trailer headers. A specially crafted, invalid trailer header could cause Tomcat to treat a single \nrequest as multiple requests leading to the possibility of request smuggling when behind a reverse proxy.\n\nUsers are recommended to upgrade to version 11.0.0-M12 onwards, 10.1.14 onwards, 9.0.81 onwards or 8.5.94 onwards, which fix the issue.',
            aliases: ['BIT-tomcat-2023-45648', 'CVE-2023-45648'],
            modified: '2024-02-16T08:22:54.509191Z',
            published: '2023-10-10T21:31:12Z',
            database_specific: {
                severity: 'MODERATE',
                github_reviewed_at: '2023-10-10T22:29:58Z',
                github_reviewed: true,
                cwe_ids: ['CWE-20'],
                nvd_published_at: '2023-10-10T19:15:09Z',
            },
            references: [
                {
                    type: 'ADVISORY',
                    url: 'https://nvd.nist.gov/vuln/detail/CVE-2023-45648',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/tomcat/commit/59583245639d8c42ae0009f4a4a70464d3ea70a0',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/tomcat/commit/8ecff306507be8e4fd3adee1ae5de1ea6661a8f4',
                },
                {
                    type: 'WEB',
                    url: 'https://github.com/apache/tomcat/commit/eb5c094e5560764cda436362254997511a3ca1f6',
                },
                {
                    type: 'PACKAGE',
                    url: 'https://github.com/apache/tomcat',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.apache.org/thread/2pv8yz1pyp088tsxfb7ogltk9msk0jdp',
                },
                {
                    type: 'WEB',
                    url: 'https://lists.debian.org/debian-lts-announce/2023/10/msg00020.html',
                },
                {
                    type: 'WEB',
                    url: 'https://security.netapp.com/advisory/ntap-20231103-0007',
                },
                {
                    type: 'WEB',
                    url: 'https://www.debian.org/security/2023/dsa-5521',
                },
                {
                    type: 'WEB',
                    url: 'https://www.debian.org/security/2023/dsa-5522',
                },
                {
                    type: 'WEB',
                    url: 'http://www.openwall.com/lists/oss-security/2023/10/10/10',
                },
            ],
            schema_version: '1.6.0',
            severity: [
                {
                    type: 'CVSS_V3',
                    score: 'CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:L/A:N',
                    baseScore: 5.3,
                    ranking: 'Medium',
                },
            ],
            fixed: '10.1.14',
        },
    },
]
