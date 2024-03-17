import { combineReducers, configureStore } from '@reduxjs/toolkit'
import storageSession from 'redux-persist/lib/storage/session'
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

const persistConfig = {
    key: 'root',
    version: 1,
    storage: storageSession,
}

const rootReducer = combineReducers({
    vulnScan: vulnScanSlice.reducer,
    vulnSearch: vulnSearchSlice.reducer,
    sbom: sbomSlice.reducer,
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
