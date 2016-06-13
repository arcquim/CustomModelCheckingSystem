create extension citext;

create extension plperl;
create language plperlu;

create or replace function check_email(email text) returns bool
language plperlu
as $$
use Email::Address;

my @addresses = Email::Address->parse($_[0]);
return scalar(@addresses) > 0 ? 1 : 0;
$$;

create table systemclients (
id serial primary key,
busy boolean not null default false,
email citext unique not null
check (check_email(email)));

create table project (
id serial primary key,
user_id int not null references systemclients(id),
title text not null,
active boolean not null default true
);

create table result (
id serial primary key,
result_name varchar(100) not null unique
);

create table projecthistory (
id serial primary key,
project_id int not null references project(id),
listing text not null,
property text not null,
counter_examples text,
result_id int not null references result(id)
);

insert into result(result_name) (select 'Property holds' union 
		select 'Property does not hold' union 
		select 'Error: input is not correct' union
		select 'Error during the verification');