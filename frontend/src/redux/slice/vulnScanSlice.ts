import { LibraryScanUI } from '../../model/library'
import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

const initialState: LibraryScanUI[] = []

export const vulnScanSlice = createSlice({
    name: 'vulnScan',
    initialState,
    reducers: {
        update: (state, action: PayloadAction<LibraryScanUI[]>) => {
            return action.payload
        },
    },
})

export const { update } = vulnScanSlice.actions

export default vulnScanSlice
