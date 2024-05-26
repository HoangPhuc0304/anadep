import { severities } from '../data/helper'
import { LibraryUI, VulnerabilitySummary } from '../model/library'
import { store } from '../redux/store'

export const getLibrarySearch = (
    libraries: LibraryUI[],
    text: String
): LibraryUI[] => {
    return libraries.filter((lib) =>
        getLibraryName(lib).toLowerCase().includes(text.toLowerCase())
    )
}

export const getLibraryName = (lib: LibraryUI): string => {
    return `${lib.name}:${lib.version}`
}

export const getRepoFromUrl = (url: string): string => {
    const repo = url.split('/').slice(-2).join('/')
    if (repo.endsWith('.git')) {
        return repo.replace('.git', '')
    }
    return repo
}

export const getVulnerabilityStatus = (
    vulnerabilitySummary: VulnerabilitySummary
) => {
    if (vulnerabilitySummary.critical) {
        return severities[3].value
    } else if (vulnerabilitySummary.high) {
        return severities[2].value
    } else if (vulnerabilitySummary.medium) {
        return severities[1].value
    } else if (vulnerabilitySummary.low) {
        return severities[0].value
    }
    return 'No issues'
}

export function getAnadepEnable() {
    return store.getState().setting.anadepEnable
}