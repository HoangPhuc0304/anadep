// import { GiftIcon, MapIcon, MedalIcon, PlaneIcon } from './Icons';
// import { Card, CardContent, CardHeader, CardTitle } from '../../ui/card';

import { ScrollArea, ScrollBar } from '../../ui/scroll-area'
import { Button } from '../../ui/button'
import { CopyIcon } from '@radix-ui/react-icons'

const FormatResult: React.FC = () => {
    // const data = fs.readFileSync('../../data/analyze-response.json', { encoding: 'utf-8', flag: 'r' });
    // const analysisResultList: AnalysisResult = JSON.parse(data);

    return (
        <section id="format" className="container text-center py-24 sm:py-32">
            <h2 className="text-3xl md:text-4xl font-bold ">
                Dependency Schema
            </h2>
            <p className="md:w-3/4 mx-auto mt-4 mb-8 text-xl text-muted-foreground">
                An example response showcasing the retrieval of a vulnerable
                dependency.
            </p>

            <div className="grid grid-cols-1 gap-8">
                <div className="grid gap-8 rounded-md bg-black ">
                    <ScrollArea className="h-[640px] w-full px-4">
                        <div className="rounded-md bg-black p-6 relative h-full mb-8">
                            <pre>
                                <code className="grid gap-1 text-sm text-muted-foreground [&_span]:h-4 text-left">
                                    <div>
                                        {`
                    {
                        "info": {
                            "name": "org.springframework.security:spring-security-core",
                            "version": "6.0.1",
                            "ecosystem": "Maven"
                        },
                        "vuln": {
                            "id": "GHSA-x873-6rgc-94jc",
                            "summary": "Spring Security logout not clearing security context",
                            "details": "In Spring Security, versions 5.7.x prior to 5.7.8, versions 5.8.x prior to 5.8.3, and versions 6.0.x prior to 6.0.3, the logout support does not properly clean the security context if using serialized versions. Additionally, it is not possible to explicitly save an empty security context to the HttpSessionSecurityContextRepository. This vulnerability can keep users authenticated even after they performed logout. Users of affected versions should apply the following mitigation. 5.7.x users should upgrade to 5.7.8. 5.8.x users should upgrade to 5.8.3. 6.0.x users should upgrade to 6.0.3.",
                            "aliases": [
                                "CVE-2023-20862"
                            ],
                            "modified": "2024-02-16T08:23:55.197987Z",
                            "published": "2023-04-19T21:30:26Z",
                            "databaseSpecific": {
                                "severity": "MODERATE",
                                "github_reviewed_at": "2023-05-05T22:58:08Z",
                                "github_reviewed": true,
                                "cwe_ids": [
                                    "CWE-459"
                                ],
                                "nvd_published_at": "2023-04-19T20:15:10Z"
                            },
                            "references": [
                                {
                                    "type": "ADVISORY",
                                    "url": "https://nvd.nist.gov/vuln/detail/CVE-2023-20862"
                                },
                                {
                                    "type": "PACKAGE",
                                    "url": "https://github.com/spring-projects/spring-security"
                                },
                                {
                                    "type": "WEB",
                                    "url": "https://security.netapp.com/advisory/ntap-20230526-0002"
                                },
                                {
                                    "type": "WEB",
                                    "url": "https://spring.io/security/cve-2023-20862"
                                }
                            ],
                            "schemaVersion": "1.6.0",
                            "severity": [
                                {
                                    "type": "CVSS_V3",
                                    "score": "CVSS:3.1/AV:N/AC:L/PR:L/UI:N/S:U/C:L/I:L/A:L",
                                    "baseScore": 6.3,
                                    "ranking": "Medium"
                                }
                            ],
                            "fixed": "6.0.3"
                        }
                    }`}
                                    </div>
                                </code>
                            </pre>
                            <Button
                                type="submit"
                                size="sm"
                                className="px-3 absolute top-2 right-2"
                            >
                                <span className="sr-only">Copy</span>
                                <CopyIcon className="h-4 w-4" />
                            </Button>
                        </div>
                        <ScrollBar orientation="horizontal" />
                    </ScrollArea>
                </div>
            </div>
        </section>
    )
}

export default FormatResult
