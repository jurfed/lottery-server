
CREATE TABLE IF NOT EXISTS Parameters
(
	parameterId          serial primary key,
	currentBalance       bigint NULL,
	maxWin               bigint NULL,
	currentBet           bigint NULL,
	currencyCode         VARCHAR(20) NULL,
	soundValue           decimal NULL,
	languageCode         VARCHAR(20) NULL,
	currentGame          int NULL
);

CREATE TABLE IF NOT EXISTS Game
(
	gameId int primary key,
	gameName VARCHAR(200) unique
);

CREATE TABLE IF NOT EXISTS Ticket
(
	ticketId             serial primary key,
	idx	             int,
	gameId               int null references Game(gameId) ON DELETE CASCADE,
	price                bigint NULL,
	win                  bigint NULL,
	context              VARCHAR(500) NULL,
	played               int default 0
);


CREATE TABLE IF NOT EXISTS PrizeDistribution
(
	prizeId              serial primary key,
	gameId               int null references Game(gameId) ON DELETE CASCADE,
	bet                  bigint NULL,
	total_tickets        int NULL,
	max_prize_hit        decimal NULL,
	any_prize_hit        decimal NULL,
	winning_tickets      int NULL,
	max_prize            bigint NULL,
	total_payout         bigint NULL,
	idx	             int
);

CREATE TABLE IF NOT EXISTS RecordEntity
(
	recordId             serial primary key,
	prizeId              int null references PrizeDistribution(prizeId) ON DELETE CASCADE,
	prize                bigint NULL,
	tickets              int NULL,
	idx	             int
);

CREATE TABLE IF NOT EXISTS QuantumPrizes
(
	quantumId            serial primary key,
	value                bigint NULL,
	prizeId              int null references PrizeDistribution(prizeId) ON DELETE CASCADE,
	idx	             int
);

CREATE unique INDEX if not exists "prizedistribution_gameid_bet_idx" ON PrizeDistribution (gameid, bet);
CREATE INDEX if not exists "quantumprizes_prizeid_idx" ON QuantumPrizes (prizeid);
CREATE INDEX if not exists "recordentity_prizeid_idx" ON RecordEntity (prizeid);