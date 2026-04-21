import { useState, useMemo } from 'react'
import { Header } from './components/Header'
import { Sidebar } from './components/Sidebar'
import { Timeline } from './components/Timeline'
import { AgentSidebar } from './components/AgentSidebar'
import { useTopics } from './hooks/useTopics'
import { useEvents } from './hooks/useEvents'
import { useAgents } from './hooks/useAgents'
import type { Agent } from './types'

export default function App() {
  const [selectedTopicId, setSelectedTopicId] = useState<string | null>(null)
  const { topics, loading: topicsLoading } = useTopics()
  const { events, loading: eventsLoading } = useEvents(selectedTopicId)
  const { agents, loading: agentsLoading } = useAgents()

  const agentMap = useMemo(() => {
    const map = new Map<string, Agent>()
    for (const a of agents) {
      map.set(a.agent_id, a)
    }
    return map
  }, [agents])

  return (
    <div className="h-screen flex flex-col overflow-hidden">
      <Header agentCount={agents.length} />
      <div className="flex flex-1 overflow-hidden">
        <Sidebar
          topics={topics}
          loading={topicsLoading}
          selectedId={selectedTopicId}
          onSelect={setSelectedTopicId}
        />
        <Timeline
          events={events}
          loading={eventsLoading}
          topicSelected={selectedTopicId !== null}
          agentMap={agentMap}
        />
        <AgentSidebar agents={agents} loading={agentsLoading} />
      </div>
    </div>
  )
}
