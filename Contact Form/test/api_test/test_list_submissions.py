from datetime import datetime
from unittest.mock import MagicMock, patch

import mysql.connector

from api.app import app


def fake_connection(rows):
    # The fake cursor hands back whatever rows we give it.
    # No real MySQL is used, so the test is fast and safe.
    cursor = MagicMock()
    cursor.fetchall.return_value = rows
    conn = MagicMock()
    conn.cursor.return_value = cursor
    return conn


def test_list_returns_rows_as_json():
    rows = [
        {'id': 2, 'name': 'Ada', 'email': 'ada@example.com',
         'message': 'Hello', 'created_at': datetime(2026, 10, 5, 9, 44, 17)},
        {'id': 1, 'name': 'Test User', 'email': 'test@example.com',
         'message': 'Hi', 'created_at': datetime(2026, 10, 4, 17, 3, 2)},
    ]
    with patch('api.app.get_db_connection', return_value=fake_connection(rows)):
        response = app.test_client().get('/api/submissions')

    assert response.status_code == 200
    data = response.get_json()
    assert len(data) == 2
    assert data[0]['name'] == 'Ada'
    # datetime is not JSON, so the API must send it as ISO text.
    assert data[0]['created_at'] == '2026-10-05T09:44:17'


def test_list_empty_table_returns_empty_list():
    with patch('api.app.get_db_connection', return_value=fake_connection([])):
        response = app.test_client().get('/api/submissions')

    assert response.status_code == 200
    assert response.get_json() == []


def test_list_database_failure_returns_500():
    with patch('api.app.get_db_connection',
               side_effect=mysql.connector.Error('boom')):
        response = app.test_client().get('/api/submissions')

    assert response.status_code == 500
    assert 'error' in response.get_json()