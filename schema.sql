-- Byaheng Langit Flight Ticketing System
-- Normalized Database Schema

CREATE DATABASE IF NOT EXISTS byaheng_langit;
USE byaheng_langit;

-- 1. Airports Table (Master Reference Table)
CREATE TABLE IF NOT EXISTS airports (
    airport_code VARCHAR(10) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL
);

-- 2. Flights Table (Normalized schedule referencing airports)
CREATE TABLE IF NOT EXISTS flights (
    flight_id VARCHAR(20) PRIMARY KEY,
    origin_code VARCHAR(10) NOT NULL,
    dest_code VARCHAR(10) NOT NULL,
    departure_date DATE NOT NULL,
    departure_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('AVAILABLE', 'FULLY BOOKED', 'DELAYED', 'CANCELLED')),
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(20) NOT NULL CHECK (category IN ('domestic', 'international')),
    airline VARCHAR(50) NOT NULL,
    gate VARCHAR(20) NOT NULL,
    seats_available INT NOT NULL DEFAULT 150,
    FOREIGN KEY (origin_code) REFERENCES airports(airport_code),
    FOREIGN KEY (dest_code) REFERENCES airports(airport_code)
);

-- 3. Bookings Table (Transaction table with Foreign Keys)
CREATE TABLE IF NOT EXISTS bookings (
    txn_id VARCHAR(20) PRIMARY KEY,
    passenger_name VARCHAR(100) NOT NULL,
    flight_id VARCHAR(20) NOT NULL,
    seat VARCHAR(10) NOT NULL,
    price_paid DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED' CHECK (status IN ('CONFIRMED', 'CANCELLED')),
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id)
);

-- Populate Lookup Airport Master Tables
INSERT INTO airports VALUES ('MNL', 'Byaheng Langit International Airport', 'Manila', 'Philippines')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('CEB', 'Mactan-Cebu International Airport', 'Cebu', 'Philippines')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('DVO', 'Francisco Bangoy International Airport', 'Davao', 'Philippines')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('PPS', 'Puerto Princesa International Airport', 'Puerto Princesa', 'Philippines')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('MPH', 'Caticlan Airport', 'Boracay', 'Philippines')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('ILO', 'Iloilo International Airport', 'Iloilo', 'Philippines')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('BCD', 'Bacolod-Silay Airport', 'Bacolod', 'Philippines')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('KLO', 'Kalibo International Airport', 'Kalibo', 'Philippines')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('TAC', 'Daniel Z. Romualdez Airport', 'Tacloban', 'Philippines')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('CGY', 'Laguindingan Airport', 'Cagayan de Oro', 'Philippines')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('TAG', 'Bohol-Panglao International Airport', 'Tagbilaran', 'Philippines')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('GES', 'General Santos International Airport', 'General Santos', 'Philippines')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('NRT', 'Narita International Airport', 'Tokyo', 'Japan')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('SIN', 'Changi International Airport', 'Singapore', 'Singapore')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('LAX', 'Los Angeles International Airport', 'Los Angeles', 'United States')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('LHR', 'London Heathrow Airport', 'London', 'United Kingdom')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('HKG', 'Hong Kong International Airport', 'Hong Kong', 'Hong Kong')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('ICN', 'Incheon International Airport', 'Seoul', 'South Korea')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('KUL', 'Kuala Lumpur International Airport', 'Kuala Lumpur', 'Malaysia')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('BKK', 'Suvarnabhumi Airport', 'Bangkok', 'Thailand')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('SYD', 'Sydney Airport', 'Sydney', 'Australia')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);
INSERT INTO airports VALUES ('DXB', 'Dubai International Airport', 'Dubai', 'United Arab Emirates')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);

