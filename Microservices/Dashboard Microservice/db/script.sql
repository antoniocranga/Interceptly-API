create table if not exists notifications
(
    id           int unsigned auto_increment
        primary key,
    type         int        default 0 not null,
    seen         tinyint(1) default 0 not null,
    message      varchar(300)         null,
    redirect_url varchar(300)         null,
    sent_to      int unsigned         null,
    created_at   datetime             not null,
    updated_at   datetime             not null,
    sent_by      int unsigned         null
);

create table if not exists users
(
    id         int unsigned auto_increment
        primary key,
    first_name varchar(20)          null,
    last_name  varchar(20)          null,
    username   varchar(100)         null,
    photo_url  varchar(100)         null,
    verified   tinyint(1) default 0 null,
    email      varchar(100)         null,
    password   varchar(100)         null,
    provider   int                  null,
    created_at datetime             not null,
    updated_at datetime             not null,
    constraint email
        unique (email)
);

create table if not exists projects
(
    id          int unsigned auto_increment
        primary key,
    title       varchar(50)                  null,
    description varchar(150)                 null,
    owner       int unsigned                 not null,
    api_key     varchar(36)                  not null,
    created_at  datetime                     not null,
    updated_at  datetime                     not null,
    color       varchar(7) default '#2196f3' not null,
    constraint api_key
        unique (api_key),
    constraint projects_users_id_fk
        foreign key (owner) references users (id)
);

create table if not exists issues
(
    id            int unsigned auto_increment
        primary key,
    title         text                         not null,
    description   mediumtext                   not null,
    project_id    int unsigned                 not null,
    status        varchar(15) default 'ACTIVE' not null,
    created_at    datetime                     not null,
    updated_at    datetime                     not null,
    type          varchar(100)                 not null,
    message       mediumtext                   not null,
    is_bookmarked tinyint(1)  default 0        not null,
    constraint issues_projects_id_fk
        foreign key (project_id) references projects (id)
);

create table if not exists collaborations
(
    user_id    int unsigned not null,
    issue_id   int unsigned not null,
    created_at datetime     not null,
    created_by int unsigned not null,
    primary key (user_id, issue_id),
    constraint collaborations_issues_id_fk
        foreign key (issue_id) references issues (id),
    constraint collaborations_users_id_fk
        foreign key (user_id) references users (id),
    constraint collaborations_users_id_fk2
        foreign key (created_by) references users (id)
);

create table if not exists comments
(
    user_id    int unsigned not null,
    issue_id   int unsigned not null,
    comment    text         not null,
    created_at datetime     not null,
    updated_at datetime     not null,
    id         int unsigned auto_increment
        primary key,
    constraint comments_issues_id_fk
        foreign key (issue_id) references issues (id),
    constraint comments_users_id_fk
        foreign key (user_id) references users (id)
);

create index comments_issue_id_index
    on comments (issue_id);

create table if not exists events
(
    id                    varchar(36)  not null
        primary key,
    message               mediumtext   not null,
    stack_trace           longtext     null,
    body                  text         null,
    headers               text         null,
    environment           varchar(15)  null,
    package_name          varchar(30)  null,
    package_version       varchar(30)  null,
    created_at            datetime     not null,
    issue_id              int unsigned not null,
    name                  text         null,
    type                  text         not null,
    version               varchar(30)  null,
    user_agent            text         null,
    tags                  text         null,
    project_id            int unsigned null,
    device_type           text         null,
    browser_major_version int          null,
    browser               text         null,
    platform_version      text         null,
    browser_type          text         null,
    constraint events_issues_id_fk
        foreign key (issue_id) references issues (id),
    constraint events_projects_id_fk
        foreign key (project_id) references projects (id)
);

create index issues_project_id_index
    on issues (project_id);

create table if not exists permissions
(
    user_id    int unsigned not null,
    project_id int unsigned not null,
    permission int unsigned not null,
    created_at datetime     not null,
    updated_at datetime     not null,
    created_by int unsigned not null,
    constraint permissions_projects_id_fk
        foreign key (project_id) references projects (id),
    constraint permissions_users_id_fk
        foreign key (user_id) references users (id)
);

