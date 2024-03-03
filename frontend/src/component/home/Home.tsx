import React from 'react'
import './Home.css'
import Info from './components/Info'
import UseApi from './components/UseApi'
import FormatResult from './components/FormatResult'
import { FreeTier } from './components/FreeTier'
import { FAQ } from './components/FAQ'
import { Services } from './components/Services'
import Architecture from './components/Architecture'
import { ScrollToTop } from './components/ScrollToTop'
import { Footer } from './components/Footer'
import Download from './components/Download'

const Home: React.FC = () => {
    return (
        <div className="home">
            <Info />
            <Services />
            <Architecture />
            <UseApi />
            <Download />
            <FormatResult />
            <FreeTier />
            <FAQ />
            <Footer />
            <ScrollToTop />
        </div>
    )
}

export default Home
