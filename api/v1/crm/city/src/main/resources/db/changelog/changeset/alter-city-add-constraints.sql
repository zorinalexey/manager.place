ALTER TABLE ONLY public.cities
    ADD CONSTRAINT cities_id_unique UNIQUE (id);

ALTER TABLE ONLY public.cities
    ADD CONSTRAINT cities_pkey PRIMARY KEY (int_id);

ALTER TABLE ONLY public.cities
    ADD CONSTRAINT t_cities_uix UNIQUE (name, region_id, country_id);

ALTER TABLE ONLY public.cities
    ADD CONSTRAINT cities_region_id_foreign FOREIGN KEY (region_id) REFERENCES public.regions(id);