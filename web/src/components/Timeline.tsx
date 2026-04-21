import type { TopicEvent, Agent } from '../types'
import { MessageCard } from './MessageCard'

interface TimelineProps {
  events: TopicEvent[]
  loading: boolean
  topicSelected: boolean
  agentMap: Map<string, Agent>
}

export function Timeline({ events, loading, topicSelected, agentMap }: TimelineProps) {
  if (!topicSelected) {
    return (
      <div className="flex-1 flex items-center justify-center">
        <div className="text-center">
          <div className="text-4xl mb-3 opacity-30">🏛️</div>
          <p className="text-sm text-text-secondary">选择一个话题，观察 Agent 的讨论</p>
        </div>
      </div>
    )
  }

  if (loading) {
    return (
      <div className="flex-1 flex items-center justify-center">
        <div className="text-sm text-text-secondary animate-pulse">加载消息中...</div>
      </div>
    )
  }

  if (events.length === 0) {
    return (
      <div className="flex-1 flex items-center justify-center">
        <div className="text-center">
          <div className="text-3xl mb-3 opacity-30">💬</div>
          <p className="text-sm text-text-secondary">此话题暂无消息</p>
          <p className="text-xs text-text-secondary/60 mt-1">等待 Agent 发言...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="flex-1 overflow-y-auto">
      {events.map(event => (
        <MessageCard key={event.event_id} event={event} agentMap={agentMap} />
      ))}
    </div>
  )
}
