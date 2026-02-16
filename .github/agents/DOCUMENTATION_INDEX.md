# 📚 Documentation Index - BaseKMP

**Complete documentation hub for the BaseKMP Kotlin Multiplatform project**

---

## 🎯 Quick Start (Choose Your Path)

### ⚡ I need to start RIGHT NOW (30 seconds)
**→ [QUICK_START.md](QUICK_START.md)** ⭐ **30-SECOND OVERVIEW**

### 👤 I'm a new AI agent
1. Start: [Quick Start Card](QUICK_START.md) ⚡ **30 seconds**
2. Then: [AI Agent Onboarding](AI_AGENT_ONBOARDING.md) ⭐ **10 minutes**
3. Quick lookup: [Quick Reference](AI_QUICK_REFERENCE.md)
4. Understand design: [Architecture Overview](ARCHITECTURE.md)
5. Make decisions: [Decision Tree](DECISION_TREE.md)

### 💻 I'm a developer
1. Quick overview: [Quick Start Card](QUICK_START.md)
2. Project overview: [README](README.md)
3. Architecture: [Architecture Overview](ARCHITECTURE.md)
4. Guidelines: [AI Agent Guidelines](AI_AGENT_GUIDELINES.md)
5. Quick reference: [Quick Reference](AI_QUICK_REFERENCE.md)

### 🔧 I need to implement something
1. Quick templates: [Quick Start Card](QUICK_START.md)
2. Decision guide: [Decision Tree](DECISION_TREE.md)
3. Code templates: [Quick Reference](AI_QUICK_REFERENCE.md)
4. Patterns: [AI Agent Guidelines](AI_AGENT_GUIDELINES.md)

---

## 📖 Documentation Files

| Document | Size | Purpose | Audience |
|----------|------|---------|----------|
| [QUICK_START.md](QUICK_START.md) | Tiny | 30-second overview + templates | Everyone (First 30 seconds) |
| [AI_AGENT_ONBOARDING.md](AI_AGENT_ONBOARDING.md) | Short | Quick start for AI agents | AI Agents (First-time) |
| [AI_QUICK_REFERENCE.md](AI_QUICK_REFERENCE.md) | 1-page | Cheat sheet, templates | Everyone (Daily use) |
| [ARCHITECTURE.md](ARCHITECTURE.md) | Medium | Visual architecture, diagrams | Architecture understanding |
| [AI_AGENT_GUIDELINES.md](AI_AGENT_GUIDELINES.md) | Long | Complete dev guide | Deep dive, reference |
| [DECISION_TREE.md](DECISION_TREE.md) | Medium | Decision flowcharts | Making architectural choices |
| [README.md](README.md) | Short | Project overview | General information |
| **This file** | Short | Documentation hub | Navigation |

---

## 🗂️ Documentation by Topic

### 🏗️ Architecture & Design

