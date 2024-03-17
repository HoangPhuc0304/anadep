import { VulnerabilityResponse } from '@/model/library'

export const vulnerability: VulnerabilityResponse = {
    id: 'GHSA-fccv-jmmp-qg76',
    summary: 'Apache Tomcat Improper Input Validation vulnerability',
    details:
        'Improper Input Validation vulnerability in Apache Tomcat. Tomcat from 11.0.0-M1 through 11.0.0-M10, from 10.1.0-M1 through 10.1.15, from 9.0.0-M1 through 9.0.82, and from 8.5.0 through 8.5.95 did not correctly parse HTTP trailer headers. A trailer header that exceeded the header size limit could cause Tomcat to treat a single request as multiple requests leading to the possibility of request smuggling when behind a reverse proxy.\n\nUsers are recommended to upgrade to version 11.0.0-M11 onwards, 10.1.16 onwards, 9.0.83 onwards or 8.5.96 onwards, which fix the issue.',
    aliases: ['BIT-tomcat-2023-46589', 'CVE-2023-46589'],
    modified: '2023-12-21T22:16:22.371265Z',
    published: '2023-11-28T18:30:23Z',
    database_specific: {
        github_reviewed_at: '2023-11-28T23:28:54Z',
        github_reviewed: true,
        severity: 'HIGH',
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
    affected: [
        {
            package: {
                name: 'org.apache.tomcat:tomcat-catalina',
                ecosystem: 'Maven',
                purl: 'pkg:maven/org.apache.tomcat/tomcat-catalina',
            },
            ranges: [
                {
                    type: 'ECOSYSTEM',
                    events: [
                        {
                            introduced: '11.0.0-M1',
                        },
                        {
                            fixed: '11.0.0-M11',
                        },
                    ],
                },
            ],
            versions: [
                '11.0.0-M1',
                '11.0.0-M10',
                '11.0.0-M3',
                '11.0.0-M4',
                '11.0.0-M5',
                '11.0.0-M6',
                '11.0.0-M7',
                '11.0.0-M9',
            ],
            database_specific: {
                source: 'https://github.com/github/advisory-database/blob/main/advisories/github-reviewed/2023/11/GHSA-fccv-jmmp-qg76/GHSA-fccv-jmmp-qg76.json',
            },
        },
        {
            package: {
                name: 'org.apache.tomcat:tomcat-catalina',
                ecosystem: 'Maven',
                purl: 'pkg:maven/org.apache.tomcat/tomcat-catalina',
            },
            ranges: [
                {
                    type: 'ECOSYSTEM',
                    events: [
                        {
                            introduced: '10.1.0-M1',
                        },
                        {
                            fixed: '10.1.16',
                        },
                    ],
                },
            ],
            versions: [
                '10.1.0',
                '10.1.0-M1',
                '10.1.0-M10',
                '10.1.0-M11',
                '10.1.0-M12',
                '10.1.0-M14',
                '10.1.0-M15',
                '10.1.0-M16',
                '10.1.0-M17',
                '10.1.0-M2',
                '10.1.0-M4',
                '10.1.0-M5',
                '10.1.0-M6',
                '10.1.0-M7',
                '10.1.0-M8',
                '10.1.1',
                '10.1.10',
                '10.1.11',
                '10.1.12',
                '10.1.13',
                '10.1.14',
                '10.1.15',
                '10.1.2',
                '10.1.4',
                '10.1.5',
                '10.1.6',
                '10.1.7',
                '10.1.8',
                '10.1.9',
            ],
            database_specific: {
                source: 'https://github.com/github/advisory-database/blob/main/advisories/github-reviewed/2023/11/GHSA-fccv-jmmp-qg76/GHSA-fccv-jmmp-qg76.json',
            },
        },
        {
            package: {
                name: 'org.apache.tomcat:tomcat-catalina',
                ecosystem: 'Maven',
                purl: 'pkg:maven/org.apache.tomcat/tomcat-catalina',
            },
            ranges: [
                {
                    type: 'ECOSYSTEM',
                    events: [
                        {
                            introduced: '9.0.0-M1',
                        },
                        {
                            fixed: '9.0.83',
                        },
                    ],
                },
            ],
            versions: [
                '9.0.0.M1',
                '9.0.0.M10',
                '9.0.0.M11',
                '9.0.0.M13',
                '9.0.0.M15',
                '9.0.0.M17',
                '9.0.0.M18',
                '9.0.0.M19',
                '9.0.0.M20',
                '9.0.0.M21',
                '9.0.0.M22',
                '9.0.0.M25',
                '9.0.0.M26',
                '9.0.0.M27',
                '9.0.0.M3',
                '9.0.0.M4',
                '9.0.0.M6',
                '9.0.0.M8',
                '9.0.0.M9',
                '9.0.1',
                '9.0.10',
                '9.0.11',
                '9.0.12',
                '9.0.13',
                '9.0.14',
                '9.0.16',
                '9.0.17',
                '9.0.19',
                '9.0.2',
                '9.0.20',
                '9.0.21',
                '9.0.22',
                '9.0.24',
                '9.0.26',
                '9.0.27',
                '9.0.29',
                '9.0.30',
                '9.0.31',
                '9.0.33',
                '9.0.34',
                '9.0.35',
                '9.0.36',
                '9.0.37',
                '9.0.38',
                '9.0.39',
                '9.0.4',
                '9.0.40',
                '9.0.41',
                '9.0.43',
                '9.0.44',
                '9.0.45',
                '9.0.46',
                '9.0.48',
                '9.0.5',
                '9.0.50',
                '9.0.52',
                '9.0.53',
                '9.0.54',
                '9.0.55',
                '9.0.56',
                '9.0.58',
                '9.0.59',
                '9.0.6',
                '9.0.60',
                '9.0.62',
                '9.0.63',
                '9.0.64',
                '9.0.65',
                '9.0.67',
                '9.0.68',
                '9.0.69',
                '9.0.7',
                '9.0.70',
                '9.0.71',
                '9.0.72',
                '9.0.73',
                '9.0.74',
                '9.0.75',
                '9.0.76',
                '9.0.78',
                '9.0.79',
                '9.0.8',
                '9.0.80',
                '9.0.81',
                '9.0.82',
            ],
            database_specific: {
                source: 'https://github.com/github/advisory-database/blob/main/advisories/github-reviewed/2023/11/GHSA-fccv-jmmp-qg76/GHSA-fccv-jmmp-qg76.json',
            },
        },
        {
            package: {
                name: 'org.apache.tomcat:tomcat-catalina',
                ecosystem: 'Maven',
                purl: 'pkg:maven/org.apache.tomcat/tomcat-catalina',
            },
            ranges: [
                {
                    type: 'ECOSYSTEM',
                    events: [
                        {
                            introduced: '8.5.0',
                        },
                        {
                            fixed: '8.5.96',
                        },
                    ],
                },
            ],
            versions: [
                '8.5.0',
                '8.5.11',
                '8.5.12',
                '8.5.13',
                '8.5.14',
                '8.5.15',
                '8.5.16',
                '8.5.19',
                '8.5.2',
                '8.5.20',
                '8.5.21',
                '8.5.23',
                '8.5.24',
                '8.5.27',
                '8.5.28',
                '8.5.29',
                '8.5.3',
                '8.5.30',
                '8.5.31',
                '8.5.32',
                '8.5.33',
                '8.5.34',
                '8.5.35',
                '8.5.37',
                '8.5.38',
                '8.5.39',
                '8.5.4',
                '8.5.40',
                '8.5.41',
                '8.5.42',
                '8.5.43',
                '8.5.45',
                '8.5.46',
                '8.5.47',
                '8.5.49',
                '8.5.5',
                '8.5.50',
                '8.5.51',
                '8.5.53',
                '8.5.54',
                '8.5.55',
                '8.5.56',
                '8.5.57',
                '8.5.58',
                '8.5.59',
                '8.5.6',
                '8.5.60',
                '8.5.61',
                '8.5.63',
                '8.5.64',
                '8.5.65',
                '8.5.66',
                '8.5.68',
                '8.5.69',
                '8.5.70',
                '8.5.71',
                '8.5.72',
                '8.5.73',
                '8.5.75',
                '8.5.76',
                '8.5.77',
                '8.5.78',
                '8.5.79',
                '8.5.8',
                '8.5.81',
                '8.5.82',
                '8.5.83',
                '8.5.84',
                '8.5.85',
                '8.5.86',
                '8.5.87',
                '8.5.88',
                '8.5.89',
                '8.5.9',
                '8.5.90',
                '8.5.91',
                '8.5.92',
                '8.5.93',
                '8.5.94',
                '8.5.95',
            ],
            database_specific: {
                source: 'https://github.com/github/advisory-database/blob/main/advisories/github-reviewed/2023/11/GHSA-fccv-jmmp-qg76/GHSA-fccv-jmmp-qg76.json',
            },
        },
        {
            package: {
                name: 'org.apache.tomcat.embed:tomcat-embed-core',
                ecosystem: 'Maven',
                purl: 'pkg:maven/org.apache.tomcat.embed/tomcat-embed-core',
            },
            ranges: [
                {
                    type: 'ECOSYSTEM',
                    events: [
                        {
                            introduced: '11.0.0-M1',
                        },
                        {
                            fixed: '11.0.0-M11',
                        },
                    ],
                },
            ],
            versions: [
                '11.0.0-M1',
                '11.0.0-M10',
                '11.0.0-M3',
                '11.0.0-M4',
                '11.0.0-M5',
                '11.0.0-M6',
                '11.0.0-M7',
                '11.0.0-M9',
            ],
            database_specific: {
                source: 'https://github.com/github/advisory-database/blob/main/advisories/github-reviewed/2023/11/GHSA-fccv-jmmp-qg76/GHSA-fccv-jmmp-qg76.json',
            },
        },
        {
            package: {
                name: 'org.apache.tomcat.embed:tomcat-embed-core',
                ecosystem: 'Maven',
                purl: 'pkg:maven/org.apache.tomcat.embed/tomcat-embed-core',
            },
            ranges: [
                {
                    type: 'ECOSYSTEM',
                    events: [
                        {
                            introduced: '10.1.0-M1',
                        },
                        {
                            fixed: '10.1.16',
                        },
                    ],
                },
            ],
            versions: [
                '10.1.0',
                '10.1.0-M1',
                '10.1.0-M10',
                '10.1.0-M11',
                '10.1.0-M12',
                '10.1.0-M14',
                '10.1.0-M15',
                '10.1.0-M16',
                '10.1.0-M17',
                '10.1.0-M2',
                '10.1.0-M4',
                '10.1.0-M5',
                '10.1.0-M6',
                '10.1.0-M7',
                '10.1.0-M8',
                '10.1.1',
                '10.1.10',
                '10.1.11',
                '10.1.12',
                '10.1.13',
                '10.1.14',
                '10.1.15',
                '10.1.2',
                '10.1.4',
                '10.1.5',
                '10.1.6',
                '10.1.7',
                '10.1.8',
                '10.1.9',
            ],
            database_specific: {
                source: 'https://github.com/github/advisory-database/blob/main/advisories/github-reviewed/2023/11/GHSA-fccv-jmmp-qg76/GHSA-fccv-jmmp-qg76.json',
            },
        },
        {
            package: {
                name: 'org.apache.tomcat.embed:tomcat-embed-core',
                ecosystem: 'Maven',
                purl: 'pkg:maven/org.apache.tomcat.embed/tomcat-embed-core',
            },
            ranges: [
                {
                    type: 'ECOSYSTEM',
                    events: [
                        {
                            introduced: '9.0.0-M1',
                        },
                        {
                            fixed: '9.0.83',
                        },
                    ],
                },
            ],
            versions: [
                '9.0.0.M1',
                '9.0.0.M10',
                '9.0.0.M11',
                '9.0.0.M13',
                '9.0.0.M15',
                '9.0.0.M17',
                '9.0.0.M18',
                '9.0.0.M19',
                '9.0.0.M20',
                '9.0.0.M21',
                '9.0.0.M22',
                '9.0.0.M25',
                '9.0.0.M26',
                '9.0.0.M27',
                '9.0.0.M3',
                '9.0.0.M4',
                '9.0.0.M6',
                '9.0.0.M8',
                '9.0.0.M9',
                '9.0.1',
                '9.0.10',
                '9.0.11',
                '9.0.12',
                '9.0.13',
                '9.0.14',
                '9.0.16',
                '9.0.17',
                '9.0.19',
                '9.0.2',
                '9.0.20',
                '9.0.21',
                '9.0.22',
                '9.0.24',
                '9.0.26',
                '9.0.27',
                '9.0.29',
                '9.0.30',
                '9.0.31',
                '9.0.33',
                '9.0.34',
                '9.0.35',
                '9.0.36',
                '9.0.37',
                '9.0.38',
                '9.0.39',
                '9.0.4',
                '9.0.40',
                '9.0.41',
                '9.0.43',
                '9.0.44',
                '9.0.45',
                '9.0.46',
                '9.0.48',
                '9.0.5',
                '9.0.50',
                '9.0.52',
                '9.0.53',
                '9.0.54',
                '9.0.55',
                '9.0.56',
                '9.0.58',
                '9.0.59',
                '9.0.6',
                '9.0.60',
                '9.0.62',
                '9.0.63',
                '9.0.64',
                '9.0.65',
                '9.0.67',
                '9.0.68',
                '9.0.69',
                '9.0.7',
                '9.0.70',
                '9.0.71',
                '9.0.72',
                '9.0.73',
                '9.0.74',
                '9.0.75',
                '9.0.76',
                '9.0.78',
                '9.0.79',
                '9.0.8',
                '9.0.80',
                '9.0.81',
                '9.0.82',
            ],
            database_specific: {
                source: 'https://github.com/github/advisory-database/blob/main/advisories/github-reviewed/2023/11/GHSA-fccv-jmmp-qg76/GHSA-fccv-jmmp-qg76.json',
            },
        },
        {
            package: {
                name: 'org.apache.tomcat.embed:tomcat-embed-core',
                ecosystem: 'Maven',
                purl: 'pkg:maven/org.apache.tomcat.embed/tomcat-embed-core',
            },
            ranges: [
                {
                    type: 'ECOSYSTEM',
                    events: [
                        {
                            introduced: '8.5.0',
                        },
                        {
                            fixed: '8.5.96',
                        },
                    ],
                },
            ],
            versions: [
                '8.5.0',
                '8.5.11',
                '8.5.12',
                '8.5.13',
                '8.5.14',
                '8.5.15',
                '8.5.16',
                '8.5.19',
                '8.5.2',
                '8.5.20',
                '8.5.21',
                '8.5.23',
                '8.5.24',
                '8.5.27',
                '8.5.28',
                '8.5.29',
                '8.5.3',
                '8.5.30',
                '8.5.31',
                '8.5.32',
                '8.5.33',
                '8.5.34',
                '8.5.35',
                '8.5.37',
                '8.5.38',
                '8.5.39',
                '8.5.4',
                '8.5.40',
                '8.5.41',
                '8.5.42',
                '8.5.43',
                '8.5.45',
                '8.5.46',
                '8.5.47',
                '8.5.49',
                '8.5.5',
                '8.5.50',
                '8.5.51',
                '8.5.53',
                '8.5.54',
                '8.5.55',
                '8.5.56',
                '8.5.57',
                '8.5.58',
                '8.5.59',
                '8.5.6',
                '8.5.60',
                '8.5.61',
                '8.5.63',
                '8.5.64',
                '8.5.65',
                '8.5.66',
                '8.5.68',
                '8.5.69',
                '8.5.70',
                '8.5.71',
                '8.5.72',
                '8.5.73',
                '8.5.75',
                '8.5.76',
                '8.5.77',
                '8.5.78',
                '8.5.79',
                '8.5.8',
                '8.5.81',
                '8.5.82',
                '8.5.83',
                '8.5.84',
                '8.5.85',
                '8.5.86',
                '8.5.87',
                '8.5.88',
                '8.5.89',
                '8.5.9',
                '8.5.90',
                '8.5.91',
                '8.5.92',
                '8.5.93',
                '8.5.94',
                '8.5.95',
            ],
            database_specific: {
                source: 'https://github.com/github/advisory-database/blob/main/advisories/github-reviewed/2023/11/GHSA-fccv-jmmp-qg76/GHSA-fccv-jmmp-qg76.json',
            },
        },
    ],
    schema_version: '1.6.0',
    severity: [
        {
            type: 'CVSS_V3',
            score: 'CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:H/A:N',
        },
    ],
}