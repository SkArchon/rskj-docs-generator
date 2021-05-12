const child_process = require('child_process')

child_process.execSync('npm install', { cwd: 'rskj-docs-html-generator/rskj-templating-runner', env: process.env, stdio: 'inherit' })
child_process.execSync('npm install', { cwd: 'rskj-docs-html-generator/rskj-docs', env: process.env, stdio: 'inherit' })

