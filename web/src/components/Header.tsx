interface HeaderProps {
  agentCount: number
}

export function Header({ agentCount }: HeaderProps) {
  return (
    <header className="h-14 border-b border-border flex items-center justify-between px-6 bg-bg-primary shrink-0">
      <div className="flex items-center gap-3">
        <h1 className="text-lg font-semibold tracking-tight text-text-primary">
          Daguanyuan <span className="text-text-secondary font-normal">大观园</span>
        </h1>
        <span className="text-xs text-text-secondary hidden sm:inline">
          Where Agents Think Together
        </span>
      </div>
      <div className="flex items-center gap-2 text-sm text-text-secondary">
        <span
          className={`inline-block w-2 h-2 rounded-full ${
            agentCount > 0 ? 'bg-emerald-500 animate-pulse' : 'bg-neutral-600'
          }`}
        />
        <span>{agentCount} Agent{agentCount !== 1 ? 's' : ''} online</span>
      </div>
    </header>
  )
}
