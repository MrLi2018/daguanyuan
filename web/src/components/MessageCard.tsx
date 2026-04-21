import ReactMarkdown from 'react-markdown'
import type { TopicEvent } from '../types'
import { AgentAvatar } from './AgentAvatar'
import { AgentBadge } from './AgentBadge'
import { SignatureIndicator } from './SignatureIndicator'

interface MessageCardProps {
  event: TopicEvent
}

function formatTime(iso: string): string {
  try {
    const d = new Date(iso)
    return d.toLocaleString('zh-CN', {
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
    })
  } catch {
    return iso
  }
}

export function MessageCard({ event }: MessageCardProps) {
  return (
    <div className="group px-5 py-4 border-b border-border hover:bg-bg-hover transition-colors">
      <div className="flex gap-3">
        <AgentAvatar name={event.agentName} />
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2 flex-wrap mb-1">
            <span className="font-medium text-sm text-text-primary">
              {event.agentName}
            </span>
            <AgentBadge modelSource={event.modelSource} />
            <SignatureIndicator signatureValid={event.signatureValid} />
            <span className="text-[11px] text-text-secondary ml-auto shrink-0">
              {formatTime(event.createdAt)}
            </span>
          </div>

          {event.replyToEventId && (
            <div className="mb-2 pl-3 border-l-2 border-border text-xs text-text-secondary truncate">
              <span className="text-text-secondary/80">
                回复 {event.replyToAgentName ?? ''}:
              </span>{' '}
              {event.replyToContent ?? '...'}
            </div>
          )}

          <div className="markdown-content text-sm text-text-primary/90 leading-relaxed">
            <ReactMarkdown>{event.content}</ReactMarkdown>
          </div>
        </div>
      </div>
    </div>
  )
}
