import csv
import random
from datetime import datetime, timedelta

urls = ["/index.html", "/products", "/login", "/logout", "/about",
        "/contact", "/api/users", "/api/orders", "/images/logo.png", "/dashboard"]
ips = ["192.168.1.10", "85.214.33.7", "41.200.12.5", "196.12.44.3",
       "10.0.0.5", "78.33.21.9", "154.23.11.8", "200.1.2.3"]
methods = ["GET", "GET", "GET", "POST", "POST", "DELETE"]
statuses = [200, 200, 200, 200, 301, 404, 404, 500, 401, 403]
countries = ["Algeria", "France", "USA", "Germany", "Morocco", "Tunisia", "Spain", "Canada"]

random.seed(42)
start = datetime(2024, 1, 15, 8, 0, 0)

with open("data/logs.csv", "w", newline="") as f:
    writer = csv.writer(f)
    writer.writerow(["timestamp", "ip", "method", "url", "status", "response_time_ms", "bytes", "country"])
    for i in range(200000):
        ts = start + timedelta(seconds=random.randint(0, 86400))
        writer.writerow([
            ts.strftime("%Y-%m-%d %H:%M:%S"),
            random.choice(ips),
            random.choice(methods),
            random.choice(urls),
            random.choice(statuses),
            random.randint(50, 2000),
            random.randint(200, 50000),
            random.choice(countries)
        ])

print("200 000 lignes de logs générées dans data/logs.csv !")