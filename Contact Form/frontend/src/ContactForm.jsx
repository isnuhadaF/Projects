import { useState } from 'react'

export default function ContactForm() {
  // STATE: React remembers these between renders.
  // useState("") returns [current value, function that changes it].
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  function handleSubmit(event) {
    // 1. Stop the browser from reloading the page (its default for forms).
    event.preventDefault()

    // 2. trim() removes spaces at both ends, so "   " becomes "".
    //    We check the trimmed copy, but we do not change what the user typed.
    const anyEmpty =
      name.trim() === '' || email.trim() === '' || message.trim() === ''

    // 3. Bad input: show the error and STOP. The later Axios request must not run.
    if (anyEmpty) {
      setError('All fields are required')
      return
    }

    // 4. Good input: clear any old error.
    //    (The Axios POST goes here in the next test.)
    setError('')
  }

  return (
    // onSubmit runs handleSubmit when the button is clicked.
    // noValidate turns off the browser's own checks so ours run instead.
    <form noValidate onSubmit={handleSubmit}>
      <label htmlFor="name">Name</label>
      {/* value = what React remembers, onChange = update it on every keystroke.
          event.target.value is the full text currently in the box. */}
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

      {/* `error && ...` shows the <p> only when error is not "".
          An empty string counts as false, so nothing is drawn. */}
      {error && <p>{error}</p>}
    </form>
  )
}