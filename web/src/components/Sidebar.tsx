import type { Topic } from '../types'

interface SidebarProps {
  topics: Topic[]
  loading: boolean
  selectedId: string | null
  onSelect: (id: string) => void
}

export function Sidebar({ topics, loading, selectedId, onSelect }: SidebarProps) {
  return (
    <aside className="w-72 border-r border-border bg-bg-primary shrink-0 flex flex-col overflow-hidden">
      <div className="px-4 py-3 border-b border-border">
        <h2 className="text-xs font-semibold uppercase tracking-widest text-text-secondary">
          Topics
        </h2>
      </div>
      <div className="flex-1 overflow-y-auto">
        {loading ? (
          <div className="p-4 text-sm text-text-secondary animate-pulse">
            加载话题中...
          </div>
        ) : topics.length === 0 ? (
          <EmptyState />
        ) : (
          topics.map(topic => (
            <TopicItem
              key={topic.id}
              topic={topic}
              selected={topic.id === selectedId}
              onClick={() => onSelect(topic.id)}
            />
          ))
        )}
      </div>
    </aside>
  )
}

function EmptyState() {
  return (
    <div className="p-4 text-center">
      <p className="text-sm text-text-secondary">暂无话题</p>
      <p className="text-xs text-text-secondary/60 mt-1">等待 Agent 创建话题...</p>
    </div>
  )
}

interface TopicItemProps {
  topic: Topic
  selected: boolean
  onClick: () => void
}

function TopicItem({ topic, selected, onClick }: TopicItemProps) {
  return (
    <button
      onClick={onClick}
      className={`w-full text-left px-4 py-3 border-b border-border transition-colors ${
        selected
          ? 'bg-accent/10 border-l-2 border-l-accent'
          : 'hover:bg-bg-hover border-l-2 border-l-transparent'
      }`}
    >
      <h3 className="text-sm font-medium text-text-primary truncate">{topic.title}</h3>
      {topic.description && (
        <p className="text-xs text-text-secondary mt-1 line-clamp-2">
          {topic.description}
        </p>
      )}
      <div className="flex items-center gap-2 mt-2">
        {topic.tags?.slice(0, 3).map(tag => (
          <span
            key={tag}
            className="text-[10px] px-1.5 py-0.5 rounded bg-bg-card text-text-secondary border border-border"
          >
            {tag}
          </span>
        ))}
        <span className="text-[10px] text-text-secondary ml-auto">
          {topic.postCount ?? 0} posts
        </span>
      </div>
    </button>
  )
}
