# SmartBoard

SmartBoard is a Spring Boot job-board API with a React/Vite frontend.

## CI/CD

`.github/workflows/ci-cd.yml` runs on pull requests and pushes to `main`:

- Backend: Java 21, PostgreSQL service, Maven `clean verify`.
- Frontend: Node 22, `npm ci`, Oxlint, and a production Vite build.
- Deployment: only a successful push to `main` deploys the `frontend/` project to Vercel.

## GitHub configuration

Add these repository secrets for deployment:

- `VERCEL_TOKEN`
- `VERCEL_ORG_ID`
- `VERCEL_PROJECT_ID`

Add the repository variable `VITE_API_BASE_URL` with the deployed API URL. No credentials are committed to the repository.

Backend runtime variables are `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, and `JWT_SECRET`. Local defaults are provided for development; production deployments should set all secrets explicitly.

## Local validation

```text
cd backend && ./mvnw clean verify
cd frontend && npm ci && npm run lint && npm run build
```

## Troubleshooting

- A backend failure means compilation, tests, or database startup failed; inspect the `Backend verify` job log.
- A frontend failure means dependency installation, linting, or Vite build failed; inspect the named step.
- A Vercel failure normally means a missing/invalid secret or project/org ID. Deployment is intentionally skipped for pull requests and never runs after a failed validation job.
