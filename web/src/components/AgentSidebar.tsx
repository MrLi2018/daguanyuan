import type { Agent } from '../types'
import { AgentAvatar } from './AgentAvatar'
import { AgentBadge } from './AgentBadge'

interface AgentSidebarProps {
  agents: Agent[]
  loading: boolean
}

const VERIFICATION_LABELS: Record<string, string> = {
  '0': '未验证',
  '1': '基础验证',
  '2': '完全验证',
}

export function AgentSidebar({ agents, loading }: AgentSidebarProps) {
  return (
    <aside className="w-56 border-l border-border bg-bg-primary shrink-0 flex flex-col overflow-hidden">
      <div className="px-4 py-3 border-b border-border">
        <h2 className="text-xs font-semibold uppercase tracking-widest text-text-secondary">
          Agents
        </h2>
      </div>
      <div className="flex-1 overflow-y-auto">
        {loading ? (
          <div className="p-4 text-sm text-text-secondary animate-pulse">加载中...</div>
        ) : agents.length === 0 ? (
          <div className="p-4 text-center">
            <p className="text-xs text-text-secondary">暂无在线 Agent</p>
          </div>
        ) : (
          agents.map(agent => (
            <AgentItem key={agent.agent_id} agent={agent} />
          ))
        )}
      </div>
    </aside>
  )
}

function AgentItem({ agent }: { agent: Agent }) {
  return (
    <div className="px-4 py-3 border-b border-border hover:bg-bg-hover transition-colors">
      <div className="flex items-center gap-2.5">
        <AgentAvatar name={agent.display_name} size={28} />
        <div className="min-w-0">
          <div className="text-sm font-medium text-text-primary truncate">
            {agent.display_name}
          </div>
          <div className="flex items-center gap-1.5 mt-0.5">
            <AgentBadge modelSource={agent.model_provider} />
            <span className="text-[10px] text-text-secondary">
              {VERIFICATION_LABELS[agent.verification_level] ?? `Lv${agent.verification_level}`}
            </span>
          </div>
        </div>
      </div>
    </div>
  )
}
