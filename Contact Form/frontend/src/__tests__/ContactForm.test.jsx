// Tools from React Testing Library:
//   render     -> draws the component into the fake browser (jsdom)
//   screen     -> searches what was drawn, like a user looking at the page
//   fireEvent  -> pretends to be the user (typing, clicking)
import { render, screen, fireEvent } from '@testing-library/react'

// This import is what fails first (Red #1): the file does not exist yet.
// It is a DEFAULT import, so ContactForm.jsx must use `export default`.
import ContactForm from '../ContactForm'

// describe/it/expect need no import because vite.config.js has globals: true
describe('ContactForm', () => {

  // TEST 1: the three fields exist.
  // getByLabelText finds an input through its <label>. If the label and input
  // are not linked (htmlFor on the label matches id on the input), it fails.
  // That also means screen readers can use the form.
  it('renders name, email and message inputs', () => {
    render(<ContactForm />)
    expect(screen.getByLabelText('Name')).toBeInTheDocument()
    expect(screen.getByLabelText('Email')).toBeInTheDocument()
    expect(screen.getByLabelText('Message')).toBeInTheDocument()
  })

  // TEST 2: the form starts with no error.
  // queryByText returns null when nothing matches. getByText would throw.
  // That is why we use query* when we expect the thing NOT to be there.
  it('shows no error before submitting', () => {
    render(<ContactForm />)
    expect(screen.queryByText('All fields are required')).not.toBeInTheDocument()
  })

  // TEST 3: spaces-only name is rejected.
  // Same trap as '' vs NULL in SQL: "   " is not empty, but "   ".trim() is.
  it('shows an error when the name is only spaces', () => {
    render(<ContactForm />)

    // fireEvent.change sets the input's value and fires the event React listens to
    fireEvent.change(screen.getByLabelText('Name'), { target: { value: '   ' } })
    fireEvent.change(screen.getByLabelText('Email'), { target: { value: 'a@b.com' } })
    fireEvent.change(screen.getByLabelText('Message'), { target: { value: 'Hello' } })

    // Clicking a submit button fires the form's submit event
    fireEvent.click(screen.getByRole('button', { name: 'Submit' }))

    expect(screen.getByText('All fields are required')).toBeInTheDocument()
  })
})