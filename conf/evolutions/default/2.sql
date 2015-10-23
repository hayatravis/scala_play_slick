# Campaigns data set

# --- !Ups
INSERT INTO Campaigns (name, title, contents_text, destination_url, created, modified, deleted, deleted_date)
VALUES
('キャンペーン1', '美空ひばり', '愛燦燦', 'http://app-liv.jp', now(), now(), 0, now()),
('キャンペーン2', 'テリー・ボジオ', 'テリー・ボジオホジホジ', 'http://ad.app-liv.jp', now(), now(), 0, now()),
('キャンペーン3', '加山雄三', '海・その愛', 'http://ad-admin.app-liv.jp', now(), now(), 0, now());

# --- !Downs

DELETE FROM Campaigns;