import { useState, useEffect, useCallback } from 'react'
import type { Topic } from '../types'
import { fetchTopics } from '../api'

export function useTopics() {
  const [topics, setTopics] = useState<Topic[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(false)

  const load = useCallback(async () => {
    try {
      const data = await fetchTopics()
      setTopics(data)
      setError(false)
    } catch {
      setError(true)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    load()
    const timer = setInterval(load, 30000)
    return () => clearInterval(timer)
  }, [load])

  return { topics, loading, error, reload: load }
}
