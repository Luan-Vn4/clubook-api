ALTER TABLE meetings
    ADD meeting_date TIMESTAMP WITHOUT TIME ZONE;

-- Backfill existing meetings with their reading goal's end date so the new
-- NOT NULL column has a sensible value for already-created encounters.
UPDATE meetings m
    SET meeting_date = rg.end_date
    FROM reading_goals rg
    WHERE m.reading_goal_id = rg.id
      AND m.meeting_date IS NULL;

ALTER TABLE meetings
    ALTER COLUMN meeting_date SET NOT NULL;
