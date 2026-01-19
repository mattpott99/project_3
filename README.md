# Grid 2.0 — LLM Context Reset Guide

> **Goal:** help a context-starved LLM rebuild the minimum working-set it needs before editing Grid 2.0 code.
>
> Always start here, then jump to the subsystem brief that matches your task.

## How to use this folder

1. **Re-establish architecture.** Skim the Architecture Snapshot below to remember how runtime, core systems, and UI layers fit together.
2. **Identify the subsystem you need.** Use the Document Map to open the relevant brief.
3. **Load the right files.** Each brief links directly to the TypeScript entry points and exported APIs you must read before coding.
4. **Check gotchas.** Follow the Update Checklist and Pitfalls sections in each brief to avoid regressions when context is thin.
5. **Confirm expectations.** Review the Prompting & Style Notes so generated code matches project conventions.

## Architecture snapshot

| Layer | What lives here | Key entry points |
| --- | --- | --- |
| **Runtime shell** | Boots Babylon/XR, composes core services, drives the frame loop. | `src/runtime/runtime.impl.ts`, `src/runtime/runtime.main-loop.ts`, `src/runtime/runtime.disposer.ts` |
| **Core systems** | Scene routing, transitions, player controller, XR host, link system, HUD. | `src/core/**` packages + runtime wiring under `src/runtime/core/**` |
| **Connected systems** | Optional multiplayer/identity/presence/shared state scaffold. | `src/runtime/connected/**` |
| **UI overlays** | HUD, diagnostics HUD, error overlay, transition FX. | `src/core/hud-system`, `src/core/transition-fx-controller`, `src/ui/error-overlay` |
| **Scenes** | World-specific logic loaded by the router. | `src/core/scene-system` + scene files under `src/runtime`/`src/scenes` |

## Document map

| Category | Briefs |
| --- | --- |
| **Foundations & Maintenance** | [Style Bible](Grid-2.0-Development-Style-Bible.md), [Runtime](runtime.md), [Leak-Free Runtime Architecture](leak_free_runtime_architecture.md), [Maintenance Guide](MAINTENANCE.md), [Brief Template](brief-template.md) |
| **Testing & Lifecycle** | [LLM Testing & Lifecycle Guide](llm-testing-lifecycle-guide.md) |
| **Contracts Layer** | [Core Contracts](core-contracts.md) |
| **Core Capsules** | [XR Host](xr-host.md), [Scene System](scene-system.md), [Player Controller](player-controller.md), [Controller Registry](controller-registry.md), [Link System](link-system.md), [Logging](brief-logging.md) |
| **Data Layer** | [DataAPI](data-api.md) |
| **Navigation & Experience** | [Transition Controller](transition-controller.md), [Transition FX Controller](transition-fx-controller.md), [HUD System](brief-hud-system.md), [Diagnostics HUD](brief-diagnostics-hud.md), [HUD / Diagnostics / Error Surfaces](hud-stack.md), [Error Overlay](error-overlay.md) |
| **Connected Runtime** | [Network System](network-system.md) |
| **VR Behaviour Specs** | [Controller Behaviours](vr-controller-behaviour-spec.md) |
| **Milestone Prompts** | [Data API (Phase E)](data-api-milestones/Phase-E-Master-Prompt.md) |
| **Archive (superseded docs)** | Historical VR posture/turning plans, HUD upgrade plan, and network milestones live under `archive-these-docs-are-superseded/` for reference only. |

> Need a new brief? Copy the [template](brief-template.md) — this is the canonical v2 brief template. and follow the [maintenance checklist](MAINTENANCE.md).

## Prompting & style notes

- **Language & tooling**: TypeScript, Vite, Babylon.js. Imports use explicit `.js` extensions.
- **File size discipline**: Follow the Style Bible’s target of ~150 lines per module. Split helpers when scope grows.
- **Babylon patterns**: Prefer factory functions that accept dependencies and return typed APIs instead of classes. - Some existing Grid 2.0 capsules predate this rule and still use classes. Their briefs document them accurately, and they remain supported. New subsystem development should follow the factory pattern.
- **Disposal discipline**: Every system must expose `dispose()` and clean up Babylon resources in reverse order of creation.
- **Testing hooks**: Harnesses live under `src/dev/**`; update them when runtime behaviour changes.

## Keeping briefs effective

- Each brief is scoped to helping a future LLM recover context fast. Avoid prose-heavy history; focus on actionable facts.
- Update briefs in the same PR as substantive code changes.
- If a brief feels too long, split out scenario-specific notes into the archive and keep the canonical brief lean.

See [MAINTENANCE.md](MAINTENANCE.md) for ownership and review guidance.

### Code anchors (2025-03)

- Runtime bootstrap: `src/runtime/runtime.impl.ts`, `src/runtime/core/core-shell.ts`, and `src/runtime/runtime.main-loop.ts` show the orchestration order and the contracts each brief refers to.
- Connected runtime scaffold: `src/runtime/connected/connected-systems.ts` wires identity, network transport, presence sync, avatars, and shared state.
- Core contracts index: `src/core/contracts/index.ts` links the shared types used across the briefs and is the fastest way to confirm exported identifiers before editing docs.
