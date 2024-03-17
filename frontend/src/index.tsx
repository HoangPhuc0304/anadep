import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import App from './App'
import { ErrorBoundary } from 'react-error-boundary'
import { DEFAULT_ERROR_MESSAGE } from './common/common'
import { Provider } from 'react-redux'
import { persistor, store } from './redux/store'
import { PersistGate } from 'redux-persist/integration/react'

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement)
root.render(
    <React.StrictMode>
        <ErrorBoundary
            fallback={<h1 className="p-2">{DEFAULT_ERROR_MESSAGE}</h1>}
        >
            <Provider store={store}>
                <PersistGate loading={null} persistor={persistor}>
                    <App />
                </PersistGate>
            </Provider>
        </ErrorBoundary>
    </React.StrictMode>
)
