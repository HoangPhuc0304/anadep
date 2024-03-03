import { DEFAULT_ERROR_MESSAGE } from '../common/common'
import { client } from '../config/requestConfig'

export const getAnalysisUIResult = async (file: File) => {
    try {
        const formData = new FormData()
        formData.append('file', file)
        const response = await client.post('/ui/analyze', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        })
        return response.status === 200
            ? response.data.libs
            : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const downloadFileFormGitHubUrl = async (url: string) => {
    try {
        const response = await client.get(`/ui/repo/download?url=${url}`, {
            responseType: 'arraybuffer',
        })
        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const getVulnerabilityFromId = async (databaseId: string) => {
    try {
        const response = await client.get(`/ui/vulns/${databaseId}`)
        return response.status === 200 ? response.data : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}

export const getSearchUIResult = async (
    name: string,
    version: string,
    ecosystem: string
) => {
    try {
        const response = await client.post(
            '/ui/retrieve',
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
        return response.status === 200
            ? response.data.libs
            : DEFAULT_ERROR_MESSAGE
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
        return response.status === 200
            ? response.data.libraries
            : DEFAULT_ERROR_MESSAGE
    } catch (err) {
        return (err as Error).message
    }
}
