# LLM Brief Maintenance Guide (v2)

This document defines **when and how** to create, update, archive, and validate  
Context Reset Briefs (`docs/llm/<subsystem>.md`) for the Grid Runtime.

It complements the template in  
`docs/llm/brief-template.md` (canonical location for the v2 template).

---

# 📌 Source of Truth

For all Context Reset Briefs:

- **The codebase is the sole source of truth.**
- Briefs must reflect *actual* exported APIs, file boundaries, lifecycle behaviour, and control flow.
- If an existing brief conflicts with the code, the brief must be updated or replaced.
- Existing documentation must never override or reinterpret the code.

Briefs are **derived artefacts**, not normative specifications.

---

# ✅ When to Create a New Brief

Create a new brief when:

- A new subsystem is introduced (e.g., new controller, renderer, router extension).
- A previously‑implicit module becomes isolated with its own file group.
- A feature or refactor gives a subsystem a stable, reusable API surface.

**Rule of thumb:**  
If modifying the subsystem requires the author to “load context” before editing,  
it gets a brief.

---

# 🔄 When to Update an Existing Brief

Update a brief immediately when any of the following occur:

### **1. Public API surface changes**
- New exports added
- Signatures changed
- Deprecated functions removed
- Contract moved to a new file

### **2. Lifecycle behaviour changes**
- `init`, `update`, `dispose`, async flow altered
- Event listener lifecycle added or removed
- New Babylon disposables introduced

### **3. Control flow changes**
- Execution order shifts
- New callbacks or cross‑system interactions added
- Timing‑sensitive logic introduced

### **4. Patch surface changes**
- A behaviour moves from one file to another
- Helpers extracted or renamed
- Core logic refactored across files

### **5. Implementation Snapshot goes stale**
Whenever:
- Functions are renamed or removed  
- File structure reorganised  
- State layout changes  
- Helper files gain/lose meaningful logic

### **6. A brief’s “See also” dependencies change**
If related briefs are created, renamed, or archived.

---

# 🔁 Full Brief Regeneration (LLM Maintenance Pass)

In addition to incremental updates, the entire `docs/llm/` folder may be regenerated in a single maintenance pass when:

- Large refactors span multiple subsystems
- File boundaries or responsibilities have shifted widely
- The brief set has drifted out of sync with the codebase
- An automated **Docs Maintenance Task** is run

In a full regeneration pass:

- Existing briefs are treated as *hints*, not authorities
- Each brief must be revalidated against the current code
- Missing briefs must be created
- Obsolete briefs must be archived
- Index documents (`README.md`, `canonical-brief-index.md`) must be rebuilt from the resulting set

---

# 🗄️ When to Archive a Brief

A brief should be archived (moved to `docs/llm/archive/`) when:

- The subsystem is fully removed from the codebase  
- It becomes merged into a different subsystem  
- It becomes purely historical (e.g., old experimental path)  
- The brief describes a subsystem replaced by a rewrite

Always leave a short redirect note in the new location.

---

# 🧪 Verification Workflow (LLM‑Safe)

After updating code or regenerating briefs:

1. **Open the Brief**
   - Ensure Responsibilities, APIs, Patch Surface, and Implementation Snapshot reflect reality.

2. **Confirm Cross‑links**
   - Check “See also” links still exist.
   - Confirm referenced files still exist.

3. **Run subsystem‑specific checks**
   - e.g. transitions: `npm run test:transitions`
   - player controller changes: Comfort Arena smoke‑test

4. **Perform a cold LLM test**
   - Generate a contextual request as if editing the subsystem.
   - Ensure the brief *alone* is sufficient for a model to apply correct edits.

If it fails the cold test → the brief needs more clarity.

---

# ⚠️ Common Maintenance Pitfalls

- **Letting the Implementation Snapshot rot**  
  → Keep it trimmed but correct.

- **Patch Surface too vague**  
  → Must contain exact filenames and responsibilities.

- **Over‑documenting historical context**  
  → Move history into `archive/` files; keep briefs minimal.

- **Forgetting to update briefs when file boundaries shift**  
  → Any split/merge/refactor requires a brief update.

- **Forgetting async/timing notes**  
  → Always note race conditions, disposer ordering, frame‑dependence.

---

# 📁 File Naming & Placement

- All briefs live under: `docs/llm/`
- File naming: `<subsystem>.md` (lowercase, kebab-case)
- Archive files under: `docs/llm/archive/`
- Template lives at:  
  `docs/llm/brief-template.md`

---

# 📘 Index Documents

The following files are **derived indices** and must reflect the current brief set:

- `docs/llm/README.md`
- `docs/llm/canonical-brief-index.md`

They must be updated whenever:

- Briefs are added, removed, renamed, or archived
- Subsystem categorisation changes
- Core architectural entry points move

These documents describe **how to consume** the brief set, not how it was generated.

---

# 🧩 Minimal Checklist for Every Brief

Before marking a brief complete:

- [ ] Responsibilities & boundaries correct  
- [ ] API summary accurate  
- [ ] File list reflects structure  
- [ ] Implementation Snapshot updated  
- [ ] Patch Surface explicit  
- [ ] Control Flow Anchor correct  
- [ ] Update checklist and pitfalls checked  
- [ ] Valid cross-links in “See also”  
- [ ] Consistent with Style Bible conventions  

---

## 🚫 What Briefs Must Never Do

Context Reset Briefs must not:

- Describe how to regenerate themselves
- Contain tooling or workflow instructions
- Encode speculative future architecture
- Replace or reinterpret code behaviour

Their only purpose is to enable safe, accurate editing under **cold context**.

---

# 🏁 Summary

Maintaining briefs is essential for safe LLM‑assisted development in Grid 2.0.

- Keep them **short but correct**  
- Update them **whenever code meaningfully changes**  
- Ensure they always support **cold‑context LLM editing**

Briefs are not historical documents — they are live tools.
