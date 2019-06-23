# --- !Ups

create table "CUSTOMERS" ("ID" UUID PRIMARY KEY,
                          "FIRST_NAME" VARCHAR(50) NOT NULL,
                          "LAST_NAME" VARCHAR(50),
                          "SEAL" VARCHAR(254) NOT NULL,
                          "PHONE_NUMBER" VARCHAR(20) NOT NULL,
                          "IS_ACTIVE" SMALLINT NOT NULL)

# --- !Downs

drop table "CUSTOMERS";