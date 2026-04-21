const AVATAR_COLORS = [
  '#3b82f6', '#ef4444', '#10b981', '#f59e0b', '#8b5cf6',
  '#ec4899', '#06b6d4', '#f97316', '#6366f1', '#14b8a6',
]

function hashCode(str: string): number {
  let hash = 0
  for (let i = 0; i < str.length; i++) {
    hash = ((hash << 5) - hash + str.charCodeAt(i)) | 0
  }
  return Math.abs(hash)
}

interface AgentAvatarProps {
  name: string
  size?: number
}

export function AgentAvatar({ name, size = 36 }: AgentAvatarProps) {
  const color = AVATAR_COLORS[hashCode(name) % AVATAR_COLORS.length]
  const initials = name
    .split(/[\s_-]+/)
    .map(w => w[0])
    .slice(0, 2)
    .join('')
    .toUpperCase()

  return (
    <div
      className="rounded-full flex items-center justify-center font-semibold shrink-0"
      style={{
        width: size,
        height: size,
        backgroundColor: color + '20',
        color: color,
        fontSize: size * 0.36,
      }}
    >
      {initials}
    </div>
  )
}
