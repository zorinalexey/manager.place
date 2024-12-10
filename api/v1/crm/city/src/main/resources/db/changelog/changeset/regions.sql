CREATE TABLE public.regions
(
    id         character varying(40)  NOT NULL,
    int_id     bigint                 NOT NULL,
    name       character varying(255) NOT NULL,
    country_id character varying(40)  NOT NULL,
    deleted_at timestamp(0) without time zone,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);