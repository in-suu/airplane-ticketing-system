# BIYAHENG LANGIT — Complete Presentation Script (Taglish)
### Enterprise Flight Management Platform | Java Swing

---

# PART 1 — OPENING (Panimula)

> "Good day po sa aming panel! Ang aming sistema ay tinatawag na **Biyaheng Langit** — isang Enterprise Flight Management Platform na ginawa sa **Java** gamit ang **Swing GUI framework**. Sini-simulate nito ang mga pangunahing operasyon ng isang airline ticketing terminal — mula sa pagtitignan ng mga available na flights, pag-book ng pasahero, pagpili ng upuan, hanggang sa pamamahala ng booking records. Lahat ng ito ay binuo gamit ang mga prinsipyo ng **Object-Oriented Programming**, na aming ipapaliwanag ngayon."

---

# PART 2 — PROJECT STRUCTURE (Istruktura ng Proyekto)

```
src/
├── com/system/
│   ├── gui/        ← Lahat ng screens at custom components
│   │   ├── MainDashboardFrame.java       (JFrame — ang tanging bintana)
│   │   ├── WelcomeFrame.java             (JPanel — Welcome screen)
│   │   ├── FlightViewFrame.java          (JPanel — Flights screen)
│   │   ├── PassengerDetailsFrame.java    (JPanel — Booking form)
│   │   ├── BookingRecordsFrame.java      (JPanel — Records screen)
│   │   ├── DomesticSeatMapDialog.java    (JDialog — Seat picker)
│   │   ├── InternationalSeatMapDialog.java (JDialog — Seat picker)
│   │   ├── SidebarPanel.java             (custom JPanel)
│   │   ├── ActionButton.java             (custom JButton)
│   │   ├── NavSidebarButton.java         (custom JButton)
│   │   ├── RoundedButton.java            (custom JButton)
│   │   ├── RoundedPanel.java             (custom JPanel)
│   │   ├── RoundedTextField.java         (custom JTextField)
│   │   ├── ModernStatCard.java           (custom JPanel)
│   │   └── ModernActionCard.java         (custom JPanel)
│   └── models/     ← Data at business logic
│       ├── DataManager.java              (static data store)
│       └── Passenger.java               (data model)
```

---

# PART 3 — SINGLE JFRAME DESIGN (Taglish Explanation)

> "Isa sa mga tatanungin sa inyo ay kung bakit isa lang ang aming `JFrame`. Eto po ang explanation:"

### Ang JFrame — Ang Bintana

> "Ang `JFrame` po ay yung **bintana** mismo ng aming application — yung buong window na lumalabas sa screen. Isa lang kaming gumamit na `JFrame` sa buong sistema, which is yung `MainDashboardFrame`."

### Ang JPanel — Ang Laman ng Bintana

> "Pero yung `JFrame` mismo ay walang laman — parang **empty na picture frame**. Kaya ang laman niya ay mga `JPanel`. Ang `JPanel` po ay isang **container** — parang isang kahon o board na pwedeng lagyan ng mga buttons, labels, tables, at iba pang components."

### Ang CardLayout — Ang Screen Switcher

> "Para lang may isang bintana pero maraming screens, ginamit namin ang tinatawag na `CardLayout`. Isipin niyo po na parang **deck of cards** o **slideshow** — mayroon kaming 5 screens na naka-load sa loob ng iisang `JPanel`, pero **isa lang ang nakikita** ng user sa isang pagkakataon."

### Paano Naka-connect?

```
JFrame  (MainDashboardFrame)  ← ang bintana ng app
  └── cardPanel (JPanel + CardLayout)  ← ang deck ng cards
        ├── WelcomeFrame         (JPanel) → "WELCOME"
        ├── pnlDashboardShell    (JPanel) → "DASHBOARD"
        │     ├── SidebarPanel   (WEST)
        │     └── pnlMain        (CENTER)
        │           ├── ModernStatCard ×3
        │           ├── pnlSearchCard
        │           └── pnlRecordsCard
        ├── FlightViewFrame      (JPanel) → "FLIGHT_VIEW"
        ├── PassengerDetailsFrame(JPanel) → "PASSENGER_DETAILS"
        └── BookingRecordsFrame  (JPanel) → "RECORDS_VIEW"
```

