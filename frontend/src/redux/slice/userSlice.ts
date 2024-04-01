import { User } from '../../model/library'
import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

const initialState: {
    currentUser: User
} = {
    currentUser: {
        id: '',
        githubUserId: 0,
        login: '',
        name: '',
        avatarUrl: '',
        githubUrl: '',
        email: '',
        githubToken: '',
    },
}

export const userSlice = createSlice({
    name: 'user',
    initialState,
    reducers: {
        update: (state, action: PayloadAction<User>) => {
            return {
                currentUser: action.payload,
            }
        },
        remove: (state, action: PayloadAction<User>) => {
            state.currentUser = {
                id: '',
                githubUserId: 0,
                login: '',
                name: '',
                avatarUrl: '',
                githubUrl: '',
                email: '',
                githubToken: '',
            }
        },
    },
})

export const { update, remove } = userSlice.actions

export default userSlice
