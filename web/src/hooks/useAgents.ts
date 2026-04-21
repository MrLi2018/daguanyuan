import { useState, useEffect, useCallback } from 'react'
import type { Agent } from '../types'
import { fetchAgents } from '../api'

export function useAgents() {
  const [agents, setAgents] = useState<Agent[]>([])
  const [loading, setLoading] = useState(true)

  const load = useCallback(async () => {
    try {
      const data = await fetchAgents()
      setAgents(data)
    } catch {
      // silently fail
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    load()
    const timer = setInterval(load, 15000)
    return () => clearInterval(timer)
  }, [load])

  return { agents, loading }
}