### Code — Paano Sila Naka-connect

```java
// Step 1: JFrame gives its interior to cardPanel
cardLayout = new CardLayout();
cardPanel  = new JPanel(cardLayout);
setContentPane(cardPanel);            // JFrame now shows cardPanel

// Step 2: Each JPanel screen is added with a name tag
cardPanel.add(new WelcomeFrame(),            "WELCOME");
cardPanel.add(pnlDashboardShell,             "DASHBOARD");
cardPanel.add(new FlightViewFrame(),         "FLIGHT_VIEW");
cardPanel.add(new PassengerDetailsFrame(),   "PASSENGER_DETAILS");
cardPanel.add(new BookingRecordsFrame(),     "RECORDS_VIEW");

// Step 3: Static method switches the visible card
public static void showCard(String cardName) {
    cardLayout.show(cardPanel, cardName);
}

// Step 4: Any child panel calls it to navigate
MainDashboardFrame.showCard("PASSENGER_DETAILS"); // from FlightViewFrame
MainDashboardFrame.showCard("DASHBOARD");         // from WelcomeFrame
```

> "Kaya po, hindi kami nagbubukas ng bagong bintana tuwing mag-navigate. **Napalitan lang yung laman ng loob ng JFrame** — parang nagpapalit ng slide sa PowerPoint."

### Bakit Ganito?

| Approach | Problema |
|---|---|
| Maraming `JFrame` | Maraming bintana sa taskbar, magulo |
| `JDialog` bilang screens | Modal — nagba-block ng interaction |
| **CardLayout sa isang JFrame** | ✅ Isa lang ang bintana, maayos, madaling i-manage |

### Paano Nag-pass ng Data sa pagitan ng Screens?

> "Dahil lahat ng panels ay nasa loob ng isang JFrame, nag-share sila ng data gamit ang `DataManager` na may **static fields** — hindi na kailangan pang mag-pass ng objects between windows."

```java
// FlightViewFrame stores the chosen flight
DataManager.selectedFlightData = rawFlight;

// PassengerDetailsFrame reads it when it becomes visible
Object[] targetFlightData = DataManager.selectedFlightData;
```

---

# PART 4 — CLASSES AT OBJECTS

> "Sa Java OOP, ang **class** ay parang blueprint, at ang **object** ay yung live na instance nito."

| Class | Extends | Role |
|---|---|---|
| `MainDashboardFrame` | `JFrame` | Main window; entry point |
| `WelcomeFrame` | `JPanel` | Welcome screen |
| `FlightViewFrame` | `JPanel` | Flight listings |
| `PassengerDetailsFrame` | `JPanel` | Booking form |
| `BookingRecordsFrame` | `JPanel` | Records ledger |
| `DomesticSeatMapDialog` | `JDialog` | Domestic seat picker |
| `InternationalSeatMapDialog` | `JDialog` | International seat picker |
| `SidebarPanel` | `JPanel` | Nav sidebar with bg image |
| `ActionButton` | `JButton` | Custom styled button |
| `NavSidebarButton` | `JButton` | Nav button with active state |
| `RoundedButton` | `JButton` | Hover-aware rounded button |
| `RoundedPanel` | `JPanel` | Rounded corner panel |
| `RoundedTextField` | `JTextField` | Rounded text field |
| `ModernStatCard` | `JPanel` | Dashboard stat card |
| `ModernActionCard` | `JPanel` | Dashboard action card |
| `DataManager` | *(none)* | Static data store |
| `Passenger` | *(none)* | Passenger data model |

### Object Creation Examples

```java
// Gumagawa ng object ng WelcomeFrame at idinagdag sa cardPanel
cardPanel.add(new WelcomeFrame(), "WELCOME");

// Gumagawa ng Passenger object at inilalagay sa DataManager
Passenger newPassenger = new Passenger(
    txtName.getText().trim(),
    txtContact.getText().trim(),
    txtPassport.getText().trim(),
    age
);
DataManager.bookedPassengers.add(newPassenger);

// Gumagawa ng dialog object para sa seat selection
DomesticSeatMapDialog dlg = new DomesticSeatMapDialog(owner, isBusinessMode, selectedSeat);
```

