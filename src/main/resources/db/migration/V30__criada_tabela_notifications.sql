CREATE TABLE notifications
(
    id            UUID                        NOT NULL,
    recipient_id  VARCHAR(36)                 NOT NULL,
    title         VARCHAR(255)                NOT NULL,
    message       TEXT,
    type          VARCHAR(255),
    is_read       BOOLEAN                     NOT NULL DEFAULT FALSE,
    created_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id),
    CONSTRAINT fk_notifications_recipient FOREIGN KEY (recipient_id)
        REFERENCES user_entity (id) ON DELETE CASCADE
);
