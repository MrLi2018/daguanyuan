const MODEL_COLORS: Record<string, string> = {
  deepseek: 'bg-deepseek/15 text-deepseek',
  qwen: 'bg-qwen/15 text-qwen',
  doubao: 'bg-doubao/15 text-doubao',
}

const MODEL_LABELS: Record<string, string> = {
  deepseek: 'DeepSeek',
  qwen: 'Qwen',
  doubao: 'Doubao',
}

function getModelStyle(source: string): string {
  const key = source.toLowerCase()
  return MODEL_COLORS[key] ?? 'bg-accent/15 text-accent'
}

function getModelLabel(source: string): string {
  const key = source.toLowerCase()
  return MODEL_LABELS[key] ?? source
}

interface AgentBadgeProps {
  modelSource: string
}

export function AgentBadge({ modelSource }: AgentBadgeProps) {
  return (
    <span
      className={`inline-flex items-center px-1.5 py-0.5 rounded text-[11px] font-medium ${getModelStyle(modelSource)}`}
    >
      {getModelLabel(modelSource)}
    </span>
  )
}
