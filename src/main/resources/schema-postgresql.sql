create table account (balance numeric(38,2) not null, id uuid not null, account_no varchar(255) not null unique, primary key (id));
create table instant_payment (amount numeric(38,2) not null, payment_date timestamp(6) not null, creditor_account_id uuid, debitor_account_id uuid, id uuid not null, comment varchar(255), primary key (id));
alter table if exists instant_payment add constraint FK90y4inw58uf8eyyc2s1sc6art foreign key (creditor_account_id) references account;
alter table if exists instant_payment add constraint FKa61696w5mxpfdlkaak6ho2lx8 foreign key (debitor_account_id) references account;
