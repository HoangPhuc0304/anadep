import { Button, buttonVariants } from '../../ui/button'
import { GitHubLogoIcon } from '@radix-ui/react-icons'
import { EcosystemCards } from './EcosystemCards'
import { Link } from 'react-router-dom'

const Info: React.FC = () => {
    // const data = fs.readFileSync('../../data/analyze-response.json', { encoding: 'utf-8', flag: 'r' });
    // const analysisResultList: AnalysisResult = JSON.parse(data);

    return (
        <section
            id="info"
            className="container grid lg:grid-cols-2 place-items-center py-20 md:py-32 gap-10"
        >
            <div className="text-center lg:text-start space-y-6">
                <main className="text-5xl md:text-6xl font-bold">
                    <div className="mb-4">
                        <span className="inline bg-gradient-to-r from-[#F596D3]  to-[#D247BF] text-transparent bg-clip-text">
                            Analyze
                        </span>{' '}
                        vulnerabilities with precision
                    </div>
                    <div>
                        Scan fully open source{' '}
                        <span className="inline bg-gradient-to-r from-[#61DAFB] via-[#1fc0f1] to-[#03a3d7] text-transparent bg-clip-text">
                            Dependencies
                        </span>
                    </div>
                </main>

                <p className="text-xl text-muted-foreground md:w-10/12 mx-auto lg:mx-0">
                    A professional tool for detecting vulnerabilities in open
                    source dependencies across popular ecosystems
                </p>

                <div className="space-y-4 md:space-y-0 md:space-x-4">
                    <Link to="/namespace" target="_blank">
                        <Button className="w-full md:w-1/3">Get Started</Button>
                    </Link>

                    <Link
                        to="https://github.com/HoangPhuc0304/sca-toolkit.git"
                        target="_blank"
                        className={`w-full md:w-1/3 ${buttonVariants({
                            variant: 'outline',
                        })}`}
                    >
                        Github Repository
                        <GitHubLogoIcon className="ml-2 w-5 h-5" />
                    </Link>
                </div>
            </div>

            {/* Hero cards sections */}
            <div style={{ zIndex: '1' }}>
                <EcosystemCards />
            </div>

            {/* Shadow effect */}
            <div className="shadow"></div>
        </section>
    )
}

export default Info
