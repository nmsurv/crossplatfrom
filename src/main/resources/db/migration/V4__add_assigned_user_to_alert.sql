ALTER TABLE alerts
ADD COLUMN assigned_to_id BIGINT;

ALTER TABLE alerts
ADD CONSTRAINT fk_alert_assigned_user
FOREIGN KEY (assigned_to_id)
REFERENCES users (id);
