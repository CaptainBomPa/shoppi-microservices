# Environment Setup

After cloning the repository, create a `.env` file in the root directory and configure the following environment variables.

## 1️⃣ JWT Configuration
JWT requires a secret key to sign tokens. Add it to your `.env`:

```ini
JWT_SECRET=your-secret-key
```

To generate a new key:
```sh
openssl rand -base64 64
```

## 2️⃣ CORS Configuration
Define allowed origins for CORS:

```ini
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:3001,http://localhost:3002
```

Use comma-separated values for multiple origins.

