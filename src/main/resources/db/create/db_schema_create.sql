CREATE SCHEMA "powerscore";
ALTER SCHEMA "powerscore" OWNER TO "powerscore";

REVOKE ALL ON DATABASE "powerscore" FROM public;
REVOKE ALL ON SCHEMA "powerscore" FROM public;

GRANT CONNECT ON DATABASE "powerscore" TO "powerscore";
GRANT USAGE ON SCHEMA "powerscore" TO "powerscore";
