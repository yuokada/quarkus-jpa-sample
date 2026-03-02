-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;
insert into public.team (id, is_active, created_at, updated_at, name)
values  (1, true, '2023-09-03 23:26:34', '2023-09-03 23:26:41', '阪神タイガース'),
        (2, true, '2023-09-03 14:28:12', '2023-09-03 14:28:12', '広島カープ'),
        (3, true, '2023-09-03 14:28:12', '2023-09-03 14:28:12', '横浜DeNAベイスターズ'),
        (4, true, '2023-09-03 14:28:12', '2023-09-03 14:28:12', '読売ジャイアンツ'),
        (5, true, '2023-09-03 14:28:12', '2023-09-03 14:28:12', '東京ヤクルトスワローズ'),
        (6, true, '2023-09-03 14:28:12', '2023-09-03 14:28:12', '中日ドラゴンズ'),
        (7, true, '2023-09-03 14:32:51', '2023-09-03 14:32:51', 'オリックス・バファローズ'),
        (8, true, '2023-09-03 14:32:51', '2023-09-03 14:32:51', '千葉ロッテマリーンズ'),
        (9, true, '2023-09-03 14:32:51', '2023-09-03 14:32:51', '福岡ソフトバンクホークス'),
        (10, true, '2023-09-03 14:32:51', '2023-09-03 14:32:51', '楽天イーグルス'),
        (11, true, '2023-09-03 14:32:51', '2023-09-03 14:32:51', '北海道日本ハムファイターズ'),
        (12, true, '2023-09-03 14:32:51', '2023-09-03 14:32:51', '埼玉西武ライオンズ');

insert into public.manager (id, is_active, team_id, created_at, updated_at, name)
values  (1, true, 1, '2023-09-03 15:42:02', '2023-09-03 15:42:02', '岡田彰布'),
        (2, true, 2, '2023-09-03 15:43:42', '2023-09-03 15:43:42', '新井貴浩'),
        (3, true, 3, '2023-09-03 15:43:42', '2023-09-03 15:43:42', '三浦大輔'),
        (4, true, 4, '2023-09-03 15:43:42', '2023-09-03 15:43:42', '原辰徳'),
        (5, true, 5, '2023-09-03 15:43:42', '2023-09-03 15:43:42', '高津臣吾'),
        (6, true, 6, '2023-09-03 15:43:42', '2023-09-03 15:43:42', '立浪和義'),
        (7, true, 7, '2023-09-03 15:47:25', '2023-09-03 15:47:25', '中嶋聡'),
        (8, true, 8, '2023-09-03 15:47:25', '2023-09-03 15:47:25', '吉井理人'),
        (9, true, 9, '2023-09-03 15:47:25', '2023-09-03 15:47:25', '藤本博史'),
        (10, true, 10, '2023-09-03 15:47:25', '2023-09-03 15:47:25', '石井一久'),
        (11, true, 11, '2023-09-03 15:47:25', '2023-09-03 15:47:25', '松井稼頭央'),
        (12, true, 12, '2023-09-03 15:47:25', '2023-09-03 15:47:25', '新庄剛志');

-- Add 120 dummy players (10 players x 12 teams)
insert into public.player (id, is_active, team_id, created_at, updated_at, name, uniform_number, position)
select
    i,
    true,
    ((i - 1) % 12) + 1,
    '2024-01-01 00:00:00',
    '2024-01-01 00:00:00',
    'ダミー選手' || lpad(i::text, 3, '0'),
    ((i - 1) / 12) + 1,
    case ((i - 1) % 6)
        when 0 then 'P'
        when 1 then 'C'
        when 2 then 'IF'
        when 3 then 'OF'
        when 4 then 'IF'
        else 'OF'
    end
from generate_series(1, 120) as s(i);

-- Backfill transfer history for existing players as initial assignment events
insert into public.player_transfer_history (player_id, from_team_id, to_team_id, transferred_at, created_at)
select
    p.id,
    null,
    p.team_id,
    p.created_at,
    p.created_at
from public.player p;

ALTER SEQUENCE team_seq RESTART WITH 13;
ALTER SEQUENCE manager_id_seq RESTART WITH 13;
ALTER SEQUENCE player_id_seq RESTART WITH 121;
ALTER SEQUENCE player_transfer_history_id_seq RESTART WITH 121;
