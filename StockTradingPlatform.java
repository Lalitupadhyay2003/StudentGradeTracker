import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StockTradingPlatform {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Market market = new Market();
        Portfolio portfolio = new Portfolio(10000.0);
        System.out.println("✅ Welcome to the Stock Trading Platform Simulator!\nAccount balance: $" + format(portfolio.getCash()));

        boolean keepRunning = true;
        while (keepRunning) {
            market.simulateRandomPriceChanges();
            System.out.println("\n===== MENU =====\n"
                    + "1. View Market\n"
                    + "2. View Portfolio\n"
                    + "3. Buy Stock\n"
                    + "4. Sell Stock\n"
                    + "5. Transaction History\n"
                    + "6. Exit");

            System.out.print("Select an option (1–6): ");
            switch (sc.nextInt()) {
                case 1 -> market.printMarket();
                case 2 -> portfolio.printHoldings(market);
                case 3 -> {
                    System.out.print("Enter symbol to BUY: ");
                    String sym = sc.next().toUpperCase();
                    System.out.print("Qty: ");
                    int qty = sc.nextInt();
                    portfolio.attemptBuy(sym, qty, market);
                }
                case 4 -> {
                    System.out.print("Enter symbol to SELL: ");
                    String sym = sc.next().toUpperCase();
                    System.out.print("Qty: ");
                    int qty = sc.nextInt();
                    portfolio.attemptSell(sym, qty, market);
                }
                case 5 -> portfolio.printTransactionHistory();
                case 6 -> {
                    keepRunning = false;
                    exportPrompt(sc, portfolio);
                }
                default -> System.out.println("Invalid choice. Choose 1–6.");
            }
        }
        sc.close();
        System.out.println("👋 Goodbye!");
    }

    private static void exportPrompt(Scanner sc, Portfolio portfolio) {
        System.out.print("\nWould you like to save portfolio & history as CSV? (y/N): ");
        if (sc.next().trim().equalsIgnoreCase("y")) {
            try {
                portfolio.exportPortfolioCSV("portfolio.csv", "transactions.csv");
                System.out.println("Saved to portfolio.csv and transactions.csv");
            } catch (IOException e) {
                System.out.println("Error saving files: " + e.getMessage());
            }
        }
    }

    private static String format(double v) {
        return new DecimalFormat("#,##0.00").format(v);
    }

    static class Stock {
        private final String symbol;
        private double price;

        public Stock(String symbol, double price) {
            this.symbol = symbol;
            this.price = price;
        }

        public String getSymbol() { return symbol; }
        public double getPrice()  { return price; }

        public void fluctuate() {
            double change = (Math.random() * 0.10) - 0.05;
            price = Math.max(0.01, price * (1 + change));
        }
    }

    static class Market {
        private final Map<String, Stock> stocks = new TreeMap<>();

        public Market() {
            stocks.put("AAPL", new Stock("AAPL", 150.0));
            stocks.put("TSLA", new Stock("TSLA", 800.0));
            stocks.put("GOOG", new Stock("GOOG", 2800.0));
            stocks.put("MSFT", new Stock("MSFT", 300.0));
        }

        public Stock find(String symbol) {
            return stocks.get(symbol);
        }

        public void simulateRandomPriceChanges() {
            for (Stock s : stocks.values()) s.fluctuate();
        }

        public void printMarket() {
            System.out.println("\n📈 Market Prices:");
            for (Stock s : stocks.values()) {
                System.out.printf("%-6s : $%8.2f%n", s.getSymbol(), s.getPrice());
            }
        }
    }

    static class Portfolio {
        private double cash;
        private final Map<String, Integer> holdings = new HashMap<>();
        private final List<Transaction> history = new ArrayList<>();

        public Portfolio(double initialCash) { this.cash = initialCash; }

        public double getCash() { return cash; }

        public void attemptBuy(String symbol, int qty, Market market) {
            Stock stock = market.find(symbol);
            if (stock == null) {
                System.out.println("❌ Symbol not found: " + symbol);
                return;
            }
            double cost = stock.getPrice() * qty;
            if (qty <= 0 || cost > cash) {
                System.out.println("❌ Cannot buy " + qty + " shares—check qty/cash.");
                return;
            }
            cash -= cost;
            holdings.put(symbol, holdings.getOrDefault(symbol, 0) + qty);
            history.add(new Transaction(LocalDateTime.now(), "BUY", symbol, qty, stock.getPrice()));
            System.out.println("✅ Bought " + qty + "× " + symbol + " @ $" + format(stock.getPrice())
                    + " → New cash: $" + format(cash));
        }

        public void attemptSell(String symbol, int qty, Market market) {
            Integer owned = holdings.get(symbol);
            Stock stock = market.find(symbol);
            if (stock == null || owned == null || owned < qty || qty <= 0) {
                System.out.println("❌ Not enough shares to sell or invalid symbol/qty.");
                return;
            }
            double proceeds = stock.getPrice() * qty;
            cash += proceeds;
            holdings.put(symbol, owned - qty);
            if (holdings.get(symbol) == 0) holdings.remove(symbol);
            history.add(new Transaction(LocalDateTime.now(), "SELL", symbol, qty, stock.getPrice()));
            System.out.println("✅ Sold " + qty + "× " + symbol + " @ $" + format(stock.getPrice())
                    + " → New cash: $" + format(cash));
        }

        public void printHoldings(Market market) {
            System.out.println("\n💼 Portfolio Summary:");
            System.out.printf("%-6s %8s %12s %-12s%n", "Symbol", "Qty", "Price", "Total Value");
            double totalVal = 0;
            for (var e : holdings.entrySet()) {
                Stock stock = market.find(e.getKey());
                double val = stock.getPrice() * e.getValue();
                totalVal += val;
                System.out.printf("%-6s %8d $%10.2f $%10.2f%n",
                        stock.getSymbol(), e.getValue(), stock.getPrice(), val);
            }
            System.out.println("----------------------------------------");
            System.out.printf("Cash balance : $%10.2f%n", cash);
            System.out.printf("Total value  : $%10.2f%n", totalVal + cash);
        }

        public void printTransactionHistory() {
            System.out.println("\n🕒 Transaction History:");
            if (history.isEmpty()) {
                System.out.println("(none yet)");
                return;
            }
            history.forEach(tx -> System.out.println(tx));
        }

        public void exportPortfolioCSV(String portFile, String txFile) throws IOException {
            try (BufferedWriter w1 = new BufferedWriter(new FileWriter(portFile));
                 BufferedWriter w2 = new BufferedWriter(new FileWriter(txFile))) {
                w1.write("Symbol,Qty\n");
                holdings.forEach((sym, q) -> {
                    try { w1.write(String.format("%s,%d\n", sym, q)); } catch (IOException e) {}
                });
                w2.write("DateTime,Type,Symbol,Qty,Price\n");
                history.forEach(tx -> {
                    try { w2.write(tx.csvString() + "\n"); } catch (IOException e) {}
                });
            }
        }
    }

    static class Transaction {
        private static final DateTimeFormatter FMT =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        private final LocalDateTime ts;
        private final String type, symbol;
        private final int qty;
        private final double price;

        public Transaction(LocalDateTime t, String type, String s, int qty, double p) {
            this.ts = t; this.type = type; this.symbol = s;
            this.qty = qty; this.price = p;
        }

        @Override
        public String toString() {
            return ts.format(FMT) + " • " + type + " " + qty + "×" + symbol +
                   " @ $" + format(price);
        }

        public String csvString() {
            return ts.format(FMT) + "," + type + "," + symbol + "," + qty + "," + price;
        }
    }
}
