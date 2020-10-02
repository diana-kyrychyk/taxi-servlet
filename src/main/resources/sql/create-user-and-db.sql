CREATE ROLE taxi_db LOGIN
  PASSWORD 'taxi-db';


CREATE DATABASE taxi_db
  WITH OWNER = taxi_db
       ENCODING = 'UTF8';
