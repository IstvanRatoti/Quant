-- query to get some data from the sql server

SELECT
  *
FROM
  activities
LEFT JOIN
  timetable ON activities.id = timetable.actId