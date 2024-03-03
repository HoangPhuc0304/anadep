import { LibraryUI } from '../model/library'

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
