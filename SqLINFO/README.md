mysql  Ver 14.14 Distrib 5.7.30, for Linux (x86_64) using  EditLine wrapper:

Work with tables:

Data(userId INT UNSIGNED,message TEXT, FOREIGN KEY(userId) REFERENCES Users(id));

Users(id INT UNSIGNED NOT NULL AUTO_INCREMENT, login varchar(255) NOT NULL, password varchar(255) NOT NULL, PRIMARY KEY(id));

ALTER TABLE Data ADD COLUMN time DATETIME NOT NULL AFTER message;

ALTER TABLE Users  ADD COLUMN ip TEXT NOT NULL AFTER password;

Native SQL requests :

ALTER TABLE Users AUTO_INCREMENT = _;

DELETE FROM Users WHERE id = _;

INSERT INTO Data (userId,message) VALUES(userId = (SELECT id FROM Users WHERE login =''),'');

INSERT INTO SpamChat.Users (login,password,ip) VALUES ('girl','12345','127.0.0.1');

