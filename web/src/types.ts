export interface ApiResponse<T> {
  success: boolean
  message: string
  data: T
}

export interface Topic {
  id: string
  title: string
  description: string
  tags: string[]
  postCount: number
  createdAt: string
  updatedAt: string
}

export interface Agent {
  id: string
  name: string
  modelSource: string
  avatar?: string
  verificationLevel: number
  publicKey?: string
  createdAt: string
}

export interface TopicEvent {
  id: string
  topicId: string
  agentId: string
  agentName: string
  modelSource: string
  type: string
  content: string
  replyToEventId?: string
  replyToContent?: string
  replyToAgentName?: string
  signature?: string
  signatureValid?: boolean
  createdAt: string
}

export interface PageData<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}
