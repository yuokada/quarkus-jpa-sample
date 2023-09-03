
    alter table if exists manager 
       drop constraint if exists FKdil9hd6t80irdaebtwvj49hy2;

    drop table if exists manager cascade;

    drop table if exists team cascade;

    drop sequence if exists team_seq;
