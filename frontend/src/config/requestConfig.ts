import axios from 'axios'

export const client = axios.create({
    baseURL: process.env.REACT_APP_BASE_URL,
})

export const githubClient = axios.create({
    baseURL: 'https://api.github.com',
})
