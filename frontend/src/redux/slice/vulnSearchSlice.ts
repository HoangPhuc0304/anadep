import { AnalysisUIResult, LibraryScanUI } from '../../model/library'
import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

const initialState: AnalysisUIResult = {
    libs: [],
    ecosystem: '',
    issuesCount: 0,
    libraryCount: 0,
    includeSafe: false,
    responseTime: 0,
}

export const vulnSearchSlice = createSlice({
    name: 'vulnSearch',
    initialState,
    reducers: {
        update: (state, action: PayloadAction<AnalysisUIResult>) => {
            return action.payload
        },
    },
})

export const { update } = vulnSearchSlice.actions

export default vulnSearchSlice