---

# PART 5 — OOP CONCEPTS

---

## 5.1 ENCAPSULATION

> "Ang **encapsulation** po ay ang pag-hide ng internal data at ang pag-expose lang ng kailangan."

```java
// Passenger.java — private fields, controlled via getters/setters
public class Passenger {
    private String name;      // hindi pwedeng direktang i-access
    private String contact;
    private String passport;
    private int age;

    public String getName() { return name; }       // controlled access
    public void setName(String name) { this.name = name; }
    // ... ganito rin ang iba
}
```

> "Hindi po pwedeng direktang baguhin yung `name` ng isang Passenger object mula sa labas. Kailangan dumaan sa `getName()` at `setName()`. Ito ang encapsulation."

**Iba pang halimbawa:**
- `NavSidebarButton` — `isActive` is private, changed only via `setActive(boolean)`
- `DomesticSeatMapDialog` — `selectedSeat` is private, accessed via `getSelectedSeat()`

---

## 5.2 INHERITANCE

> "Ang **inheritance** ay nagbibigay-daan sa child class na gamitin at palawigin ang behavior ng parent class."

```java
// ActionButton ay nag-inherit ng lahat ng JButton methods
public class ActionButton extends JButton {
    // awtomatikong mayroon na: addActionListener(), setText(), setFont()...
    // dinagdag lang namin yung custom rounded painting
}
```

> "Halimbawa po, ang `ActionButton extends JButton`. Hindi na namin kailangang isulat muli ang lahat ng button logic — na-inherit na namin ito mula sa `JButton`. Ang ginawa lang namin ay dagdag na custom na rendering."

| Child | extends | Inherits |
|---|---|---|
| `MainDashboardFrame` | `JFrame` | Window, titlebar, close button |
| `FlightViewFrame` | `JPanel` | Panel rendering, add() method |
| `ActionButton` | `JButton` | Button behavior, listeners |
| `RoundedTextField` | `JTextField` | Text input behavior |
| `DomesticSeatMapDialog` | `JDialog` | Modal dialog behavior |

---

## 5.3 POLYMORPHISM

> "Ang **polymorphism** ay ibig sabihing 'maraming anyo' — ang parehong method name ay nag-behave nang iba depende sa object."

### Runtime Polymorphism — Method Overriding

> "Bawat custom component namin ay nag-o-`@Override` ng `paintComponent()` ng parent niya:"

```java
// RoundedButton.java — iba ang rendering kaysa default JButton
@Override
protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    // draws shadow, rounded rect, hover/press colors
    g2.fillRoundRect(0, 0, w - 2, h - 2, arc, arc);
    g2.dispose();
    super.paintComponent(g); // tapos hinahayaan ang JButton na iguhit ang text
}

// NavSidebarButton.java — iba rin ang rendering
@Override
protected void paintComponent(Graphics g) {
    // draws active-state orange bar or EXIT pill style
}

// SidebarPanel.java — iba rin
@Override
protected void paintComponent(Graphics g) {
    // draws background image + gradient overlay
}
```

> "Tatlo silang `paintComponent()` — parehong method name, pero magkakaibang behavior. Ito ang polymorphism."

### Polymorphism — Anonymous ActionListener

```java
// Parehong ActionListener, iba ang laman ng actionPerformed()
btnNavDashboard.addActionListener(new ActionListener() {
    @Override public void actionPerformed(ActionEvent e) { showCard("DASHBOARD"); }
});
btnNavViewFlights.addActionListener(new ActionListener() {
    @Override public void actionPerformed(ActionEvent e) { showCard("FLIGHT_VIEW"); }
});
```

---

## 5.4 ABSTRACTION

> "Ang **abstraction** ay nagtatago ng kumplikadong implementation at nagpapakita lang ng kailangan ng ibang class."

```java
// showCard() — ang user ng method ay hindi kailangang malaman kung paano gumagana ang CardLayout
public static void showCard(String cardName) {
    cardLayout.show(cardPanel, cardName); // kumplikadong logic nakatago dito
}

// handleSeatSelection() — nakatago ang logic kung domestic o international
private void handleSeatSelection() {
    if (isInternational) {
        new InternationalSeatMapDialog(owner, isBusinessMode, selectedSeat).setVisible(true);
    } else {
        new DomesticSeatMapDialog(owner, isBusinessMode, selectedSeat).setVisible(true);
    }
}
```

