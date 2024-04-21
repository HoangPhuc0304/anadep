export interface LibraryScanUI {
    info: LibraryUI
    vuln: VulnerabilityResponse
}

export interface LibraryUI {
    name: string
    version: string
    ecosystem: string
}

export interface ScanningResult {
    libraries: LibraryUI[]
    ecosystem: string
    libraryCount: number
    includeTransitive: boolean
    responseTime: number
}

export interface AnalysisUIResult {
    libs: LibraryScanUI[]
    ecosystem: string
    issuesCount: number
    libraryCount: number
    includeSafe: boolean
    responseTime: number
}

export interface VulnerabilityResponse {
    id: string
    summary?: string
    details?: string
    aliases: string[]
    modified: string
    published: string
    database_specific?: DatabaseSpecific
    references: Reference[]
    affected?: Affected[]
    schema_version: string
    severity: Severity[]
    fixed?: string
}

export interface DatabaseSpecific {
    github_reviewed_at?: string
    github_reviewed?: boolean
    severity?: string
    cwe_ids?: string[]
    nvd_published_at?: string
}

export interface Reference {
    type?: string
    url?: string
}

export interface Severity {
    type?: string
    score?: string
    baseScore?: number
    ranking?: string
}

export interface Affected {
    package?: LibraryEcosystem
    ranges?: Range[]
    versions?: string[]
    database_specific?: DatabaseSourceLink
}

export interface Range {
    type?: string
    events?: Event[]
}

export interface LibraryEcosystem {
    name?: string
    ecosystem?: string
    purl?: string
}

export interface Event {
    introduced?: string
    fixed?: string
    last_affected?: string
    limit?: string
}

export interface DatabaseSourceLink {
    source?: string
}

export interface ReportForm {
    projectName: string
    author: string
    format: 'pdf' | 'excel' | 'json'
    type?: 'vulns' | 'sbom'
}

export interface AccessTokenResponse {
    accessToken: string
    scope: string
    tokenType: string
}

export interface User {
    id?: string
    githubUserId: number
    login: string
    name: string
    avatarUrl: string
    githubUrl: string
    email: string
    githubToken?: string
}

export interface Repository {
    id?: string
    githubRepoId: number
    name: string
    fullName: string
    owner: string
    defaultBranch: string
    isPublic: boolean
    githubUrl: string
    language: string
    scanningResult?: ScanningResult
    userId?: string
    updatedBy?: string
    createdAt?: string
    vulnerabilityResult?: AnalysisUIResult
    vulnerabilitySummary?: VulnerabilitySummary
    historyId: string
}

export interface VulnerabilitySummary {
    none: number
    low: number
    medium: number
    high: number
    critical: number
    status: string
}

export interface History {
    id: string
    scanningResult: ScanningResult
    vulnerabilityResult: AnalysisUIResult
    type: string
    createdAt: string
    repoId: string
}

export interface LibraryFix {
    name: string
    currentVersion: string
    fixedVersion: string
    ecosystem: string
    severity: string
}

export interface FixResult {
    libs: LibraryFix[]
    ecosystem: string
    responseTime: number
}
