// import { GiftIcon, MapIcon, MedalIcon, PlaneIcon } from './Icons';
// import { Card, CardContent, CardHeader, CardTitle } from '../../ui/card';

import { Link } from 'react-router-dom'
import { Button } from '../../ui/button'
import { CopyIcon } from '@radix-ui/react-icons'

const Download: React.FC = () => {
    // const data = fs.readFileSync('../../data/analyze-response.json', { encoding: 'utf-8', flag: 'r' });
    // const analysisResultList: AnalysisResult = JSON.parse(data);

    return (
        <section id="download" className="container text-center py-24 sm:py-32">
            <h2 className="text-3xl md:text-4xl font-bold ">Use at local</h2>
            <p className="md:w-3/4 mx-auto mt-4 mb-8 text-xl text-muted-foreground">
                Install AnaDep locally using Docker Compose. You can either
                download the necessary files from{' '}
                <Link to="#" target="_blank" className="underline">
                    the provided link
                </Link>{' '}
                or paste the script below into your{' '}
                <span className="font-bold">docker-compose.yml</span> file.
            </p>

            <div className="grid grid-cols-1 gap-8">
                <div className="grid gap-4">
                    <div className="rounded-md bg-black p-6 relative">
                        <pre>
                            <code className="grid gap-1 text-sm text-muted-foreground [&_span]:h-4 text-left">
                                <span>
                                    <span className="text-sky-300">import</span>{' '}
                                    os
                                </span>
                                <span>
                                    <span className="text-sky-300">import</span>{' '}
                                    openai
                                </span>
                                <span />
                                <span>
                                    openai.api_key = os.getenv(
                                    <span className="text-green-300">
                                        &quot;OPENAI_API_KEY&quot;
                                    </span>
                                    )
                                </span>
                                <span />
                                <span>
                                    response = openai.Completion.create(
                                </span>
                                <span>
                                    {' '}
                                    model=
                                    <span className="text-green-300">
                                        &quot;davinci&quot;
                                    </span>
                                    ,
                                </span>
                                <span>
                                    {' '}
                                    prompt=
                                    <span className="text-amber-300">
                                        &quot;&quot;
                                    </span>
                                    ,
                                </span>
                                <span>
                                    {' '}
                                    temperature=
                                    <span className="text-amber-300">0.9</span>,
                                </span>
                                <span>
                                    {' '}
                                    max_tokens=
                                    <span className="text-amber-300">5</span>,
                                </span>
                                <span>
                                    {' '}
                                    top_p=
                                    <span className="text-amber-300">1</span>,
                                </span>
                                <span>
                                    {' '}
                                    frequency_penalty=
                                    <span className="text-amber-300">0</span>,
                                </span>
                                <span>
                                    {' '}
                                    presence_penalty=
                                    <span className="text-green-300">0</span>,
                                </span>
                                <span>)</span>
                            </code>
                        </pre>
                        <Button
                            type="submit"
                            size="sm"
                            className="px-3 absolute top-2 right-2"
                        >
                            <span className="sr-only">Copy</span>
                            <CopyIcon className="h-4 w-4" />
                        </Button>
                    </div>
                </div>
            </div>

            <br />
            <p className="md:w-3/4 mx-auto mt-4 mb-8 text-xl text-muted-foreground">
                Run this command to start the container
            </p>

            <div className="grid grid-cols-1 gap-8">
                <div className="grid gap-4">
                    <div className="rounded-md bg-black p-6 relative">
                        <pre>
                            <code className="grid gap-1 text-sm text-muted-foreground [&_span]:h-4 text-left">
                                <span>
                                    <span className="text-sky-300">import</span>{' '}
                                    os
                                </span>
                                <span>
                                    <span className="text-sky-300">import</span>{' '}
                                    openai
                                </span>
                                <span />
                                <span>
                                    openai.api_key = os.getenv(
                                    <span className="text-green-300">
                                        &quot;OPENAI_API_KEY&quot;
                                    </span>
                                    )
                                </span>
                                <span />
                                <span>
                                    response = openai.Completion.create(
                                </span>
                                <span>
                                    {' '}
                                    model=
                                    <span className="text-green-300">
                                        &quot;davinci&quot;
                                    </span>
                                    ,
                                </span>
                                <span>
                                    {' '}
                                    prompt=
                                    <span className="text-amber-300">
                                        &quot;&quot;
                                    </span>
                                    ,
                                </span>
                                <span>
                                    {' '}
                                    temperature=
                                    <span className="text-amber-300">0.9</span>,
                                </span>
                                <span>
                                    {' '}
                                    max_tokens=
                                    <span className="text-amber-300">5</span>,
                                </span>
                                <span>
                                    {' '}
                                    top_p=
                                    <span className="text-amber-300">1</span>,
                                </span>
                                <span>
                                    {' '}
                                    frequency_penalty=
                                    <span className="text-amber-300">0</span>,
                                </span>
                                <span>
                                    {' '}
                                    presence_penalty=
                                    <span className="text-green-300">0</span>,
                                </span>
                                <span>)</span>
                            </code>
                        </pre>
                        <Button
                            type="submit"
                            size="sm"
                            className="px-3 absolute top-2 right-2"
                        >
                            <span className="sr-only">Copy</span>
                            <CopyIcon className="h-4 w-4" />
                        </Button>
                    </div>
                </div>
            </div>

            <p className="md:w-3/4 mx-auto mt-4 mb-8 text-xl text-muted-foreground">
                By using application at local, database organized by OSV will be
                integrated into the project instead of building your own
                database. To do this, OSV provides APIs for vulnerability
                retrieval. Further details are available{' '}
                <Link to="#" target="_blank" className="underline">
                    here
                </Link>
                .
            </p>
        </section>
    )
}

export default Download
