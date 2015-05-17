CREATE TABLE
  id BIGINT(20) UNSIGNED AUTO_INCREMENT,
  group_name VARCHAR(255) NOT NULL,
  UNIQUE (group_name),
  PRIMARY KEY (group_id)
) ENGINE=InnoDB;

CREATE TABLE players (
  id BIGINT(20) UNSIGNED AUTO_INCREMENT,
  player_name VARCHAR(255) NOT NULL,
  group_id BIGINT(20) UNSIGNED,
  UNIQUE (player_name),
  PRIMARY KEY (player_id),
  FOREIGN KEY (group_id) REFERENCES groups(group_id)
) ENGINE=InnoDB;
