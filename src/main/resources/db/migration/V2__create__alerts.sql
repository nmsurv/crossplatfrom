CREATE TABLE alerts (
    id BIGSERIAL PRIMARY KEY,
    sensor_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    description VARCHAR(500),
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_alert_sensor
        FOREIGN KEY (sensor_id)
        REFERENCES sensors (id)
);

CREATE TABLE alert_photos (
    alert_id BIGINT NOT NULL,
    photo_url VARCHAR(500) NOT NULL,
    CONSTRAINT fk_alert_photos
        FOREIGN KEY (alert_id)
        REFERENCES alerts (id)
);
