import React from 'react'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import './App.css'
import Header from './component/header/Header'
import Home from './component/home/Home'
import NamespacePage from './page/namespace/page'
import { VulnerabilityScan } from './page/namespace/vulnerability-scan'
import { SbomScan } from './page/namespace/sbom'
import { Toaster } from './component/ui/toaster'
import { SearchingScan } from './page/namespace/vulnerability-search'
import DetailsPage from './page/details/page'
import { AuthCallbackPage } from './page/AuthCallbackPage'
import { Project } from './page/namespace/project'
import { History } from './page/namespace/history'
import { HistoryDetails } from './page/details/page-history'
// import MailPage from './component/mail/pageMail';
// import TaskPage from './component/tasks/page';
// import VulnerabilityPage from './component/table/page';

const App: React.FC = () => {
    return (
        <div id="App">
            <Router>
                <Header />
                <div
                    id="body"
                    style={{
                        marginTop: 'var(--header-height)',
                        padding: '0 var(--spacing-three-x)',
                    }}
                >
                    <Routes>
                        <Route path="/" element={<Home />} />
                        {/* <Route path="/test" element={<MailPage />} /> */}
                        <Route
                            path="/namespace"
                            element={
                                <NamespacePage
                                    component={<VulnerabilityScan />}
                                />
                            }
                        />
                        <Route
                            path="/search"
                            element={
                                <NamespacePage component={<SearchingScan />} />
                            }
                        />
                        <Route
                            path="/sbom"
                            element={<NamespacePage component={<SbomScan />} />}
                        />
                        <Route
                            path="/project"
                            element={<NamespacePage component={<Project />} />}
                        />
                        <Route
                            path="/project/:id"
                            element={<NamespacePage component={<History />} />}
                        />
                        <Route
                            path="/project/:repoId/history/:id"
                            element={
                                <NamespacePage component={<HistoryDetails />} />
                            }
                        />
                        <Route path="/vuln/:id" element={<DetailsPage />} />
                        {/* <Route element={<Layout />}>
              {["/","/news"].map((path) => <Route path={path} element={<Home />} />)}
              <Route path="/video" element={<Video />} />
              <Route path="/picture" element={<Picture />} />
            </Route>
            <Route path="/profile" element={<Profile />} /> */}
                        <Route
                            path="/auth/callback"
                            element={<AuthCallbackPage />}
                        />
                    </Routes>
                </div>
            </Router>
            <Toaster />
        </div>
    )
}

export default App
