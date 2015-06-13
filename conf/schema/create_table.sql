CREATE TABLE groups (
  id BIGINT(20) UNSIGNED AUTO_INCREMENT,
  group_name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE players (
  id BIGINT(20) UNSIGNED AUTO_INCREMENT,
  player_name VARCHAR(255) NOT NULL,
  group_id BIGINT(20) UNSIGNED,
  balance BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (id,group_id),
  FOREIGN KEY (group_id) REFERENCES groups(id)
) ENGINE=InnoDB;

CREATE TABLE balance_history (
  id BIGINT(20) UNSIGNED AUTO_INCREMENT,
  player_id BIGINT(20) UNSIGNED NOT NULL,
  group_id BIGINT(20) UNSIGNED NOT NULL,
  amount INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (player_id) REFERENCES players(id),
  FOREIGN KEY (group_id) REFERENCES groups(id)
) ENGINE=InnoDB;
