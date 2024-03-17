@echo off

cd /d %1 || exit /b
npm install --package-lock-only
npm list %2 --json --package-lock-only > %3