> "Ang `DataManager` mismo ay isang halimbawa ng abstraction — lahat ng screens ay tumatawag lang ng `DataManager.getMockFlightsDB()` nang hindi alam kung saan galing yung data."

### Anonymous Icon Interface (PassengerDetailsFrame)

```java
// Ang Icon interface ay abstract — kinoccontrol ng implementor kung paano iguhit
Icon unselectedIcon = new Icon() {
    @Override public void paintIcon(Component c, Graphics g, int x, int y) {
        // draws empty rounded box
    }
    @Override public int getIconWidth()  { return 20; }
    @Override public int getIconHeight() { return 20; }
};
Icon selectedIcon = new Icon() {
    @Override public void paintIcon(Component c, Graphics g, int x, int y) {
        // draws orange checkmark
    }
    @Override public int getIconWidth()  { return 20; }
    @Override public int getIconHeight() { return 20; }
};
```

---

## 5.5 CONSTRUCTORS

> "Ang **constructor** ay isang espesyal na method na tinatawag tuwing gumagawa ng bagong object."

### Parameterized Constructors

```java
// Passenger.java — constructor na tumatanggap ng 4 na parameters
public Passenger(String name, String contact, String passport, int age) {
    this.name = name;
    this.contact = contact;
    this.passport = passport;
    this.age = age;
}

// ActionButton.java — constructor na tumatanggap ng text, bg color, fg color
public ActionButton(String text, Color bg, Color fg) {
    super(text);       // tinatawag ang JButton constructor
    this.bgColor = bg;
    setFont(new Font("Segoe UI", Font.BOLD, 13));
    setForeground(fg);
}

// DomesticSeatMapDialog.java
public DomesticSeatMapDialog(Frame owner, boolean isBusinessMode, String currentSeat) {
    super(owner, "Seats Availability Matrix", true); // JDialog constructor
    this.isBusinessMode = isBusinessMode;
    this.tempSelected[0] = currentSeat;
    initComponents();
    initSeatStyles();
}
```

---

## 5.6 CONSTRUCTOR OVERLOADING

> "Ang **constructor overloading** ay ang pagkakaroon ng maraming constructor sa iisang class — parehong pangalan, iba ang parameters."

```java
// RoundedTextField.java — dalawang constructor
public RoundedTextField() {           // walang argument
    super();
    init();
}
public RoundedTextField(int columns) { // may column count
    super(columns);
    init();
}

// ActionButton.java — dalawang constructor
public ActionButton() {
    this("Button", Color.WHITE, Color.BLACK); // tinatawag ang ibang constructor
}
public ActionButton(String text, Color bg, Color fg) {
    super(text); ...
}

// ModernStatCard.java — dalawang constructor
public ModernStatCard() {
    this("TITLE", "VALUE");
}
public ModernStatCard(String title, String value) { ... }
```

> "Ginawa namin ito para compatible sa **WindowBuilder** — ang no-arg constructor ang tinatawag nito para i-preview ang component sa design view."

---

## 5.7 METHOD OVERRIDING

> "Ang **method overriding** ay ang pag-replace ng implementation ng parent class method sa child class."

| Class | Overridden Method | Bagong Behavior |
|---|---|---|
| `ActionButton` | `paintComponent()` | Draws filled rounded rect |
| `NavSidebarButton` | `paintComponent()` | Draws active-state bar + EXIT pill |
| `RoundedButton` | `paintComponent()` | Draws shadow + hover/press states |
| `RoundedPanel` | `paintComponent()` | Draws rounded corner fill |
| `RoundedTextField` | `paintComponent()` | Draws white rounded bg |
| `SidebarPanel` | `paintComponent()` | Draws bg image + gradient |
| `ModernStatCard` | `paintComponent()` | Draws navy card + orange accent |
| `WelcomeFrame` | `paintComponent()` | Draws full-panel bg image |
| `FlightViewFrame table` | `isCellEditable()` | Always returns `false` |
| `Anonymous MouseAdapter` | `mouseEntered/Exited` | Hover effects |
| `Anonymous ComponentAdapter` | `componentResized()` | Responsive layout |

