import csv
import sqlite3
from datetime import datetime

def import_data():
    # Connect to SQLite database
    conn = sqlite3.connect('error_analyzer.db')
    cursor = conn.cursor()

    try:
        # Read and import errors.csv
        with open('src/main/resources/data/errors.csv', 'r') as f:
            reader = csv.DictReader(f)
            for row in reader:
                # Convert timestamp string to datetime object
                timestamp = datetime.strptime(row['timestamp'], '%Y-%m-%d %H:%M:%S')
                
                # Insert into error_records table
                cursor.execute("""
                    INSERT INTO error_records (user, account, error, timestamp)
                    VALUES (?, ?, ?, ?)
                """, (
                    row['user_id'],
                    row['account_id'],
                    row['error_message'],
                    timestamp
                ))

        # Commit the changes
        conn.commit()
        print("Data imported successfully!")

    except Exception as e:
        print(f"Error importing data: {e}")
        conn.rollback()

    finally:
        conn.close()

if __name__ == "__main__":
    import_data() 