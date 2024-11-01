import {
    AnalysisUIResult,
    LibraryScanUI,
    ReportForm,
    Repository,
    ScanningResult,
    User,
} from '../model/library'
import { DEFAULT_ERROR_MESSAGE } from '../common/common'
import { client, githubClient } from '../config/requestConfig'
import { getAnadepEnable, getVulnerabilityStatus } from '../util/util'
import { Octokit } from 'octokit'

export const getAnalysisUIResult = async (file: File) => {
    try {
        const formData = new FormData()
        formData.append('file', file)
        const response = await client.post(
            getAnadepEnable()
                ? '/ui/analyze/v2'
                : '/ui/analyze',
            formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            }
        )
        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const getAuthAnalysisUIResult = async (
    file: File,
    repoId: string,
    accessToken: string
) => {
    try {
        const formData = new FormData()
        formData.append('file', file)
        const response = await client.post(
            getAnadepEnable()
                ? `/ui/analyze/v2/repo/${repoId}`
                : `/ui/analyze/repo/${repoId}`,
            formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: `Bearer ${accessToken}`,
                },
            }
        )
        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const downloadFileFormGitHubUrl = async (
    url: string,
    accessToken: any
) => {
    try {
        const response = await client.get(
            `/ui/repo/download?url=${url}&accessToken=${accessToken}`,
            {
                responseType: 'arraybuffer',
            }
        )
        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const getVulnerabilityFromId = async (databaseId: string) => {
    try {
        const response = await client.get(
            getAnadepEnable()
                ? `/ui/vulns/${databaseId}/v2`
                : `/ui/vulns/${databaseId}`
        )
        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const getSearchUIResult = async (
    name: string,
    version: string | null,
    ecosystem: string
) => {
    try {
        const response = await client.post(
            getAnadepEnable()
                ? '/ui/retrieve/v2'
                : '/ui/retrieve',
            {
                name,
                version,
                ecosystem,
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        )
        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const getScanUIResult = async (file: File) => {
    try {
        const formData = new FormData()
        formData.append('file', file)
        const response = await client.post('/ui/scan', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        })
        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const getAuthScanUIResult = async (
    file: File,
    repoId: string,
    accessToken: string
) => {
    try {
        const formData = new FormData()
        formData.append('file', file)
        const response = await client.post(
            `/ui/scan/repo/${repoId}`,
            formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: `Bearer ${accessToken}`,
                },
            }
        )
        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const generateReport = async (
    data: AnalysisUIResult | ScanningResult,
    reportForm: ReportForm,
    type: string
) => {
    try {
        const response = await client.post(
            '/api/report',
            {
                data: data,
            },
            {
                params: {
                    projectName: reportForm.projectName,
                    author: reportForm.author,
                    type: type,
                    format: reportForm.format,
                },
                responseType: 'blob',
            }
        )
        if (response.status === 200) {
            const blob = new Blob([response.data])
            const link = document.createElement('a')
            link.href = window.URL.createObjectURL(blob)
            link.download = response.headers['content-disposition']
                .split(';')[1]
                .split('=')[1]
                .replace(/"/g, '')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link)
        }
        return response.status === 200
    } catch (err) {
        return (err as Error).message
    }
}

export const getAccessToken = async (code: string) => {
    try {
        const response = await client.post(
            '/api/token',
            {
                code,
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        )

        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const getAuthUser = async (token: string) => {
    try {
        const response = await githubClient.get('/user', {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
        })
        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const saveUser = async (
    user: User,
    accessToken: string,
    refreshToken: string
) => {
    try {
        const response = await client.post(
            '/api/user',
            {
                ...user,
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        )
        const data = response.data

        const updateResponse = await client.post(
            '/ui/user/token',
            {
                userId: data.id,
                githubToken: accessToken,
                refreshToken: refreshToken,
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        )

        return response.status === 200 && updateResponse.status === 200
            ? data
            : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const getUser = async (userId: string | null, accessToken: string) => {
    try {
        let response
        if (userId) {
            response = await client.get(`/api/user/${userId}`, {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${accessToken}`,
                },
            })
        } else {
            response = await client.get('/api/user', {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${accessToken}`,
                },
            })
        }

        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const getAuthRepos = async (accessToken: string) => {
    try {
        const octokit = new Octokit({
            auth: accessToken,
        })

        const repos = await octokit.paginate(
            octokit.rest.repos.listForAuthenticatedUser
        )

        return repos.map(
            (res: {
                id: any
                name: any
                full_name: any
                owner: any
                default_branch: any
                private: any
                language: any
                html_url: any
            }) => ({
                githubRepoId: res.id,
                name: res.name,
                fullName: res.full_name,
                owner: res.owner.login,
                defaultBranch: res.default_branch,
                isPublic: !res.private,
                githubUrl: res.html_url,
                language: res.language,
            })
        )
    } catch (err) {
        return (err as Error).message
    }
}

export const getRepoById = async (repoId: string, accessToken: string) => {
    try {
        const response = await client.get(`/api/repo/${repoId}`, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${accessToken}`,
            },
        })

        const vulnerabilitySummary = await getVulnerabilitySummary(
            response.data.vulnerabilityResult?.libs
        )

        return response.status === 200
            ? {
                ...response.data,
                vulnerabilitySummary: {
                    ...vulnerabilitySummary,
                    status: getVulnerabilityStatus(vulnerabilitySummary),
                },
            }
            : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const saveRepo = async (repo: Repository, accessToken: string) => {
    try {
        const response = await client.post(
            '/api/repo',
            {
                ...repo,
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${accessToken}`,
                },
            }
        )

        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

// export const updateRepoWithVulns = async (
//     repo: Repository,
//     analysisUIResult: AnalysisUIResult,
//     accessToken: string
// ) => {
//     try {
//         const response = await client.put(
//             `/api/repo/${repo.id}/analyze`,
//             {
//                 ...analysisUIResult,
//             },
//             {
//                 headers: {
//                     'Content-Type': 'application/json',
//                     Authorization: `Bearer ${accessToken}`,
//                 },
//             }
//         )

//         return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
//     } catch (err) {
//         return (err as Error).message
//     }
// }

// export const updateRepoWithSbom = async (
//     repo: Repository,
//     scanningResult: ScanningResult,
//     accessToken: string
// ) => {
//     try {
//         const response = await client.put(
//             `/api/repo/${repo.id}/scan`,
//             {
//                 ...scanningResult,
//             },
//             {
//                 headers: {
//                     'Content-Type': 'application/json',
//                     Authorization: `Bearer ${accessToken}`,
//                 },
//             }
//         )

//         return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
//     } catch (err) {
//         return (err as Error).message
//     }
// }

export const getRepos = async (accessToken: string) => {
    try {
        const response = await client.get('/api/repo', {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${accessToken}`,
            },
        })

        return response.status === 200
            ? await Promise.all(
                response.data.map(async (res: any) => {
                    const vulnerabilitySummary =
                        await getVulnerabilitySummary(
                            res.vulnerabilityResult?.libs
                        )
                    return {
                        ...res,
                        vulnerabilitySummary: {
                            ...vulnerabilitySummary,
                            status: getVulnerabilityStatus(
                                vulnerabilitySummary
                            ),
                        },
                    }
                })
            )
            : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const removeRepoById = async (repoId: string, accessToken: string) => {
    try {
        const response = await client.delete(`/api/repo/${repoId}`, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${accessToken}`,
            },
        })

        return response.status === 200 ? true : false
    } catch (err) {
        return false
    }
}

export const getVulnerabilitySummary = async (
    libraryScanUI: LibraryScanUI[]
) => {
    try {
        const response = await client.post(
            '/ui/summary',
            {
                libs: libraryScanUI,
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        )
        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const getHistories = async (
    repoId: string,
    type: string,
    accessToken: string
) => {
    try {
        const response = await client.get(
            `/api/repo/${repoId}/history?type=${type}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${accessToken}`,
                },
            }
        )

        if (type === 'vulns') {
            return response.status === 200
                ? await Promise.all(
                    response.data.map(async (res: any) => {
                        const vulnerabilitySummary =
                            await getVulnerabilitySummary(
                                res.vulnerabilityResult?.libs
                            )
                        return {
                            ...res,
                            updatedBy: (
                                await getUser(res.userId, accessToken)
                            ).login,
                            vulnerabilitySummary: {
                                ...vulnerabilitySummary,
                                status: getVulnerabilityStatus(
                                    vulnerabilitySummary
                                ),
                            },
                        }
                    })
                )
                : DEFAULT_ERROR_MESSAGE
        } else {
            return response.status === 200
                ? await Promise.all(response.data.map(async (res: any) => {
                    return {
                        ...res,
                        updatedBy: (await getUser(res.userId, accessToken))
                            .login,
                    }
                }))
                : DEFAULT_ERROR_MESSAGE
        }
    } catch (err) {
        return (err as Error).message
    }
}

export const getHistoryById = async (
    repoId: string,
    historyId: string,
    accessToken: string
) => {
    try {
        const response = await client.get(
            `/api/repo/${repoId}/history/${historyId}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${accessToken}`,
                },
            }
        )

        return response.status === 200
            ? {
                ...response.data,
                updatedBy: (await getUser(response.data.userId, accessToken))
                    .login,
            }
            : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const removeHistoryById = async (
    repoId: string,
    historyId: string,
    accessToken: string
) => {
    try {
        const response = await client.delete(
            `/api/repo/${repoId}/history/${historyId}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${accessToken}`,
                },
            }
        )

        return response.status === 200 ? true : false
    } catch (err) {
        return false
    }
}

export const getAutoFix = async (analysisUIResult: AnalysisUIResult) => {
    try {
        const response = await client.post(
            '/api/auto-fix',
            {
                ...analysisUIResult,
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        )

        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const createPullRequest = async (
    repoId: string,
    historyId: string,
    accessToken: string
) => {
    try {
        const response = await client.post(
            `/api/fix/repo/${repoId}/history/${historyId}`,
            {},
            {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${accessToken}`,
                },
            }
        )
        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const createSecurityAdvisory = async (
    repoId: string,
    historyId: string,
    accessToken: string
) => {
    try {
        const response = await client.post(
            getAnadepEnable()
                ? `/api/security-advisories/v2/repo/${repoId}/history/${historyId}`
                : `/api/security-advisories/repo/${repoId}/history/${historyId}`,
            {},
            {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${accessToken}`,
                },
            }
        )
        return response.status === 200 ? true : false
    } catch (err) {
        return (err as Error).message
    }
}

export const isInstallGithubApp = async (
    token: string,
    githubUserId: number
) => {
    try {
        const octokit = new Octokit({
            auth: token,
        })

        const installations = await octokit.paginate(
            octokit.rest.apps.listInstallationsForAuthenticatedUser
        )
        return (
            Array.from(installations).filter(
                (i: any) => i.account && i.account.id === githubUserId
            ).length > 0
        )
    } catch (err) {
        return (err as Error).message
    }
}
