# --- !Ups

create table "CUSTOMERS" ("ID" UUID PRIMARY KEY,
                          "FIRST_NAME" VARCHAR(50) NOT NULL,
                          "LAST_NAME" VARCHAR(50),
                          "SEAL" VARCHAR(254) NOT NULL,
                          "PHONE_NUMBER" VARCHAR(20) NOT NULL,
                          "IS_ACTIVE" SMALLINT NOT NULL);

create table "PRODUCTS" ("ID" UUID PRIMARY KEY,
                        "NAME" VARCHAR(50) NOT NULL,
                        "P_TYPE" VARCHAR(50) NOT NULL,
                        "DESCRIPTION" VARCHAR(254),
                        "IS_ACTIVE" SMALLINT NOT NULL);

create table "UNITS" ("ID" UUID PRIMARY KEY,
                        "NAME" VARCHAR(50) NOT NULL,
                        "DESCRIPTION" VARCHAR(254),
                        "IS_ACTIVE" SMALLINT NOT NULL);

create table "BILLS" ("ID" UUID PRIMARY KEY,
                        "CUST_ID" UUID NOT NULL,
                        "DATE_CREATED" TIMESTAMPTZ NOT NULL,
                        "DATE_MODIFIED" TIMESTAMPTZ NOT NULL,
                        "TOTAL_AMOUNT" REAL NOT NULL,
                        "PAID_AMOUNT" REAL NOT NULL,
                        "IS_ACTIVE" SMALLINT NOT NULL,
                        FOREIGN KEY ("CUST_ID") REFERENCES "CUSTOMERS" ("ID"));

create table "BILL_PRODUCTS" ("BILL_ID" UUID NOT NULL,
                        "PRD_ID" UUID NOT NULL,
                        "UNIT_ID" UUID NOT NULL,
                        "QUANTITY" INTEGER NOT NULL,
                        "PRICE" REAL NOT NULL,
                        "IS_ACTIVE" SMALLINT NOT NULL,
                        PRIMARY KEY ("BILL_ID", "PRD_ID","UNIT_ID"),
                        FOREIGN KEY ("BILL_ID") REFERENCES "BILLS" ("ID"),
                        FOREIGN KEY ("PRD_ID") REFERENCES "PRODUCTS" ("ID"),
                        FOREIGN KEY ("UNIT_ID") REFERENCES "UNITS" ("ID"));


# --- !Downs

drop table "CUSTOMERS";

drop table "PRODUCTS";

drop table "UNITS";

drop table "BILLS";

drop table "BILL_PRODUCTS";