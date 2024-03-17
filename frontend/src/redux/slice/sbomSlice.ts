import { LibraryUI } from '../../model/library'
import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

const initialState: LibraryUI[] = []

export const sbomSlice = createSlice({
    name: 'sbom',
    initialState,
    reducers: {
        update: (state, action: PayloadAction<LibraryUI[]>) => {
            return action.payload
        },
    },
})

export const { update } = sbomSlice.actions

export default sbomSlice
