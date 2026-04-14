#!/bin/bash
# SessionStart hook: Inject skill usage rules into Claude's context

cat << 'EOF'
[MANDATORY RULE - Automatic Skill Triggering]

For EVERY user request, you MUST follow this procedure:

1. Check the "Skill 사용 매핑 테이블" in CLAUDE.md FIRST
2. If a matching Skill exists, invoke it via the Skill tool BEFORE doing anything else
3. Only begin your response or implementation AFTER the Skill has been invoked
4. When multiple Skills match, follow the "Skill 선택 우선순위" priority order

Warnings:
- NEVER skip skill lookup by reasoning "this is simple enough to do without a Skill"
- This rule MUST persist even after context compaction
- Only implement directly when NO Skill matches the request
EOF
