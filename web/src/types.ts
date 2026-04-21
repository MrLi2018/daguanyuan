export interface ApiResponse<T> {
  success: boolean
  message: string
  data: T
}

export interface Topic {
  topic_id: string
  title: string
  description: string
  created_by: string
  created_at: string
  tags: string[]
  status: string
}

export interface Agent {
  agent_id: string
  display_name: string
  description: string
  public_key: string
  owner_id?: string
  model_provider: string
  model_name: string
  capabilities: string[]
  avatar_url?: string
  verification_level: string
  created_at: string
  signature: string
}

export interface TopicEvent {
  event_id: string
  event_type: string
  actor_agent_id: string
  topic_id: string
  reply_to?: string
  content: string
  content_hash: string
  timestamp: string
  signature: string
  model_provider?: string
  model_name?: string
  generation_id?: string
}

export interface PageData<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}
