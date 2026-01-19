# <Subsystem> — Context Reset Brief (v2)

> **Use this brief when…** you need to safely modify or extend this subsystem (bugfix, feature, refactor).  
> Keep all content minimal, authoritative, and focused on what an LLM must know before touching code.

## Responsibilities & boundaries
- Summarise what the subsystem owns and guarantees.
- State explicit non-goals to prevent accidental expansion.
- Clarify “this subsystem *never* touches X” rules.

## Primary APIs & types
- List exported functions/classes/interfaces with concise signature highlights.
- Note lifecycle surface: `init`, `update`, `dispose`, and any async patterns.
- Call out shape of relevant data structures.

## Key files & entry points
- Enumerate TypeScript modules in priority order (`src/path/to/file.ts`).
- Describe the specific role of each file in one line.
- Include any dev harnesses under `src/dev/**` that exercise this subsystem.

## Interactions & sequencing
- Describe call order with bullets or a compact diagram.
- Summarise how this subsystem collaborates with others.
- Link to dependent briefs, e.g. `[Scene Routing](scene-routing.md)`.

## Implementation Snapshot
> **Purpose:** Give an LLM the minimum concrete picture of the *actual* code structure it is editing.

- Bullet list of core functions/methods inside each file.
- Note important shared utilities, helpers, or cross-file invariants.
- Include any non-obvious state, caches, singletons, or registries.
- Highlight async boundaries, event listeners, Babylon disposables, etc.

## Patch Surface
> **Purpose:** Tell the LLM *exactly where to make changes* depending on the task.

- When editing behaviour **A**, modify: `src/.../file-A.ts`
- When extending capability **B**, touch: `src/.../file-B.helpers.ts`
- When adding new API surface, update:
  - `*.contracts.ts`
  - the barrel `index.ts`
  - any dependent subsystem briefs
- When adjusting lifecycle logic, review files: `<X>.impl.ts`, `<Y>.disposer.ts`

Keep this extremely explicit — no guessing.

## Control Flow Anchor
> **Purpose:** Provide a minimal, unambiguous execution map for this subsystem.

Example:

```
createSubsystem()
  → internalInit()
  → attachToScene()
  → registerListeners()
  → (each frame) update(delta)
  → onDispose()
```

Or list the order in bullets:

- Input arrives from Provider → transformed into `XState`
- Subsystem processes state → computes Y
- Subsystem calls Z in dependent system
- Disposer unwinds: Z → Y → X

Keep this ≤10 lines. This is the core mental model.

## Update checklist
- When changing behaviour, update:
  - Related briefs
  - Relevant tests
  - Any runtime wiring or contracts
  - Documentation in `/docs/llm/`
- Include verification steps (e.g., “run Comfort Arena local teleport”, “rerun `npm run test:transitions`”).

## Pitfalls & gotchas
- Capture tricky invariants and timing issues.
- Mention any conditions that cause leaks, double-disposal, or re-entrancy.
- Keep short; prefer bullets referencing exact code regions.

## See also
- Cross-links to related briefs, Style Bible rules, deep dives, or archived behaviour history.

## Code anchors
- `docs/llm/README.md` — naming + placement conventions for all briefs.
- `docs/llm/MAINTENANCE.md` — when to create, archive, or refresh briefs after code changes.
