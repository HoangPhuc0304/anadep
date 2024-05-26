import { combineReducers, configureStore } from '@reduxjs/toolkit'
import storage from 'redux-persist/lib/storage'
import {
    FLUSH,
    PAUSE,
    PERSIST,
    PURGE,
    REGISTER,
    REHYDRATE,
    persistReducer,
    persistStore,
} from 'redux-persist'
import vulnScanSlice from './slice/vulnScanSlice'
import vulnSearchSlice from './slice/vulnSearchSlice'
import sbomSlice from './slice/sbomSlice'
import userSlice from './slice/userSlice'
import settingSlice from './slice/settingSlice'

const persistConfig = {
    key: 'root',
    version: 1,
    storage: storage,
}

const rootReducer = combineReducers({
    vulnScan: vulnScanSlice.reducer,
    vulnSearch: vulnSearchSlice.reducer,
    sbom: sbomSlice.reducer,
    user: userSlice.reducer,
    setting: settingSlice.reducer,
})

const persistedReducer = persistReducer(persistConfig, rootReducer)

export const store = configureStore({
    reducer: persistedReducer,
    devTools: process.env.DEV_TOOLS === 'true',
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
            serializableCheck: {
                ignoredActions: [
                    FLUSH,
                    REHYDRATE,
                    PAUSE,
                    PERSIST,
                    PURGE,
                    REGISTER,
                ],
            },
        }),
})
export const persistor = persistStore(store)
export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch
