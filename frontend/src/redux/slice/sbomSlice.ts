import { LibraryUI, ScanningResult } from '../../model/library'
import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

const initialState: ScanningResult = {
    libraries: [],
    ecosystem: '',
    libraryCount: 0,
    includeTransitive: false,
    responseTime: 0,
}

export const sbomSlice = createSlice({
    name: 'sbom',
    initialState,
    reducers: {
        update: (state, action: PayloadAction<ScanningResult>) => {
            return action.payload
        },
    },
})

export const { update } = sbomSlice.actions

export default sbomSlice
