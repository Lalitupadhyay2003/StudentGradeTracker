stock_prices = {
    "AAPL": 180,
    "TSLA": 250,
    "GOOGL": 140,
    "AMZN": 125,
    "MSFT": 310
}
portfolio = {}
print("Welcome to the Stock Portfolio Tracker!")
print("Available stocks:", ", ".join(stock_prices.keys()))

while True:
    stock = input("Enter stock symbol (or 'done' to finish): ").upper()
    
    if stock == "DONE":
        break
    elif stock not in stock_prices:
        print("Stock not found. Please enter a valid symbol.")
        continue

    try:
        quantity = int(input(f"Enter quantity of {stock} shares: "))
    except ValueError:
        print("Invalid quantity. Please enter a number.")
        continue

    if stock in portfolio:
        portfolio[stock] += quantity
    else:
        portfolio[stock] = quantity

print("\nInvestment Summary:")
total_value = 0

for stock, quantity in portfolio.items():
    price = stock_prices[stock]
    value = price * quantity
    total_value += value
    print(f"{stock}: {quantity} shares × ₹{price} = ₹{value}")

print(f"\n Total Investment: ₹{total_value}")

save_option = input("\nDo you want to save this report to a file? (yes/no): ").lower()

if save_option == "yes":
    filename = "portfolio_summary.txt"
    with open(filename, "w") as file:
        file.write("Investment Summary:\n")
        for stock, quantity in portfolio.items():
            price = stock_prices[stock]
            value = price * quantity
            file.write(f"{stock}: {quantity} shares × ₹{price} = ₹{value}\n")
        file.write(f"\n Total Investment: ₹{total_value}")
    print(f"Report saved to {filename}")
