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

## Production deployment

`render.yaml` describes the Spring Boot API and managed PostgreSQL deployment on Render. Create the Render resources from the blueprint, then set `DATABASE_URL` to a JDBC PostgreSQL URL (`jdbc:postgresql://host:5432/database`), database credentials, a strong `JWT_SECRET`, and `CORS_ALLOWED_ORIGINS` to the exact Vercel origin (for example `https://smart-board.example.vercel.app`). The API readiness endpoint is `/api/health`; it returns `200` only when PostgreSQL is reachable and `503` otherwise.

On Vercel, set `VITE_API_BASE_URL` to the HTTPS Render API URL and deploy the `frontend/` directory. `frontend/vercel.json` rewrites client-side routes to `index.html`, preserving React Router navigation on refresh.

The current upload implementation stores files locally. This is suitable for development only because Render filesystem storage is ephemeral; production file uploads should use S3, Azure Blob, or another durable object store behind the existing file-storage abstraction before enabling user uploads at scale.

Run the schema/migrations against the managed database before starting the API. Keep `spring.jpa.hibernate.ddl-auto=validate` in production so unexpected schema drift fails startup rather than silently changing data.

## Local validation

```text
cd backend && ./mvnw clean verify
cd frontend && npm ci && npm run lint && npm run build
```

## Troubleshooting

- A backend failure means compilation, tests, or database startup failed; inspect the `Backend verify` job log.
- A frontend failure means dependency installation, linting, or Vite build failed; inspect the named step.
- A Vercel failure normally means a missing/invalid secret or project/org ID. Deployment is intentionally skipped for pull requests and never runs after a failed validation job.
