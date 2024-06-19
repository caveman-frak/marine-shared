--liquibase formatted sql

--changeset cavemanfrak:01 contextFilter:ddl
--comment: create Audit table
CREATE TABLE IF NOT EXISTS audit (
    id UUID NOT NULL DEFAULT UUID() PRIMARY KEY COMMENT 'Internal Audit Identifier',
    created TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT 'Audit Record Created',
    principal VARCHAR(100) NOT NULL COMMENT 'Audit Record Principal',
    type VARCHAR(100) NOT NULL COMMENT 'Audit Record Type'
);

--changeset cavemanfrak:01a contextFilter:ddl dbms:mariadb
--comment: create Audit indexes
ALTER TABLE IF EXISTS audit (
    ADD INDEX IF NOT EXISTS idx_audit_created (created),
    ADD INDEX IF NOT EXISTS idx_audit_principal (principal, created),
    ADD INDEX IF NOT EXISTS idx_audit_type (type, created)
);

--changeset cavemanfrak:01b contextFilter:ddl dbms:mariadb
--comment: create restrict delete trigger on Audit table
CREATE OR REPLACE TRIGGER tgr_audit_restrict_delete BEFORE DELETE ON audit FOR EACH ROW
	SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Unable to delete audit';

--changeset cavemanfrak:01c contextFilter:ddl dbms:mariadb
--comment: create restrict update trigger on Audit table
CREATE OR REPLACE TRIGGER tgr_audit_restrict_update BEFORE UPDATE ON audit FOR EACH ROW
	SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Unable to update audit';

--changeset cavemanfrak:02 contextFilter:ddl
--comment: create Audit Data child table
CREATE TABLE IF NOT EXISTS audit_data (
    audit_id UUID NOT NULL COMMENT 'Reference to Audit Identifier',
    property VARCHAR(100) NOT NULL COMMENT 'Audit Data Property Key',
    data VARCHAR(1000) NOT NULL COMMENT 'Audit Data Property Value',
    PRIMARY KEY (audit_id, property),
    CONSTRAINT fk_audit FOREIGN KEY (audit_id) REFERENCES audit(id)
      ON DELETE RESTRICT
      ON UPDATE RESTRICT
);

--changeset cavemanfrak:02a contextFilter:ddl dbms:mariadb
--comment: create restrict delete trigger on Audit Data table
CREATE OR REPLACE TRIGGER tgr_audit_data_restrict_delete BEFORE DELETE ON audit_data FOR EACH ROW
	SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Unable to delete audit data';

--changeset cavemanfrak:02b contextFilter:ddl dbms:mariadb
--comment: create restrict update trigger on Audit Data table
CREATE OR REPLACE TRIGGER tgr_audit_data_restrict_update BEFORE UPDATE ON audit_data FOR EACH ROW
	SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Unable to update audit data';