| Topic | Primary Source | Quick Reference |
|-------|----------------|-----------------|
| Overall Architecture | [ARCHITECTURE.md](ARCHITECTURE.md) | [Quick Ref - Architecture](AI_QUICK_REFERENCE.md#-architecture-pattern) |
| Clean Architecture Layers | [ARCHITECTURE.md](ARCHITECTURE.md#-clean-architecture-layers) | [Guidelines - Architecture](AI_AGENT_GUIDELINES.md#-architecture--design-patterns) |
| MVI Pattern | [ARCHITECTURE.md](ARCHITECTURE.md#-mvi-pattern-flow) | [Quick Ref - Creating Feature](AI_QUICK_REFERENCE.md#-creating-a-new-feature) |
| Module Organization | [ARCHITECTURE.md](ARCHITECTURE.md#-module-dependency-graph) | [Onboarding - Module Org](AI_AGENT_ONBOARDING.md#1-module-organization) |
| Navigation Pattern | [ARCHITECTURE.md](ARCHITECTURE.md#-navigation-architecture) | [Decision - Navigation](DECISION_TREE.md#-how-should-i-handle-navigation) |

### 🛠️ Implementation Guides

| Topic | Primary Source | Quick Reference |
|-------|----------------|-----------------|
| Creating New Feature | [Quick Reference](AI_QUICK_REFERENCE.md#-creating-a-new-feature) | [Onboarding - Add Feature](AI_AGENT_ONBOARDING.md#add-a-new-feature-module) |
| Platform-Specific Code | [Guidelines - Platform](AI_AGENT_GUIDELINES.md#-platform-specific-guidelines) | [Decision - Platform](DECISION_TREE.md#-is-it-platform-specific) |
| State Management | [Guidelines - Patterns](AI_AGENT_GUIDELINES.md#1-state-management) | [Decision - State](DECISION_TREE.md#-how-should-i-manage-state) |
| Navigation | [Guidelines - Navigation](AI_AGENT_GUIDELINES.md#4-navigation) | [Decision - Navigation](DECISION_TREE.md#-how-should-i-handle-navigation) |
| Error Handling | [Guidelines - Error Handling](AI_AGENT_GUIDELINES.md#2-error-handling) | [Decision - Async](DECISION_TREE.md#-how-should-i-handle-async-operations) |
| Testing | [Guidelines - Testing](AI_AGENT_GUIDELINES.md#-testing-strategy) | [Decision - Tests](DECISION_TREE.md#-do-i-need-to-write-tests) |

### 📦 Dependencies & Configuration

| Topic | Primary Source | Quick Reference |
|-------|----------------|-----------------|
| Adding Dependencies | [Quick Reference - Dependencies](AI_QUICK_REFERENCE.md#-adding-new-dependency) | [Decision - Dependencies](DECISION_TREE.md#-should-i-add-a-dependency) |
| Version Catalog | [Guidelines - Dependency Management](AI_AGENT_GUIDELINES.md#-dependency-management) | [Quick Ref - Dependencies](AI_QUICK_REFERENCE.md#-common-dependencies-by-layer) |
| Build Configuration | [Guidelines - Build](AI_AGENT_GUIDELINES.md#-build--configuration) | [Onboarding - Build Config](AI_AGENT_ONBOARDING.md#-build-configuration) |
| Gradle Plugins | [Guidelines - Convention Plugins](AI_AGENT_GUIDELINES.md#gradle-convention-plugins) | [Quick Ref - Build](AI_QUICK_REFERENCE.md#-build-configuration) |

### 📝 Code Style & Conventions

| Topic | Primary Source | Quick Reference |
|-------|----------------|-----------------|
| Kotlin Style Guide | [Guidelines - Code Style](AI_AGENT_GUIDELINES.md#-code-style--conventions) | [Onboarding - Code Style](AI_AGENT_ONBOARDING.md#-code-style-quick-rules) |
| Naming Conventions | [Guidelines - Naming](AI_AGENT_GUIDELINES.md#1-naming-conventions) | [Onboarding - Style](AI_AGENT_ONBOARDING.md#-code-style-quick-rules) |
| Do's and Don'ts | [Guidelines - Do's and Don'ts](AI_AGENT_GUIDELINES.md#-dos-and-donts) | [Onboarding - Critical Rules](AI_AGENT_ONBOARDING.md#-critical-rules-never-break-these) |
| Code Quality Tools | [Guidelines - Linting](AI_AGENT_GUIDELINES.md#linting--static-analysis) | [Quick Ref - Commands](AI_QUICK_REFERENCE.md#-common-commands) |

### 🧩 Common Patterns

| Topic | Primary Source | Quick Reference |
|-------|----------------|-----------------|
| ViewModel Pattern | [Quick Reference - ViewModel](AI_QUICK_REFERENCE.md#3-viewmodel-template) | [Architecture - MVI](ARCHITECTURE.md#-mvi-pattern-flow) |
| Screen Pattern | [Quick Reference - Screen](AI_QUICK_REFERENCE.md#5-screen-template) | [Onboarding - UI Patterns](AI_AGENT_ONBOARDING.md#-ui-patterns) |
| Repository Pattern | [Guidelines - Repository](AI_AGENT_GUIDELINES.md#repository-pattern) | [Decision - Data Ops](DECISION_TREE.md#-data-operations) |
| expect/actual Pattern | [Guidelines - Platform](AI_AGENT_GUIDELINES.md#expectactual-pattern) | [Quick Ref - Platform Code](AI_QUICK_REFERENCE.md#-platform-specific-code) |
| DI with Koin | [Guidelines - DI](AI_AGENT_GUIDELINES.md#dependency-injection-with-koin) | [Architecture - DI](ARCHITECTURE.md#-dependency-injection-structure) |

### 🎯 Decision Making

| Question | Guide |
|----------|-------|
| Where should my code go? | [Decision Tree - Location](DECISION_TREE.md#-where-should-my-code-go) |
| Which module owns this? | [Decision Tree - Module](DECISION_TREE.md#-which-module-should-own-this) |
| Is it platform-specific? | [Decision Tree - Platform](DECISION_TREE.md#-is-it-platform-specific) |
| How to manage state? | [Decision Tree - State](DECISION_TREE.md#-how-should-i-manage-state) |
| How to handle async? | [Decision Tree - Async](DECISION_TREE.md#-how-should-i-handle-async-operations) |
| Do I need tests? | [Decision Tree - Testing](DECISION_TREE.md#-do-i-need-to-write-tests) |
| Should I add dependency? | [Decision Tree - Dependencies](DECISION_TREE.md#-should-i-add-a-dependency) |
| How to navigate? | [Decision Tree - Navigation](DECISION_TREE.md#-how-should-i-handle-navigation) |

---

## 🔍 Find Information By...

### By Task

| Task | Start Here |
|------|------------|
| Creating new feature | [Quick Reference - Creating Feature](AI_QUICK_REFERENCE.md#-creating-a-new-feature) |
| Adding dependency | [Quick Reference - Adding Dependency](AI_QUICK_REFERENCE.md#-adding-new-dependency) |
| Implementing navigation | [Decision Tree - Navigation](DECISION_TREE.md#-how-should-i-handle-navigation) |
| Writing tests | [Guidelines - Testing](AI_AGENT_GUIDELINES.md#-testing-strategy) |
| Platform-specific code | [Quick Reference - Platform Code](AI_QUICK_REFERENCE.md#-platform-specific-code) |
| State management | [Decision Tree - State](DECISION_TREE.md#-how-should-i-manage-state) |
| Error handling | [Guidelines - Error Handling](AI_AGENT_GUIDELINES.md#2-error-handling) |

### By Component

| Component | Documentation |
|-----------|---------------|
| ViewModel | [Quick Ref - ViewModel](AI_QUICK_REFERENCE.md#3-viewmodel-template), [Architecture - MVI](ARCHITECTURE.md#-mvi-pattern-flow) |
| Screen/Composable | [Quick Ref - Screen](AI_QUICK_REFERENCE.md#5-screen-template), [Onboarding - UI](AI_AGENT_ONBOARDING.md#-ui-patterns) |
| Repository | [Guidelines - Repository](AI_AGENT_GUIDELINES.md#repository-pattern) |
| UseCase | [Guidelines - UseCase](AI_AGENT_GUIDELINES.md#core-architecture) |
| Navigation | [Architecture - Navigation](ARCHITECTURE.md#-navigation-architecture) |
| Network | [Guidelines - Ktor](AI_AGENT_GUIDELINES.md#6-ktor-client-setup) |
| DI/Koin | [Architecture - DI](ARCHITECTURE.md#-dependency-injection-structure) |

### By Question

| Question | Answer |
|----------|--------|
| How is project structured? | [README - Structure](README.md#-project-structure) |
| What tech stack? | [README - Tech Stack](README.md#-technology-stack) |
| How to run project? | [README - Getting Started](README.md#-getting-started) |
| What are key patterns? | [Guidelines - Patterns](AI_AGENT_GUIDELINES.md#-architecture--design-patterns) |
| What are rules? | [Onboarding - Critical Rules](AI_AGENT_ONBOARDING.md#-critical-rules-never-break-these) |
| How to debug? | [Onboarding - Debugging](AI_AGENT_ONBOARDING.md#-debugging-checklist) |

---

## 📊 Documentation Statistics

| Metric | Count |
|--------|-------|
| Total Documents | 7 |
| Total Sections | 150+ |
| Code Examples | 100+ |
| Decision Trees | 10+ |
| Diagrams | 15+ |
| Quick References | 30+ |

---

## 🎓 Recommended Reading Order

### For First-Time AI Agents
1. [QUICK_START.md](QUICK_START.md) - **30 seconds** (instant overview)
2. [AI_AGENT_ONBOARDING.md](AI_AGENT_ONBOARDING.md) - 10 min read
3. [AI_QUICK_REFERENCE.md](AI_QUICK_REFERENCE.md) - 5 min scan
4. [ARCHITECTURE.md](ARCHITECTURE.md) - 15 min read
5. Browse: [DECISION_TREE.md](DECISION_TREE.md) - as needed
6. Reference: [AI_AGENT_GUIDELINES.md](AI_AGENT_GUIDELINES.md) - when needed

### For Implementing Features
1. [QUICK_START.md](QUICK_START.md) - Copy templates
2. [DECISION_TREE.md](DECISION_TREE.md) - Find your scenario
3. [AI_QUICK_REFERENCE.md](AI_QUICK_REFERENCE.md) - Detailed templates
4. [AI_AGENT_GUIDELINES.md](AI_AGENT_GUIDELINES.md) - Deep dive details

### For Understanding Architecture
1. [QUICK_START.md](QUICK_START.md) - Quick overview
2. [ARCHITECTURE.md](ARCHITECTURE.md) - Visual overview
3. [AI_AGENT_GUIDELINES.md](AI_AGENT_GUIDELINES.md) - Detailed patterns
4. [README.md](README.md) - Project context

---

## 🔗 Cross-References

### Frequently Used Combinations

**Creating a Feature:**
- [Quick Ref - New Feature](AI_QUICK_REFERENCE.md#-creating-a-new-feature) → [Decision - Location](DECISION_TREE.md#-where-should-my-code-go) → [Guidelines - Module](AI_AGENT_GUIDELINES.md#creating-new-feature-modules)

**Platform-Specific Code:**
- [Decision - Platform](DECISION_TREE.md#-is-it-platform-specific) → [Quick Ref - expect/actual](AI_QUICK_REFERENCE.md#-platform-specific-code) → [Guidelines - Platform](AI_AGENT_GUIDELINES.md#-platform-specific-guidelines)

**Navigation:**
- [Decision - Navigation](DECISION_TREE.md#-how-should-i-handle-navigation) → [Quick Ref - Navigation](AI_QUICK_REFERENCE.md#7-add-navigation) → [Architecture - Navigation](ARCHITECTURE.md#-navigation-architecture)

**State Management:**
- [Decision - State](DECISION_TREE.md#-how-should-i-manage-state) → [Onboarding - UI Patterns](AI_AGENT_ONBOARDING.md#-ui-patterns) → [Guidelines - State](AI_AGENT_GUIDELINES.md#1-state-management)

---

## 📝 Document Maintenance

### Last Updated
- All documents: February 16, 2026
- Major version: 1.0
- Project version: 1.0.3

### Update Checklist
When making project changes, update:
- [ ] [README.md](README.md) - If tech stack or structure changes
- [ ] [ARCHITECTURE.md](ARCHITECTURE.md) - If architecture changes
- [ ] [AI_AGENT_GUIDELINES.md](AI_AGENT_GUIDELINES.md) - If patterns/conventions change
- [ ] [AI_QUICK_REFERENCE.md](AI_QUICK_REFERENCE.md) - If templates change
- [ ] [DECISION_TREE.md](DECISION_TREE.md) - If decision paths change
- [ ] [AI_AGENT_ONBOARDING.md](AI_AGENT_ONBOARDING.md) - If onboarding flow changes
- [ ] This file - If documentation structure changes

---

## 💡 Tips for Using Documentation

### 🎯 Efficient Navigation
1. **Bookmark frequently used sections** in your IDE/browser
2. **Use Ctrl/Cmd+F** to search within documents
3. **Follow cross-references** for related information
4. **Check Decision Tree first** when unsure

### 📚 Learning Strategy
- **Day 1**: Read onboarding + quick reference
- **Day 2-3**: Study architecture + examples
- **Day 4+**: Reference guidelines as needed
- **Always**: Use decision tree for choices

### 🔄 Feedback Loop
1. Try to implement using docs
2. Note what was unclear
3. Check if answer exists elsewhere
4. Improve documentation if needed

---

## 🆘 Still Can't Find What You Need?

### Checklist
- [ ] Searched this index
- [ ] Used Ctrl/Cmd+F in relevant document
- [ ] Checked decision tree
- [ ] Looked at existing code examples
- [ ] Reviewed similar features in codebase

### Next Steps
1. Search codebase: `grep -r "pattern" sharedFrontend/`
2. Check existing similar module
3. Review commit history for similar changes
4. Look at test files for usage examples

---

## 🎉 Quick Wins

**Save time with these shortcuts:**

| Need | Document | Section |
|------|----------|---------|
| Copy/paste templates | [Quick Reference](AI_QUICK_REFERENCE.md) | Templates section |
| Understand flow | [Architecture](ARCHITECTURE.md) | Diagrams |
| Make decision | [Decision Tree](DECISION_TREE.md) | Relevant tree |
| Find rule | [Onboarding](AI_AGENT_ONBOARDING.md) | Critical Rules |
| See example | [Guidelines](AI_AGENT_GUIDELINES.md) | Common Patterns |

---

**🚀 Ready to build? Start with the [AI Agent Onboarding](AI_AGENT_ONBOARDING.md)!**

---

*This documentation is maintained as part of the BaseKMP project. All documents are versioned together with the codebase.*
