-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;
insert into public.team (id, is_active, created_at, updated_at, name)
values  (1, true, '2023-09-03 23:26:34.000000', '2023-09-03 23:26:41.000000', '阪神タイガース'),
        (2, true, '2023-09-03 14:28:12.868750', '2023-09-03 14:28:12.868750', '広島カープ'),
        (3, true, '2023-09-03 14:28:12.868750', '2023-09-03 14:28:12.868750', '横浜DeNAベイスターズ'),
        (4, true, '2023-09-03 14:28:12.868750', '2023-09-03 14:28:12.868750', '読売ジャイアンツ'),
        (5, true, '2023-09-03 14:28:12.868750', '2023-09-03 14:28:12.868750', '東京ヤクルトスワローズ'),
        (6, true, '2023-09-03 14:28:12.868750', '2023-09-03 14:28:12.868750', '中日ドラゴンズ'),
        (7, true, '2023-09-03 14:32:51.941390', '2023-09-03 14:32:51.941390', 'オリックス・バファローズ'),
        (8, true, '2023-09-03 14:32:51.941390', '2023-09-03 14:32:51.941390', '千葉ロッテマリーンズ'),
        (9, true, '2023-09-03 14:32:51.941390', '2023-09-03 14:32:51.941390', '福岡ソフトバンクホークス'),
        (10, true, '2023-09-03 14:32:51.941390', '2023-09-03 14:32:51.941390', '楽天イーグルス'),
        (11, true, '2023-09-03 14:32:51.941390', '2023-09-03 14:32:51.941390', '北海道日本ハムファイターズ'),
        (12, true, '2023-09-03 14:32:51.941390', '2023-09-03 14:32:51.941390', '埼玉西武ライオンズ');