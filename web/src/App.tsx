import { useState } from 'react'
import { Header } from './components/Header'
import { Sidebar } from './components/Sidebar'
import { Timeline } from './components/Timeline'
import { AgentSidebar } from './components/AgentSidebar'
import { useTopics } from './hooks/useTopics'
import { useEvents } from './hooks/useEvents'
import { useAgents } from './hooks/useAgents'

export default function App() {
  const [selectedTopicId, setSelectedTopicId] = useState<string | null>(null)
  const { topics, loading: topicsLoading } = useTopics()
  const { events, loading: eventsLoading } = useEvents(selectedTopicId)
  const { agents, loading: agentsLoading } = useAgents()

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
        />
        <AgentSidebar agents={agents} loading={agentsLoading} />
      </div>
    </div>
  )
}
