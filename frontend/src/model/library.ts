interface LibraryScanUI {
    info: LibraryUI
    vuln: VulnerabilityResponse
}

export interface LibraryUI {
    name: string
    version: string
    ecosystem: string
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

export default LibraryScanUI
