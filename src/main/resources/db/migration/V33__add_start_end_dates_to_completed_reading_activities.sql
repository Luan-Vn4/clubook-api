-- Add reading period dates to member completed reading activities
ALTER TABLE member_completed_reading_activities
  ADD COLUMN start_date DATE,
  ADD COLUMN end_date DATE;

-- Add reading period dates to user completed reading activities
ALTER TABLE user_completed_reading_activities
  ADD COLUMN start_date DATE,
  ADD COLUMN end_date DATE;
