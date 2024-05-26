import { Setting, User } from '../../model/library'
import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

const initialState: Setting = {
    anadepEnable: process.env.REACT_APP_ENABLE_ANADEP_DB === 'true'
}

export const settingSlice = createSlice({
    name: 'setting',
    initialState,
    reducers: {
        updateAnadepEnable: (state, action: PayloadAction<Setting>) => {
            state.anadepEnable = action.payload.anadepEnable
        }
    },
})

export const { updateAnadepEnable } = settingSlice.actions

export default settingSlice
