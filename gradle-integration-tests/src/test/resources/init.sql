create table if not exists customer(
    id varchar(10) primary key,
    first_name varchar(50),
    last_name varchar(50),
    email varchar(50),
    created_at timestamp not null default now()
)