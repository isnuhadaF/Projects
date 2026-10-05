// Import the component from src\ContactForm.jsx.
// './' means "the same folder as this file".
import ContactForm from './ContactForm'

// main.jsx draws <App />, and App draws the form.
// This is the only job App has for now.
function App() {
  return <ContactForm />
}

// main.jsx imports App as a default export, so this line must stay.
export default App