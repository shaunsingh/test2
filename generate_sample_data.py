import sqlite3
import random
from datetime import datetime, timedelta

# Sample data
users = ["John Doe", "Jane Smith", "Mike Wilson", "Sarah Johnson", "Alex Brown"]
accounts = ["ACC-001", "ACC-002", "ACC-003", "ACC-004", "ACC-005"]
errors = [
    "Margin call warning for account {account}",
    "Commentary not uploaded for Q4 2023 report",
    "Missing trade confirmation for transaction #45678",
    "Incomplete KYC documentation",
    "Failed to reconcile cash position",
    "Price validation failed for instrument ABC",
    "Missing regulatory filing for account {account}",
    "Trade settlement failed - insufficient funds",
    "Risk limit exceeded for portfolio P123",
    "Failed to generate compliance report"
]
severities = ["LOW", "MEDIUM", "HIGH", "CRITICAL"]
statuses = ["OPEN", "IN_PROGRESS", "RESOLVED", "PENDING"]

def create_database():
    conn = sqlite3.connect('error_analyzer.db')
    cursor = conn.cursor()
    
    # Create table if it doesn't exist
    cursor.execute('''
    CREATE TABLE IF NOT EXISTS error_records (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        account_id INTEGER NOT NULL,
        user TEXT NOT NULL,
        account TEXT NOT NULL,
        error TEXT NOT NULL,
        error_text TEXT NOT NULL,
        status TEXT CHECK(status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'PENDING')) DEFAULT 'OPEN',
        severity TEXT CHECK(severity IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')) DEFAULT 'MEDIUM',
        commentary TEXT,
        timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
    )
    ''')
    
    # Generate and insert sample records
    for _ in range(50):  # Generate 50 sample records
        user = random.choice(users)
        account = random.choice(accounts)
        account_id = int(account.split('-')[1])  # Extract number from account
        error_template = random.choice(errors)
        error = error_template.format(account=account)
        severity = random.choice(severities)
        status = random.choice(statuses)
        
        # Generate a random timestamp within the last 30 days
        timestamp = datetime.now() - timedelta(days=random.randint(0, 30))
        
        cursor.execute('''
        INSERT INTO error_records (
            account_id, user, account, error, error_text, status, severity, timestamp
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        ''', (account_id, user, account, error, error, status, severity, timestamp))
    
    conn.commit()
    conn.close()

if __name__ == "__main__":
    print("Generating sample database...")
    create_database()
    print("Sample database generated successfully!") 