```java
// Halimbawa — NavSidebarButton.java
@Override
protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    if (text.equals("EXIT")) {
        g2.setColor(getModel().isRollover() ? SOFT_CARD_NAVY : REFINED_NAVY);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
    } else if (isActive || getModel().isRollover()) {
        g2.setColor(new Color(40, 20, 10, 160));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        g2.setColor(DataManager.SUNSET_ORANGE);
        g2.fillRoundRect(0, 0, 5, getHeight(), 5, 5); // orange left bar
    }
    g2.dispose();
    super.paintComponent(g);
}
```

---

## 5.8 STATIC MEMBERS

> "Ang **static** members ay hindi naka-attach sa isang object — sila ay pag-aari ng class mismo. Kahit anong part ng program ay makakapag-access nito nang direkta."

```java
// DataManager.java — lahat ay static
public class DataManager {
    public static final Color SUNSET_ORANGE = new Color(255, 140, 0); // shared color
    public static Object[] selectedFlightData;                         // shared flight
    public static ArrayList<Passenger> bookedPassengers = new ArrayList<>();
    public static ArrayList<Object[]> bookingRecords = new ArrayList<>();

    public static Object[][] getMockFlightsDB() { ... }
    public static Object[][] getMockRecordsDB() { ... }
}

// MainDashboardFrame.java — static nav router
private static CardLayout cardLayout;
private static JPanel     cardPanel;

public static void showCard(String cardName) {
    cardLayout.show(cardPanel, cardName);
}
```

> "Halimbawa, ang `DataManager.SUNSET_ORANGE` — isang beses lang ito naka-define, pero ginagamit ng lahat ng screens bilang consistent na kulay. Hindi na kailangang gumawa ng bagong object para ma-access ito."

---

# PART 6 — COMPONENTS PER SCREEN (Quick Reference)

### MainDashboardFrame (JFrame)
- `cardPanel` (JPanel + CardLayout) — holds all 5 screens
- `SidebarPanel` — WEST, shows bg image + nav buttons
- `NavSidebarButton ×5` — DASHBOARD, VIEW FLIGHTS, BOOK FLIGHTS, RECORDS, EXIT
- `ModernStatCard ×3` — Active Flights, Today's Bookings, System Status
- `pnlSearchCard` — Quick Flight Search action card
- `pnlRecordsCard` — Passenger Records action card
- `RoundedButton` — SEARCH NOW, VIEW LOGS

### FlightViewFrame (JPanel)
- `SidebarPanel` — WEST nav
- `RoundedPanel` — main rounded content card
- `JComboBox ×2` — Status filter, Category filter
- `JTextField` — keyword search
- `JTable + DefaultTableModel` — flight listings (read-only)
- `DefaultTableCellRenderer` — custom color coding per status
- `JScrollPane` — scrollable table
- `ActionButton ×2` — CANCEL, PROCEED TO BOOKING

### PassengerDetailsFrame (JPanel)
- `SidebarPanel` — WEST nav
- `RoundedPanel` — main content area
- `RoundedTextField ×5` — Name, Age, Contact, Passport, Flight Search
- `JComboBox ×6` — Nationality, Trip Type, Cabin Class, Baggage, Discount, Special Cargo
- `JCheckBox` — Travel Insurance (custom Icon drawn)
- `ActionButton ×4` — VERIFY, CHANGE, SELECT SEAT, CANCEL, CONFIRM
- Opens `DomesticSeatMapDialog` or `InternationalSeatMapDialog`

### BookingRecordsFrame (JPanel)
- `SidebarPanel` — WEST nav
- `RoundedPanel` — main content area
- `RoundedTextField` — search bar
- `JTable + DefaultTableModel` — booking records
- `JLabel` — total revenue analytics strip
- `ActionButton ×3` — CANCEL BOOKING, VIEW RECEIPT, RETURN TO DASHBOARD

