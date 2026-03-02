
    set client_min_messages = WARNING;

    alter table if exists manager 
       drop constraint if exists FKdil9hd6t80irdaebtwvj49hy2;

    alter table if exists player 
       drop constraint if exists FKdvd6ljes11r44igawmpm1mc5s;

    drop table if exists manager cascade;

    drop table if exists player cascade;

    drop table if exists team cascade;

    drop sequence if exists team_seq;
