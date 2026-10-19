import os
import re
from flask import Flask, request, jsonify
import mysql.connector

app = Flask(__name__)

# Syntax pattern for email validation
EMAIL_REGEX = r'^[\w\.-]+@[\w\.-]+\.\w+$'

DB_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': os.environ.get('DB_PASSWORD', ''), # read from the shell, never from a file
    'database': 'contact_form'
}


def get_db_connection():
    # Mechanics: Establishes a raw TCP socket connection to the MySQL daemon on port 3306
    return mysql.connector.connect(**DB_CONFIG)


@app.route('/api/submit', methods=['POST'])
def handle_submission():
    data = request.get_json() or {}

    # 1. Sanitize & trim whitespace
    name = str(data.get('name', '')).strip()
    email = str(data.get('email', '')).strip()
    message = str(data.get('message', '')).strip()

    # 2. Field presence check post-trim
    if not name or not email or not message:
        return jsonify({'error': 'All fields are required and cannot be empty'}), 400

    # 3. Email syntax validation
    if not re.match(EMAIL_REGEX, email):
        return jsonify({'error': 'Invalid email format'}), 400

    conn = None
    cursor = None
    try:
        conn = get_db_connection()
        cursor = conn.cursor()

        query = "INSERT INTO submissions (name, email, message) VALUES (%s, %s, %s)"
        cursor.execute(query, (name, email, message))
        conn.commit()

        return jsonify({'message': 'Submission saved successfully'}), 201

    except mysql.connector.Error as err:
        return jsonify({'error': 'Database transaction failed'}), 500

    finally:
        if cursor:
            cursor.close()
        if conn and conn.is_connected():
            conn.close()


@app.route('/api/submissions', methods=['GET'])
def list_submissions():
    conn = None
    cursor = None
    try:
        conn = get_db_connection()
        # dictionary=True gives each row as {'id': 2, 'name': 'Ada', ...}
        # instead of a plain tuple, so jsonify can use the column names.
        cursor = conn.cursor(dictionary=True)

        # No user input in this query, so there is nothing to inject.
        cursor.execute(
            "SELECT id, name, email, message, created_at "
            "FROM submissions ORDER BY created_at DESC, id DESC"
        )
        rows = cursor.fetchall()

        # A datetime object is not JSON. Turn it into text like
        # '2026-10-05T09:44:17'. The test checks this exact format.
        for row in rows:
            row['created_at'] = row['created_at'].isoformat()

        return jsonify(rows), 200

    except mysql.connector.Error:
        # Fixed message only. Never send str(err): MySQL errors can leak
        # host, user and table names.
        return jsonify({'error': 'Could not load submissions'}), 500

    finally:
        # Same pattern as your POST route. If the connection itself failed,
        # conn is still None, so there is nothing to close.
        if cursor:
            cursor.close()
        if conn and conn.is_connected():
            conn.close()


if __name__ == '__main__':
    app.run(debug=True, port=5000)