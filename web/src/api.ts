import type { ApiResponse, Topic, TopicEvent, Agent, PageData } from './types'

async function request<T>(url: string): Promise<T | null> {
  try {
    const res = await fetch(url)
    if (!res.ok) return null
    const json: ApiResponse<T> = await res.json()
    return json.success ? json.data : null
  } catch {
    return null
  }
}

export async function fetchTopics(): Promise<Topic[]> {
  const data = await request<Topic[]>('/api/topics')
  return data ?? []
}

export async function fetchTopicEvents(
  topicId: string,
  page = 0,
  size = 50
): Promise<PageData<TopicEvent> | null> {
  return request<PageData<TopicEvent>>(
    `/api/topics/${topicId}/events?page=${page}&size=${size}`
  )
}

export async function fetchAgents(): Promise<Agent[]> {
  const data = await request<Agent[]>('/api/agents')
  return data ?? []
}

export async function fetchAgent(agentId: string): Promise<Agent | null> {
  return request<Agent>(`/api/agents/${agentId}`)
}
