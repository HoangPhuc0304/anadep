import { LibraryScanUI } from '../../model/library'
import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

const initialState: LibraryScanUI[] = []

export const vulnSearchSlice = createSlice({
    name: 'vulnSearch',
    initialState,
    reducers: {
        update: (state, action: PayloadAction<LibraryScanUI[]>) => {
            return action.payload
        },
    },
})

export const { update } = vulnSearchSlice.actions

export default vulnSearchSlice
