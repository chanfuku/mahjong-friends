package db

import scalikejdbc._

trait TestDBSetup {

  setup()

  def setup(): Unit = {
    System.setProperty("skinny.env", "test")
    skinny.DBSettings.initialize()
    implicit val session = AutoSession
    createTables()
  }

  def deleteAll()(implicit session: DBSession): Unit = {
    sql"DELETE FROM balance_history".update().apply()
    sql"DELETE FROM events".update().apply()
    sql"DELETE FROM players".update().apply()
    sql"DELETE FROM groups".update().apply()
  }

  def createTables()(implicit session: DBSession) {
    sql"""
CREATE TABLE IF NOT EXISTs groups (
  id BIGINT(20) UNSIGNED AUTO_INCREMENT,
  group_name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS players (
  id BIGINT(20) UNSIGNED AUTO_INCREMENT,
  player_name VARCHAR(255) NOT NULL,
  group_id BIGINT(20) UNSIGNED,
  balance BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (id,group_id),
  FOREIGN KEY (group_id) REFERENCES groups(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS events (
  id BIGINT(20) UNSIGNED AUTO_INCREMENT,
  date DATE NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS payment_history (
  id BIGINT(20) UNSIGNED AUTO_INCREMENT,
  player_id BIGINT(20) UNSIGNED NOT NULL,
  group_id BIGINT(20) UNSIGNED NOT NULL,
  event_id BIGINT(20) UNSIGNED NOT NULL,
  amount INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (player_id) REFERENCES players(id),
  FOREIGN KEY (group_id) REFERENCES groups(id),
  FOREIGN KEY (event_id) REFERENCES events(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS balance_history (
  id BIGINT(20) UNSIGNED AUTO_INCREMENT,
  player_id BIGINT(20) UNSIGNED NOT NULL,
  group_id BIGINT(20) UNSIGNED NOT NULL,
  balance BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  FOREIGN KEY (player_id) REFERENCES players(id),
  FOREIGN KEY (group_id) REFERENCES groups(id)
) ENGINE=InnoDB;
      """.update().apply
  }
}
