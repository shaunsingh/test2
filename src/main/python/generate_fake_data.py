import csv
from faker import Faker
import random
from datetime import datetime, timedelta

fake = Faker()

# Common investment banking errors
ERROR_TYPES = [
    "Market value not found for security {symbol}",
    "Commentary not uploaded for account {account}",
    "Missing price data for {symbol}",
    "Invalid trade date for {symbol}",
    "Insufficient liquidity for {symbol}",
    "Position reconciliation failed for account {account}",
    "NAV calculation error for account {account}",
    "Missing corporate action data for {symbol}",
    "Trade settlement failed for {symbol}",
    "Risk limits exceeded for account {account}",
    "Margin call warning for account {account}",
    "Compliance breach detected for {symbol}",
    "Missing benchmark data for portfolio {account}",
    "Failed to generate P&L report for {account}",
    "Data feed interruption for {symbol} pricing"
]

# Generate fake users
def generate_users(num_users):
    return [{
        'user_id': i,
        'name': fake.name(),
        'email': fake.email()
    } for i in range(1, num_users + 1)]

# Generate fake accounts for users
def generate_accounts(users, min_accounts=1, max_accounts=5):
    accounts = []
    account_id = 1
    
    for user in users:
        num_accounts = random.randint(min_accounts, max_accounts)
        for _ in range(num_accounts):
            account_type = random.choice(['Trading', 'Investment', 'Retirement', 'Margin', 'Corporate'])
            accounts.append({
                'account_id': account_id,
                'user_id': user['user_id'],
                'account_number': f"ACC-{fake.unique.random_number(digits=8)}",
                'account_type': account_type
            })
            account_id += 1
    return accounts

# Generate errors for accounts
def generate_errors(accounts, min_errors=1, max_errors=10):
    errors = []
    error_id = 1
    
    for account in accounts:
        num_errors = random.randint(min_errors, max_errors)
        for _ in range(num_errors):
            symbol = fake.unique.bothify(text='???#').upper()
            error_template = random.choice(ERROR_TYPES)
            error_message = error_template.format(
                symbol=symbol,
                account=account['account_number']
            )
            
            # Generate a random date within the last 30 days
            error_date = datetime.now() - timedelta(
                days=random.randint(0, 30),
                hours=random.randint(0, 23),
                minutes=random.randint(0, 59)
            )
            
            errors.append({
                'error_id': error_id,
                'user_id': account['user_id'],
                'account_id': account['account_id'],
                'error_message': error_message,
                'timestamp': error_date.strftime('%Y-%m-%d %H:%M:%S'),
                'severity': random.choice(['HIGH', 'MEDIUM', 'LOW'])
            })
            error_id += 1
    return errors

def main():
    # Generate 50 users
    users = generate_users(50)
    
    # Generate 1-5 accounts per user
    accounts = generate_accounts(users, 1, 5)
    
    # Generate 1-10 errors per account
    errors = generate_errors(accounts, 1, 10)
    
    # Write to CSV files
    with open('src/main/resources/data/users.csv', 'w', newline='') as f:
        writer = csv.DictWriter(f, fieldnames=['user_id', 'name', 'email'])
        writer.writeheader()
        writer.writerows(users)

    with open('src/main/resources/data/accounts.csv', 'w', newline='') as f:
        writer = csv.DictWriter(f, fieldnames=['account_id', 'user_id', 'account_number', 'account_type'])
        writer.writeheader()
        writer.writerows(accounts)

    with open('src/main/resources/data/errors.csv', 'w', newline='') as f:
        writer = csv.DictWriter(f, fieldnames=['error_id', 'user_id', 'account_id', 'error_message', 'timestamp', 'severity'])
        writer.writeheader()
        writer.writerows(errors)

if __name__ == "__main__":
    main() 