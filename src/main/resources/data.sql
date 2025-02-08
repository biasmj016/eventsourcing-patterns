
-- 회원(Member) 테이블 생성
CREATE TABLE IF NOT EXISTS member (
                                      member_id VARCHAR(50) PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL
);

INSERT INTO member (member_id, name) VALUES
                                         ('mem-001', 'Alice'),
                                         ('mem-002', 'Bob'),
                                         ('mem-003', 'Charlie')
ON CONFLICT (member_id) DO NOTHING;

-- 상품(Product) 테이블 생성
CREATE TABLE IF NOT EXISTS product (
                                       item_id VARCHAR(50) PRIMARY KEY,
                                       price DOUBLE PRECISION NOT NULL,
                                       description TEXT
);

INSERT INTO product (item_id, price, description) VALUES
                                                      ('CONCERT_TICKET', 10000.00, 'Concert Ticket'),
                                                      ('SPORTS_MATCH_TICKET', 25000, 'Sports Match Ticket'),
                                                      ('BUS_TICKET', 5000, 'Bus Ticket')
ON CONFLICT (item_id) DO NOTHING;