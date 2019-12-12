DROP TABLE toby.users;
CREATE TABLE toby.users
(
    id        VARCHAR(10) PRIMARY KEY,
    name      VARCHAR(20) NOT NULL,
    password  VARCHAR(10) NOT NULL,
    level     TINYINT     NOT NULL,
    login     INT         NOT NULL,
    recommend INT         NOT NULL,
    email     VARCHAR(50) NOT NULL
);
ALTER TABLE toby.users CONVERT TO CHARSET utf8;