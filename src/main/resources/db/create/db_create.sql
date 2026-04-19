CREATE USER "powerscore" LOGIN ENCRYPTED PASSWORD 'powerscore' VALID UNTIL 'infinity';
CREATE DATABASE "powerscore" WITH OWNER = "powerscore" ENCODING = 'UTF8' CONNECTION LIMIT = 100;

GRANT ALL ON DATABASE "powerscore" TO "powerscore";

ALTER ROLE "powerscore" SET search_path TO "powerscore";