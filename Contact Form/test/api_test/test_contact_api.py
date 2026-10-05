import unittest
from unittest.mock import patch, MagicMock
from api.app import app


class TestContactAPI(unittest.TestCase):

    def setUp(self):
        self.client = app.test_client()
        self.client.testing = True

    # --- Existing Test: Missing Field Validation ---
    def test_submit_missing_field_returns_400(self):
        payload = {
            "name": "Dotun",
            "email": "dotun@example.com"
        }
        response = self.client.post('/api/submit', json=payload)
        self.assertEqual(response.status_code, 400)
        self.assertIn('error', response.get_json())

    # --- NEW TEST: Whitespace-Only Fields (Expected: 400 Bad Request) ---
    def test_submit_whitespace_only_returns_400(self):
        payload = {
            "name": "   ",
            "email": "dotun@example.com",
            "message": "   "
        }
        response = self.client.post('/api/submit', json=payload)
        self.assertEqual(response.status_code, 400)

    # --- NEW TEST: Invalid Email Syntax (Expected: 400 Bad Request) ---
    def test_submit_invalid_email_returns_400(self):
        payload = {
            "name": "Dotun",
            "email": "dotun-invalid-email",
            "message": "Testing edge case"
        }
        response = self.client.post('/api/submit', json=payload)
        self.assertEqual(response.status_code, 400)
        self.assertIn('Invalid email', response.get_json()['error'])

    # --- NEW TEST: Whitespace Trimming & Successful Insertion (Expected: 201 Created) ---
    @patch('api.app.get_db_connection')
    def test_submit_trims_whitespace_and_succeeds(self, mock_get_db):
        mock_conn = MagicMock()
        mock_cursor = MagicMock()
        mock_get_db.return_value = mock_conn
        mock_conn.cursor.return_value = mock_cursor

        payload = {
            "name": "  Dotun  ",
            "email": "  dotun@example.com ",
            "message": "  Testing edge cases  "
        }
        response = self.client.post('/api/submit', json=payload)

        self.assertEqual(response.status_code, 201)

        # Verify that parameters passed to SQL query execution were stripped clean
        mock_cursor.execute.assert_called_once_with(
            "INSERT INTO submissions (name, email, message) VALUES (%s, %s, %s)",
            ("Dotun", "dotun@example.com", "Testing edge cases")
        )
        mock_conn.commit.assert_called_once()
        mock_cursor.close.assert_called_once()
        mock_conn.close.assert_called_once()


if __name__ == '__main__':
    unittest.main()