
    create sequence team_seq start with 1 increment by 1;

    create table manager (
        id serial not null,
        is_active boolean default true,
        team_id integer unique,
        created_at timestamp DEFAULT CURRENT_TIMESTAMP not null,
        updated_at timestamp DEFAULT CURRENT_TIMESTAMP not null,
        name varchar(255) not null,
        primary key (id)
    );

    comment on column manager.name is
        '監督名';

    create table team (
        id integer not null,
        is_active boolean default true not null,
        created_at timestamp DEFAULT CURRENT_TIMESTAMP not null,
        updated_at timestamp DEFAULT CURRENT_TIMESTAMP not null,
        name varchar(64) not null unique,
        primary key (id)
    );

    alter table if exists manager 
       add constraint FKdil9hd6t80irdaebtwvj49hy2 
       foreign key (team_id) 
       references team;
insert into public.team (id, is_active, created_at, updated_at, name) values  (1, true, '2023-09-03 23:26:34.000000', '2023-09-03 23:26:41.000000', '阪神タイガース'),         (2, true, '2023-09-03 14:28:12.868750', '2023-09-03 14:28:12.868750', '広島カープ'),         (3, true, '2023-09-03 14:28:12.868750', '2023-09-03 14:28:12.868750', '横浜DeNAベイスターズ'),         (4, true, '2023-09-03 14:28:12.868750', '2023-09-03 14:28:12.868750', '読売ジャイアンツ'),         (5, true, '2023-09-03 14:28:12.868750', '2023-09-03 14:28:12.868750', '東京ヤクルトスワローズ'),         (6, true, '2023-09-03 14:28:12.868750', '2023-09-03 14:28:12.868750', '中日ドラゴンズ'),         (7, true, '2023-09-03 14:32:51.941390', '2023-09-03 14:32:51.941390', 'オリックス・バファローズ'),         (8, true, '2023-09-03 14:32:51.941390', '2023-09-03 14:32:51.941390', '千葉ロッテマリーンズ'),         (9, true, '2023-09-03 14:32:51.941390', '2023-09-03 14:32:51.941390', '福岡ソフトバンクホークス'),         (10, true, '2023-09-03 14:32:51.941390', '2023-09-03 14:32:51.941390', '楽天イーグルス'),         (11, true, '2023-09-03 14:32:51.941390', '2023-09-03 14:32:51.941390', '北海道日本ハムファイターズ'),         (12, true, '2023-09-03 14:32:51.941390', '2023-09-03 14:32:51.941390', '埼玉西武ライオンズ');
insert into public.manager (id, is_active, team_id, created_at, updated_at, name) values  (1, true, 1, '2023-09-03 15:42:02.716150', '2023-09-03 15:42:02.716150', '岡田彰布'),         (2, true, 2, '2023-09-03 15:43:42.011073', '2023-09-03 15:43:42.011073', '新井貴浩'),         (3, true, 3, '2023-09-03 15:43:42.011073', '2023-09-03 15:43:42.011073', '三浦大輔'),         (4, true, 4, '2023-09-03 15:43:42.011073', '2023-09-03 15:43:42.011073', '原辰徳'),         (5, true, 5, '2023-09-03 15:43:42.011073', '2023-09-03 15:43:42.011073', '高津臣吾'),         (6, true, 6, '2023-09-03 15:43:42.011073', '2023-09-03 15:43:42.011073', '立浪和義'),         (7, true, 7, '2023-09-03 15:47:25.954927', '2023-09-03 15:47:25.954927', '中嶋聡'),         (8, true, 8, '2023-09-03 15:47:25.954927', '2023-09-03 15:47:25.954927', '吉井理人'),         (9, true, 9, '2023-09-03 15:47:25.954927', '2023-09-03 15:47:25.954927', '藤本博史'),         (10, true, 10, '2023-09-03 15:47:25.954927', '2023-09-03 15:47:25.954927', '石井一久'),         (11, true, 11, '2023-09-03 15:47:25.954927', '2023-09-03 15:47:25.954927', '松井稼頭央'),         (12, true, 12, '2023-09-03 15:47:25.954927', '2023-09-03 15:47:25.954927', '新庄剛志');
