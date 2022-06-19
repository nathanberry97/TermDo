CREATE TABLE progress(
progress_id int PRIMARY KEY,
progress varchar(255) NOT NULL
);

CREATE TABLE project(
project_id int PRIMARY KEY,
project varchar(255) NOT NULL
);

CREATE TABLE task(
task_id int PRIMARY KEY,
task varchar(255) NOT NULL,
project_id int REFERENCES project(project_id) NOT NULL,
progress_id int REFERENCES progress(progress_id) NOT NULL
)

