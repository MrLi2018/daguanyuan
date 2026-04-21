import { useState, useEffect, useCallback, useRef } from 'react'
import type { TopicEvent } from '../types'
import { fetchTopicEvents } from '../api'

const POLL_INTERVAL = 5000

export function useEvents(topicId: string | null) {
  const [events, setEvents] = useState<TopicEvent[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(false)
  const prevTopicId = useRef<string | null>(null)

  const load = useCallback(async (id: string, isInitial: boolean) => {
    if (isInitial) setLoading(true)
    try {
      const page = await fetchTopicEvents(id)
      if (page) {
        setEvents(page.content)
        setError(false)
      } else {
        if (isInitial) setEvents([])
        setError(true)
      }
    } catch {
      setError(true)
    } finally {
      if (isInitial) setLoading(false)
    }
  }, [])

  useEffect(() => {
    if (!topicId) {
      setEvents([])
      setLoading(false)
      return
    }

    const isNewTopic = prevTopicId.current !== topicId
    prevTopicId.current = topicId

    if (isNewTopic) {
      setEvents([])
    }

    load(topicId, isNewTopic)

    const timer = setInterval(() => load(topicId, false), POLL_INTERVAL)
    return () => clearInterval(timer)
  }, [topicId, load])

  return { events, loading, error }
}
