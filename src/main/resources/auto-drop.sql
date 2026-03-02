
    -- 詳細メッセージを抑制し、警告レベル以上のみ表示する
    set client_min_messages = WARNING;

    -- manager テーブルに存在する外部キー制約を削除する
    alter table if exists manager 
       drop constraint if exists FKdil9hd6t80irdaebtwvj49hy2;

    -- player テーブルに存在する外部キー制約を削除する
    alter table if exists player 
       drop constraint if exists FKdvd6ljes11r44igawmpm1mc5s;

    -- manager テーブルを依存オブジェクトごと削除する
    drop table if exists manager cascade;

    -- player テーブルを依存オブジェクトごと削除する
    drop table if exists player cascade;

    -- team テーブルを依存オブジェクトごと削除する
    drop table if exists team cascade;

    -- team_seq シーケンスを削除する
    drop sequence if exists team_seq;
