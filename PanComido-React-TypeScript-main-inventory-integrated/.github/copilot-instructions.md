<!-- .github/copilot-instructions.md
  Purpose: short, actionable guidance for AI coding agents working on this repo.
  Created: 2025-11-04
-->

# Repo-specific Copilot instructions (concise)

This project is a small Vite + React + TypeScript front-end app. The guidance below focuses on patterns and files that are important for making safe, useful edits.

1. Big picture
   - Architecture: single-page React app built with Vite (entry points: `index.html`, `src/main.tsx`, `src/RaizApp.tsx`). UI is componentized under `src/ComponentesCompartidos` and `src/imagesComponents`. Page-level routes/pages live in `src/paginas` and are simple TSX components.
   - Data: mock data is provided under `src/mock-data` (e.g. `panesMocks.ts`). The app currently reads local mocks rather than calling external APIs.
   - Assets: static images live in `public/images/` and referenced from components under `src/imagesComponents`.

2. Important files and examples
   - `src/main.tsx` — app bootstrap, provider mounting, ReactDOM rendering.
   - `src/RaizApp.tsx` — top-level app component and router wrapper. Prefer modifying routes here for new pages.
   - `src/ComponentesCompartidos/` — shared components (NavBar.tsx, SearchBar.tsx, Footer.tsx, BotonComprarTodo.tsx). Use these when adding UI that should be reused.
   - `src/mock-data/index.ts` & `panesMocks.ts` — example data shapes. Follow these shapes when creating or mocking new data.
   - `src/types/` — TypeScript types (e.g., `ImagePanType.ts`, `NavBarType.ts`) — add or update types here and import where relevant.

3. Project patterns & conventions (observe and follow)
   - Components are TypeScript React (.tsx) files. Prefer explicit prop interfaces and export via `export default` where present.
   - `index.ts` files in component folders re-export items — add exports there when adding new components to keep imports consistent (e.g., `src/ComponentesCompartidos/index.ts`).
   - Mock-first data flow: many UI components import sample data from `src/mock-data`. To add features quickly, extend mocks and types first, then wire them into components.
   - Styling: global styles are in `src/index.css`; components use local classNames (no CSS modules observed).

4. Build / dev / debug (assumptions — please confirm)
   - Likely NPM scripts (typical for Vite projects):
     - `npm install` — install deps
     - `npm run dev` — start Vite dev server
     - `npm run build` — produce production build
     - `npm run preview` — preview production build
   - There is an `eslint.config.js` file; prefer running the project's lint script if present. If a test runner is needed, none were found in root; ask maintainers before adding test infra.
   - When debugging in dev: open browser console and Vite overlay. For component-level debugging, use React devtools and inspect `src/ComponentesCompartidos/*`.

5. Integration points and external deps
   - No external API calls found in the UI (mock-data used). If adding external integrations, centralize fetch logic in a new `src/services/` folder.
   - Images are served from `public/images/`. Use absolute paths like `/images/...` so Vite serves them correctly.

6. Safe-edit checklist for contributors and AI agents
   - Preserve public API of shared components (props shape). If changing a prop name, update all imports and usages across `src/`.
   - Update types in `src/types/` when modifying data shapes; update mocks in `src/mock-data` accordingly.
   - Add re-exports to `index.ts` inside the folder when adding a new component so other modules import consistently.
   - Keep changes small and focused: single component or single page per PR.

7. Examples of concrete edits
   - To add a page: create `src/paginas/newPage.tsx`, export default component, then add route in `src/RaizApp.tsx` and a Nav link in `src/ComponentesCompartidos/NavBar.tsx` and update `src/ComponentesCompartidos/index.ts` if needed.
   - To add a new shared component: create folder under `src/ComponentesCompartidos/MyComp.tsx`, export default, add export to `src/ComponentesCompartidos/index.ts`.

8. When in doubt / missing info
   - I couldn't confirm package.json scripts or CI config from an automated search — please confirm the project's npm scripts and whether a linter/test command exists.
   - If the project should call external APIs, point to the service endpoints and auth approach so I can wire `src/services/` correctly.

If anything above is unclear or you want the doc to include exact npm scripts and CI commands, allow me to run a quick repo search or share `package.json` and CI files and I will update and merge the file.
