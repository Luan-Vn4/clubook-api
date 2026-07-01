-- Add activity_type column as nullable for backfill
ALTER TABLE activities ADD COLUMN activity_type VARCHAR(255);

-- Backfill based on JOINED child tables
UPDATE activities SET activity_type = 'MEETING_DEFINED'
    WHERE id IN (SELECT mda.id FROM meeting_defined_activities mda);
UPDATE activities SET activity_type = 'MEMBER_COMPLETED_READING'
    WHERE id IN (SELECT mcra.id FROM member_completed_reading_activities mcra);
UPDATE activities SET activity_type = 'READING_GOAL_DEFINED'
    WHERE id IN (SELECT rgda.id FROM reading_goal_defined_activities rgda);
UPDATE activities SET activity_type = 'USER_COMPLETED_READING'
    WHERE id IN (SELECT ucra.id FROM user_completed_reading_activities ucra);

-- Make NOT NULL after backfill
ALTER TABLE activities ALTER COLUMN activity_type SET NOT NULL;
