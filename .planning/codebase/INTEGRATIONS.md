# External Integrations

**Analysis Date:** 2025-03-01

## APIs & External Services

**Eclipse Platform APIs:**
- Core Platform - Resource management (`org.eclipse.core.resources`), runtime (`org.eclipse.core.runtime`), and filesystem (`org.eclipse.core.filesystem`) for handling `.properties` files.
- UI Framework - Standard JFace/SWT components, workbench editors (`org.eclipse.ui.editors`), and view systems (`org.eclipse.ui.views`).
- Text Editor Framework - Partitioning, syntax coloring, content assist, and code folding (`org.eclipse.jface.text`, `org.eclipse.ui.workbench.texteditor`).
- JDT (Java Development Tools) - Deep integration for hover assistance, Ctrl+Click navigation, and property key completion within Java source code (`org.eclipse.jdt.core`, `org.eclipse.jdt.ui`).
- Search API - Find usages support for property keys across the workspace (`org.eclipse.search`).

## Data Storage

**Databases:**
- None detected. The application operates directly on workspace files.

**File Storage:**
- Local filesystem - Edits and manages `.properties` files on disk. Integration via `org.eclipse.core.resources` within the IDE or direct Java NIO/File APIs in standalone mode.

**Caching:**
- AppCDS (Application Class Data Sharing) - Used for classes loading acceleration in the standalone application (`build-leyden.sh`).

## Authentication & Identity

**Auth Provider:**
- Custom / Platform - No external auth services integrated. Relies on local OS permissions and Eclipse IDE configuration.

## Monitoring & Observability

**Error Tracking:**
- Eclipse Error Log - Errors and exceptions are typically logged to the workspace `.log` file through the platform's logging mechanism (`org.eclipse.core.runtime`).

**Logs:**
- Console - Standard output for standalone application.
- Workspace Log - Standard Eclipse plugin logging.

## CI/CD & Deployment

**Hosting:**
- GitHub Pages - Used for hosting the P2 update site (`.github/workflows/build.yml`).
- GitHub Releases - Stores zipped P2 repositories and standalone binaries.

**CI Pipeline:**
- GitHub Actions - Automated build and deployment process (`.github/workflows/build.yml`) on Java 25.

## Environment Configuration

**Required env vars:**
- None for runtime. Standalone application might require `JAVA_HOME` pointed to JDK 25 if not in the PATH.

**Secrets location:**
- GitHub Actions Secrets - Used for repository maintenance (implied by permissions in `.github/workflows/build.yml`).

## Webhooks & Callbacks

**Incoming:**
- None detected.

**Outgoing:**
- None detected.

---

*Integration audit: 2025-03-01*
