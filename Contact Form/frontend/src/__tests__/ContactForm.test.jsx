import { render, screen, fireEvent } from '@testing-library/react'
import axios from 'axios'
import ContactForm from '../ContactForm'

// vi.mock replaces axios with a fake. No real request leaves the test,
// so Flask and MySQL do not need to run.
vi.mock('axios')

// Wipe the fake's call history so one test cannot affect the next.
beforeEach(() => {
  vi.clearAllMocks()
})

// Helper: type into the three fields. Each argument has a default,
// so a test only names the field it wants to change.
function fillForm({
  name = 'Ada',
  email = 'ada@example.com',
  message = 'Hello there',
} = {}) {
  fireEvent.change(screen.getByLabelText('Name'), { target: { value: name } })
  fireEvent.change(screen.getByLabelText('Email'), { target: { value: email } })
  fireEvent.change(screen.getByLabelText('Message'), { target: { value: message } })
}

function clickSubmit() {
  fireEvent.click(screen.getByRole('button', { name: 'Submit' }))
}

describe('ContactForm', () => {
  it('renders name, email and message inputs', () => {
    render(<ContactForm />)
    expect(screen.getByLabelText('Name')).toBeInTheDocument()
    expect(screen.getByLabelText('Email')).toBeInTheDocument()
    expect(screen.getByLabelText('Message')).toBeInTheDocument()
  })

  it('shows no error before submitting', () => {
    render(<ContactForm />)
    expect(screen.queryByText('All fields are required')).not.toBeInTheDocument()
  })

  // Same test three times, one blank field each. If you remove .trim()
  // from any field in the component, exactly one of these fails.
  it('shows an error when the name is only spaces', () => {
    render(<ContactForm />)
    fillForm({ name: '   ' })
    clickSubmit()
    expect(screen.getByText('All fields are required')).toBeInTheDocument()
  })

  it('shows an error when the email is only spaces', () => {
    render(<ContactForm />)
    fillForm({ email: '   ' })
    clickSubmit()
    expect(screen.getByText('All fields are required')).toBeInTheDocument()
  })

  it('shows an error when the message is only spaces', () => {
    render(<ContactForm />)
    fillForm({ message: '   ' })
    clickSubmit()
    expect(screen.getByText('All fields are required')).toBeInTheDocument()
  })

  // Bad input must stop BEFORE the request. No request, no wasted call.
  it('does not send a request when a field is blank', () => {
    render(<ContactForm />)
    fillForm({ name: '   ' })
    clickSubmit()
    expect(axios.post).not.toHaveBeenCalled()
  })

  // TEST: valid form sends the data
  it('posts the three fields to /api/submit', async () => {
    axios.post.mockResolvedValue({ data: { message: 'Submission saved successfully' } })
    render(<ContactForm />)
    fillForm()
    clickSubmit()

    // The reply comes later (async), so wait for the result to show up.
    await screen.findByText('Submission saved successfully')
    expect(axios.post).toHaveBeenCalledWith('/api/submit', {
      name: 'Ada',
      email: 'ada@example.com',
      message: 'Hello there',
    })
  })

  // TEST: success shows the server message and clears the form
  it('shows the success message and clears the fields', async () => {
    axios.post.mockResolvedValue({ data: { message: 'Submission saved successfully' } })
    render(<ContactForm />)
    fillForm()
    clickSubmit()

    // findBy... waits up to 1 second for the text to appear.
    expect(await screen.findByText('Submission saved successfully')).toBeInTheDocument()
    expect(screen.getByLabelText('Name')).toHaveValue('')
    expect(screen.getByLabelText('Email')).toHaveValue('')
    expect(screen.getByLabelText('Message')).toHaveValue('')
  })

  // TEST: the server says no (400), and the form shows the server's words
  it('shows the error text sent by the server', async () => {
    axios.post.mockRejectedValue({
      response: { data: { error: 'Invalid email format' } },
    })
    render(<ContactForm />)
    fillForm({ email: 'not-an-email' })
    clickSubmit()

    expect(await screen.findByText('Invalid email format')).toBeInTheDocument()
    // On failure the user's text stays, so they can fix it.
    expect(screen.getByLabelText('Email')).toHaveValue('not-an-email')
  })

  // TEST: the server is down, so there is no response at all
  it('shows a fallback message when the server cannot be reached', async () => {
    axios.post.mockRejectedValue(new Error('Network Error'))
    render(<ContactForm />)
    fillForm()
    clickSubmit()

    expect(await screen.findByText('Could not reach the server')).toBeInTheDocument()
  })
})