# --- !Ups

create table "USERS" ("NAME" VARCHAR(254) NOT NULL,"ID" SERIAL NOT NULL PRIMARY KEY);

create table "CUSTOMERS" ("ID" SERIAL NOT NULL PRIMARY KEY,
                          "FIRST_NAME" VARCHAR(254) NOT NULL,
                          "LAST_NAME" VARCHAR(254) NOT NULL,
                          "PHONE_NUMBER" VARCHAR(20) NOT NULL)

# --- !Downs

drop table "USERS";

drop table "CUSTOMERS";