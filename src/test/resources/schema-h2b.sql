CREATE TABLE time_entries (
  id         INT PRIMARY KEY,
  project_id INT,
  user_id    INT,
  date       DATE,
  hours      INT,
)