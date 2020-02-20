drop table QuantumPrizes;
drop table RecordEntity;
drop table PrizeDistribution;
drop table Ticket;
drop table Game;
drop table Parameters;


CREATE TABLE Parameters
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

CREATE TABLE Game
(
	gameId int primary key,
	gameName VARCHAR(200) unique
);

/*CREATE TABLE Ticket
(
	ticketId             serial primary key,
	idx	             int,
	gameId               int null references Game(gameId) ON DELETE CASCADE,
	price                bigint NULL,
	win                  bigint NULL,
	context              VARCHAR(500) NULL,
	played               int default 0
);*/

CREATE TABLE Ticket
(
	ticketId             serial primary key,
	idx	                 serial,
	gameId               int null references Game(gameId) ON DELETE CASCADE,
	price                bigint NULL,
	win                  bigint NULL,
	context              VARCHAR(500) NULL,
	played               int default 0
);

CREATE TABLE PrizeDistribution
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

CREATE TABLE RecordEntity
(
	recordId             serial primary key,
	prizeId              int null references PrizeDistribution(prizeId) ON DELETE CASCADE,
	prize                bigint NULL,
	tickets              int NULL,
	idx	             int
);

CREATE TABLE QuantumPrizes
(
	quantumId            serial primary key,
	value                bigint NULL,
	prizeId              int null references PrizeDistribution(prizeId) ON DELETE CASCADE,
	idx	             int
);

commit;



CREATE INDEX  indexTicket  ON Ticket(gameId)

select * from Parameters;
select * from Game;
select * from Ticket order by ticketId;
select * from PrizeDistribution;
select * from RecordEntity;
select * from QuantumPrizes;


select * from Ticket where played=1

delete from Parameters;
delete from Game;
delete from Ticket;
delete from PrizeDistribution;
delete from RecordEntity;
delete from QuantumPrizes;


with alltickets as(
	select  t.gameid, t.price, t.win, count(t.win) tickets, sum(t.win) from ticket t group by t.gameid, t.price, t.win order by gameid, price, win
), dist as(
	select prizeid, gameid, bet from prizedistribution
), prizewithtickets as(
select al.gameid, al.price, al.win, al.tickets, sum, p.prizeid from alltickets al left join dist p on(al.gameid=p.gameid and al.price = p.bet)
)
update recordentity r set tickets = (select p.tickets from prizewithtickets p where r.prizeid=p.prizeid and r.prize = p.win)


with maxprized as(
select prizeid, max(prize) max_prize from recordentity group by prizeid
)update prizedistribution p set max_prize = (select m.max_prize from maxprized m where m.prizeid = p.prizeid);

with ticket_sum as(
	select p.gameId, p.bet, count(t.win) count_not_empty_tickets, sum(t.win) from ticket t
	join prizedistribution p on (t.gameId = p.gameId and t.price = p.bet)
	group by(p.gameId, p.bet)
)update prizedistribution p set total_tickets = (select t.count_not_empty_tickets from ticket_sum t where p.gameid=t.gameid and p.bet = t.bet);

with ticket_sum as(
	select p.gameId, p.bet, count(t.win) count_not_empty_tickets, sum(t.win) from ticket t
	join prizedistribution p on (t.gameId = p.gameId and t.price = p.bet)
	where win>0
	group by(p.gameId, p.bet)
)update prizedistribution p set winning_tickets = (select t.count_not_empty_tickets from ticket_sum t where p.gameid=t.gameid and p.bet = t.bet);

with ticket_sum as(
	select p.gameId, p.bet, count(t.win) count_not_empty_tickets, sum(t.win) from ticket t
	join prizedistribution p on (t.gameId = p.gameId and t.price = p.bet);
	where win>0
	group by(p.gameId, p.bet)
)update prizedistribution p set total_payout = (select t.sum from ticket_sum t where p.gameid=t.gameid and p.bet = t.bet);

update prizedistribution p set any_prize_hit = cast (winning_tickets as decimal )/total_tickets

with subquery as(
select prizeid, prize, tickets from recordentity where (prizeid, prize) in(
select prizeid, max(prize) from recordentity group by prizeid order by prizeid)
and tickets>0
) update prizedistribution p set max_prize_hit = cast((select s.tickets from subquery s where p.prizeid = s.prizeid) as decimal)/p.total_tickets;