-- Populate Baseline Flights (Sample data for immediate use)
INSERT INTO flights VALUES ('PR-DOM001', 'MNL', 'CEB', '2026-05-18', '08:00:00', 'AVAILABLE', 2500.00, 'domestic', 'PAL', 'G-01', 145)
ON DUPLICATE KEY UPDATE status=VALUES(status);
INSERT INTO flights VALUES ('PR-DOM002', 'MNL', 'DVO', '2026-05-18', '10:30:00', 'AVAILABLE', 3200.00, 'domestic', 'Cebu Pacific', 'G-02', 148)
ON DUPLICATE KEY UPDATE status=VALUES(status);
INSERT INTO flights VALUES ('PR-DOM003', 'MNL', 'PPS', '2026-05-18', '13:00:00', 'DELAYED', 2100.00, 'domestic', 'AirAsia', 'G-03', 148)
ON DUPLICATE KEY UPDATE status=VALUES(status);
INSERT INTO flights VALUES ('PR-DOM004', 'MNL', 'MPH', '2026-05-18', '15:45:00', 'FULLY BOOKED', 4500.00, 'domestic', 'Cebu Pacific', 'G-04', 0)
ON DUPLICATE KEY UPDATE status=VALUES(status);
INSERT INTO flights VALUES ('PR-DOM005', 'MNL', 'ILO', '2026-05-18', '18:20:00', 'AVAILABLE', 2300.00, 'domestic', 'PAL', 'G-05', 148)
ON DUPLICATE KEY UPDATE status=VALUES(status);
INSERT INTO flights VALUES ('PR-INT001', 'MNL', 'NRT', '2026-05-18', '06:15:00', 'AVAILABLE', 18500.00, 'international', 'ANA', 'T1-12', 197)
ON DUPLICATE KEY UPDATE status=VALUES(status);
INSERT INTO flights VALUES ('PR-INT002', 'MNL', 'SIN', '2026-05-18', '09:40:00', 'AVAILABLE', 12200.00, 'international', 'Singapore Airlines', 'T1-15', 197)
ON DUPLICATE KEY UPDATE status=VALUES(status);
INSERT INTO flights VALUES ('PR-INT003', 'MNL', 'LAX', '2026-05-18', '22:10:00', 'AVAILABLE', 35000.00, 'international', 'PAL', 'T2-04', 197)
ON DUPLICATE KEY UPDATE status=VALUES(status);
INSERT INTO flights VALUES ('PR-INT004', 'MNL', 'LHR', '2026-05-18', '23:45:00', 'DELAYED', 42000.00, 'international', 'British Airways', 'T2-08', 199)
ON DUPLICATE KEY UPDATE status=VALUES(status);

