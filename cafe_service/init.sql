-- This script was generated by the ERD tool in pgAdmin 4.
-- Please log an issue at https://github.com/pgadmin-org/pgadmin4/issues/new/choose if you find any bugs, including reproduction steps.
BEGIN;


CREATE TABLE IF NOT EXISTS public.food_order
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    person_id integer NOT NULL,
    total_amount integer NOT NULL,
    ind_in_order integer NOT NULL,
    status character varying(256) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT food_order_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.menu_item
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    item_name character varying(256) COLLATE pg_catalog."default" NOT NULL,
    price integer NOT NULL,
    count integer NOT NULL,
    "time" integer NOT NULL,
    CONSTRAINT menu_item_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.order_menu_item
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    order_id integer NOT NULL,
    menu_item_id integer NOT NULL,
    status character varying(256) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT order_menu_item_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.person
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying(256) COLLATE pg_catalog."default" NOT NULL,
    login character varying(256) COLLATE pg_catalog."default" NOT NULL,
    password character varying(256) COLLATE pg_catalog."default" NOT NULL,
    user_type character varying(256) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT person_pkey PRIMARY KEY (id)
    );

ALTER TABLE IF EXISTS public.food_order
    ADD CONSTRAINT person_id FOREIGN KEY (person_id)
    REFERENCES public.person (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public.order_menu_item
    ADD CONSTRAINT menu_item_id FOREIGN KEY (menu_item_id)
    REFERENCES public.menu_item (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public.order_menu_item
    ADD CONSTRAINT order_id FOREIGN KEY (order_id)
    REFERENCES public.food_order (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION;

END;