### DomesticSeatMapDialog & InternationalSeatMapDialog (JDialog)
- `JPanel(GridLayout)` — 8 rows of seat buttons
- `JButton ×48` (domestic) or `JButton ×72` (international) — individual seats
- `JPanel(FlowLayout)` — color legend
- `JButton` — CONFIRM SELECTION

---

# PART 7 — SYSTEM FLOW (Para sa Live Demo)

> "Ito po ang buo na daloy ng aming sistema:"

```
1. App starts
   → new MainDashboardFrame()
   → showCard("WELCOME")

2. User clicks "Start Your Journey" (WelcomeFrame)
   → MainDashboardFrame.showCard("DASHBOARD")

3. Dashboard shown
   → 3 ModernStatCards, 2 action cards, 5 nav buttons
   → User clicks "SEARCH NOW" or "VIEW FLIGHTS"
   → showCard("FLIGHT_VIEW")

4. FlightViewFrame shown
   → AncestorListener fires → loadFlightRecordsData()
   → User filters, selects a row, clicks PROCEED
   → DataManager.selectedFlightData = chosen flight
   → showCard("PASSENGER_DETAILS")

5. PassengerDetailsFrame shown
   → AncestorListener fires → startNewBookingSession()
   → Auto-fills flight if DataManager.selectedFlightData != null
   → User fills form → clicks SELECT SEAT
       → DomesticSeatMapDialog (if domestic flight)
       → InternationalSeatMapDialog (if international flight)
   → Seat returned, user clicks CONFIRM & PROCEED
   → new Passenger object created
   → DataManager.bookedPassengers.add(newPassenger)
   → DataManager.bookingRecords.add(...)
   → displayModernInvoice() opens as JDialog
   → User clicks FINISH & PRINT
   → showCard("DASHBOARD")

6. User clicks RECORDS nav button
   → showCard("RECORDS_VIEW")
   → AncestorListener → loadBookingLedgerData()
   → User can CANCEL booking or VIEW RECEIPT (opens modal JDialog)
```

---

# PART 8 — SUMMARY TABLE (Para sa Tanong ng Panel)

| OOP Concept | Saan Makikita |
|---|---|
| **Class** | Bawat `.java` file |
| **Object** | `new WelcomeFrame()`, `new Passenger(...)`, `new DomesticSeatMapDialog(...)` |
| **Encapsulation** | `Passenger.java` — private fields + getters/setters |
| **Inheritance** | Lahat ng custom components — `extends JPanel / JButton / JDialog / JFrame` |
| **Polymorphism** | `@Override paintComponent()` sa bawat custom component |
| **Abstraction** | `showCard()`, `DataManager`, `ActionListener`, `Icon` interface |
| **Constructor** | `Passenger(String,String,String,int)`, dialog constructors |
| **Constructor Overloading** | `RoundedTextField()` vs `RoundedTextField(int)` |
| **Method Overriding** | `paintComponent`, `isCellEditable`, `componentResized` |
| **Static Members** | `DataManager.SUNSET_ORANGE`, `DataManager.selectedFlightData`, `showCard()` |
| **Anonymous Inner Class** | `ActionListener`, `MouseAdapter`, `ComponentAdapter`, `Icon` |
| **Generic Method** | `createStyledComboBox<T>()` in PassengerDetailsFrame |

---

# PART 9 — CLOSING STATEMENT (Pangwakas)

> "Sa kabuuan po, ang aming sistema ay gumagamit ng **iisang `JFrame`** bilang application window. Sa loob nito ay isang `JPanel` na may `CardLayout` — parang isang deck ng cards na nagtatago ng lahat ng 5 screens namin. Ang bawat screen ay isang `JPanel` na naka-register sa `CardLayout` gamit ang isang pangalan. Para mag-navigate, tinatawag lang namin ang aming static na `showCard()` method — walang bagong bintana na bumubukas, napalitan lang yung laman ng loob ng JFrame."

> "Ang lahat ng aming components — buttons, panels, text fields, dialogs — ay gumagamit ng OOP concepts: **Inheritance** para ma-reuse ang Swing functionality, **Polymorphism** at **Overriding** para ma-customize ang rendering, **Encapsulation** para maprotektahan ang data, at **Abstraction** para maitago ang kumplikadong logic. Salamat po!"