-- Populate Beautiful Passenger Booking Ledger History (Specific Passenger List)
INSERT INTO bookings VALUES ('TXN-10042', 'AGLUBAT, JOHN GRAY D.', 'PR-DOM001', '12A', 2500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10043', 'ALBERO, JOHN MHEL EDRIAN D.', 'PR-DOM002', '04C', 3200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10044', 'ALCOVENDAS, ALLYZA JOY M.', 'PR-INT002', '18F', 12200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10045', 'AMIDO, AMIEL R.', 'PR-DOM003', '21B', 2100.00, 'CANCELLED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10046', 'AMORES, SOPHIA KATE D.', 'PR-INT001', '02A', 18500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10047', 'ANAJAO, DAREL C.', 'PR-DOM001', '03B', 2500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10048', 'ANCHETA, REYNALDO JR. M.', 'PR-DOM001', '14D', 2500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10049', 'APARECE, HARRY D.', 'PR-DOM002', '05F', 3200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10050', 'BAJIO, KAIZEN PETER U.', 'PR-DOM004', '08A', 4500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10051', 'BALTAZAR, BIANCA DAVE N.', 'PR-DOM004', '09A', 4500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10052', 'BAUTISTA, CHLOE G.', 'PR-DOM005', '10B', 2300.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10053', 'BAUTISTA, MAN JELORD A.', 'PR-DOM005', '11C', 2300.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10054', 'BERMEJO, RAYVER DANE S.', 'PR-INT001', '14A', 18500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10055', 'BERMUNDO, JOHN ANDRE M.', 'PR-INT002', '15C', 12200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10056', 'BISMONTE, LEONARDO B.', 'PR-INT003', '01A', 35000.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10057', 'BONGCAYAO, JEAN AARON J.', 'PR-INT003', '02D', 35000.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10058', 'BUENA, JOHN PATRICK C.', 'PR-INT004', '19F', 42000.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10059', 'CANAPI, JUMEL CEDRIC G.', 'PR-DOM001', '04A', 2500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10060', 'CANTONES, CHARMEE JOY P.', 'PR-DOM002', '06C', 3200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10061', 'CARLOS, RAFAEL III M.', 'PR-DOM003', '22D', 2100.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10062', 'COSING, YUN-TZU C.', 'PR-INT001', '03B', 18500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10063', 'DARNAYLA, ADIAN KRYLE P.', 'PR-INT002', '16F', 12200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10064', 'DORONILA, CHARLIE MONIQUE R.', 'PR-DOM001', '05C', 2500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10065', 'ELEPTICO, JARED ANTHONY V.', 'PR-DOM002', '07A', 3200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10066', 'ESPERIDA, JUSTINE H.', 'PR-INT003', '01D', 35000.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10067', 'FABILLAR, JOSH HENRICH M.', 'PR-DOM001', '06A', 2500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10068', 'GABUTIN, LEVIN TRISTRAM P.', 'PR-DOM002', '08B', 3200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10069', 'LUMABI, JAYVEE B.', 'PR-INT003', '03A', 35000.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10070', 'MALDO, KACEY BJORN M.', 'PR-DOM003', '23A', 2100.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10071', 'MANALO, CYRIL C.', 'PR-DOM005', '12A', 2300.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10072', 'MANUEL, ALTHEA NICOLE B.', 'PR-INT001', '04C', 18500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10073', 'ORIAS, CLIVE KIROS A.', 'PR-INT002', '17A', 12200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10074', 'OROPILLA, RALPH EIVAN P.', 'PR-INT004', '20A', 42000.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10075', 'PINEDA, ZANJOE E.', 'PR-DOM001', '07E', 2500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10076', 'ROLDAN, JAYDEL JADE C.', 'PR-DOM002', '09D', 3200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10077', 'SANTIAGO, ABRAHAM B.', 'PR-DOM004', '10A', 4500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10078', 'SIBLERO, YSAMARIE T.', 'PR-DOM005', '14F', 2300.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10079', 'SILVANO, RANEL M.', 'PR-INT001', '05E', 18500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10080', 'TADONG, RADLEY CYNRIC E.', 'PR-INT002', '20C', 12200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10081', 'TAGUNICAR, ETHAN HEINRICH R.', 'PR-INT003', '03D', 35000.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10082', 'TAN, JUSTIN H.', 'PR-INT004', '22B', 42000.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10083', 'TOGONON, LYAN JOHN L.', 'PR-DOM001', '08F', 2500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10084', 'VILLARTA, SCOTT ANDREI R.', 'PR-DOM002', '10F', 3200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10085', 'DE LA CRUZ, JUAN M.', 'PR-DOM001', '01C', 2500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10086', 'REYES, MARIA CLARA S.', 'PR-DOM002', '01A', 3200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10087', 'SANTOS, CARLOS P.', 'PR-DOM003', '03F', 2100.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10088', 'DELA CRUZ, PEDRO G.', 'PR-DOM004', '04E', 4500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10089', 'GONZALES, ANA LOURDES T.', 'PR-DOM005', '05B', 2300.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10090', 'AQUINO, BENIGNO F.', 'PR-INT001', '06F', 18500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10091', 'BONDOC, RAMONCITO L.', 'PR-INT002', '08C', 12200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10092', 'MACAPAGAL, GLORIA M.', 'PR-INT003', '01F', 35000.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10093', 'ESTRADA, JOSEPH E.', 'PR-INT004', '02D', 42000.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10094', 'MARCOS, FERDINAND R.', 'PR-DOM001', '02B', 2500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10095', 'DUTERTE, RODRIGO R.', 'PR-DOM002', '03D', 3200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10096', 'RAMOS, FIDEL V.', 'PR-DOM003', '04B', 2100.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10097', 'CORAZON, AQUINO C.', 'PR-DOM004', '05C', 4500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10098', 'ROXAS, MANUEL A.', 'PR-DOM005', '06A', 2300.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10099', 'QUEZON, MANUEL L.', 'PR-INT001', '07K', 18500.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);
INSERT INTO bookings VALUES ('TXN-10100', 'OSMENA, SERGIO H.', 'PR-INT002', '08E', 12200.00, 'CONFIRMED')
ON DUPLICATE KEY UPDATE passenger_name=VALUES(passenger_name);


