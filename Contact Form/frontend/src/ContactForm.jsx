import { useState } from 'react'
import axios from 'axios'

export default function ContactForm() {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  // async lets us use `await`, which pauses THIS function until the
  // server answers. The rest of the page keeps working while it waits.
  async function handleSubmit(event) {
    event.preventDefault()

    // Clear old messages so a stale error never sits next to a new success.
    setError('')
    setSuccess('')

    const anyEmpty =
      name.trim() === '' || email.trim() === '' || message.trim() === ''

    // Guard: bad input stops here. No request is sent.
    if (anyEmpty) {
      setError('All fields are required')
      return
    }

    try {
      // Axios turns this object into JSON. Flask reads these 3 keys.
      const response = await axios.post('/api/submit', { name, email, message })

      // Axios only gets here when the status is 2xx (your 201).
      setSuccess(response.data.message)
      setName('')
      setEmail('')
      setMessage('')
    } catch (err) {
      // Axios jumps here on 400 and 500, and on network failure.
      // With an HTTP error, the server's JSON is in err.response.data.
      // With no response (server down), err.response is undefined,
      // and `?.` returns undefined instead of crashing.
      setError(err.response?.data?.error ?? 'Could not reach the server')
    }
  }

  return (
    <form noValidate onSubmit={handleSubmit}>
      <label htmlFor="name">Name</label>
      <input
        id="name"
        type="text"
        value={name}
        onChange={(event) => setName(event.target.value)}
      />

      <label htmlFor="email">Email</label>
      <input
        id="email"
        type="email"
        value={email}
        onChange={(event) => setEmail(event.target.value)}
      />

      <label htmlFor="message">Message</label>
      <textarea
        id="message"
        value={message}
        onChange={(event) => setMessage(event.target.value)}
      />

      <button type="submit">Submit</button>

      {error && <p role="alert">{error}</p>}
      {success && <p role="status">{success}</p>}
    </form>
  )
}