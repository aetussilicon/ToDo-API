create table if not exists tasks (
    task_id serial primary key,
    title varchar(100) not null,
    description text,
    priority_level text default 'NONE',
    status text default 'PENDING',
    created_date timestamp not null,
    updated_date timestamp not null,
    dead_line timestamp
);